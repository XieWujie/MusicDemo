package com.example.xiewujie.musicdemo.module.http

import android.graphics.Bitmap
import android.os.Handler
import android.widget.ImageView
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import android.os.Looper
import com.example.xiewujie.musicdemo.tool.CondenseImage
import okhttp3.Request
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor


object LoadUtil{

    private val CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1 //线程池的核心线程数
    private val MAX_POOL_SIZE = CORE_POOL_SIZE * 2 + 1 //线程池所能容纳的最大线程数
    val EXECUTORS: ExecutorService = ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 1, TimeUnit.SECONDS, LinkedBlockingDeque<Runnable>())
    private val errorUrl: String? = null
    private val handler = Handler(Looper.getMainLooper())
    val LRUCACHE = 0
    val DISCACHE = 1
    val DOUBLE_CACHE = 2
    val okHttpClient = OkHttpClient()

    init {

    }
    fun loadImage(image:ImageView,url:String){
        loadImageFromNet(image,url)
    }

    fun loadImageFromNet(image:ImageView,url: String){
        val runnable = Runnable {
            val request = Request.Builder()
                    .url(url)
                    .build()
            val response =  okHttpClient.newCall(request).execute()
            val inputString = response.body()?.byteStream()
            if (inputString!=null) {
                val bitmap = CondenseImage.decodeBitmapFromInputStream(inputString,200,200)
                setImageView(image,url,bitmap)
            }
        }
        EXECUTORS.submit(runnable)
    }

    fun setImageView(image:ImageView,url: String,bitmap: Bitmap){
        handler.post { image.setImageBitmap(bitmap) }
    }
}