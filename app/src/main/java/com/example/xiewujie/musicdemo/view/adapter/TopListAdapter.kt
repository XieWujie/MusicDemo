package com.example.xiewujie.musicdemo.view.adapter

import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.xiewujie.musicdemo.R
import com.example.xiewujie.musicdemo.module.http.LoadUtil
import com.example.xiewujie.musicdemo.module.json.Song
import com.example.xiewujie.musicdemo.module.json.Songlist
import com.example.xiewujie.musicdemo.module.json.TopX
import com.example.xiewujie.musicdemo.tool.SongsDecoration
import com.example.xiewujie.musicdemo.view.activity.MainActivity

class TopListAdapter:RecyclerView.Adapter<TopListAdapter.ViewHolder>(){

    private val mList = ArrayList<TopX>()
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.top_list_layout,p0,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size

    }

    fun setList(list:List<TopX>){
        mList.clear()
        mList.addAll(list)
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
       val topX = mList[p1]
        LoadUtil.loadImage(holder.imageView,topX.pic)
        loadList(holder,topX.songlist)
        holder.itemLayout.setOnClickListener{
            val intent = Intent()
            intent.action = MainActivity.MAIN_BROADCAST_SERVICE
            intent.putExtra(MainActivity.TOPID,topX.topID)
            it.context.sendBroadcast(intent)
        }
    }

    private fun loadList(holder: ViewHolder,songList:List<Songlist>){
        val context = holder.listLayout.context
        holder.listLayout.removeAllViews()
        for (i in 0 until 3){
            val song = songList[i]
            val textView = TextView(context)
            textView.text = " ${i+1} ${song.songname} - ${song.singername}"
            textView.gravity = Gravity.CENTER_VERTICAL
            textView.setTextColor(holder.color)
            textView.maxLines = 1
            holder.listLayout.addView(textView,holder.layoutParams)
        }
    }

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val listLayout = itemView.findViewById<LinearLayout>(R.id.top_list_view)
        val imageView = itemView.findViewById<ImageView>(R.id.top_list_imageview)
        val itemLayout = itemView.findViewById<CardView>(R.id.top_list_layout)
        val layoutParams by lazy<LinearLayout.LayoutParams> { val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,1f)
        params}
        val color = Color.parseColor("#000000")
    }
}