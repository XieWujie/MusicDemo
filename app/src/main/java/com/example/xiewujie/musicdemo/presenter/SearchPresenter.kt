package com.example.xiewujie.musicdemo.presenter

import android.content.Context
import com.example.xiewujie.musicdemo.module.http.HttpMathods
import com.example.xiewujie.musicdemo.module.json.Search
import com.example.xiewujie.musicdemo.module.json.Song
import com.example.xiewujie.musicdemo.view.view.SearchView
import rx.Subscriber

class SearchPresenter(context: Context,val view: SearchView):BasePresenter(context,view){

    fun getData(key:String,page:Int,number:Int,isComplete:Boolean){
        val subscriber = object :Subscriber<Song>(){
            override fun onNext(t: Song?) {
                if (t!=null){
                    view.onData(t)
                }
            }

            override fun onCompleted() {
                view.onFinish()
            }

            override fun onError(e: Throwable?) {
              if (e!=null){
                  view.onFail(e)
              }
            }
        }
        if (isComplete){
            HttpMathods.getAutoCompleteService(subscriber,key)
        }else {
            HttpMathods.getSearchService(subscriber, key, page, number)
        }
    }

    fun getHotKey(){
        val keys = ArrayList<String>()
        val subscriber = object :Subscriber<String>(){
            override fun onNext(t: String?) {
                if (t!=null){
                    keys.add(t)
                }
            }

            override fun onCompleted() {
                view.onHotKey(keys)
            }

            override fun onError(e: Throwable?) {
                view.onFail(e!!)
            }
        }
        HttpMathods.getHotKeyService(subscriber = subscriber)
    }
}