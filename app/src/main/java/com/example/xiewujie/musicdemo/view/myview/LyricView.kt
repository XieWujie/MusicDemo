package com.example.xiewujie.musicdemo.view.myview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.xiewujie.musicdemo.module.json.LyricTime


class LyricView:View {

    private lateinit var selectPaint: TextPaint
    private lateinit var unSelectPaint: TextPaint
    private val spaceHeight = resources.displayMetrics.density*50
    private var halfWidth = 0f
    private val timeMap = HashMap<Int, Float>()
    private val lyrics = ArrayList<LyricTime>();
    private var currentTime = 0

    constructor(context: Context) : super(context) {
        initPaint()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initPaint()
    }

    private fun initPaint() {
        selectPaint = TextPaint()
        selectPaint.color = Color.parseColor("#ffffff")
        selectPaint.strokeWidth = 2f
        selectPaint.style = Paint.Style.FILL
        selectPaint.textAlign = Paint.Align.CENTER
        selectPaint.textSize = 40f
        unSelectPaint = TextPaint()
        unSelectPaint.color = Color.parseColor("#999999")
        unSelectPaint.strokeWidth = 2f
        unSelectPaint.textSize = 40f
        unSelectPaint.style = Paint.Style.FILL
        unSelectPaint.textAlign = Paint.Align.CENTER
    }

    fun initLyric(list: List<LyricTime>) {
        post {
            lyrics.clear()
            lyrics.addAll(list)
            halfWidth = measuredWidth / 2f;
            currentTime = 0
            timeMap.clear()
            invalidate()
        }
    }

    private fun drawLyricFirst(canvas: Canvas) {
        var position = 0f
        val firstPosition = measuredHeight/2
        lyrics.forEachIndexed { i, lyric ->
            position = spaceHeight * (i + 1)
            timeMap[lyric.time] = position - firstPosition
            if (currentTime != lyric.time) {
                canvas.drawText(lyric.content, halfWidth, position, unSelectPaint)
            }else{
                canvas.drawText(lyric.content, halfWidth, position, selectPaint)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
        drawLyricFirst(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return super.onTouchEvent(event)
    }

    fun scrollLyric(time: Int) {
        post {
            if (timeMap.containsKey(time)){
                val position = timeMap[time]!!.toInt()
                scrollTo(0,position)
                currentTime = time
                //postInvalidate(0,position-20,measuredWidth,position+20)
                invalidate()
            }
        }
    }
}