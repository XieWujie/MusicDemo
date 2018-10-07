package com.example.xiewujie.musicdemo.view.service

import android.app.Service
import android.content.*
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Base64
import com.example.xiewujie.musicdemo.module.http.HttpMathods
import com.example.xiewujie.musicdemo.module.json.Lyric
import com.example.xiewujie.musicdemo.module.json.LyricTime
import com.example.xiewujie.musicdemo.module.json.Song
import com.example.xiewujie.musicdemo.tool.CurrentSongsProvider
import com.example.xiewujie.musicdemo.tool.SongInfoListener
import com.example.xiewujie.musicdemo.view.activity.PlayActivity
import rx.Subscriber
import java.util.*
import java.util.regex.Pattern


class PlayMusicService : Service() {

    private val mediaPlayer = MediaPlayer()
    private val bind = PlayBind()
    var currentIndex = 0
    private var isPlayComplete = true
    val intent = Intent()

    init {
        intent.action = PlayActivity.SONG_MESSAGE_BROADCAST
    }
    val countDownTimer = object :CountDownTimer(1000000,100){
        override fun onFinish() {

        }

        override fun onTick(millisUntilFinished: Long) {
            intent.putExtra(PlayActivity.ACTION_DURATION,mediaPlayer.duration/1000)
            intent.putExtra(PlayActivity.ACTION_NOW_TIME,mediaPlayer.currentPosition/100)
            intent.putExtra(PlayActivity.ACTION_INDEX,currentIndex)

            sendBroadcast(intent)
        }
    }

    override fun onBind(intent: Intent): IBinder {
        registerMyReceiver()
        return bind
    }

    inner class PlayBind:Binder(){

        fun initMusic(song:Song){
            var url = song.path

            if (url.isNullOrBlank()){
                url = getRealUrl(songmid = song.songmid)
            }
            isPlayComplete = true
            mediaPlayer.reset()
            mediaPlayer.setDataSource(this@PlayMusicService,Uri.parse(url))
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer.prepareAsync()
            initCurrentSongIndex(song)
            getLyric(song)
        }

        fun initCurrentSongIndex(song: Song){
            val index = CurrentSongsProvider.getSongs().indexOf(song)
            if (index==-1){
                CurrentSongsProvider.addSong(song)
                currentIndex = CurrentSongsProvider.getSongs().size-1
            }else{
                currentIndex = index
            }
        }
        fun start(){
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()
                sendSongMessage()
            }
            mediaPlayer.setOnCompletionListener {
                if (isPlayComplete){
                    playOther(1)
                }
            }
        }

        fun getCurrentSong() = CurrentSongsProvider.getSongs()[currentIndex]



        fun start(isFirst:Boolean){
            if (!isFirst){
                mediaPlayer.start()
                sendSongMessage()
            }
        }

        fun seekTo(second:Int){
            mediaPlayer.seekTo(second*1000)
        }

        fun pause(){
            countDownTimer.cancel()
            mediaPlayer.pause()
        }
        fun playOther(one:Int){
            playByIndex(currentIndex+one)
        }

        fun playByIndex(index: Int){
            isPlayComplete = false
            countDownTimer.cancel()
            CurrentSongsProvider.getSongByIndex(index,object :SongInfoListener{
                override fun onFinish(song: Song?) {
                    bind.initMusic(song!!)
                    bind.start()
                }

                override fun onFail(e: Throwable?) {

                }
            })
            }
    }

    private fun sendSongMessage(){
        countDownTimer.start()
    }
    private fun getRealUrl(songmid:String) = "http://ws.stream.qqmusic.qq.com/C100$songmid.m4a?fromtag=0&guid=126548448"

    private fun registerMyReceiver(){
        val broadcast = MyMusicBroadcast()
        val intentFilter = IntentFilter()
        intentFilter.addAction(PlayMusicService.PLAY_MUSIC_SERVICE)
        registerReceiver(broadcast,intentFilter)
    }

    companion object {
        val PLAY_MUSIC_SERVICE = "music_service"
        val PLAY_MUSIC_ACTION = "music_action"
        val ACTION_BEFORE = "action_before"
        val ACTION_PAUSE = "action_pause"
        val ACTION_START = "action_start"
        val ACTION_NEXT = "action_next"
        val SEEK_TO_SECOND = "seek_to_second"
        val PLAY_INDEX = "play_index"
    }
    inner class MyMusicBroadcast: BroadcastReceiver(){

        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.getStringExtra(PlayMusicService.PLAY_MUSIC_ACTION)
            val second = intent?.getIntExtra(SEEK_TO_SECOND,-1)
            val index = intent?.getIntExtra(PLAY_INDEX,-1)
            when (action) {
                ACTION_START -> {
                    bind?.start(false)
                }
                ACTION_PAUSE -> {
                    bind?.pause()
                }
                ACTION_NEXT ->bind.playOther(1)
                ACTION_BEFORE ->bind.playOther(-1)
            }
            if (index !=-1&&index!=null){
                bind.playByIndex(index)
            }
            if (second!=null&&second!=-1){
                bind.seekTo(second)
            }
        }
    }

    private fun getLyric(song:Song){
        val subscriber = object : Subscriber<Lyric>(){

            override fun onNext(l: Lyric?) {
                if (l ==null)return
                if (song.timeList.size!=0) {
                    return
                }
                val lyricStr = String(Base64.decode(l.lyric, Base64.DEFAULT), charset("utf-8"))
                val lrcArray = lyricStr.split("\\n|\r|\t".toRegex())
                val timeRegex = "\\[(\\d{2}):(\\d{2})\\.(\\d{2})\\](.+)"
                val timePattern = Pattern.compile(timeRegex)
                var str = ""

                for (i in 0 until lrcArray.size){
                    str = lrcArray[i]
                    val timeMatcher = timePattern.matcher(str)
                    if (timeMatcher.find()&&timeMatcher.groupCount()==4){
                        val time = timeMatcher.group(1).toInt()*60*10+
                                timeMatcher.group(2).toInt()*10+timeMatcher.group(3).toInt()/10
                        song.timeList.add(LyricTime(time,timeMatcher.group(4)))
                    }
                }
            }
            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

                e?.printStackTrace()
            }
        }
        HttpMathods.getLyricService(subscriber,song.songmid,getHeaders())
    }

    private fun getHeaders():Map<String,String>{
        val map = HashMap<String,String>()
        map["User-Agent"] = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36"
        map["Accept"] = "*/*"
        map["Referer"] = "https://y.qq.com/portal/player.html"
        map[ "Accept-Language"] = "zh-CN,zh;q=0.8"
        map["Cookie"] =  "pgv_pvid=8455821612; ts_uid=1596880404; pgv_pvi=9708980224; yq_index=0; pgv_si=s3191448576; pgv_info=ssid=s8059271672; ts_refer=ADTAGmyqq; yq_playdata=s; ts_last=y.qq.com/portal/player.html; yqq_stat=0; yq_playschange=0; player_exist=1; qqmusic_fromtag=66; yplayer_open=1"
        map["Host"] = "c.y.qq.com"
        return map
    }
}


