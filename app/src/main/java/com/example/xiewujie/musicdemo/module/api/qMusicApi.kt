package com.example.xiewujie.musicdemo.module.api


import com.example.xiewujie.musicdemo.module.json.*
import okhttp3.ResponseBody
import retrofit2.http.*
import rx.Observable


interface TopService{
    @GET("v8/fcg-bin/fcg_v8_toplist_cp.fcg?tpl=3&page=detail&type=top&song_begin=0&song_num=30&g_tk=5381&loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0")

   fun getService(@Query("topid")topid:Int):Observable<Songs>
}

interface PicService{
    @GET("music/photo/album_{width_1}/{id_1}/{width}_albumpic_{id}_0.jpg")
     fun getService(@Path("id")id:Int,@Path("width")width:Int,@Path("width_1")width1:Int = width,@Path("id_1")id_1:Int = id%100):Observable<ResponseBody>
}

interface LyricService{


    @GET("lyric/fcgi-bin/fcg_query_lyric_new.fcg?callback=MusicJsonCallback_lrc&pcachetime=1537697199869&g_tk=5381&loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0")
     fun getService(@Query("songmid")id:String,@HeaderMap map: Map<String,String>):Observable<Lyric>
}

interface SearchService{

    @GET("soso/fcgi-bin/client_search_cp?ct=24&qqmusic_ver=1298&new_json=1&remoteplace=txt.yqq.song&searchid=62858079080690086&t=0&aggr=1&cr=1&catZhida=1&lossless=0&flag_qc=0&g_tk=5381&loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0")
     fun getService(@Query("w")key:String,@Query("p")page:Int,@Query("n")number:Int):Observable<Search>
}

interface SearchAutoCompleteService{
    @GET("splcloud/fcgi-bin/smartbox_new.fcg?is_xml=0&format=jsonp&loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0")
      fun getService(@Query("key")key: String):Observable<SearchAutoComplete>
}

interface TopListService{
    @GET("v8/fcg-bin/fcg_v8_toplist_opt.fcg?page=index&format=html&tpl=macv4&v8debug=1&jsonCallback=jsonCall")
     fun getService():Observable<TopList>
}

interface HotKeyService{
    @GET("/splcloud/fcgi-bin/gethotkey.fcg?g_tk=5381&loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0")
    fun getService():Observable<HotKey>
}

interface SongInfoService{
    @GET("/cgi-bin/musicu.fcg?g_tk=1640663555&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0")
    fun getService(@Query("data")data:String):Observable<SongInfo>
}

