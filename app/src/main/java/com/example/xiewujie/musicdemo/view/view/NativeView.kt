package com.example.xiewujie.musicdemo.view.view

import com.example.xiewujie.musicdemo.module.json.Song

interface NativeView:BaseView{
    fun onData(songs:List<Song>)
}