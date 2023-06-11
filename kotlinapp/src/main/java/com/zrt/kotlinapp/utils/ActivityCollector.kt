package com.zrt.kotlinapp.utils

import android.app.Activity

/**
 * @author：Zrt
 * @date: 2022/6/7
 * Activity管理器
 */
object ActivityCollector{
    private val activitys = ArrayList<Activity>()
    fun addActivity(activity: Activity){
        activitys.add(activity)
    }
    fun removeActivity(activity: Activity){
        activitys.remove(activity)
    }
    fun finishAll(){
        for(activity in activitys){
            if (!activity.isFinishing){
                activity.finish()
            }
        }
        activitys.clear()
    }
}