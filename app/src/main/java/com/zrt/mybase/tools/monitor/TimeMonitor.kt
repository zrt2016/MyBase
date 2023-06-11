package com.zrt.mybase.tools.monitor

import android.util.Log

/**
 * @author：Zrt
 * @date: 2023/1/3
 * 统计耗时的工具类
 *  1、在上传数据到服务器时建议根据用户ID的尾号来抽样上报。
 *  2、在项目中核心基类的关键回调函数和核心方法中加入打点。
 */

/**
 * 为了使代码更好管理，定义一个打点配置类：
 * 打点配置类，用于统计各阶段的耗时，便于代码的维护和管理。
 */
object TimeMonitorConfig {
    // 应用启动耗时
    val TIME_MONITOR_ID_APPLICATION_START = 1
}

/**
 * 耗时监视器对象，记录整个过程的耗时情况，可以用在很多需要统计的地方，比如Activity的启动耗时和Fragment的启动耗时。
 */
public class TimeMonitor {

    val TAG: String = TimeMonitor::class.java.simpleName;
    var mMonitorId:Int = -1;
    // 保存一个耗时统计模块的各种耗时，tag对应某一个阶段的时间
    val mTimeTag: HashMap<String, Long> = HashMap()
    var mStartTime:Long = 0;

    constructor(mMonitorId: Int) {
        Log.d(TAG, "init TimeMonitor id: " + mMonitorId);
        this.mMonitorId = mMonitorId;
    }

    fun getMonitorId():Int {
        return mMonitorId;
    }

    fun startMonitor() {
        // 每次重新启动都把前面的数据清除，避免统计错误的数据
        if (mTimeTag.size > 0) {
            mTimeTag.clear();
        }
        mStartTime = System.currentTimeMillis();
    }

    /**
     * 每打一次点，记录某个tag的耗时
     */
    fun recordingTimeTag(tag: String) {
        // 若保存过相同的tag，先清除
        if (mTimeTag.get(tag) != null) {
            mTimeTag.remove(tag);
        }
        var time = System.currentTimeMillis() - mStartTime;
        Log.d(TAG, tag + ": " + time);
        mTimeTag.put(tag, time);
    }

    fun end(tag: String, writeLog: Boolean) {
        recordingTimeTag(tag);
        end(writeLog);
    }

    fun end(writeLog: Boolean) {
        if (writeLog) {
            //写入到本地文件
        }
    }

    fun getTimeTags():HashMap<String, Long> {
        return mTimeTag;
    }
}

/**
 * 采用单例管理各个耗时统计的数据。
 */
class TimeMonitorManager {
    lateinit var mTimeMonitorMap: HashMap<Int, TimeMonitor>
    init {
        mTimeMonitorMap = HashMap()
    }
    /**
     * 初始化打点模块
     */
    fun resetTimeMonitor(id: Int) {
        if (mTimeMonitorMap!![id] != null) {
            mTimeMonitorMap.remove(id)
        }
        getTimeMonitor(id)
    }

    /**
     * 获取打点器
     */
    fun getTimeMonitor(id: Int): TimeMonitor {
        var monitor = mTimeMonitorMap!![id]
        if (monitor == null) {
            monitor = TimeMonitor(id)
            mTimeMonitorMap[id] = monitor
        }
        return monitor
    }

    companion object {
        val instance: TimeMonitorManager by lazy {
            TimeMonitorManager()
        }

    }
}