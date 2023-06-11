package com.zrt.weatherapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * @author：Zrt
 * @date: 2022/8/15
 */
class MyApplication : Application(){
    companion object{
        // 彩云添加令牌
        const val TOKEN_WEATHER = "U1DpkitabDe7zOHn" // 填入你申请到的令牌值
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}