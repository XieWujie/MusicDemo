package com.example.xiewujie.musicdemo.view.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.xiewujie.musicdemo.R
import com.example.xiewujie.musicdemo.module.json.Song
import com.example.xiewujie.musicdemo.presenter.SearchPresenter
import com.example.xiewujie.musicdemo.tool.CurrentSongsProvider
import com.example.xiewujie.musicdemo.tool.HorizonDecoration
import com.example.xiewujie.musicdemo.tool.MyService
import com.example.xiewujie.musicdemo.tool.SongsDecoration
import com.example.xiewujie.musicdemo.view.activity.PlayActivity
import com.example.xiewujie.musicdemo.view.adapter.SingleTextAdapter
import com.example.xiewujie.musicdemo.view.adapter.SongListAdapter
import com.example.xiewujie.musicdemo.view.holder.BaseHolder
import com.example.xiewujie.musicdemo.view.service.PlayMusicService
import com.example.xiewujie.musicdemo.view.view.SearchView


class SearchFragment:Fragment(),SearchView,android.support.v7.widget.SearchView.OnQueryTextListener,View.OnClickListener{

    private lateinit var recyclerView: RecyclerView
    private lateinit var inputText:android.support.v7.widget.SearchView
    private lateinit var presenter: SearchPresenter
    private lateinit var backView:ImageView
    private lateinit var cancelView:TextView
    private lateinit var hotKeyLayout:LinearLayout
    private lateinit var hotKeyRcView:RecyclerView
    private val hotKeyAdapter = SingleTextAdapter()
    private val songs = ArrayList<Song>()
    private val adapter = SongListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate( R.layout.search_layout,container,false)
        recyclerView = view.findViewById(R.id.search_rc_view)
        inputText = view.findViewById(R.id.search_input_view)
        backView = view.findViewById(R.id.search_back_view)
        cancelView = view.findViewById(R.id.search_cancel_text)
        hotKeyLayout = view.findViewById(R.id.hot_key_layout)
        hotKeyRcView = view.findViewById(R.id.hot_key_rc_view)
        if (context==null){
            return null
        }
        initView()
        presenter = SearchPresenter(context!!,this)
        presenter.getHotKey()
        return view
    }

    fun initView(){
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        hotKeyRcView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL)
        hotKeyRcView.adapter = hotKeyAdapter
        hotKeyRcView.addItemDecoration(HorizonDecoration(MyService.dip2px(context!!,15)))
        recyclerView.addItemDecoration(SongsDecoration())
        inputText.setOnQueryTextListener(this)
        inputText.isSubmitButtonEnabled = true
        backView.setOnClickListener(this)
        cancelView.setOnClickListener(this)
        adapter.addItemClickListener(object :BaseHolder.ItemClickCallBack {
            override fun callBack(data: Any) {
                if (data is Song) {
                    val intentOfActivity = Intent(context, PlayActivity::class.java)
                    CurrentSongsProvider.addSong(data)
                    val intentOfBroadcast = Intent()
                    intentOfBroadcast.action = PlayMusicService.PLAY_MUSIC_SERVICE
                    intentOfBroadcast.putExtra(PlayMusicService.PLAY_INDEX,CurrentSongsProvider.addSong(data))
                    context?.sendBroadcast(intentOfBroadcast)
                    context?.startActivity(intentOfActivity)
                }
            }
        })
        hotKeyAdapter.addItemClickListenter(object :BaseHolder.ItemClickCallBack{
            override fun callBack(data: Any) {
                if (data is String) {
                    inputText.setQuery(data, true)
                }
            }
        })
    }

    override fun onData(song: Song) {
        songs.add(song)
    }

    override fun onFail(e: Throwable) {
        e?.printStackTrace()
    }

    override fun onFinish() {
        adapter.setList(songs)
        adapter.notifyDataSetChanged()
    }

    override fun onHotKey(keys: List<String>) {
        hotKeyAdapter.setList(keys)
        hotKeyAdapter.notifyDataSetChanged()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query==null||TextUtils.isEmpty(query)){
            return true
        }
        songs.clear()
        adapter.notifyDataSetChanged()
        adapter.type = adapter.TYPE_SONGS_TOP
        presenter.getData(query,1,20,false)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText==null||TextUtils.isEmpty(newText)){
            hotKeyLayout.visibility = View.VISIBLE
            return true
        }
        hotKeyLayout.visibility = View.GONE
        songs.clear()
        adapter.notifyDataSetChanged()
        adapter.type = adapter.TYPE_SEARCH
        presenter.getData(newText,1,100,true)
        return true
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.search_back_view,R.id.search_cancel_text->{
                fragmentManager?.popBackStack()
            }
        }
    }
}