package com.zrt.kotlinapp.broadcasts

import android.content.*
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.zrt.kotlinapp.MainActivity
import com.zrt.kotlinapp.utils.ActivityCollector
import com.zrt.kotlinapp.utils.ToastUtils

/**
 * @author：Zrt
 * @date: 2022/6/20
 * 广播分为两种类型：标准广播和有序广播
 * 标准广播：完全异步执行的广播，广播发出后，所有的BroadcastReceiver都会接收到这条广播信息，无先后顺序，无法被截停
 * 例：             ---> BroadcastReceiver1
 *      发送一条广播 ---> BroadcastReceiver2
 *                  ---> BroadcastReceiver3
 * 有序广播：一种同步执行的广播,广播发出后，同一时刻只会有一个BroadcastReceiver接收，且逻辑执行完毕后才会继续传递。
 *                                     可在此处截停             可在此处截停
 *                                         ↑                        ↑
 * 例：发送一条广播 ---> BroadcastReceiver1 ---> BroadcastReceiver2 ---> BroadcastReceiver3
 * 注册BroadcastReceiver有两种方式：代码中注册（动态注册） or AndroidManifest.xml注册（静态注册）
 *
 * 完整的系统广播列表：
 * <Android SDK> /platforms/<任意 android api 版本>/data/broadcast_actions.text
 *
 * Android 8.0后所有隐式广播不允许使用静态注册的方式接收
 *
 * 不要在onReceive中执行耗时操作，因为BroadcastReceiver中不允许开起线程。如果运行较长时间，程序会出现错误
 *
 * 发送标准广播：sendBroadcast(intent)
 * 发送有序广播：sendOrderedBroadcast(intent, null)
 *      需要注册时设置优先值：<intent-filter android:priority="100"> 100最高值
 *      截断广播：abortBroadcast()
 */
class BroadCastDemo {
}

/**
 * 动态注册系统广播
 * 用途：监听系统时间
 * 每分钟接收一次
 */
class TimeChangeReceiver:BroadcastReceiver(){
    companion object {
        var mInstances: TimeChangeReceiver? = null
        val action = "android.intent.action.TIME_TICK"
        fun newInstance() :TimeChangeReceiver?{
            if (mInstances == null){
                mInstances = TimeChangeReceiver()
            }
            return mInstances
        }
    }
    override fun onReceive(context: Context, intent: Intent?) {
        Log.i(">>>>","##onReceive")
        ToastUtils.show(context, "Time has change")
    }

}

/**
 * 静态注册广播
 * 实现开机启动功能
 * 在AndroidManifest.xml中添加以下内容注册：
 *  <receiver android:name=".broadcasts.BootcastReceiver"
        android:enabled="true"
        android:exported="true"/>
 *  enabled: 是否启用这个BootcastReceiver广播
 *  exported：是否允许BootcastReceiver接收本程序以外的广播
 *  需要在<receiver>中添加<intent-filter >：
 *  <receiver android:name=".broadcasts.BootcastReceiver"
        android:enabled="true"
        android:exported="true">
            <intent-filter >
            <action android:name="android.intent.action.BOOT_COMPLETED"/>
            </intent-filter>
    </receiver>
 * 添加权限：
 * <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
 *
 */
class BootcastReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        ToastUtils.show(context, "Boot Complete")
    }
}

/**
 * 发送自定义广播
 * 标准广播
 * 在AndroidManifest.xml中添加以下内容注册：
 * <receiver android:name=".broadcasts.MyBroadcastReceiver"
        android:enabled="true"
        android:exported="true">
        <intent-filter android:priority="100">
            <action android:name="com.zrt.kotlinapp.broadcasts.MY_BROADCAST"/>
        </intent-filter>
    </receiver>
 * priority：优先级，最高100
 */
class MyBroadcastReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        Log.i(">>>>","##onReceive-MyBroadcastReceiver")
        ToastUtils.show(context, "Receive in MyBroadcastReceiver")
        // 截断广播，后面的AnotherBroadcastReceiver不会再接收到该广播
        abortBroadcast()
    }
}

/**
 * 发送有序广播
 * 在AndroidManifest.xml中添加以下内容注册：
 * <receiver android:name=".broadcasts.AnotherBroadcastReceiver"
        android:enabled="true"
        android:exported="true">
        <intent-filter>
            <action android:name="com.zrt.kotlinapp.broadcasts.MY_BROADCAST"/>
        </intent-filter>
    </receiver>
 */
class AnotherBroadcastReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        Log.i(">>>>","##onReceive-AnotherBroadcastReceiver")
        ToastUtils.show(context, "Receive in AnotherBroadcastReceiver")
    }
}

/**
 * 实现强制下线功能
 * 一般在Activity基类中的onResume注册，onPause中解除。
 * 这里只是测试因此放在BCActivity中
 */
class ForceOffLineReceiver: BroadcastReceiver(){
    companion object{
        var mInstances: ForceOffLineReceiver? = null
        val action = "com.zrt.kotlinapp.broadcasts.FORCE_OFFLINE"
        fun newInstance() :ForceOffLineReceiver?{
            if (mInstances == null){
                mInstances = ForceOffLineReceiver()
            }
            return mInstances
        }
        fun registerReceiver(context: Context)
            : Intent? {
            newInstance()
            val intentFilter = IntentFilter()
            intentFilter.addAction(action)
            return context.registerReceiver(newInstance(), intentFilter)
        }
        fun unregisterReceiver(context: Context){
            context.unregisterReceiver(newInstance())
        }
    }
    override fun onReceive(context: Context, intent: Intent) {
        AlertDialog.Builder(context).apply {
            setTitle("Warning")
            setMessage("You are forced to be offine. Please try to login again.")
            setCancelable(false)
            setPositiveButton("OK", DialogInterface.OnClickListener{
                dialog, which ->
                dialog.dismiss()
                ActivityCollector.finishAll()
                val i = Intent(context, MainActivity::class.java)
                context.startActivity(i)
            })
            show()
        }
    }
}

