package com.example.xiewujie.musicdemo.view.holder

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseHolder(itemView:View):RecyclerView.ViewHolder(itemView){

   protected lateinit var context: Context
    var listener:ItemClickCallBack? = null

    init {
        context = itemView.context
    }

    abstract fun bindData(data:Any)

    fun addItemClickCallBack(listener: ItemClickCallBack){
         if (this.listener== null){
             this.listener = listener
         }
     }


    interface ItemClickCallBack{
        fun callBack(data: Any)
    }
}