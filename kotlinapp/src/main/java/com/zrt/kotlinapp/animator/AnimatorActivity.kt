package com.zrt.kotlinapp.animator

import android.animation.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.marginLeft
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.learnkotlin.log
import kotlinx.android.synthetic.main.activity_animator.*

/**
 * 动画有三种：帧动画、View动画和属性动画，其中属性动画是在3.0推出的
 * View动画：提供了AlphaAnimation（淡化）、RotateAnimation（旋转）、TranslateAnimation（平移）和ScaleAnimation（缩放）
 * View动画的缺点：不具有交互性，某个元素发生View动画后，其响应事件还在动画进行前的位置.
 *          优点：效率比较高，使用方便。
 *      总结：所以推荐一般只做动画效果。
 * 属性动画：
 *      常用AnimatorSet和ObjectAnimator配合：使用ObjectAnimator进行精细化控制，控制一个对象和一个属性值，
 *   使用多个ObjectAnimator组合到AnimatorSet形成一个动画
 *
 *   ObjectAnimator.ofFloat(a_a_object_translation, "translationX", 400f)
 *   参数二：操作的属性（该属性必须要有get和set方法）
 *      a、translationX和translationY：用来沿着X轴和Y轴移动
 *      b、rotation、rotationX和rotationY：围绕View的支点进行旋转
 *      c、PrivotX和PrivotY：控制View对象支点的位置，围绕支点旋转和缩放变换处理。默认支点位置是View对象的中心点
 *      d、alpha：透明度，默认1（不透明），0（完全透明）
 *      e、x和y描述View对象在其容器中最终的位置
 */
class AnimatorActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int = R.layout.activity_animator

    override fun initData() {
        a_a_object_translation.setOnClickListener {
            // 向右平移
            // translationX+100f，在现基础上继续平移
            val ofFloat = ObjectAnimator.ofFloat(a_a_object_translation,
                    "translationX", a_a_object_translation.translationX+100f);
            ofFloat.setDuration(2000)
            ofFloat.start() // 可使用AnimatorSet控制
            addAnimator(ofFloat)
            // 加宽
            var myView = MyView(a_a_object_translation)
            val duration = ObjectAnimator.ofInt(myView, "width", myView.getWidth() * 3 / 2).setDuration(2000)
            duration.start() // 可使用AnimatorSet控制
//            val animatorSet = AnimatorSet()
//            animatorSet.play(ofFloat).with(duration)
//            animatorSet.start()
        }
        // ValueAnimtor:不提供任何动画效果，用来产生有一定规律的数字，从而让调用者控制动画的实现过程
        val ofFloat = ValueAnimator.ofFloat(0f, 100f)
        ofFloat.setTarget(a_a_object_translation)
        ofFloat.setDuration(2000).start()
        ofFloat.addUpdateListener {
            animation ->
            val animatedValue = animation.animatedValue
            log("##Animator Listener value=$animatedValue")
        }
        // animatorSet
        a_a_object_as.setOnClickListener {
            val trans = ObjectAnimator.ofFloat(a_a_object_as, "translationX", 0.0f, 200.0f, 0f)
            val scale = ObjectAnimator.ofFloat(a_a_object_as, "scaleX", 1.0f, 2.0f,1.0f)
            val rotation = ObjectAnimator.ofFloat(a_a_object_as, "rotationX", 0f, 90f, a_a_object_as.marginLeft.toFloat())
            val animator = AnimatorSet()
            animator.setDuration(2000)
            animator.play(trans).with(scale).after(rotation)
            animator.start()
        }
        a_a_object_pvh.setOnClickListener {
            val translationX = PropertyValuesHolder.ofFloat("translationX",  0.0f, 200.0f, 0f)
            val scaleX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.5f, 1.0f)
            val ratationX = PropertyValuesHolder.ofFloat("ratationX", 0f, 90f, 180f, 0f)
            val alpha = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.5f,1.0f)
            val animator = ObjectAnimator.ofPropertyValuesHolder(a_a_object_pvh, translationX, scaleX, ratationX, alpha)
            animator.setDuration(2000).start()
        }
        a_a_object_4.setOnClickListener {
            val loadAnimator = AnimatorInflater.loadAnimator(this, R.animator.object_scale)
            loadAnimator.setTarget(a_a_object_4)
            loadAnimator.start()
        }
    }
    fun addAnimator(animator: ObjectAnimator){
        // 添加监听
        animator.addListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {
                log("##onAnimationRepeat")
            }

            override fun onAnimationEnd(animation: Animator?) {
                log("##onAnimationEnd")
            }

            override fun onAnimationCancel(animation: Animator?) {
                log("##onAnimationCancel")
            }
            // 开始
            override fun onAnimationStart(animation: Animator?) {
                log("##onAnimationStart")
            }
        })
        // 选择必要事件监听
//        animator.addListener(object : AnimatorListenerAdapter() {
//            override fun onAnimationEnd(animation: Animator?) {
//                super.onAnimationEnd(animation)
//            }
//        })
    }

    class MyView(var mTarget:View){
        fun getWidth():Int{
            return mTarget.layoutParams.width
        }
        fun setWidth(width:Int){
            mTarget.layoutParams.width = width
            mTarget.requestLayout()
        }
    }

}