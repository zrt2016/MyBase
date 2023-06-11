package com.zrt.kotlinapp.services

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection

import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_service.*
import java.util.*

/**
 * startService:启动service
 * bindService：绑定Activity与Service，用来交互
 *
 * 不同启动，调用生命周期
 * startService：onCreate —》onStartCommand
 * 多次调用startService：onStartCommand
 * stopService：onDestroy
 *
 * bindService：onCreate —》onBind  —》（onServiceConnected）
 * unBindService：onUnbind —》onDestroy
 *
 * 先startService后bindService
 *      onCreate —》onStartCommand —》onStart —》onBind  —》（onServiceConnected）
 * 先unBindService，后stopService。 (注：unBindService-onUnbind。 stopService-onDestroy)
 *      onUnbind —》onDestroy
 * 先stopService，后unBindService （注：stopService不会执行任何操作）
 *      onUnbind —》onDestroy
 *
 * 先bindService后startService
 *      onCreate —》onBind  —》（onServiceConnected） —》onStartCommand -> onStart
 * 先unBindService，后stopService。(注：unBindService-onUnbind。 stopService-onDestroy)
 *      onUnbind —》onDestroy
 * 先stopService，后unBindService。（注：stopService不会执行任何操作）
 *      onUnbind —》onDestroy
 *
 * Android8.0后，只有应用处于前台可见状态下，service 才能保证运行，一旦进入后台，service会被系统随时回收
 * 因此在service启动后，在service的onCreate内调用startForeground
 * 如果是使用startForegroundService启动的Service，则必须在5秒内调用startForeground。否则会anr
 *
 * <!-- android 9.0 后，前台Service需要进行权限声明 -->
 * <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
 *
 * IntentService:在执行完毕后，会自动关闭
 */
class ServiceActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_service
    }

    override fun initData() {
        // 启动service
        a_s_start.setOnClickListener {
            MyService.actionStart(this)
//            val intent = Intent(this, MyService::class.java)
//            this.startService(intent)
        }
        // 关闭service
        a_s_stop.setOnClickListener {
            MyService.actionStop(this)
        }
        // 绑定service后，关闭Activity时要解绑，不然会报错
        a_s_bind.setOnClickListener {
            MyService.bindService(this, connection)
        }
        a_s_unbind.setOnClickListener {
            MyService.unbindService(this, connection)
        }
        // 通过downloadBinder，完成交互
        a_s_jiaohu.setOnClickListener {
            downloadBinder?.startDownload()
        }
        a_s_intent_service.setOnClickListener {
            Log.d("ServiceActivity", "MainThread id is ${Thread.currentThread().name}")
            MyIntentService.startActionBaz(this, "1", "2")
        }

    }
    var downloadBinder:MyService.DownloadBinder? = null
    val connection:ServiceConnection = object : ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("MyService", "ServiceConnection onServiceDisconnected")
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d("MyService", "ServiceConnection onServiceConnected")
            // 获取到DownloadBinder的实例，通过该实例完成与service的交互
            downloadBinder = service as MyService.DownloadBinder
//            downloadBinder.startDownload()
//            downloadBinder.getProgress()
        }

    }
}