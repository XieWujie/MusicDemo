package com.example.xiewujie.musicdemo.presenter

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.BitmapFactory
import android.support.v4.app.FragmentManager
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.example.xiewujie.musicdemo.R
import com.example.xiewujie.musicdemo.module.http.HttpMathods
import com.example.xiewujie.musicdemo.module.json.Lyric
import com.example.xiewujie.musicdemo.module.json.Song
import com.example.xiewujie.musicdemo.view.fragment.BottomListFragment
import com.example.xiewujie.musicdemo.view.view.MainView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.http.GET
import rx.Subscriber
import java.nio.charset.Charset
import java.util.regex.Matcher
import java.util.regex.Pattern

class MainPresenter(val context: Context,val view:MainView):BasePresenter(context,view){


    private var dialog:Dialog? = null
    private var bottomFragment:BottomListFragment? = null

    fun showSongsList(songs:List<Song>,fragmentManager: FragmentManager){
        if (dialog == null) {
            dialog = Dialog(context,R.style.DialogTheme)
            val view = View.inflate(context, R.layout.main_bottom_layout, null)
            view.findViewById<TextView>(R.id.close_bottom_dialog_text).setOnClickListener { dialog?.dismiss() }
            dialog?.setContentView(view)
            val window = dialog?.window
            window?.setGravity(Gravity.BOTTOM)
            val layoutParams = window?.attributes
            val displayMetrics = view.resources.displayMetrics
            layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
            window?.decorView?.setPadding(0,0,0,0)
            layoutParams?.height = displayMetrics.heightPixels/2
            window?.attributes = layoutParams
            bottomFragment = fragmentManager.findFragmentById(R.id.bottom_songs_fragment) as BottomListFragment

        }
        bottomFragment?.setSongsList(songs)
        dialog?.show()
    }

    fun dismissDialog(){
        dialog?.dismiss()
    }

    fun getSongPic(id:Int,view:ImageView){
        val subscriber = object :Subscriber<ResponseBody>(){
            override fun onNext(t: ResponseBody?) {
               val bitmap = BitmapFactory.decodeStream(t?.byteStream())
                view.setImageBitmap(bitmap)
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
               e?.printStackTrace()
            }
        }
        HttpMathods.getPicService(subscriber,id,500)
    }
}