package com.zrt.kotlinapp.animator

import android.os.Bundle
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.activity_view.recyclerview.CustomViewNavigation
import com.zrt.kotlinapp.activity_view.recyclerview.addRecycleView
import com.zrt.kotlinapp.animator.demo.LoadingAnimationActivity
import com.zrt.kotlinapp.animator.demo.PathAnimationActivity
import com.zrt.kotlinapp.animator.demo.ScannerAnimationActivity
import com.zrt.kotlinapp.animator.demo.SplashAnimationActivity
import com.zrt.kotlinapp.animator.path_measure.PathMeasureActivity
import com.zrt.kotlinapp.animator.property_animation.PropertAdvancedActivity
import com.zrt.kotlinapp.animator.property_animation.PropertyAnimationActivity
import com.zrt.kotlinapp.animator.view_animation.ViewAnimationActivity
import kotlinx.android.synthetic.main.activity_animation_basic.*

/**
 * Android动画分为两种：
 *  @see com.zrt.kotlinapp.animator.view_animation.ViewAnimationActivity
 *  1、视图动画（View Animation），包含以下两种：
 *      ① 补间动画 （Tween Animation）
 *      ② 逐帧动画（Frame Animation）
 *    命名方式：XXXXAnimation,在android.view.animation包中
 *
 *  @see com.zrt.kotlinapp.animator.property_animation.PropertyAnimationActivity
 *  2、属性动画（Property Animation），包含以下两种：
 *      ① ValueAnimator
 *      ② ObjectAnimator
 *    命名方式：XXXXAnimator,在android.animation包中，在3.0引入
 *   属性动画可处理一个View在1分钟内从绿色变为红色，而补间动画不行。
 */
class AnimationBasicActivity : BasicActivity() {
    val mList = ArrayList<CustomViewNavigation>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_animation_basic
    }

    override fun initData() {
        initList()
        addRecycleView(this, a_a_basic_frame, mList)
    }

    fun initList(){
        // 自定义View，包含自定义Rect、自定义标题栏和自定义横向滑动列表切换
        mList.add(CustomViewNavigation("动画", AnimatorActivity::class.java))
        mList.add(CustomViewNavigation("视图动画", ViewAnimationActivity::class.java))
        mList.add(CustomViewNavigation("Splash 镜头由远及近效果", SplashAnimationActivity::class.java))
        mList.add(CustomViewNavigation("加载框动画效果", LoadingAnimationActivity::class.java))
        mList.add(CustomViewNavigation("波纹动画效果", ScannerAnimationActivity::class.java))
        mList.add(CustomViewNavigation("属性动画", PropertyAnimationActivity::class.java))
        mList.add(CustomViewNavigation("路径扇形平移动画", PathAnimationActivity::class.java))
        mList.add(CustomViewNavigation("动画进阶PropertyValuesHolder与Keyframe", PropertAdvancedActivity::class.java))
        mList.add(CustomViewNavigation("动画进阶PathMeasure、矢量图动画", PathMeasureActivity::class.java))


    }

}