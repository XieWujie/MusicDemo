package com.example.xiewujie.musicdemo.view.holder

import android.content.Intent
import android.view.View
import android.widget.TextView
import com.example.xiewujie.musicdemo.R
import com.example.xiewujie.musicdemo.module.json.Song
import com.example.xiewujie.musicdemo.tool.CurrentSongsProvider
import com.example.xiewujie.musicdemo.view.activity.MainActivity
import com.example.xiewujie.musicdemo.view.service.PlayMusicService

class BottomListHolder(itemView: View):BaseHolder(itemView){

    val songName = itemView.findViewById<TextView>(R.id.bottom_list_song_name)
    val singerName = itemView.findViewById<TextView>(R.id.bottom_list_singer_name)



    override fun bindData(data: Any) {
        if (data is Song){
            songName.text = data.songName
            singerName.text = " - ${data.singerName}"
            itemView.setOnClickListener{
                val intent = Intent()
                intent.action = PlayMusicService.PLAY_MUSIC_SERVICE
                intent.putExtra(PlayMusicService.PLAY_INDEX,CurrentSongsProvider.getSongs().indexOf(data))
                context.sendBroadcast(intent)
                val mainIntent = Intent()
                mainIntent.action = MainActivity.MAIN_BROADCAST_SERVICE
                mainIntent.putExtra(MainActivity.ACTION_DISMISS_DIALOG,true)
                context.sendBroadcast(mainIntent)
            }
        }
    }
}