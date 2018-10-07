package com.example.xiewujie.musicdemo.view.activity

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.*
import com.example.xiewujie.musicdemo.R
import com.example.xiewujie.musicdemo.module.json.Song
import com.example.xiewujie.musicdemo.presenter.MainPresenter
import com.example.xiewujie.musicdemo.tool.CurrentSongsProvider
import com.example.xiewujie.musicdemo.view.fragment.*
import com.example.xiewujie.musicdemo.view.holder.BaseHolder
import com.example.xiewujie.musicdemo.view.myview.SongMessageTextView
import com.example.xiewujie.musicdemo.view.service.PlayMusicService
import com.example.xiewujie.musicdemo.view.view.MainView

class MainActivity : BaseActivity(),BaseHolder.ItemClickCallBack,View.OnClickListener,MainView{

    lateinit var mainViewLayout: FrameLayout
    lateinit var songName:TextView
    lateinit var playView:ImageView
    lateinit var nextView: ImageView
    lateinit var bottomListView:ImageView
    lateinit var bottomPicLayout:RelativeLayout
    lateinit var bottomTextLayout:LinearLayout
    lateinit var bottomPicView:ImageView
    lateinit var btSongContentText:SongMessageTextView
    val fragmentManager = supportFragmentManager
    lateinit var transaction:FragmentTransaction
    var currentIndex = 0
    var currentSongsIndex = 2
    var fragments = arrayOfNulls<Fragment>(6)
    var playListIndex = -1
    var bind:PlayMusicService.PlayBind? = null
    val main_broadcast = MainReceiver()
    lateinit var presenter: MainPresenter
    val serviceConnection = object :ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {

        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            bind = service as PlayMusicService.PlayBind
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        presenter = MainPresenter(this,this)
        showFragment(MAIN)
        bindPlayService()
        registerMainBroadcast()
        getPermission()
    }

