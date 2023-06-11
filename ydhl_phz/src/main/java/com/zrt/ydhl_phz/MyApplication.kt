package com.zrt.ydhl_phz

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.zrt.ydhl_phz.logic.model.NurseUser

/**
 * @author：Zrt
 * @date: 2022/10/29
 */
class MyApplication: Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    var currentNurseUser: NurseUser = NurseUser()
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}