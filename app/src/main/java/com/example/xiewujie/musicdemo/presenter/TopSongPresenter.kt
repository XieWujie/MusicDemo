package com.example.xiewujie.musicdemo.presenter

import android.content.Context
import android.util.Log
import com.example.xiewujie.musicdemo.module.http.HttpMathods
import com.example.xiewujie.musicdemo.module.json.Song
import com.example.xiewujie.musicdemo.view.view.TopView
import okhttp3.ResponseBody
import rx.Subscriber

class TopSongPresenter(val context: Context,val view:TopView):BasePresenter(context,view){

    fun getData(topid:Int){
        val subscriber =object :Subscriber<Song>(){
            override fun onNext(t: Song?) {
                   if (t!=null){
                       view.data(t)
                   }
            }

            override fun onCompleted() {
               view.onFinish()
            }

            override fun onError(e: Throwable?) {
              view.onFail(e!!)
        }
        }
        HttpMathods.getTopService(subscriber,topid)
    }
}