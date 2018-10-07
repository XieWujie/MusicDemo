package com.example.xiewujie.musicdemo.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.xiewujie.musicdemo.R
import com.example.xiewujie.musicdemo.module.json.SearchAutoComplete
import com.example.xiewujie.musicdemo.module.json.Song
import com.example.xiewujie.musicdemo.view.holder.BaseHolder
import com.example.xiewujie.musicdemo.view.holder.BottomListHolder
import com.example.xiewujie.musicdemo.view.holder.SearchAutoHodler
import com.example.xiewujie.musicdemo.view.holder.SongListHodler
import java.util.ArrayList

class SongListAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>(),BaseHolder.ItemClickCallBack{

    private val songs = ArrayList<Song>()
    private var listener:BaseHolder.ItemClickCallBack? = null
    val TYPE_BOTTOM = 0
    val TYPE_SONGS_TOP = 1
    val TYPE_SEARCH = 2
    var type = TYPE_SONGS_TOP

    fun setList(songs: List<Song>){
        this.songs.clear()
        this.songs.addAll(songs)
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {

        var recId = when(type){
            TYPE_BOTTOM ->R.layout.buttom_songs_rc_item_layout
            TYPE_SEARCH ->R.layout.search_rc_item_layout
            else ->R.layout.song_list_item_layout
        }
       val view = LayoutInflater.from(p0.context).inflate(recId,p0,false)
        val holder = when(type){
            TYPE_BOTTOM ->BottomListHolder(view)
            TYPE_SEARCH ->SearchAutoHodler(view)
            else -> SongListHodler(view)
        }
        holder.addItemClickCallBack(this)
        return holder
    }

    override fun getItemCount() =songs.size

    override fun getItemViewType(position: Int) = type

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, p1: Int) {
        if (songs.size==0)
            return
        (holder as BaseHolder).bindData(songs[p1])
    }

    override fun callBack(data: Any) {
       listener?.callBack(data)
    }

    fun addItemClickListener(itemClickCallBack: BaseHolder.ItemClickCallBack){
        if (listener==null){
            listener = itemClickCallBack
        }
    }
}