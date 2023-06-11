package com.zrt.kotlinapp.activity_view.custom_basic.view_shijianfenfa

import android.os.Bundle
import android.view.MotionEvent
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.learnkotlin.log

/**
 * 事件 分发
 */
class ViewEventActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int =R.layout.activity_view_event

    override fun initData() {

    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN ->
                log("## Event Activiy dispatchTouchEvent ACTION_DOWN")
            MotionEvent.ACTION_UP ->
                log("## Event Activiy dispatchTouchEvent ACTION_UP")
            MotionEvent.ACTION_MOVE ->
                log("## Event Activiy dispatchTouchEvent ACTION_MOVE")
            else -> log("## Event Activiy dispatchTouchEvent null")
        }
        return super.dispatchTouchEvent(event)
//        return true // 拦截事件，自我处理
//        return false // 拦截事件，不向下传递事件
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN ->
                log("## Event Activiy onTouchEvent ACTION_DOWN")
            MotionEvent.ACTION_UP ->
                log("## Event Activiy onTouchEvent ACTION_UP")
            MotionEvent.ACTION_MOVE ->
                log("## Event Activiy onTouchEvent ACTION_MOVE")
            else -> log("## Event Activiy onTouchEvent null")
        }
        return super.onTouchEvent(event)
//                return true // 拦截事件，自我处理
//        return false // 传递个上级的onTouchEvent处理
    }

}