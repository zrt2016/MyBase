package com.zrt.kotlinapp.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.notification.createNoticeChannel
import com.zrt.kotlinapp.notification.getNoticeManage
import kotlin.concurrent.thread

class MyService : Service() {
    companion object{
        // 启动service
        fun actionStart(context: Context){
            val intent = Intent(context, MyService::class.java)
            context.startService(intent)
        }
        fun actionStop(context: Context){
            val intent = Intent(context, MyService::class.java)
            context.stopService(intent)
        }
        // 绑定service
        fun bindService(context: Context, connection: ServiceConnection){
            val intent = Intent(context, MyService::class.java)
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
        fun unbindService(context: Context, connection: ServiceConnection){
            context.unbindService(connection)
        }
    }

    private val mBinder = DownloadBinder()

    class DownloadBinder : Binder() {
        fun startDownload() {
            Log.d("MyService", "startDownload executed")
        }
        fun getProgress(): Int {
            Log.d("MyService", "getProgress executed")
            return 0
        }
    }
    //
    override fun onBind(intent: Intent): IBinder {
        Log.i("MyService","MyService onBind")
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i("MyService","MyService onUnbind")
        return super.onUnbind(intent)
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("MyService","MyService onCreate")
        createNoticeChannel(getNoticeManage(this), "MyService", "前台 Service")
        val intent = Intent(this, ServiceActivity::class.java)
        val activity = PendingIntent.getActivity(this, 0, intent, 0)
        val build = NotificationCompat.Builder(this, "MyService")
                .setContentTitle("this is Content title")
                .setContentText("this is Content Text")
                .setSmallIcon(R.drawable.small_icon)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.large_icon))
                .setContentIntent(activity)
                .build()
        startForeground(1, build)
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        Log.i("MyService","MyService onStart")
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("MyService","MyService onStartCommand")
//        thread{
//            // 启动5秒后关闭线程同时关闭service
//            Thread.sleep(5000)
//            stopSelf()
//        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MyService","MyService onDestroy")
    }

}
