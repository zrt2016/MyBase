package com.zrt.kotlinapp.activity_view.custom_view.loading_view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import com.zrt.kotlinapp.R

/**
 * @author：Zrt
 * @date: 2023/5/31
 */

class BounceLoadingView: LinearLayout {
    lateinit var mContext: Context
    // 动画view
    lateinit var shapeLoadingView:ImageView
    lateinit var shapeLoadingView2:GraphChangeLoadingView
    // 阴影view
    lateinit var indication:ImageView
    val mTranslationYDistance: Float = 100f
    constructor(context: Context):this(context, null)
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr) {
        val view = View.inflate(context, R.layout.view_loading_bounce, this)
        mContext = context
        shapeLoadingView = view.findViewById(R.id.shapeLoadingView)
        shapeLoadingView.visibility = View.GONE
        // 使用新的 shapeLoadingView2 替换 shapeLoadingView
        shapeLoadingView2 = view.findViewById(R.id.shapeLoadingView2)
        indication = view.findViewById(R.id.indication)

        val animatorSet = AnimatorSet()
        // 上下平移
        val shapTYAnima = ObjectAnimator.ofFloat(shapeLoadingView2, "translationY", 0f, mTranslationYDistance)
        shapTYAnima.interpolator = AccelerateInterpolator(1f)
        shapTYAnima.repeatCount = ObjectAnimator.INFINITE
        shapTYAnima.repeatMode = ObjectAnimator.REVERSE
        // 旋转动画
        val shapRYAnima = ObjectAnimator.ofFloat(shapeLoadingView2, "rotation", 0f, 180f)
        shapRYAnima.repeatCount = ObjectAnimator.INFINITE
        shapRYAnima.repeatMode = ObjectAnimator.REVERSE
        // 阴影缩放动画
        val indicaScaleXAnima = ObjectAnimator.ofFloat(indication, "scaleX", 1f, 0.3f)
        indicaScaleXAnima.repeatCount = ObjectAnimator.INFINITE
        indicaScaleXAnima.repeatMode = ObjectAnimator.REVERSE
        animatorSet.setDuration(1000)

        animatorSet.playTogether(shapTYAnima, shapRYAnima, indicaScaleXAnima)
        shapTYAnima.addListener(object : AnimatorListenerAdapter(){
            //设置动画监听器，监听该动画的开始、停止、取消、结束等状态，我们往往会用AnimtorListener适配器类来只实现我们需要的方法
            override fun onAnimationEnd(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {
                // 重复播放时，改变颜色，然后执行上抛动画
//                if (num%2 == 0) {
//                    shapeLoadingView2.setBackground(ColorDrawable(Color.BLUE))
//                }else {
//                    shapeLoadingView2.setBackground(ColorDrawable(Color.GREEN))
//                }
//                num++
                shapeLoadingView2.changeGraph()
            }
        })
        animatorSet.start()
    }
    var num = 0;
}

class GraphChangeLoadingView : View {
    // 图片类型，0、矩形；1、圆形；2、三角形
    var graph:Int = 2
    var maxGraph = 2
    lateinit var mPaint: Paint
    lateinit var mPath:Path
    var w = 20
    var h = 20
    constructor(context: Context):this(context, null)
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr) {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.strokeWidth = 1f
        mPaint.style = Paint.Style.FILL_AND_STROKE
        mPath = Path()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var wSize = MeasureSpec.getSize(widthMeasureSpec)
        var wMode = MeasureSpec.getMode(widthMeasureSpec)
        var hSize = MeasureSpec.getSize(heightMeasureSpec)
        var hMode = MeasureSpec.getMode(heightMeasureSpec)
        if (wMode == MeasureSpec.AT_MOST && hMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(w, h)
        }else if (wMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(w, hSize)
            h = hSize
        }else if (hMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(wSize, h)
            w = wSize
        }else {
            setMeasuredDimension(wSize, hSize)
            w = wSize
            h = hSize
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPath.reset()

        when (graph) {
            0 -> {
                mPaint.setColor(resources.getColor(R.color.orange))
                mPath.addRect(RectF(0f, 0f, w.toFloat(), h.toFloat()), Path.Direction.CW) // 顺时针
            }
            1 -> {
                mPaint.setColor(resources.getColor(R.color.green1))
                mPath.addCircle(w/2f, h/2f, w/2f, Path.Direction.CW)
            }
            2 -> {
                mPaint.setColor(resources.getColor(R.color.violet))
                mPath.moveTo(w/2f, 0f)
                mPath.lineTo(w.toFloat(), h.toFloat())
                mPath.lineTo(0f, h.toFloat())
                mPath.close()
            }
        }
        canvas?.drawPath(mPath, mPaint)
    }

    fun changeGraph() {
        graph++
        if (graph > maxGraph) graph = 0
        postInvalidate()
    }
}