package com.zrt.ydhl_phz.tools.behavior

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.zrt.ydhl_phz.R
import com.zrt.ydhl_phz.tools.DensityUtils

/**
 * @author：Zrt
 * @date: 2022/11/9
 */
/**
 * 定义的View监听另一个View的状态变化，例如:View的大小、位置和显示状态等
 * 第二种：layoutDependsOn和onDependsViewChanged方法
 */
class FooterBehaviorAppBar(val context: Context, val attrs: AttributeSet)
    : CoordinatorLayout.Behavior<View>(context, attrs) {
    /**
     * 返回我们关心的类
     * @param parent 当前CoordinatorLayout
     * @param child 我们设置behavior的VIew
     * @param dependency 关心的View
     */
    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        // 表示我们关心的是AppBarLayout
        return dependency is AppBarLayout
    }

    /**
     * 根据我们关心的View变化来对我们设置behavior的VIew进行一系列的处理。
     * 此处根据dependency的垂直移动距离来设定child的移动距离
     */
    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
//        val translationY = Math.abs(dependency.y)
//        child.translationY = translationY
//        child.y/
        var main_toolbar = parent.findViewById<Toolbar>(R.id.main_toolbar)
        // 最大移动距离
        var maxTransY = dependency.height-main_toolbar.height
        // child移动的距离
        var trans = (dependency.height-main_toolbar.height)/2


        var y = dependency.y
        var minTop = DensityUtils.dip2px(parent.context, 75f)
        Log.i(">>>>", "##y=$y , main_toolbar.height=${main_toolbar.height}, child.height=${child.height}, dependency.height=${dependency.height}")
        Log.i(">>>>", "##dependency.y=${dependency.y}, child.y=${child.y}, parent.y=${parent.y}")
        child.translationY = dependency.y / (maxTransY/trans)
        return true
    }
}