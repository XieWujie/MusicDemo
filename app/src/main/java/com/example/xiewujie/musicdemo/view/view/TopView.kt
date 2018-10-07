package com.example.xiewujie.musicdemo.view.view

import com.example.xiewujie.musicdemo.module.json.Song

interface TopView:BaseView{

    fun data(song:Song)
}