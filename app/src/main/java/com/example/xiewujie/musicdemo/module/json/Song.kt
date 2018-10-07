package com.example.xiewujie.musicdemo.module.json

import android.util.Base64
import android.util.Log
import com.example.xiewujie.musicdemo.module.http.HttpMathods
import rx.Subscriber
import java.util.HashMap
import java.util.regex.Pattern

data class Song(
        val songid:Int = -1,
        val songmid:String = "",
        val albumid:Int = -1,
        val position:Int = -1,
        val songName:String,
        val singerName:String,
        val timeList:ArrayList<LyricTime> = ArrayList<LyricTime>(),
        val path:String? = null
)

data class LyricTime(val time:Int,val content:String)

