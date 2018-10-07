package com.example.xiewujie.musicdemo.view.view

import com.example.xiewujie.musicdemo.module.json.Song

interface SearchView:BaseView{
    fun onData(song: Song)

    fun onHotKey(keys:List<String>)

}