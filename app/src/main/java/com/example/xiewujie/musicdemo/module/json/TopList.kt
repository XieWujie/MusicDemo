package com.example.xiewujie.musicdemo.module.json


data class TopList(
    val GroupID: Int,
    val GroupName: String,
    val List: List<TopX>,
    val Type: Int
)

data class TopX(
    val ListName: String,
    val MacDetailPicUrl: String,
    val MacListPicUrl: String,
    val headPic_v12: String,
    val listennum: Int,
    val pic: String,
    val pic_Detail: String,
    val pic_h5: String,
    val pic_v11: String,
    val pic_v12: String,
    val showtime: String,
    val songlist: List<Songlist>,
    val topID: Int,
    val type: Int,
    val update_key: String
)

data class Songlist(
    val singerid: Int,
    val singername: String,
    val songid: Int,
    val songname: String
)