package com.example.xiewujie.musicdemo.tool;

import com.example.xiewujie.musicdemo.module.json.Song;

public interface SongInfoListener {

    void onFinish(Song song);

    void onFail(Throwable e);
}
