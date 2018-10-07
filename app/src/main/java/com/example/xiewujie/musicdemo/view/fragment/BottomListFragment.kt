package com.example.xiewujie.musicdemo.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.xiewujie.musicdemo.R
import com.example.xiewujie.musicdemo.module.json.Song
import com.example.xiewujie.musicdemo.tool.SongsDecoration
import com.example.xiewujie.musicdemo.view.adapter.SongListAdapter
import com.example.xiewujie.musicdemo.view.holder.BaseHolder


class BottomListFragment:Fragment(),BaseHolder.ItemClickCallBack{

    lateinit var recyclerView: RecyclerView
    lateinit var adapter:SongListAdapter
    lateinit var bottomPlayCount:TextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_play_list_layout,container,false)
        recyclerView = view.findViewById(R.id.bottom_songs_rc_view)
        bottomPlayCount = view.findViewById(R.id.bottom_play_count)
        adapter = SongListAdapter()
        adapter.type = adapter.TYPE_BOTTOM
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(SongsDecoration())
        recyclerView.adapter = adapter
        adapter.addItemClickListener(this)
        return view
    }

    fun setSongsList(songs:List<Song>){
        bottomPlayCount.text = "播放队列（${songs.size})"
        adapter.setList(songs)
        adapter.notifyDataSetChanged()
    }

    override fun callBack(data: Any) {

    }
}