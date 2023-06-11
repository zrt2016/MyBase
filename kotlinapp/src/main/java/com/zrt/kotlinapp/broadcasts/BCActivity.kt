package com.zrt.kotlinapp.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_b_c.*

class BCActivity : BasicActivity() {
    lateinit var timeChangeReceiver: TimeChangeReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentFilter = IntentFilter()
        intentFilter.addAction(TimeChangeReceiver.action)
        registerReceiver(TimeChangeReceiver.newInstance(), intentFilter)
        Log.i(">>>>", "##BcActivity-onCreate=${TimeChangeReceiver.newInstance().hashCode()}")
//        intentFilter.addAction("android.intent.action.TIME_TICK")
//        timeChangeReceiver = TimeChangeReceiver()
//        registerReceiver(timeChangeReceiver, intentFilter)
    }

    override fun getLayoutResID(): Int = R.layout.activity_b_c

    override fun initData() {
        //发送自定义标准广播
        btn_MyBroadcastReceiver.setOnClickListener {
            val intent = Intent("com.zrt.kotlinapp.broadcasts.MY_BROADCAST")
            // 由于Android8.0后，静态注册的广播是无法接收隐式广播的，而默认发出的自定义广播是隐式广播
            // 使用setPackage指定这条广播是发送给哪个应用的，从而变成一条显示广播
            intent.setPackage(packageName)
//          由于注册了多个广播（MyBroadcastReceiver和AnotherBroadcastReceiver）
//          此处发送后，2个广播都会接收到该消息
//            sendBroadcast(intent)
//            发送有序广播
            sendOrderedBroadcast(intent, null)
        }
        btn_ForceOffLineReceiver.setOnClickListener {
            val intent = Intent(ForceOffLineReceiver.action)
            sendBroadcast(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        ForceOffLineReceiver.registerReceiver(this)
    }

    override fun onPause() {
        super.onPause()
        ForceOffLineReceiver.unregisterReceiver(this)
    }
    override fun onDestroy() {
        super.onDestroy()
//        unregisterReceiver(timeChangeReceiver)
        unregisterReceiver(TimeChangeReceiver.newInstance())
//        Log.i(">>>>", "##BcActivity-onDestroy=${TimeChangeReceiver.newInstance().hashCode()}")
    }

}
