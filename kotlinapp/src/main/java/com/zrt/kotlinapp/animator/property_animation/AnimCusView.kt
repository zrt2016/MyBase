package com.zrt.kotlinapp.animator.property_animation

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.zrt.kotlinapp.R

/**
 * @author：Zrt
 * @date: 2023/2/21
 * 自定义ImageView，完成抛物线动画
 */
class FallingBallImageView: AppCompatImageView {
    constructor(context: Context):this(context, null)
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr:Int):super(context, attrs, defStyleAttr)

    fun setFallingPos(point: Point){
        layout(point.x, point.y, point.x+width, point.y+height)
    }
}

/**
 * @author：Zrt
 * @date: 2023/2/14
 * 加载动画
 */
class LoadingImageView : ImageView {
    var mTop:Int = 0
    var mLeft:Int = 0
    var mRight:Int = 0
    var repeatCount:Int = 0  // 重复次数
    var imgCount: Int = 3 // 切换图片个数
    var valueAnim: ValueAnimator? = null
    constructor(context:Context):this(context, null)
    constructor(context:Context, attrs: AttributeSet?):this(context, null, 0)
    constructor(context:Context, attrs: AttributeSet?, defStyleAttr:Int):super(context, attrs, defStyleAttr){
        init()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        // 获取View顶部初始位置
        mTop = top
        mLeft = left
        mRight = right
    }

    private fun init() {
        valueAnim = ValueAnimator.ofInt(0, 100, 0)
        valueAnim?.let {
            it.duration = 2000
            it.repeatCount = ValueAnimator.INFINITE
            it.repeatMode = ValueAnimator.REVERSE
            it.interpolator = AccelerateDecelerateInterpolator()
            it.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener{
                override fun onAnimationUpdate(animation: ValueAnimator) {
                    val value = animation.animatedValue as Int
//                top = mTop + value
//                    left = mLeft + value
                    setLeft(mLeft + value)
                    setRight(mRight + value)
                }
            })
            it.addListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator?) {
                    repeatCount = 0
                    setImageDrawable(resources.getDrawable(R.mipmap.pic_1))
                }
                override fun onAnimationEnd(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationRepeat(animation: Animator?) {
                    repeatCount++
                    when(repeatCount % imgCount) {
                        1 -> {
                            setImageDrawable(resources.getDrawable(R.mipmap.pic_2))
                        }
                        2 -> {
                            setImageDrawable(resources.getDrawable(R.mipmap.pic_3))
                        }
                        else -> {
                            setImageDrawable(resources.getDrawable(R.mipmap.pic_1))
                        }
                    }
                }
            })
            it.start()
        }
    }

    fun startAnim(){
        valueAnim?.start()
    }
}

/**
 * 缩放动画
 */
class ScaleAnimTV: AppCompatTextView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setScaleSize(num:Float){
        scaleX = num
    }
    fun getScaleSize():Float{
        return 0.5f
    }
}

/**
 * 字符更新
 */
class CharTV: AppCompatTextView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
// Method setCharText() with type class java.lang.Character not found on target class
// class com.zrt.kotlinapp.animator.property_animation.CharTV
//    fun setCharText(value: Char){}
    fun setCharText(value: Character){
//        Log.i(">>>>", "##text value=${value.toString()}")
        setText(value.toString())
    }
}