package com.example.xiewujie.musicdemo.view.adapter


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.xiewujie.musicdemo.R

import com.example.xiewujie.musicdemo.view.holder.BaseHolder

class SingleTextAdapter: RecyclerView.Adapter<SingleTextAdapter.ViewHolder>(){

    private val mList = ArrayList<String>()
    private var listener:BaseHolder.ItemClickCallBack? = null
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.single_text_layout,p0,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setList(list:List<String>){
        mList.clear()
        mList.addAll(list)
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        val content = mList[p1]
        holder.contentText.text = content
        holder.contentText.setOnClickListener { listener?.callBack(content) }
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
       val contentText = itemView.findViewById<TextView>(R.id.single_text_content_view)
    }

    fun addItemClickListenter(listener:BaseHolder.ItemClickCallBack){
        if (this.listener == null) {
            this.listener = listener
        }
    }
}