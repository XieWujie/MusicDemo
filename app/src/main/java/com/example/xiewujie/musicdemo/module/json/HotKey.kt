package com.example.xiewujie.musicdemo.module.json


data class HotKey(
    val code: Int,
    val data: HotKeyData,
    val subcode: Int
)

data class HotKeyData(
    val hotkey: List<Key>,
    val special_key: String,
    val special_url: String
)

data class Key(
    val k: String,
    val n: Int
)