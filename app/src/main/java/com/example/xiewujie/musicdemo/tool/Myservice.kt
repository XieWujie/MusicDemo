package com.example.xiewujie.musicdemo.tool

import android.graphics.Color.parseColor
import android.view.WindowManager
import android.os.Build
import android.app.Activity
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.Color
import android.support.v4.content.ContextCompat.getSystemService


object MyService {


    fun getDisplayWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return windowManager.defaultDisplay.width
    }

    fun dip2px(mContext: Context, dp: Int): Int {
        val density = mContext.getResources().getDisplayMetrics().density
        return (dp * density + 0.5).toInt()
    }

    fun setStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.parseColor("#ff5CACFC")
        }
    }
}