package com.zrt.kotlinapp.utils

import android.util.Log

/**
 * @author：Zrt
 * @date: 2022/8/11
 */
object LogUtil {
    val TAG = ">>>>"
    private const val VERBOSE = 1
    private const val DEBUG = 2
    private const val INFO = 3
    private const val WARN = 4
    private const val ERROR = 5
    private var level = VERBOSE
    fun v(tag:String = TAG, msg:String){
        if (level <= VERBOSE){
            Log.v(tag, msg)
        }
    }
    fun d(tag:String = TAG, msg:String){
        if (level <= DEBUG){
            Log.d(tag, msg)
        }
    }
    fun i(tag:String = TAG, msg:String){
        if (level <= INFO){
            Log.i(tag, msg)
        }
    }
    fun w(tag:String = TAG, msg:String){
        if (level <= WARN){
            Log.w(tag, msg)
        }
    }
    fun e(tag:String = TAG, msg:String){
        if (level <= ERROR){
            Log.e(tag, msg)
        }
    }
}