package com.example.xiewujie.musicdemo.view.holder

import android.view.View
import android.widget.TextView
import com.example.xiewujie.musicdemo.R
import com.example.xiewujie.musicdemo.module.json.Song
import kotlinx.android.synthetic.main.search_rc_item_layout.view.*

class SearchAutoHodler(itemView:View):BaseHolder(itemView){

     private lateinit var searchText:TextView

    init {
        searchText = itemView.findViewById(R.id.search_item_name)
    }
    override fun bindData(data: Any) {
        if (data is Song){
            searchText.text = "${data.songName}-${data.singerName}"
            searchText.setOnClickListener { listener?.callBack(data) }
        }
    }

}