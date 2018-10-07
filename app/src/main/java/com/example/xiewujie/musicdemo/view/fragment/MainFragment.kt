package com.example.xiewujie.musicdemo.view.fragment

import android.content.Intent
import android.os.Bundle
import android.os.IInterface
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.xiewujie.musicdemo.R
import com.example.xiewujie.musicdemo.view.activity.MainActivity

class MainFragment:Fragment(),View.OnClickListener{

    lateinit var topText:TextView
    lateinit var searchView: SearchView
    lateinit var nativeText:TextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.main_fragment_layout,container,false)
        topText = view.findViewById(R.id.top_text)
        searchView = view.findViewById(R.id.main_fragment_search)
        nativeText = view.findViewById(R.id.native_text)
        topText.setOnClickListener(this)
        searchView.setOnClickListener(this)
        nativeText.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.top_text->{
                toFragment(MainActivity.TOPLIST_FRAGMENT)
            }
            R.id.main_fragment_search->{
               toFragment(MainActivity.SEARCH_FRAGMENT)
            }
            R.id.native_text->{
                toFragment(MainActivity.NATIVE_FRAGMENT)
            }
        }
    }

    fun toFragment(fragmentIndex:Int){
        val intent= Intent()
        intent.action = MainActivity.MAIN_BROADCAST_SERVICE
        intent.putExtra(MainActivity.ACTION_FRAGMENT,fragmentIndex)
        context?.sendBroadcast(intent)
    }
}