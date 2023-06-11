package com.zrt.kotlinapp.fragment

import android.os.Bundle
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.fragment.one.LeftFragment
import com.zrt.kotlinapp.fragment.one.RightFragment
import kotlinx.android.synthetic.main.activity_f_one.*

/**
 * 静态创建Fragment:一般很少使用
 * <fragment
        android:id="@+id/f_left_frag"
        android:name="com.zrt.kotlinapp.fragment.one.LeftFragment"
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_weight="1"/>
 *  通过name声明要添加的Fragment类名
 */
class FOneActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayoutResID(): Int = R.layout.activity_f_one

    override fun initData() {
        // 获取fragment中的实例 进行交互
        val leftFragment = supportFragmentManager.findFragmentById(R.id.f_left_frag) as LeftFragment
        val rightFragment = f_right_frag as RightFragment
    }
}