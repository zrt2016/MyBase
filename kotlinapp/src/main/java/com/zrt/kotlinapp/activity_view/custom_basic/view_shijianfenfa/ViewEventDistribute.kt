package com.zrt.kotlinapp.activity_view.custom_basic.view_shijianfenfa

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.zrt.kotlinapp.learnkotlin.log

/**
 * @author：Zrt
 * @date: 2022/9/16
 * View的事件分发机制
 * 一个Activity包含一个Window对象（Window对象由PhoneWindow实现）
 * PhoneWindow以DecorView作为根View。DecorView将屏幕划分为2个区域：TitleView和ContentView。
 *
 * 当我们点击屏幕时，即产生了点击事件，该事件被封装成了一个类：MotionEvent。
 * 然后系统将这个对象进行传递，传递到View的层级，即该过程被称为事件分发
 * 事件分发的3个重要方法
 *  1、dispatchTouchEvent(event: MotionEvent?):用来进行事件分发
 *      a、return true，则事件不会往下分，自我处理
 *      b、return false，会进行拦截，交由上一层的onTouchEvent处理（view的交给ViewGroup，ViewGroup交由Activity）
 *      c、默认 逐下进行分发 Activity -> ViewGroup -> View 的dispatchTouchEvent事件
 *  2、onInterceptTouchEvent(ev: MotionEvent?)：用来拦截事件，在dispatchTouchEvent中调用（该函数只有ViewGroup提供该方法）
 *      a、return true， 进行拦截，交由自己的onTouchEvent处理
 *      b、默认将事件传递给view的dispatchTouchEvent事件，进行处理
 *  3、onTouchEvent(event: MotionEvent?)：用来处理点击事件，在dispatchTouchEvent中调用
 *      a、return true，事件自己消费
 *      b、return false，事件交由上级的onTouchEvent处理，即：View -> ViewGroup -> Activity
 *
 * 1、默认返回的情况下，点击不同View生成的日志打印顺序
 *   A、点击ViewGroup中的View默认打印日志：
 *      ## Event Activiy dispatchTouchEvent ACTION_DOWN
 *      ## Event ViewGroup dispatchTouchEvent ACTION_DOWN
 *      ## Event ViewGroup onInterceptTouchEvent ACTION_DOWN
 *      ## Event View dispatchTouchEvent ACTION_DOWN
 *      ## Event View onTouchEvent ACTION_DOWN
 *      ## Event ViewGroup onTouchEvent ACTION_DOWN
 *      ## Event Activiy onTouchEvent ACTION_DOWN
 *      ## Event Activiy dispatchTouchEvent ACTION_UP
 *      ## Event Activiy onTouchEvent ACTION_UP
 *   B、点击ViewGroup打印的日志：
 *      ## Event Activiy dispatchTouchEvent ACTION_DOWN
 *      ## Event ViewGroup dispatchTouchEvent ACTION_DOWN
 *      ## Event ViewGroup onInterceptTouchEvent ACTION_DOWN
 *      ## Event ViewGroup onTouchEvent ACTION_DOWN
 *      ## Event Activiy onTouchEvent ACTION_DOWN
 *      ## Event Activiy dispatchTouchEvent ACTION_UP
 *      ## Event Activiy onTouchEvent ACTION_UP
 *   C、点击View打印的日志：
 *      ## Event Activiy dispatchTouchEvent ACTION_DOWN
 *      ## Event View dispatchTouchEvent ACTION_DOWN
 *      ## Event View onTouchEvent ACTION_DOWN
 *      ## Event Activiy onTouchEvent ACTION_DOWN
 *      ## Event Activiy dispatchTouchEvent ACTION_UP
 *      ## Event Activiy onTouchEvent ACTION_UP
 *
 * 2、dispatchTouchEvent 不同返回的情况：
 *   A、Activity
 *      a、return true或者false, 不向下传递：
 *          ## Event Activiy dispatchTouchEvent ACTION_DOWN
 *          ## Event Activiy dispatchTouchEvent ACTION_UP
 *      b、return false ,activity已经为最外层，则不会再传递给其他事件，打印与返回true一致
 *   B、GroupView
 *      a、return true， 拦截事件，自我处理
 *          ## Event Activiy dispatchTouchEvent ACTION_DOWN
 *          ## Event ViewGroup dispatchTouchEvent ACTION_DOWN
 *          ## Event Activiy dispatchTouchEvent ACTION_UP
 *          ## Event ViewGroup dispatchTouchEvent ACTION_UP
 *      b、return false， 拦截事件，传递给上级的onTouchEvent事件
 *          ## Event Activiy dispatchTouchEvent ACTION_DOWN
 *          ## Event ViewGroup dispatchTouchEvent ACTION_DOWN
 *          ## Event Activiy onTouchEvent ACTION_DOWN
 *          ## Event Activiy dispatchTouchEvent ACTION_UP
 *          ## Event Activiy onTouchEvent ACTION_UP
 *   C、View
 *      a、true 拦截事件，自我处理
 *          ## Event Activiy dispatchTouchEvent ACTION_DOWN
 *          ## Event ViewGroup dispatchTouchEvent ACTION_DOWN
 *          ## Event ViewGroup onInterceptTouchEvent ACTION_DOWN
 *          ## Event View dispatchTouchEvent ACTION_DOWN
 *          ## Event Activiy dispatchTouchEvent ACTION_UP
 *          ## Event ViewGroup dispatchTouchEvent ACTION_UP
 *          ## Event ViewGroup onInterceptTouchEvent ACTION_UP
 *          ## Event View dispatchTouchEvent ACTION_UP
 *      b、false，拦截事件，传递给上级的onTouchEvent处理
 *          ## Event Activiy dispatchTouchEvent ACTION_DOWN
 *          ## Event ViewGroup dispatchTouchEvent ACTION_DOWN
 *          ## Event ViewGroup onInterceptTouchEvent ACTION_DOWN
 *          ## Event View dispatchTouchEvent ACTION_DOWN
 *          ## Event ViewGroup onTouchEvent ACTION_DOWN
 *          ## Event Activiy onTouchEvent ACTION_DOWN
 *          ## Event Activiy dispatchTouchEvent ACTION_UP
 *          ## Event Activiy onTouchEvent ACTION_UP
 *
 *  3、onInterceptTouchEvent不同返回情况：
 *      a、true, 拦截 交由自己的onTouchEvent处理
 *          ## Event Activiy dispatchTouchEvent ACTION_DOWN
 *          ## Event ViewGroup dispatchTouchEvent ACTION_DOWN
 *          ## Event ViewGroup onInterceptTouchEvent ACTION_DOWN
 *          ## Event ViewGroup onTouchEvent ACTION_DOWN
 *          ## Event Activiy onTouchEvent ACTION_DOWN
 *          ## Event Activiy dispatchTouchEvent ACTION_UP
 *          ## Event Activiy onTouchEvent ACTION_UP
 *      b、false 不拦截，继续向下传递
 *          ## Event Activiy dispatchTouchEvent ACTION_DOWN
 *          ## Event ViewGroup dispatchTouchEvent ACTION_DOWN
 *          ## Event ViewGroup onInterceptTouchEvent ACTION_DOWN
 *          ## Event View dispatchTouchEvent ACTION_DOWN
 *          ## Event View onTouchEvent ACTION_DOWN
 *          ## Event ViewGroup onTouchEvent ACTION_DOWN
 *          ## Event Activiy onTouchEvent ACTION_DOWN
 *          ## Event Activiy dispatchTouchEvent ACTION_UP
 *          ## Event Activiy onTouchEvent ACTION_UP
 *  4、onTouchEvent不同返回情况
 *    A、ViewGroup
 *       a、true，拦截  自己处理
 *          ## Event Activiy dispatchTouchEvent ACTION_DOWN
 *          ## Event ViewGroup dispatchTouchEvent ACTION_DOWN
 *          ## Event ViewGroup onInterceptTouchEvent ACTION_DOWN
 *          ## Event View dispatchTouchEvent ACTION_DOWN
 *          ## Event View onTouchEvent ACTION_DOWN
 *          ## Event ViewGroup onTouchEvent ACTION_DOWN
 *          ## Event Activiy dispatchTouchEvent ACTION_UP
 *          ## Event ViewGroup dispatchTouchEvent ACTION_UP
 *          ## Event ViewGroup onTouchEvent ACTION_UP
 *       b、false, 不拦截，继续传递给上级
 *    B、View
 *       a、true，拦截 自我处理
 *          ## Event Activiy dispatchTouchEvent ACTION_DOWN
 *          ## Event ViewGroup dispatchTouchEvent ACTION_DOWN
 *          ## Event ViewGroup onInterceptTouchEvent ACTION_DOWN
 *          ## Event View dispatchTouchEvent ACTION_DOWN
 *          ## Event View onTouchEvent ACTION_DOWN
 *          ## Event Activiy dispatchTouchEvent ACTION_UP
 *          ## Event ViewGroup dispatchTouchEvent ACTION_UP
 *          ## Event ViewGroup onInterceptTouchEvent ACTION_UP
 *          ## Event View dispatchTouchEvent ACTION_UP
 *          ## Event View onTouchEvent ACTION_UP
 *       b、false 不拦截，继续传递个上级
 */

