package com.example.xiewujie.musicdemo.view.holder


import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.xiewujie.musicdemo.R
import com.example.xiewujie.musicdemo.module.json.Song

class SongListHodler(itemView:View):BaseHolder(itemView){

    val orderText = itemView.findViewById<TextView>(R.id.list_order)
    val nameText = itemView.findViewById<TextView>(R.id.list_song_name)
    val singerText = itemView.findViewById<TextView>(R.id.list_singer)
    val leftLayout = itemView.findViewById<RelativeLayout>(R.id.song_list_left_layout)
    var currentPosition = 0

    override fun bindData(data: Any) {
        val song = data as Song
        if (song.position !=-1) {
            if (song.position<101) {
                orderText.text = song.position.toString()
            }
            leftLayout.visibility = View.VISIBLE
        }else{
            leftLayout.visibility = View.GONE
        }
        nameText.text = song.songName
        singerText.text = song.singerName
        itemView.setOnClickListener{listener?.callBack(song)}
    }
}