package com.zrt.kotlinapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.zrt.kotlinapp.utils.ActivityCollector
import com.zrt.mybase.tools.monitor.TimeMonitorConfig
import com.zrt.mybase.tools.monitor.TimeMonitorManager

/**
 * @author：Zrt
 * @date: 2022/6/4
 */
abstract class BasicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        TimeMonitorManager.instance
                .getTimeMonitor(TimeMonitorConfig.TIME_MONITOR_ID_ACTIVITY_START)
                .startMonitor()
                .recordingTimeTag("${javaClass.simpleName}-onCreate")

        super.onCreate(savedInstanceState)
        setContentView(getLayoutResID())
        ActivityCollector.addActivity(this)
        Log.i("BasicActivity","taskID=$taskId")// 打印Activity 栈ID
        // javaClass等价于java中的getClass()
        Log.i("BasicActivity", "activity=${javaClass.simpleName}") // 打印当前activity
        initData()
        // 记录Activity启动耗时
        TimeMonitorManager.instance
                .getTimeMonitor(TimeMonitorConfig.TIME_MONITOR_ID_ACTIVITY_START)
                .recordingTimeTag("${javaClass.simpleName}-onCreate-Over");
    }
    @LayoutRes
    abstract fun getLayoutResID(): Int
    abstract fun initData()

    override fun onStart() {
        super.onStart()
        TimeMonitorManager.instance
                .getTimeMonitor(TimeMonitorConfig.TIME_MONITOR_ID_ACTIVITY_START)
                .end("${javaClass.simpleName}-onStart", false)
    }
    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

    /**
     * 退出APP
     */
    fun exitApp(){
        ActivityCollector.finishAll()
        // 杀掉当前进程的代码，保证程序的完全退出
        // 仅用于杀掉当前进程
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    /**
     * 获取上一个activity通过setResult返回的数据
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * 重写返回键方法，点击back键返回时，也带入返回值
     */
    override fun onBackPressed() {
        // 重写时，注掉super.onBackPressed()
        super.onBackPressed()

    }

    /**
     * Activity在被回收前会调用该方法，可在该处保存相关数据，等下次进入时带入该数据
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }


}