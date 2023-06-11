package com.zrt.kotlinapp

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.multidex.MultiDex
import com.zrt.mybase.tools.monitor.TimeMonitorConfig
import com.zrt.mybase.tools.monitor.TimeMonitorManager
import java.util.concurrent.TimeUnit

//import kotlin.time.milliseconds


/**
 * @author：Zrt
 * @date: 2022/6/25
 */
class MyApplication: Application() {
    companion object{
        lateinit var context:Context
    }

    override fun onCreate() {
        super.onCreate()
        // 记录Application启动时间
        TimeMonitorManager.instance
                .getTimeMonitor(TimeMonitorConfig.TIME_MONITOR_ID_APPLICATION_START)
                .recordingTimeTag("Application-onCreate")
        context = applicationContext
        registerActivity()
    }

    /**
     * 解决异常报错问题：
     *  java.lang.RuntimeException: Unable to get provider androidx.core.content.FileProvider:
     *  java.lang.ClassNotFoundException: Didn't find class "androidx.core.content.FileProvider"
     *  on path: DexPathList[[zip file "/data/app/com.zrt.kotlinapp-1.apk"],nativeLibraryDirectories=[/data/app-lib/com.zrt.kotlinapp-1, /vendor/lib, /system/lib]]
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        //		MultiDex.install(this);
//        BoostMultiDex.install(base)
        MultiDex.install(this)
        // 启动时在Application中添加打点记录
        TimeMonitorManager.instance
                .resetTimeMonitor(TimeMonitorConfig.TIME_MONITOR_ID_APPLICATION_START)
                .startMonitor()
    }


    /**
     * 监控Activity的生命周期
     */
    fun registerActivity(){
        // 监控Activity的生命周期调用
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks{
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                val now = System.currentTimeMillis()
                Log.i(">>>>", "##activity Created ${activity.componentName} ${TimeUnit.MILLISECONDS.toSeconds(now)} ")
            }
            override fun onActivityStarted(activity: Activity) {
                val now = System.currentTimeMillis()
                Log.i(">>>>", "##activity Started ${activity.componentName} ${TimeUnit.MILLISECONDS.toSeconds(now)} ")
            }
            override fun onActivityPaused(activity: Activity) {
                val now = System.currentTimeMillis()
                Log.i(">>>>", "##activity Paused ${activity.componentName} ${TimeUnit.MILLISECONDS.toSeconds(now)} ")
            }
            override fun onActivityResumed(activity: Activity) {
                val now = System.currentTimeMillis()
                Log.i(">>>>", "##activity Resumed ${activity.componentName} ${TimeUnit.MILLISECONDS.toSeconds(now)} ")
            }
            override fun onActivityStopped(activity: Activity) {
                val now = System.currentTimeMillis()
                Log.i(">>>>", "##activity Stopped ${activity.componentName} ${TimeUnit.MILLISECONDS.toSeconds(now)} ")
            }
            override fun onActivityDestroyed(activity: Activity) {
                val now = System.currentTimeMillis()
                Log.i(">>>>", "##activity Destroyed ${activity.componentName} ${TimeUnit.MILLISECONDS.toSeconds(now)} ")
            }
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                val now = System.currentTimeMillis()
                Log.i(">>>>", "##activity SaveInstanceState ${activity.componentName} ${TimeUnit.MILLISECONDS.toSeconds(now)} ")
            }
        })
    }

}