package com.example.xiewujie.musicdemo.presenter

import android.content.Context
import com.example.xiewujie.musicdemo.module.http.HttpMathods
import com.example.xiewujie.musicdemo.module.json.TopList
import com.example.xiewujie.musicdemo.view.view.TopListView
import rx.Subscriber
import kotlin.concurrent.fixedRateTimer

class TopListPresenter(context: Context,val view:TopListView):BasePresenter(context,view){

    fun getDate(){
     val subscriber = object :Subscriber<TopList>() {
         override fun onNext(t: TopList?) {
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
        HttpMathods.getTopListService(subscriber)
    }
}