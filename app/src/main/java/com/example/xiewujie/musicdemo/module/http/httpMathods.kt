package com.example.xiewujie.musicdemo.module.http

import android.util.Log
import com.example.xiewujie.musicdemo.module.CallbackToJsonConvertFactory
import com.example.xiewujie.musicdemo.module.api.*
import com.example.xiewujie.musicdemo.module.json.*
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Function
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.math.log

object HttpMathods{
    const val SONG_LIST_BASE_URL = "https://c.y.qq.com/"
    const val SONG_INFO_BASE_URL = "https://u.y.qq.com/"
    const val DEFAULT_TIMEOUT = 10
    const val PIC_BASE_RRL = "http://imgcache.qq.com/"
    const val SONG_LYRIC = "http://music.qq.com/"

    private val builder = OkHttpClient.Builder()
    private val songlistRt by lazy<Retrofit>{ initRetrofit(SONG_LIST_BASE_URL) }
    private val picRt by lazy<Retrofit> { initRetrofit(PIC_BASE_RRL) }
    private val lyricRt by lazy<Retrofit> { initRetrofit(SONG_LIST_BASE_URL) }
    private val songInfoRt by lazy<Retrofit>{ initRetrofit(SONG_INFO_BASE_URL) }

    init {
        builder.connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
    }

    private fun initRetrofit(basrUrl:String):Retrofit{
        return Retrofit.Builder()
                .client(builder.build())
                .baseUrl(basrUrl)
                .addConverterFactory(CallbackToJsonConvertFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
    }
    fun getTopService(subscriber: Subscriber<Song>,topid:Int){
        var position = 0
        val observable = songlistRt.create(TopService::class.java!!)
                .getService(topid)
                .flatMap{
                    Observable.from(it.songlist)
                }
                .map {
                    if (it.cur_count.toInt()<=100){
                        position = it.cur_count.toInt()
                    }else{
                        position++
                    }
                    Song(it.data.songid,it.data.songmid,it.data.albumid,
                            position = position,
                            songName = it.data.songname
                            ,singerName = it.data.singer[0].name) }
        toSubscribe(observable,subscriber)
    }

    fun getPicService(subscriber: Subscriber<ResponseBody>,id:Int,width:Int){
        val observable = picRt.create(PicService::class.java)
                .getService(id,width)
        toSubscribe(observable,subscriber)
    }

    fun getLyricService(subscriber: Subscriber<Lyric>, id:String, headers: Map<String,String>){
        val observable = lyricRt.create(LyricService::class.java)
                .getService(id,headers)
        toSubscribe(observable,subscriber)
    }

    fun getSearchService(subscriber: Subscriber<Song>,key:String,page:Int,number:Int){
        val observable = songlistRt.create(SearchService::class.java)
                .getService(key,page,number)
                .flatMap { Observable.from(it.data.song.list) }
                .map { Song(it.id,it.mid,it.album.id,songName = it.name,singerName = it.singer[0].name) }
        toSubscribe(observable,subscriber)
    }
    fun getAutoCompleteService(subscriber: Subscriber<Song>,key:String){
        val observable = songlistRt.create(SearchAutoCompleteService::class.java)
                .getService(key)
                .flatMap { Observable.from(it.data.song.itemlist) }
                .map {
                    Log.d("songinfo",it.id)
                    Song(it.id.toInt(),it.mid,-1,songName = it.name,singerName = it.singer) }
        toSubscribe(observable,subscriber)
    }

    fun getTopListService(subscriber: Subscriber<TopList>){
        val observable = songlistRt.create(TopListService::class.java)
                .getService()
        toSubscribe(observable,subscriber)
    }

    fun getHotKeyService(subscriber: Subscriber<String>){
        val observable = songlistRt.create(HotKeyService::class.java)
                .getService()
                .flatMap { Observable.from(it.data.hotkey) }
                .map { it.k }
        toSubscribe(observable,subscriber)
    }

    fun getSongInfo(subscriber: Subscriber<Song>,songmid:String,songid:Int){
        val iData = IData(SongI(param = Param(song_id = songid,song_mid = songmid)))
        val data = Gson().toJson(iData,IData::class.java)
        Log.d("songinfo",data)
        val observable = songInfoRt.create(SongInfoService::class.java)
                .getService(data)
                .map {
                    Log.d("songInfo",it.toString())
                    Song(songmid = songmid,songid = songid,
                        singerName = it.songinfo.data.track_info.singer[0].name,
                        albumid = it.songinfo.data.track_info.album.id,
                        songName = it.songinfo.data.track_info.name) }
        toSubscribe(observable,subscriber)
    }
     private fun<T>  toSubscribe(observable: Observable<T>, s: Subscriber<T>) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(s)
    }
}