@file:JvmName("NotificationUtils")
package com.zrt.kotlinapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.zrt.kotlinapp.R
import java.util.*
import kotlin.concurrent.schedule

/**
 * @author：Zrt
 * @date: 2022/7/16
 */

//fun createNoticeChannel(context: Context, channelID: String, channelName: String) : NotificationManager{
//    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//        val channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT)
//        manager.createNotificationChannel(channel)
//    }
//    return manager
//}
fun getNoticeManage(context: Context): NotificationManager{
    return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}
fun createNoticeChannel(manager: NotificationManager?, channelID: String, channelName: String,
                        importance:Int = NotificationManager.IMPORTANCE_DEFAULT,
                        block:NotificationChannel.() -> Unit = {}) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val channel = NotificationChannel(channelID, channelName, importance)
        channel.block()
        manager?.createNotificationChannel(channel)
    }
}

val channelID_Reply = "reply_name"
val noticeID_Reply1 = 101
val noticeID_Reply2 = 102
val resultKey_Reply = "key_text_reply"
/**
 * 回复消息的通知
 */
class ReplyMessageReceiver: BroadcastReceiver(){
    companion object{
        val action = "com.zrt.kotlinapp.notification.REPLY_BROADCAST"
    }
    override fun onReceive(context: Context, intent: Intent?) {
        Log.e("", "onReceive 消息回复")
        // 获取回复消息的内容
        // resultKey_Reply需要与定义时的key保持一致
        val inputContent  = RemoteInput.getResultsFromIntent(intent)
                ?.getCharSequence(resultKey_Reply)?.toString()
        if (inputContent == null){
            Log.e("", "onReceive: 没有回复消息！")
            return
        }
        Log.e("", "onReceive 消息内容: $inputContent")
        val repliedNotification = NotificationCompat.Builder(context, channelID_Reply).apply {
            setSmallIcon(R.mipmap.ic_launcher)//小图标（显示在状态栏）
            setContentTitle("1008666")//标题
            setContentText("消息发送成功！")//内容
        }.build()
        val manager = getNoticeManage(context)
        createNoticeChannel(manager, channelID_Reply, "回复消息2", NotificationManager.IMPORTANCE_HIGH){}
        manager.notify(noticeID_Reply2, repliedNotification)
        Timer().schedule(2000){
            manager.cancel(noticeID_Reply2)
        }
    }

}

