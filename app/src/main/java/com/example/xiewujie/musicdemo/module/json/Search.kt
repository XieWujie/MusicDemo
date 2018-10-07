package com.example.xiewujie.musicdemo.module.json



data class Search(
    val code: Int,
    val data: SearchData,
    val message: String,
    val notice: String,
    val subcode: Int,
    val time: Int,
    val tips: String
)

data class SearchData(
    val keyword: String,
    val priority: Int,
    val qc: List<Any>,
    val semantic: Semantic,
    val song: SearchSong,
    val tab: Int,
    val taglist: List<Any>,
    val totaltime: Int,
    val zhida: Zhida
)

data class Semantic(
    val curnum: Int,
    val curpage: Int,
    val list: List<Any>,
    val totalnum: Int
)

data class SearchSong(
    val curnum: Int,
    val curpage: Int,
    val list: List<SearchX>,
    val totalnum: Int
)

data class SearchX(
    val action: Action,
    val album: Album,
    val chinesesinger: Int,
    val desc: String,
    val desc_hilight: String,
    val docid: String,
    val file: File,
    val fnote: Int,
    val genre: Int,
    val grp: List<Any>,
    val id: Int,
    val index_album: Int,
    val index_cd: Int,
    val interval: Int,
    val isonly: Int,
    val ksong: Ksong,
    val language: Int,
    val lyric: String,
    val lyric_hilight: String,
    val mid: String,
    val mv: Mv,
    val name: String,
    val newStatus: Int,
    val nt: Int,
    val pay: Pay,
    val pure: Int,
    val singer: List<Singer>,
    val status: Int,
    val subtitle: String,
    val t: Int,
    val tag: Int,
    val time_public: String,
    val title: String,
    val title_hilight: String,
    val type: Int,
    val url: String,
    val ver: Int,
    val volume: Volume
)

data class Volume(
    val gain: Double,
    val lra: Double,
    val peak: Double
)

data class Mv(
    val id: Int,
    val vid: String
)


data class Ksong(
    val id: Int,
    val mid: String
)

data class Zhida(
    val type: Int,
    val zhida_mv: ZhidaMv
)

data class ZhidaMv(
    val desc: String,
    val id: Int,
    val pic: String,
    val publish_date: String,
    val title: String,
    val vid: String
)