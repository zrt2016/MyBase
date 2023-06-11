package com.zrt.kotlinapp.activity_view.custom_basic.view_basic

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_custom_view_two.*
import java.lang.StringBuilder
import java.util.*

class CustomViewTwoActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int = R.layout.activity_custom_view_two

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        // 加载流式布局
        initFlowLayout()
        // 初始化手势检测
        initGesture()
    }

    private fun initFlowLayout() {
        // 流式布局添加
        for (i in 0..10) {
            val view = TextView(this)
            val margin = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            margin.setMargins(4, 4, 4, 4)
            view.layoutParams = margin
            view.background = resources.getDrawable(R.drawable.flow_layout_flag_03)
            view.setTextColor(Color.WHITE)
            view.setTextSize(15f)
    //            view.setTextAppearance(R.style.text_flag_01)
            val randomInt = Random().nextInt(10)
            val sb = StringBuilder()
            for (x in 0..randomInt) {
                sb.append("$i-")
            }
            view.setText(sb.toString())
            a_c_V_two_flow.addView(view)
        }
        // 流式布局添加2
        for (i in 0..40) {
            val view = TextView(this)
            val margin = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            margin.setMargins(10, 5, 10, 5)
            view.layoutParams = margin
            view.background = resources.getDrawable(R.drawable.flow_layout_flag_03)
            view.setTextColor(Color.WHITE)
            view.setTextSize(15f)
    //            view.setTextAppearance(R.style.text_flag_01)
            val randomInt = Random().nextInt(100)
            val sb = StringBuilder()
            for (x in 0..randomInt) {
                sb.append("$i-")
            }
            view.setText(sb.toString())
            a_c_V_two_flow2.addView(view)
        }
    }

    private fun initGesture() {
        // 添加 OnGestureListener 监听
//        mGestureDetector = GestureDetector(this, MyGestureListener())
        // 添加 MyOnDoubleTapListener 监听的2种方式
        // 方式一：MyOnDoubleTapListener 必须同时实现 OnGestureListener 和 OnDoubleTapListener 实例
        mGestureDetector = GestureDetector(this, MyOnDoubleTapListener())
        // 方式二：MyOnDoubleTapListener 可只实现 OnDoubleTapListener 实例
        mGestureDetector.setOnDoubleTapListener(MyOnDoubleTapListener())
        a_c_v_two_gesture.setOnTouchListener(onTouch)
    }

    lateinit var mGestureDetector: GestureDetector
    val onTouch = object : View.OnTouchListener{
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            // 事件拦截
            return mGestureDetector.onTouchEvent(event)
        }
    }


}