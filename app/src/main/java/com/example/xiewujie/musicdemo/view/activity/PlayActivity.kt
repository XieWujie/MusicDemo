package com.example.xiewujie.musicdemo.view.activity


import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.example.xiewujie.musicdemo.R
import com.example.xiewujie.musicdemo.module.http.HttpMathods
import com.example.xiewujie.musicdemo.module.json.Song
import com.example.xiewujie.musicdemo.tool.CurrentSongsProvider
import com.example.xiewujie.musicdemo.view.myview.LyricView
import com.example.xiewujie.musicdemo.view.myview.PlayTimeVIew
import okhttp3.ResponseBody
import rx.Subscriber

class PlayActivity :BaseActivity(),View.OnClickListener{

    lateinit var playTimeView: PlayTimeVIew
    lateinit var beforeView:ImageView
    lateinit var pauseOrStartView:ImageView
    lateinit var nextView: ImageView
    lateinit var lyricView: LyricView
    lateinit var playLayout: RelativeLayout
    val broadcast = SongMessageBroadcast()
    private var isHaveLyric = false
    var currentSong:Song? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21){
            window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_play)
        initView()
        registerMyBroadcast()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcast)
    }
    fun initView(){
        playTimeView = findViewById(R.id.play_time_view)
        beforeView = findViewById(R.id.play_before)
        pauseOrStartView = findViewById(R.id.play_pause_start)
        playLayout = findViewById(R.id.play_layout)
        nextView = findViewById(R.id.play_next)
        lyricView = findViewById(R.id.play_lyric_view)
        beforeView.setOnClickListener(this)
        pauseOrStartView.setOnClickListener(this)
        nextView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.play_before ->playTimeView.before()
            R.id.play_next ->playTimeView.next()
            R.id.play_pause_start ->{
                if (pauseOrStartView.isSelected){
                    playTimeView.pause()
                    pauseOrStartView.isSelected = false
                }else{
                    playTimeView.start()
                    pauseOrStartView.isSelected = true
                }
            }
        }
    }

    fun registerMyBroadcast(){
        val intentFilter = IntentFilter()
        intentFilter.addAction(SONG_MESSAGE_BROADCAST)
        registerReceiver(broadcast,intentFilter)
    }



    inner class SongMessageBroadcast:BroadcastReceiver(){

        var oldDuration = 0
        var currentIndex = -1
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent==null)return
            val duration = intent.getIntExtra(ACTION_DURATION,0)
            val nowTime = intent.getIntExtra(ACTION_NOW_TIME,0)
            val index = intent.getIntExtra(ACTION_INDEX,0)
            if (oldDuration != duration){
                playTimeView.initAllTime(duration)
                oldDuration = duration
                pauseOrStartView.isSelected = true
            }
            if (nowTime%10 == 0) {
                playTimeView.initNowTime(nowTime/10)
            }

            if (currentIndex != index){
                currentSong = CurrentSongsProvider.getSongs()[index%CurrentSongsProvider.getSongs().size]
                getSongPic(currentSong?.albumid)
                currentIndex = index
                isHaveLyric = false
            }

            if (!isHaveLyric){
                val timeList = currentSong?.timeList
                if (timeList?.size==0){
                    return
                }else {
                   lyricView.initLyric(timeList!!)
                    isHaveLyric = true
                }
            }else{
                lyricView.scrollLyric(nowTime)
            }
        }
    }

    companion object {
        val SONG_MESSAGE_BROADCAST = "song_broadcast"
        val ACTION_DURATION = "action_duration"
        val ACTION_NOW_TIME = "action_now_time"
        val ACTION_INDEX = "action_index"
    }

    fun getSongPic(id:Int?){
        if (id==null)return
        val subscriber = object : Subscriber<ResponseBody>(){
            override fun onNext(t: ResponseBody?) {
                val bitmap = BitmapFactory.decodeStream(t?.byteStream())
               window.decorView.background = BitmapDrawable(resources,blurImageAmeliorate(bitmap))
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
            }
        }
        HttpMathods.getPicService(subscriber,id,500)
    }

    private fun blurImageAmeliorate(bmp: Bitmap): Bitmap {
        // 高斯矩阵
        val gauss = intArrayOf(1, 2, 1, 2, 4, 2, 1, 2, 1)

        val width = bmp.width
        val height = bmp.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        var pixR = 0
        var pixG = 0
        var pixB = 0

        var pixColor = 0

        var newR = 0
        var newG = 0
        var newB = 0

        val delta = 32 // 值越小图片会越亮，越大则越暗

        var idx = 0
        val pixels = IntArray(width * height)
        bmp.getPixels(pixels, 0, width, 0, 0, width, height)
        var i = 1
        val length = height - 1
        while (i < length) {
            var k = 1
            val len = width - 1
            while (k < len) {
                idx = 0
                for (m in -1..1) {
                    for (n in -1..1) {
                        pixColor = pixels[(i + m) * width + k + n]
                        pixR = Color.red(pixColor)
                        pixG = Color.green(pixColor)
                        pixB = Color.blue(pixColor)

                        newR = newR + pixR * gauss[idx]
                        newG = newG + pixG * gauss[idx]
                        newB = newB + pixB * gauss[idx]
                        idx++
                    }
                }

                newR /= delta
                newG /= delta
                newB /= delta

                newR = Math.min(255, Math.max(0, newR))
                newG = Math.min(255, Math.max(0, newG))
                newB = Math.min(255, Math.max(0, newB))

                pixels[i * width + k] = Color.argb(255, newR, newG, newB)

                newR = 0
                newG = 0
                newB = 0
                k++
            }
            i++
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }
}
