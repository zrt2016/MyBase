package com.zrt.kotlinapp.animator.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_loading_animation.*
import kotlinx.android.synthetic.main.activity_splash_animation.*

/**
 * 加载框动画效果
 */
class LoadingAnimationActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun getLayoutResID(): Int {
        return R.layout.activity_loading_animation
    }

    override fun initData() {
        a_l_a_loading.startAnimation(rotateAnim())
    }
    fun rotateAnim(): Animation {
        val rotateAnim = RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
        rotateAnim.duration = 1000
        rotateAnim.repeatCount = 10
        rotateAnim.interpolator = LinearInterpolator()
        return rotateAnim
    }
}