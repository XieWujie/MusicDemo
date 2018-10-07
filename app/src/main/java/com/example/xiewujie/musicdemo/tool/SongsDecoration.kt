package com.example.xiewujie.musicdemo.tool

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class SongsDecoration(val width:Float = 1f,val isHaveDecoration:Boolean = true):RecyclerView.ItemDecoration(){

    lateinit var paint: Paint

    init {
        paint = Paint()
        paint.style = Paint.Style.FILL
        paint.strokeWidth = width
        paint.color = Color.parseColor("#99999999")
    }
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.top = width.toInt()

    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (isHaveDecoration) {
            val size = parent.childCount
            for (i in 0 until size) {
                val view = parent.getChildAt(i)
                c.drawLine(view.left.toFloat(), view.bottom.toFloat(), view.right.toFloat(), view.bottom + 1.toFloat(), paint)
            }
        }
    }
}