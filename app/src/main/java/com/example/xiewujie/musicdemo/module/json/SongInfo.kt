package com.example.xiewujie.musicdemo.module.json


data class SongInfo(
    val songinfo: Sinfo,
    val code: Int,
    val ts: Long
)

data class Sinfo(
    val data: InfoData,
    val code: Int
)

data class InfoData(
    val info: Info,
    val extras: Extras,
    val track_info: TrackInfo
)

data class Info(
    val company: Company,
    val genre: Genre,
    val lan: Lan,
    val pub_time: PubTime
)

data class Lan(
    val title: String,
    val type: String,
    val content: List<Content>,
    val pos: Int,
    val more: Int,
    val selected: String,
    val use_platform: Int
)

data class Content(
    val id: Int,
    val value: String,
    val mid: String,
    val type: Int,
    val show_type: Int,
    val is_parent: Int,
    val picurl: String,
    val read_cnt: Int,
    val author: String,
    val jumpurl: String,
    val ori_picurl: String
)

data class PubTime(
    val title: String,
    val type: String,
    val content: List<Content>,
    val pos: Int,
    val more: Int,
    val selected: String,
    val use_platform: Int
)

data class Company(
    val title: String,
    val type: String,
    val content: List<Content>,
    val pos: Int,
    val more: Int,
    val selected: String,
    val use_platform: Int
)

data class Genre(
    val title: String,
    val type: String,
    val content: List<Content>,
    val pos: Int,
    val more: Int,
    val selected: String,
    val use_platform: Int
)

data class TrackInfo(
    val id: Int,
    val type: Int,
    val mid: String,
    val name: String,
    val title: String,
    val subtitle: String,
    val singer: List<Singer>,
    val album: Album,
    val mv: Mv,
    val interval: Int,
    val isonly: Int,
    val language: Int,
    val genre: Int,
    val index_cd: Int,
    val index_album: Int,
    val time_public: String,
    val status: Int,
    val fnote: Int,
    val file: File,
    val pay: Pay,
    val action: Action,
    val ksong: Ksong,
    val volume: Volume,
    val label: String,
    val url: String,
    val bpm: Int,
    val version: Int,
    val trace: String,
    val data_type: Int,
    val modify_stamp: Int,
    val pingpong: String
)

data class Action(
    val switch: Int,
    val msgid: Int,
    val alert: Int,
    val icons: Int,
    val msgshare: Int,
    val msgfav: Int,
    val msgdown: Int,
    val msgpay: Int
)

data class File(
    val media_mid: String,
    val size_24aac: Int,
    val size_48aac: Int,
    val size_96aac: Int,
    val size_192ogg: Int,
    val size_192aac: Int,
    val size_128mp3: Int,
    val size_320mp3: Int,
    val size_ape: Int,
    val size_flac: Int,
    val size_dts: Int,
    val size_try: Int,
    val try_begin: Int,
    val try_end: Int,
    val url: String,
    val size_hires: Int,
    val hires_sample: Int,
    val hires_bitdepth: Int,
    val b_30s: Int,
    val e_30s: Int
)

data class Singer(
    val id: Int,
    val mid: String,
    val name: String,
    val title: String,
    val type: Int,
    val uin: Int
)

data class Album(
    val id: Int,
    val mid: String,
    val name: String,
    val title: String,
    val subtitle: String,
    val time_public: String
)

data class Extras(
    val name: String,
    val transname: String,
    val subtitle: String,
    val from: String
)


data class IData(
    val songinfo: SongI
)

data class SongI(
    val method: String = "get_song_detail_yqq",
    val param: Param,
    val module: String = "music.pf_song_detail_svr"
)

data class Param(
    val song_type: Int = 0,
    val song_mid: String,
    val song_id: Int
)