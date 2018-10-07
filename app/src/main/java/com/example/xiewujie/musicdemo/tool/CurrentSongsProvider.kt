package com.example.xiewujie.musicdemo.tool

import com.example.xiewujie.musicdemo.module.http.HttpMathods
import com.example.xiewujie.musicdemo.module.json.Song
import rx.Subscriber

object CurrentSongsProvider{
    private val songs = ArrayList<Song>()
    private var currentIndex = 0

    fun initSongs(list: List<Song>){
        songs.clear()
        songs.addAll(list)
    }
    fun getSongs() = songs

    fun addSong(song: Song):Int{
        songs.add(song)
        return songs.indexOf(song)
    }

    fun getSongByIndex(index:Int,listener: SongInfoListener){
        val song = songs[index% songs.size]
        currentIndex= songs.indexOf(song)
        if (song.songmid.isNullOrEmpty()){
          getSongInfo(song.songName,song.singerName,listener)
        }else{
            listener.onFinish(song)
        }
    }


    fun getSongInfo(songName:String,singerName:String,callBack:SongInfoListener){
        val subscriber = object : Subscriber<Song>(){

            var isGetFirst = false
            override fun onNext(t: Song?) {
                if (t!=null){
                    if (!isGetFirst) {
                        getSongInfo(t.songid, t.songmid, callBack)
                        isGetFirst = true
                    }
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
                callBack.onFail(e)
            }
        }
        HttpMathods.getAutoCompleteService(subscriber,songName+singerName)
    }

    fun getSongInfo(songid:Int,songmid:String,callBack: SongInfoListener){
        val subscriber = object : Subscriber<Song>(){
            override fun onNext(t: Song?) {
                if (t!=null){
                    songs[currentIndex] = t
                    callBack.onFinish(t)
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
                callBack.onFail(e)
            }
        }

        HttpMathods.getSongInfo(subscriber,songmid,songid)
    }
}