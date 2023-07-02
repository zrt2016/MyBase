package com.zrt.kotlinapp.activity_view.custom_view.loading_view

import android.R.attr.factor
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
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

    lateinit var animatorSetFall: AnimatorSet
    lateinit var animatorSetUp: AnimatorSet

    val ANIM_DURATION:Long = 600

    companion object {
        // 代码添加
        fun attach(parent: ViewGroup):BounceLoadingView {
            val loadingView = BounceLoadingView(parent.context)
            loadingView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//            loadingView.gravity = Gravity.CENTER
            parent.addView(loadingView)

            return loadingView
        }
    }

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

        initFallAnim()
        initUpAnim()
        fallAnimStart()
    }
    fun fallAnimStart() {
        animatorSetFall.start()
    }
    // 初始化 下落动画
    private fun initFallAnim() {
        animatorSetFall = AnimatorSet()
        // 上下平移
        val shapTYAnima = ObjectAnimator.ofFloat(shapeLoadingView2, "translationY", 0f, mTranslationYDistance)
        shapTYAnima.interpolator = AccelerateInterpolator(1f)
//        shapTYAnima.repeatCount = ObjectAnimator.INFINITE
//        shapTYAnima.repeatMode = ObjectAnimator.REVERSE
        // 旋转动画
//        val shapRYAnima = ObjectAnimator.ofFloat(shapeLoadingView2, "rotation", 0f, 180f)
//        shapRYAnima.repeatCount = ObjectAnimator.INFINITE
//        shapRYAnima.repeatMode = ObjectAnimator.REVERSE
        // 阴影缩放动画
        val indicaScaleXAnima = ObjectAnimator.ofFloat(indication, "scaleX", 1f, 0.2f)
//        indicaScaleXAnima.repeatCount = ObjectAnimator.INFINITE
//        indicaScaleXAnima.repeatMode = ObjectAnimator.REVERSE
        animatorSetFall.setDuration(ANIM_DURATION)
        animatorSetFall.playTogether(shapTYAnima, indicaScaleXAnima)
//        animatorSetFall.playTogether(shapTYAnima, shapRYAnima, indicaScaleXAnima)
        animatorSetFall.addListener(object : AnimatorListenerAdapter() {
            //设置动画监听器，监听该动画的开始、停止、取消、结束等状态，我们往往会用AnimtorListener适配器类来只实现我们需要的方法
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                upAnimStart()
                // 结束时，改变图像，然后执行上抛动画
                shapeLoadingView2.changeGraph()
            }

            override fun onAnimationRepeat(animation: Animator?) {
//                // 重复播放时，改变颜色，然后执行上抛动画
//                shapeLoadingView2.changeGraph()
            }
        })
    }
    fun upAnimStart() {
        animatorSetUp.start();
    }
    // 初始化向上的动画
    fun initUpAnim() {
        animatorSetUp = AnimatorSet()
        // 上下平移
        val shapTYAnima = ObjectAnimator.ofFloat(shapeLoadingView2, "translationY", mTranslationYDistance, 0f)
        shapTYAnima.interpolator = AccelerateInterpolator(1f)
        // 阴影缩放动画
        val indicaScaleXAnima = ObjectAnimator.ofFloat(indication, "scaleX", 0.2f, 1f)
        animatorSetUp.setDuration(ANIM_DURATION)
        animatorSetUp.playTogether(shapTYAnima, indicaScaleXAnima)
        animatorSetUp.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                // 动画开始，播放旋转动画
                shapeLoadingView2.startShapeRoteAnimator(ANIM_DURATION)
            }
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                // 播放向上动画
                fallAnimStart()
            }
        })
    }

    /**
     * 隐藏优化
     */
    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        when(visibility) {
            View.GONE, View.INVISIBLE -> {
                parent?.let {
                    val vp = it as ViewGroup
                    vp.removeView(this)
                    shapeLoadingView2.clearAnimation()
                    indication.clearAnimation()
                    this.removeAllViews()
                }
            }
        }
    }
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
        // 初始化动画
        initRotationAnim()
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
                mPath.addCircle(w / 2f, h / 2f, w / 2f, Path.Direction.CW)
            }
            2 -> {
                mPaint.setColor(resources.getColor(R.color.violet))
                mPath.moveTo(w / 2f, 0f)
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
    lateinit var mRectRoteAnimation:ObjectAnimator
    lateinit var mCircleRoteAnimation:ObjectAnimator
    lateinit var mTriangleRoteAnimation:ObjectAnimator
    fun initRotationAnim() {
        mRectRoteAnimation = ObjectAnimator.ofFloat(this, "rotation", 0f, -120f)
        mCircleRoteAnimation = ObjectAnimator.ofFloat(this, "rotation", 0f, 120f)
        mTriangleRoteAnimation = ObjectAnimator.ofFloat(this, "rotation", 0f, 180f)
    }
    // 0、矩形；1、圆形；2、三角形
    fun getUpThrowRoteAnimation(): ObjectAnimator {
        return when(graph) {
            1 -> mCircleRoteAnimation
            2 -> mTriangleRoteAnimation
            else -> mRectRoteAnimation
        }
    }

    /**
     * 执行旋转动画
     */
    fun startShapeRoteAnimator(duration:Long) {
        val roteAnimation: ObjectAnimator = getUpThrowRoteAnimation()
        roteAnimation.duration = duration
        roteAnimation.interpolator = DecelerateInterpolator()
        roteAnimation.start()
    }
}