package com.example.xiewujie.musicdemo.presenter

import android.content.Context
import android.provider.MediaStore
import com.example.xiewujie.musicdemo.module.json.Song
import com.example.xiewujie.musicdemo.view.view.BaseView
import com.example.xiewujie.musicdemo.view.view.NativeView

class NativePresenter(val context: Context,private val view:NativeView):BasePresenter(context,view){

    fun getMusicData() {
        val list = ArrayList<Song>()
        // 媒体库查询语句（写一个工具类MusicUtils）
        val cursor = context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.AudioColumns.IS_MUSIC)
        if (cursor != null) {
            var position = 1
            while (cursor!!.moveToNext()) {

                var name = cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                var singer = cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val path = cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                val size  = cursor!!.getLong(cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                if (size > 1000 * 800) {
                    // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
                    if (name.contains("-")) {
                        val str = name.split("-")
                        name = str[0]
                        singer = str[1]
                    }
                    list.add(Song(songName = name,singerName = singer,path = path,position = position))
                    position++
                }
            }
            // 释放资源
            cursor!!.close()
        }
        view.onData(list)
    }

    /**
     * 定义一个方法用来格式化获取到的时间
     */
    fun formatTime(time: Int): String {
        return if (time / 1000 % 60 < 10) {
            (time / 1000 / 60).toString() + ":0" + time / 1000 % 60

        } else {
            (time / 1000 / 60).toString() + ":" + time / 1000 % 60
        }
    }
}