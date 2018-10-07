package com.example.xiewujie.musicdemo.view.myview

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.example.xiewujie.musicdemo.module.json.LyricTime
import com.example.xiewujie.musicdemo.module.json.Song


class SongMessageTextView:TextView{

    private var song:Song? = null
    private val timeMap = HashMap<Int,String>()
    constructor(context: Context):super(context){

    }
    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet){

    }

    fun initSinger(singerName:String){
        post {
            text = singerName
        }
    }

    fun initLyric(timeList:List<LyricTime>){
        post {
            timeMap.clear()
            timeList?.forEach { timeMap[it.time] = it.content }
        }
    }

    fun initContent(time:Int){
        post {
            if (timeMap.containsKey(time)){
                text = timeMap[time]
            }
        }
    }
}