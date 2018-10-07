package com.example.xiewujie.musicdemo.module.json

data class Songs(
    val code: Int,
    val color: Int,
    val comment_num: Int,
    val cur_song_num: Int,
    val date: String,
    val day_of_year: String,
    val song_begin: Int,
    val songlist: List<TopSong>,
    val topinfo: Topinfo,
    val total_song_num: Int,
    val update_time: String
)

data class Topinfo(
    val ListName: String,
    val MacDetailPicUrl: String,
    val MacListPicUrl: String,
    val UpdateType: String,
    val albuminfo: String,
    val headPic_v12: String,
    val info: String,
    val listennum: Int,
    val pic: String,
    val picDetail: String,
    val pic_album: String,
    val pic_h5: String,
    val pic_v11: String,
    val pic_v12: String,
    val topID: String,
    val type: String
)

data class TopSong(
    val Franking_value: String,
    val cur_count: String,
    val data: Data,
    val in_count: String,
    val mb: String,
    val old_count: String,
    val vid: Vid
)

data class Vid(
    val Fmv_id: String,
    val Fstatus: String,
    val Fvid: String
)

data class Data(
    val albumdesc: String,
    val albumid: Int,
    val albummid: String,
    val albumname: String,
    val alertid: Int,
    val belongCD: Int,
    val cdIdx: Int,
    val interval: Int,
    val isonly: Int,
    val label: String,
    val msgid: Int,
    val pay: Pay,
    val rate: Int,
    val singer: List<Singer>,
    val size128: Int,
    val size320: Int,
    val size5_1: Int,
    val sizeape: Int,
    val sizeflac: Int,
    val sizeogg: Int,
    val songid: Int,
    val songmid: String,
    val songname: String,
    val songorig: String,
    val songtype: Int,
    val strMediaMid: String,
    val stream: Int,
    val switch: Int,
    val type: Int,
    val vid: String
)


data class Pay(
    val payalbum: Int,
    val payalbumprice: Int,
    val paydownload: Int,
    val payinfo: Int,
    val payplay: Int,
    val paytrackmouth: Int,
    val paytrackprice: Int,
    val timefree: Int
)

data class Singer3(
    val Fgenre: String,
    val Fsinger_id: String,
    val Fsinger_mid: String,
    val Fsinger_name: String,
    val Ftype: String
)

