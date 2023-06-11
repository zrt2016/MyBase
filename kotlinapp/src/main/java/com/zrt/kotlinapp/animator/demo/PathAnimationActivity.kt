package com.zrt.kotlinapp.animator.demo

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_path_animation.*

/**
 * 使用Animator完成路径动画
 * 点击按钮，然后多个按钮呈扇形方式移动显示
 */
class PathAnimationActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayoutResID(): Int = R.layout.activity_path_animation

    override fun initData() {
        a_p_a_menu.setOnClickListener {
            showPathAnimator()
        }
        a_p_a_menu_item1.setOnClickListener {
            ToastUtils.show("item1")
        }
        a_p_a_menu_item2.setOnClickListener {
            ToastUtils.show("item2")
        }
        a_p_a_menu_item3.setOnClickListener {
            ToastUtils.show("item3")
        }
        a_p_a_menu_item4.setOnClickListener {
            ToastUtils.show("item4")
        }
        a_p_a_menu_item5.setOnClickListener {
            ToastUtils.show("item5")
        }
    }
    var isPlayAnim = false;
    var isShowMenu: Boolean = false
    private fun showPathAnimator() {
        if (isPlayAnim) return
        if(!isShowMenu){
            // 展开所有菜单
            isShowMenu = true
            openMenu()
        }else{
            // 关闭菜单
            isShowMenu = false
            closeMenu()
        }

    }

    /**
     * 菜单展开
     */
    private fun openMenu() {
        val sum:Int = 5 // item总数
        doAnimatorOpen(a_p_a_menu_item1, 0, sum, 300)
        doAnimatorOpen(a_p_a_menu_item2, 1, sum, 300)
        doAnimatorOpen(a_p_a_menu_item3, 2, sum, 300)
        doAnimatorOpen(a_p_a_menu_item4, 3, sum, 300)
        doAnimatorOpen(a_p_a_menu_item5, 4, sum, 300)
    }

    /**
     * 关闭菜单
     */
    private fun closeMenu(){
        val sum:Int = 5 // item总数
        doAnimatorClose(a_p_a_menu_item1, 0, sum, 300)
        doAnimatorClose(a_p_a_menu_item2, 1, sum, 300)
        doAnimatorClose(a_p_a_menu_item3, 2, sum, 300)
        doAnimatorClose(a_p_a_menu_item4, 3, sum, 300)
        doAnimatorClose(a_p_a_menu_item5, 4, sum, 300)
    }
    /**
     * @param view：指定View
     * @param index：第几个view
     * @param sum：展开view总数
     * @param radius：平移距离，即展开扇形半径
     */
    private fun doAnimatorOpen(view:View, index:Int, sum:Int, radius:Int) {
        isPlayAnim = true
        view.visibility = View.VISIBLE
        // 计算当前view的角度对应的弧度值
        val radianValue = Math.toRadians(90.0) / (sum - 1) * index
        val translateX = -(radius * Math.sin(radianValue)).toFloat() // 终点 X 轴的坐标
        val translateY = -(radius * Math.cos(radianValue)).toFloat() // 终端 Y 轴的坐标
        val animSet = AnimatorSet()
        // 平移，缩放、淡化
        animSet.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", 0f, translateX),
                ObjectAnimator.ofFloat(view, "translationY", 0f, translateY),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        )
        animSet.addListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                isPlayAnim = false
            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        })
        animSet.setDuration(500)
        animSet.start()
    }

    fun doAnimatorClose(view:View, index:Int, sum:Int, radius:Int) {
        isPlayAnim = true
        view.visibility = View.VISIBLE
        val radianValue = Math.toRadians(90.0) / (sum - 1) * index
        val translateX = -(radius * Math.sin(radianValue)).toFloat()
        val translateY = -(radius * Math.cos(radianValue)).toFloat()
        val animSet = AnimatorSet()
        animSet.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", translateX, 0f),
                ObjectAnimator.ofFloat(view, "translationY", translateY, 0f),
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f),
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f),
                ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
        )
        animSet.addListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                view.visibility = View.GONE
                isPlayAnim = false
            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        })
        animSet.setDuration(500)
        animSet.start()
    }
}