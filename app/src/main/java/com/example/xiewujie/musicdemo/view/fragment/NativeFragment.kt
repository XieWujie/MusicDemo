package com.example.xiewujie.musicdemo.view.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.xiewujie.musicdemo.R
import com.example.xiewujie.musicdemo.module.json.Song
import com.example.xiewujie.musicdemo.presenter.NativePresenter
import com.example.xiewujie.musicdemo.presenter.TopSongPresenter
import com.example.xiewujie.musicdemo.tool.CurrentSongsProvider
import com.example.xiewujie.musicdemo.tool.SongsDecoration
import com.example.xiewujie.musicdemo.view.activity.PlayActivity
import com.example.xiewujie.musicdemo.view.adapter.SongListAdapter
import com.example.xiewujie.musicdemo.view.holder.BaseHolder
import com.example.xiewujie.musicdemo.view.service.PlayMusicService
import com.example.xiewujie.musicdemo.view.view.BaseView
import com.example.xiewujie.musicdemo.view.view.NativeView


class NativeFragment: Fragment(),NativeView{


    private val adapter: SongListAdapter = SongListAdapter()
    private val list = ArrayList<Song>()
    private lateinit var recyclerView: RecyclerView
    lateinit var presenter: NativePresenter
    private lateinit var refreshView: SwipeRefreshLayout
    var itemClickListener: BaseHolder.ItemClickCallBack? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.song_list_layout,container,false)
        recyclerView = view.findViewById(R.id.top_song_rc_view)
        refreshView = view.findViewById(R.id.songList_refresh_view)
        initView()
        return view
    }

    fun initView(){
        presenter = NativePresenter(context!!,this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(SongsDecoration())
        adapter.addItemClickListener(object :BaseHolder.ItemClickCallBack{
            override fun callBack(data: Any) {
                if (data is Song){
                    val intentOfBroadcast = Intent()
                    intentOfBroadcast.action = PlayMusicService.PLAY_MUSIC_SERVICE
                    intentOfBroadcast.putExtra(PlayMusicService.PLAY_INDEX,CurrentSongsProvider.addSong(data))
                    context?.sendBroadcast(intentOfBroadcast)
                }
            }
        })
        refreshView.setOnRefreshListener {
            presenter.getMusicData()
        }
        presenter.getMusicData()
    }

    override fun onFail(e: Throwable) {

    }

    override fun onFinish() {

    }

    override fun onData(songs: List<Song>) {
        refreshView.isRefreshing = false
        adapter.setList(songs)
        adapter.notifyDataSetChanged()
    }
}