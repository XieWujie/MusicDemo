package com.example.xiewujie.musicdemo.module.json


data class SearchAutoComplete(
    val code: Int,
    val data: AutoData,
    val subcode: Int
)

data class AutoData(
    val album: AutoAlbum,
    val mv: AutoMv,
    val singer: AutoSinger,
    val song: AutoSong
)

data class AutoMv(
    val count: Int,
    val itemlist: List<Itemlist>,
    val name: String,
    val order: Int,
    val type: Int
)

data class Itemlist(
    val docid: String,
    val id: String,
    val mid: String,
    val name: String,
    val singer: String,
    val vid: String
)

data class AutoAlbum(
    val count: Int,
    val itemlist: List<Itemlist>,
    val name: String,
    val order: Int,
    val type: Int
)



data class AutoSinger(
    val count: Int,
    val itemlist: List<Itemlist>,
    val name: String,
    val order: Int,
    val type: Int
)

data class AutoSong(
    val count: Int,
    val itemlist: List<Itemlist>,
    val name: String,
    val order: Int,
    val type: Int
)
