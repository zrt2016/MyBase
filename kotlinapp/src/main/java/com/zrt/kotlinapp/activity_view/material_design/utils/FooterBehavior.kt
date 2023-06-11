package com.zrt.kotlinapp.activity_view.material_design.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.appbar.AppBarLayout

/**
 * @author：Zrt
 * @date: 2022/9/11
 * 自定义Behavior有两种方法：
 *      1、定义的View监听CoordinatorLayout的滑动状态。
 *      2、定义的View监听另一个View的状态变化，例如:View的大小、位置和显示状态等
 * 第一种方法需要注意：onStartNestedScroll和onNestedPreScroll方法
 * 第二种方法需要注意：layoutDependsOn和onDependsViewChanged方法
 */
/**
 * 定义的View监听CoordinatorLayout的滑动状态。
 * 第一种：onStartNestedScroll和onNestedPreScroll方法
 */
class FooterBehavior(val context:Context, val attrs:AttributeSet)
        : CoordinatorLayout.Behavior<View>(context, attrs) {
    var directionChange = 0;
    // 根据返回值判断是否是否需要关系此次滑动，此次关心Y轴上的滑动
    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: View,
             directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return (axes and  ViewCompat.SCROLL_AXIS_VERTICAL) != 0
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: View,
                                     directTargetChild: View, target: View, axes: Int): Boolean {
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes)
    }

    /**
     * 处理滑动
     * @param child 我们定义的View
     * @param dy 水平滑动距离，向上 dy为正值，向下dy为负值
     *      如果滑动距离累加值directionChange大于我们设置的child的高度，并且该child是Visible，则隐藏LinearLayout
     *      如果滑动距离累加值directionChange小于0，并且child是GONE的状态，则显示child
     */
    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
//        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        if (dy > 0 && directionChange < 0 || dy < 0 && directionChange > 0) {
            child.animate().cancel()
            directionChange = 0
        }

        directionChange += dy
        if (directionChange > child.height && child.visibility == View.VISIBLE){
            hide(child)
        }else if (directionChange < 0 && child.visibility == View.GONE){
            show(child)
        }
    }
    fun hide(child: View){
        val animate = child.animate().translationY(child.height.toFloat())
                .setInterpolator(FastOutSlowInInterpolator())
                .setDuration(200)
        animate.setListener(object : AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                child.visibility = View.GONE
            }
        })
        animate.start()
    }
    fun show(child: View){
        val animate = child.animate().translationY(0f)
                .setInterpolator(FastOutSlowInInterpolator())
                .setDuration(200)
        animate.setListener(object : AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                child.visibility = View.VISIBLE
            }
        })
        animate.start()
    }
}

/**
 * 定义的View监听另一个View的状态变化，例如:View的大小、位置和显示状态等
 * 第二种：layoutDependsOn和onDependsViewChanged方法
 */
class FooterBehaviorAppBar(val context:Context, val attrs:AttributeSet)
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
        val translationY = Math.abs(dependency.y)
        child.translationY = translationY
        return true
    }
}