    fun bindPlayService(){
        val intent = Intent(this,PlayMusicService::class.java)
        this.bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE)
    }

    fun initView(){
        mainViewLayout = findViewById(R.id.main_view_layout)
        songName = findViewById(R.id.bottom_song_name)
        playView = findViewById(R.id.bottom_play)
        nextView = findViewById(R.id.bottom_next)
        bottomPicView = findViewById(R.id.bottom_singer_pic)
        bottomPicLayout = findViewById(R.id.bottom_song_pic_layout)
        bottomTextLayout = findViewById(R.id.bottom_text_layout)
        bottomListView = findViewById(R.id.bottom_list)
        btSongContentText = findViewById(R.id.bottom_song_content)
        bottomListView.setOnClickListener(this)
        playView.setOnClickListener(this)
        nextView.setOnClickListener(this)
        bottomPicLayout.setOnClickListener(this)
        bottomTextLayout.setOnClickListener(this)
    }

    fun showFragment(index:Int,topid:Int = -1) {
        currentIndex = fragments.indexOf(getCurrentFragment())
        if (currentIndex == index&&currentIndex!=0){
            return
        }
        if (fragments[index] == null) {
            createFragment(index)
        }
        val fragment = fragments[index]!!
        transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.main_view_layout,fragment)
        if (index!=MAIN) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
        if (fragment is SongTopFragment){
            currentSongsIndex = index
            fragment.initData(topid)
        }
    }

    fun getCurrentFragment():Fragment?{
        for (fragment in fragmentManager.fragments){
            if (fragment!=null&&fragment.isVisible){
                return fragment
            }
        }
        return null
    }

    fun createFragment(index: Int){
        when (index) {
            TOP_SONGS -> {
                val songTopFragment= SongTopFragment()
                fragments[index] = songTopFragment
                songTopFragment.addItemClickListener(this)
            }
            MAIN->{
                val mainFragment = MainFragment()
                fragments[index] = mainFragment
            }
            SEARCH_FRAGMENT->{
                fragments[index] = SearchFragment()
            }

            TOPLIST_FRAGMENT->{
                fragments[index] = TopListFragment()
            }
            NATIVE_FRAGMENT ->fragments[index] = NativeFragment()
            else -> Exception("Can not find fragment")
        }
    }

    override fun callBack(data: Any) {
        if (currentIndex!=playListIndex){
            initSongs()
        }
        if (data is Song){
             initSong(data)
        }
    }

    fun initSongs(){
        val fragment = fragments[currentSongsIndex] as SongListListener
        CurrentSongsProvider.initSongs( fragment.getSongs())
        playListIndex = currentIndex
    }

    fun initSong(song: Song?){
        if (song==null)return
        bind?.initMusic(song)
        bind?.start()
    }

    fun initBottom(){
        val song = bind?.getCurrentSong()
        if (song == null)return
        songName.text = song.songName
        playView.isSelected = true
        presenter.getSongPic(song.albumid,bottomPicView)
        btSongContentText.initSinger(song.singerName)
    }

    override fun onFail(e: Throwable) {

    }

    override fun onFinish() {

    }




    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bottom_play ->{
                if (playView.isSelected){
                    playView.isSelected = false
                    bind?.pause()
                }else{
                    playView.isSelected = true
                    bind?.start(false)
                }
            }
            R.id.bottom_list ->{
                presenter.showSongsList(CurrentSongsProvider.getSongs(),fragmentManager)
            }

            R.id.bottom_next ->{
                bind?.playOther(1)
            }
            R.id.bottom_song_pic_layout,R.id.bottom_text_layout ->{
                val intent = Intent(this,PlayActivity::class.java)
                intent.putExtra("SongIndex",currentIndex)
                startActivity(intent)
            }
        }
    }

   fun registerMainBroadcast(){
       val intentFilter = IntentFilter()
       intentFilter.addAction(PlayActivity.SONG_MESSAGE_BROADCAST)
       intentFilter.addAction(MAIN_BROADCAST_SERVICE)
       registerReceiver(main_broadcast,intentFilter)
   }

    inner class MainReceiver :BroadcastReceiver(){

        var currentLyricSize = 0
        var songIndex = -1

        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null)return
            if (intent.action == MAIN_BROADCAST_SERVICE){
                val fragmentSelect = intent.getIntExtra(ACTION_FRAGMENT,-1)
                val isDismissDialog = intent.getBooleanExtra(ACTION_DISMISS_DIALOG,false)
                val topid = intent.getIntExtra(TOPID,-1)
                when(fragmentSelect){
                   in 0..6 ->showFragment(fragmentSelect)
                }
                if (topid!=-1){
                    showFragment(TOP_SONGS,topid)
                }
                if (isDismissDialog){
                    presenter.dismissDialog()
                }
            }else if (intent.action == PlayActivity.SONG_MESSAGE_BROADCAST){
                val nowTime = intent.getIntExtra(PlayActivity.ACTION_NOW_TIME,0)
                val index = intent.getIntExtra(PlayActivity.ACTION_INDEX,0)
                if (songIndex!=index){
                    initBottom()
                    songIndex= index
                   currentLyricSize = 0
                }
                btSongContentText.initContent(nowTime)
                if (currentLyricSize == 0){
                    val timeList = bind?.getCurrentSong()?.timeList
                    currentLyricSize = timeList?.size?:0
                    if (currentLyricSize>0){
                        btSongContentText.initLyric(timeList!!)
                    }
                }
            }
        }
    }

    companion object {
        val ACTION_FRAGMENT = "action_fragment"
        val NATIVE_FRAGMENT = 4
        val MAIN_BROADCAST_SERVICE = "main_broadcast_service"
        val ACTION_DISMISS_DIALOG = "dismiss_dialog"
        val TOP_SONGS = 1
        val MAIN = 0
        val SEARCH_FRAGMENT = 2
        val TOPLIST_FRAGMENT = 3
        val TOPID = "topid"

    }

    fun getPermission(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            1->{
                if (permissions.size==0||grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"拒绝权限将不能正常使用程序", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}


