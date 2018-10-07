package com.example.xiewujie.musicdemo.view.fragment

import com.example.xiewujie.musicdemo.module.json.Song


interface SongListListener{
    fun getSongs():ArrayList<Song>
}