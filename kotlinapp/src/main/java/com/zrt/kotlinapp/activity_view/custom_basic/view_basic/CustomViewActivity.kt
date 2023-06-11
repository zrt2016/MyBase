package com.zrt.kotlinapp.activity_view.custom_basic.view_basic

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_custom_view.*

/**
 * 自定义View，包含自定义Rect、自定义标题栏和自定义横向滑动列表切换
 */
class CustomViewActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int = R.layout.activity_custom_view

    override fun initData() {
        a_c_v_mtitle.setLeftListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                ToastUtils.show("点击左键")
            }
        })
        a_c_v_mtitle.setRightListener{
            ToastUtils.show("点击右键")
        }
        val strs1 = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15")
        val adapter1: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, strs1)
        lv_one.adapter = adapter1

        val strs2 = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O")
        val adapter2: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, strs2)
        lv_two.adapter = adapter2
    }
}