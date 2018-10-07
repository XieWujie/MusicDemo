package com.example.xiewujie.musicdemo.view.fragment


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.xiewujie.musicdemo.R
import com.example.xiewujie.musicdemo.module.json.Song
import com.example.xiewujie.musicdemo.presenter.TopSongPresenter
import com.example.xiewujie.musicdemo.tool.SongsDecoration
import com.example.xiewujie.musicdemo.view.adapter.SongListAdapter
import com.example.xiewujie.musicdemo.view.holder.BaseHolder
import com.example.xiewujie.musicdemo.view.view.TopView

class SongTopFragment:Fragment(),TopView,BaseHolder.ItemClickCallBack,SongListListener{

    private val adapter:SongListAdapter = SongListAdapter()
    private val list = ArrayList<Song>()
    private lateinit var recyclerView: RecyclerView
    private  var presenter: TopSongPresenter? = null
    private lateinit var refreshView:SwipeRefreshLayout
    var itemClickListener:BaseHolder.ItemClickCallBack? = null
    private var topid = 27

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.song_list_layout,container,false)
        recyclerView = view.findViewById(R.id.top_song_rc_view)
        refreshView = view.findViewById(R.id.songList_refresh_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(SongsDecoration())
        presenter = TopSongPresenter(context!!,this)
        adapter.addItemClickListener(this)
        refreshView.setOnRefreshListener {
            if (presenter!=null){
                list.clear()
                presenter?.getData(topid)
            }else{
                refreshView.isRefreshing = false
            }
        }
        return view
    }

    fun initData(topid:Int){
        this.topid = topid
    }

    override fun onStart() {
        super.onStart()
        if (presenter!=null&&topid!=-1){
            list.clear()
            presenter?.getData(topid)
        }
    }

    override fun data(song:Song) {
        list.add(song)
    }

    override fun onFail(e: Throwable) {
        e.printStackTrace()
    }

    override fun onFinish() {
        adapter.setList(list)
        adapter.notifyDataSetChanged()
        if (refreshView.isRefreshing){
            refreshView.isRefreshing = false
        }
    }

    override fun callBack(data: Any) {
        itemClickListener?.callBack(data)
    }

    fun addItemClickListener(itemClickCallBack: BaseHolder.ItemClickCallBack){
        if (itemClickListener==null){
            itemClickListener = itemClickCallBack
        }
    }

    override fun getSongs(): ArrayList<Song> {
        return list
    }
}