package com.zrt.kotlinapp.animator.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_loading_animation.*
import kotlinx.android.synthetic.main.activity_scanner_animation.*

/**
 * 扫描波纹动画
 */
class ScannerAnimationActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun getLayoutResID(): Int {
        return R.layout.activity_scanner_animation
    }

    override fun initData() {
        // 实现动画边放大边降低透明度的效果
        val anima1 = AnimationUtils.loadAnimation(this, R.anim.scanner_scale_aplha_anim)
        val anima2 = AnimationUtils.loadAnimation(this, R.anim.scanner_scale_aplha_anim)
        val anima3 = AnimationUtils.loadAnimation(this, R.anim.scanner_scale_aplha_anim)
        val anima4 = AnimationUtils.loadAnimation(this, R.anim.scanner_scale_aplha_anim)
        a_s_a_start.setOnClickListener{
            a_s_a_circle1.startAnimation(anima1)
            anima2.startOffset = 500
            a_s_a_circle2.startAnimation(anima2)
            anima3.startOffset = 1000
            a_s_a_circle3.startAnimation(anima3)
            anima4.startOffset = 1500
            a_s_a_circle4.startAnimation(anima4)
        }
    }
}