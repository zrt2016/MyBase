package com.zrt.kotlinapp.activity_view.custom_basic


import android.os.Bundle
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_view_scroll.*

class ViewScrollActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int = R.layout.activity_view_scroll

    override fun initData() {
        //使用View动画使view滑动
//        a_v_s_view.animation = AnimationUtils.loadAnimation(this, R.anim.translate)
        //使用属性动画使view滑动, 缺点点击VIew原地会触发点击事件，该View并不会因为动画改变原有位置
//        ObjectAnimator.ofFloat(a_v_s_view, "translationX",0f,300f).setDuration(1000).start();
        //使用Scroll来进行平滑移动
        a_v_s_view.smoothScrollTo(200,0);

    }

}