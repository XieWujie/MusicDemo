package com.example.xiewujie.musicdemo.view.myview

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import android.widget.Chronometer
import android.widget.LinearLayout
import com.example.xiewujie.musicdemo.R
import com.example.xiewujie.musicdemo.view.service.PlayMusicService

class PlayTimeVIew:LinearLayout{

    private lateinit var playedTimeView:Chronometer
    private lateinit var playAllTImeView: Chronometer
    private lateinit var playBarView:PlayProgressView
    private var allSecond = 0
    constructor(context: Context):super(context){
        initView()
    }
    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet){
        initView()
    }

    fun initView(){
        View.inflate(context,R.layout.play_time_layout,this)
        playAllTImeView = findViewById(R.id.play_all_time)
        playedTimeView = findViewById(R.id.played_time)
        playBarView = findViewById(R.id.play_bar_view)
        playBarView.addPositionListener(object :PlayProgressView.PositionListener{
            override fun callBack(second: Int) {
                playedTimeView.base = SystemClock.elapsedRealtime() - second*1000
                val intent = Intent()
                intent.action = PlayMusicService.PLAY_MUSIC_SERVICE
                intent.putExtra(PlayMusicService.SEEK_TO_SECOND,second)
                context.sendBroadcast(intent)
            }
        })
    }
    fun initAllTime(allSecond:Int){
        this.allSecond = allSecond
        playBarView.initData(allSecond)
        playAllTImeView.base = SystemClock.elapsedRealtime() - allSecond*1000
    }
    fun start(){
        start(0)
    }

    fun initNowTime(time:Int){
        playedTimeView.base = SystemClock.elapsedRealtime() - time*1000
        playBarView.start(time)
    }

    fun start(time:Int){
        action(PlayMusicService.ACTION_START)
    }

    private fun action(action:String){
        val intent = Intent()
        intent.action = PlayMusicService.PLAY_MUSIC_SERVICE
        intent.putExtra(PlayMusicService.PLAY_MUSIC_ACTION,action)
        context.sendBroadcast(intent)
    }

    fun pause(){
        action(PlayMusicService.ACTION_PAUSE)
    }

    fun next(){
        action(PlayMusicService.ACTION_NEXT)
    }

    fun before(){
        action(PlayMusicService.ACTION_BEFORE)
    }
}