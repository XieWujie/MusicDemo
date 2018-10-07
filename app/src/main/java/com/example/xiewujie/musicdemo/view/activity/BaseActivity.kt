package com.example.xiewujie.musicdemo.view.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.xiewujie.musicdemo.R
import com.example.xiewujie.musicdemo.tool.MyService

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyService.setStatusBar(this)
    }
}
