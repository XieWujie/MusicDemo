package com.example.xiewujie.musicdemo.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.xiewujie.musicdemo.R
import com.example.xiewujie.musicdemo.module.json.TopList
import com.example.xiewujie.musicdemo.module.json.TopX
import com.example.xiewujie.musicdemo.presenter.TopListPresenter
import com.example.xiewujie.musicdemo.tool.MyService
import com.example.xiewujie.musicdemo.tool.SongsDecoration
import com.example.xiewujie.musicdemo.view.adapter.TopListAdapter
import com.example.xiewujie.musicdemo.view.view.TopListView

class TopListFragment:Fragment(),TopListView{

    lateinit var recyclerView: RecyclerView
    val adapter = TopListAdapter()
    lateinit var presenter: TopListPresenter
    lateinit var freshView:SwipeRefreshLayout
    val qqMusicTopList = ArrayList<TopX>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.top_list_fragment_layout,container,false)
        recyclerView = view.findViewById(R.id.top_list_fragment_rc_view)
        freshView = view.findViewById(R.id.top_list_fresh_view)
       initView()
        return view
    }
    fun initView(){
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(SongsDecoration(MyService.dip2px(context!!,15).toFloat(),false))
        adapter.setList(qqMusicTopList)
        if (context!=null) {
            presenter = TopListPresenter(context!!, this)
            presenter.getDate()
        }
        freshView.setOnRefreshListener { presenter.getDate() }
    }

    override fun onData(topList: TopList) {
       adapter.setList(topList.List)
    }

    override fun onFail(e: Throwable) {
        e.printStackTrace()
    }

    override fun onFinish() {
       adapter.notifyDataSetChanged()
        freshView.isRefreshing = false
    }
}