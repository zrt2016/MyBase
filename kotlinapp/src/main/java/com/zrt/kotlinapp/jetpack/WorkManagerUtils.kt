package com.zrt.kotlinapp.jetpack

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * @author：Zrt
 * @date: 2022/8/6
 * WorkManager与Service不相同，
 * 1、Service在没销毁的情况下是一直保持在后台运行
 * 2、WorkManager只是一个处理定时任务的工具，可以保证即使在应用退出甚至手机重庆的情况下，之前注册的任务依然会执行。
 * WorkManager的基本用法分为3步：
 * 1、定义一个后台任务，并实现具体的任务逻辑
 * 2、配置后台任务的运行条件和约束信息，并构建后台任务请求
 * 3、将该后台任务请求传入WorkManager的enqueue()方法中，系统会在合适的时间运行
 *
 *  写法定义一个类，继承Worker类
 *  doWork():不会运行在主线程中
 *
 *  调用：
 *  // 构造一次性任务：OneTimeWorkRequest
 *  val request = OneTimeWorkRequest.Builder(SimpleWorker::class.java)
 *              .setInitialDelay(5, TimeUnit.SECONDS) //设置任务在5秒后运行
 *              .build()
 *  WorkManager.getInstance(this).enqueue(request) // 加入到运行队列中
 *  // 周期性运行后台任务请求，设置周期间隔时间15分钟
 *  val builder = PeriodicWorkRequest.Builder(SimpleWorker::class.java, 15, TimeUnit.MINUTES).build()
 *
 */

class SimpleWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        Log.d("SimpleWorker", "do work in SimpleWorker")
        return Result.success()
    }
}