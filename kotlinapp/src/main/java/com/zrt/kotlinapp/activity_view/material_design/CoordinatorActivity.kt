package com.zrt.kotlinapp.activity_view.material_design

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.activity_view.material_design.utils.RecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_coordinator.*

/**
 * CoordinatorLayout最经典的设计就是Behavior
 * 常用的app:layout_behavior="@string/appbar_scrolling_view_behavior"对应着AppBarLayout$ScrollingViewBehavior
 * 用来通知AppBarLayout发生了滚动事件
 *
 * 自定义Behavior有两种方法：
 *      1、定义的View监听CoordinatorLayout的滑动状态。
 *      2、定义的View监听另一个View的状态变化，例如:View的大小、位置和显示状态等
 * 第一种方法需要注意：onStartNestedScroll和onNestedPreScroll方法
 * 第二种方法需要注意：layoutDependsOn和onDependsViewChanged方法
 *
 * android:fitsSystemWindows="true" ： 表示空间会出现在系统状态栏里.
 *
 * 此处我们定义一个提示条，向上滚动的时候消失，向下滚动的时候就出现
 *  自定义：FooterBehavior
 *  app:layout_behavior=".activity_view.material_design.utils.FooterBehavior"
 *
 */
class CoordinatorActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int = R.layout.activity_coordinator

    override fun initData() {
        setSupportActionBar(a_c_toolbar)
        a_c_collapsing.title = "哆啦A梦"
        a_c_recyclerView.layoutManager = LinearLayoutManager(this)
        a_c_recyclerView.itemAnimator = DefaultItemAnimator()
        a_c_recyclerView.adapter = RecyclerViewAdapter(this, 50)
    }
}