class EventView: View{
    constructor(context: Context):this(context, null)
    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr:Int):super(context,attrs,defStyleAttr){
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN ->
                log("## Event View dispatchTouchEvent ACTION_DOWN")
            MotionEvent.ACTION_UP ->
                log("## Event View dispatchTouchEvent ACTION_UP")
            MotionEvent.ACTION_MOVE ->
                log("## Event View dispatchTouchEvent ACTION_MOVE")
            else -> log("## Event View dispatchTouchEvent null")
        }
        return super.dispatchTouchEvent(event)
//        return true // 拦截事件，自我处理
//        return false // 拦截事件，但是会先触发上层的onTouchEvent事件
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN ->
                log("## Event View onTouchEvent ACTION_DOWN")
            MotionEvent.ACTION_UP ->
                log("## Event View onTouchEvent ACTION_UP")
            MotionEvent.ACTION_MOVE ->
                log("## Event View onTouchEvent ACTION_MOVE")
            else -> log("## Event View onTouchEvent null")
        }
        return super.onTouchEvent(event)
//        return true // 拦截自我处理
//        return false
    }
}
class EventViewGroup: LinearLayout{
    constructor(context: Context):this(context, null)
    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr:Int):super(context,attrs,defStyleAttr){
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN ->
                log("## Event ViewGroup dispatchTouchEvent ACTION_DOWN")
            MotionEvent.ACTION_UP ->
                log("## Event ViewGroup dispatchTouchEvent ACTION_UP")
            MotionEvent.ACTION_MOVE ->
                log("## Event ViewGroup dispatchTouchEvent ACTION_MOVE")
            else -> log("## Event ViewGroup dispatchTouchEvent null")
        }
        return super.dispatchTouchEvent(event)
//        return true // 拦截事件，自我处理
//        return false // 拦截事件，但是会先触发上层的onTouchEvent事件
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when(ev?.action){
            MotionEvent.ACTION_DOWN ->
                log("## Event ViewGroup onInterceptTouchEvent ACTION_DOWN")
            MotionEvent.ACTION_UP ->
                log("## Event ViewGroup onInterceptTouchEvent ACTION_UP")
            MotionEvent.ACTION_MOVE ->
                log("## Event ViewGroup onInterceptTouchEvent ACTION_MOVE")
            else -> log("## Event ViewGroup onInterceptTouchEvent null")
        }
        return super.onInterceptTouchEvent(ev)
//        return true // 拦截 交由自己的onTouchEvent处理
//        return false // 不拦截，继续传递
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN ->
                log("## Event ViewGroup onTouchEvent ACTION_DOWN")
            MotionEvent.ACTION_UP ->
                log("## Event ViewGroup onTouchEvent ACTION_UP")
            MotionEvent.ACTION_MOVE ->
                log("## Event ViewGroup onTouchEvent ACTION_MOVE")
            else -> log("## Event ViewGroup onTouchEvent null")
        }
        return super.onTouchEvent(event)
//        return true
//        return false
    }

//    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

//    }
}