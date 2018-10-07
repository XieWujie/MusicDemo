package com.example.xiewujie.musicdemo.view.view

import com.example.xiewujie.musicdemo.module.json.TopList

interface TopListView:BaseView{
    fun onData(topList: TopList)
}