package com.example.xiewujie.musicdemo.tool

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream


object CondenseImage {
    fun decodeBitmapFromResources(resources: Resources, res: Int, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, res, options)
        options.inSampleSize = getInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(resources, res, options)
    }

    fun decodeBitmapFromInputStream(`in`: InputStream, reqWidth: Int, reqHeight: Int): Bitmap {
        val inputStream = BufferedInputStream(`in`) //BufferedInputStream 支持来回读写操作
        inputStream.mark(Integer.MAX_VALUE)//标记流的初始位置
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream, null, options)
        options.inSampleSize = getInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        try {
            inputStream.reset() //重置读写时流的初始位置
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return BitmapFactory.decodeStream(inputStream, null, options)

    }

    private fun getInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val oldWidth = options.outWidth
        val oldHeight = options.outHeight
        var inSampleSize = 1
        if (oldHeight > reqHeight || oldWidth > reqWidth) {
            val haftWidth = oldWidth / 2
            val haftHeight = oldHeight / 2
            while (haftHeight / inSampleSize >= reqHeight && haftWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}