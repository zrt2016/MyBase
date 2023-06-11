package com.zrt.kotlinapp.animator.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_splash_animation.*

/**
 * 设计一个由远到近的镜头效果
 */
class SplashAnimationActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_splash_animation
    }

    override fun initData() {
        a_s_a_img.startAnimation(scaleAnim())
    }

    fun scaleAnim():Animation{
        val scaleAnim = ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
        scaleAnim.duration = 5000
        scaleAnim.fillAfter = true
        scaleAnim.interpolator = BounceInterpolator()
        return scaleAnim
    }
}