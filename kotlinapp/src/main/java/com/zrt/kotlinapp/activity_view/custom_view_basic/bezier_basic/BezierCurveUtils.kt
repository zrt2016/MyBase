package com.zrt.kotlinapp.activity_view.custom_view_basic.bezier_basic

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator

/**
 * @author：Zrt
 * @date: 2022/9/13
 * 参考博客：
 * 贝塞尔曲线：https://blog.csdn.net/xiaozhangcsdn/article/details/98963937
 * 烟花效果：https://mp.weixin.qq.com/s/qBNsu2WycKuq7__n7ooHCA
 *
 * 贝济埃曲线（Bezier Curve）：
 *    图中有A、B、C三个点，AB、BC两条线。AB中取点a,BC中取点b,连接ab.在ab中取点c
 *    另Aa/aB == Bb/bC == ac/cb = t , 0<= t <= 1. c点运行的轨迹即贝塞尔曲线（二阶）
 *    一阶公式（2个控制点A、B）：b(t) = (1-t)A+tB
 *    二阶公式（3个控制点A、B、C）：b(t) = A(1-t)^2 + B2t(1-t) + Ct^2
 *    三阶公式（4个控制点A、B、C、D）：b(t) = A(1-t)^3 + 3Bt(1-t)^2 + 3Ct^2(1-t) + Dt^3
 * Path 类中与贝济埃曲线有关的函数
 *    二阶曲线 (起始点通过 moveTo 绘制， 默认(0,0)为起始点)
 *      quadTo(float x1, float y1, float x2, float y2)
 *          moveTo(x,y)起始点、(x1,x1)控制点坐标、(x2,y2)终点坐标
 *          连续绘制，上一个quadTo的终点，就是下一个的起点
 *      rQuadTo(float dx1, float dy1, float dx2, float dy2)
 *         dx1：控制点相对起点的x位移，dy1：控制点相对起点的y位，
 *         dx2：终点相对起点的x位移，dy2：终点相对起点的y位移
 *      quadTo 与 rQuadTo 比较，此处默认初始起始点为 moveTo(x,y)
 *        dx1 = x1 - x; dy2 = y1 - y; dx2 = x2 - x; dy2 = y2 - y
 *    三阶曲线
 *      cubicTo(float x1, float y1, float x2, float y2, float x3, float y3)
 *      rCubicTo(float x1, float y1, float x2, float y2, float x3, float y3)
 * @see BezierGestureTrackView 使用 贝济埃曲线 优化 手势轨迹捕。使用函数 quadTo。
 * @see BezierAnimWareView 使用 贝济埃曲线 完成波浪动画。 使用函数 rQuadTo
 */
class BezierTwoView: View {
    val mPaint = Paint()
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context):this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr) {
        mPaint.strokeWidth = 2f
        mPaint.style = Paint.Style.STROKE
        mPaint.isAntiAlias = true // 设置抗锯齿
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 绘制2条控制线
        mPaint.setColor(Color.BLUE)
        canvas?.drawLine(10f, 100f, 30f, 10f, mPaint)
        canvas?.drawLine(30f, 10f, 120f, 100f, mPaint)
        canvas?.drawLine(120f, 100f, 180f, 10f, mPaint)
        canvas?.drawLine(180f, 10f, 250f, 50f, mPaint)
        // 绘制起点、控制点和终点
        mPaint.strokeWidth = 4f
        mPaint.setColor(Color.BLACK)
        canvas?.drawPoint(10f, 100f, mPaint)
        canvas?.drawPoint(30f, 10f, mPaint)
        canvas?.drawPoint(120f, 100f, mPaint)
        canvas?.drawPoint(180f, 10f, mPaint)
        canvas?.drawPoint(250f, 50f, mPaint)
        // 使用 path.quadTo 绘制贝济埃曲线
        mPaint.strokeWidth = 2f
        mPaint.setColor(Color.RED)
        val path:Path = Path()
        path.moveTo(10f, 100f)
        // 方式一
        path.quadTo(30f, 10f, 120f, 100f)
        // 连续绘制已前一个 quadTo 的终点为起点
        path.quadTo(180f, 10f, 250f, 50f)
        // 方式二
//        path.rQuadTo(20f, -90f, 110f, 0f)
//        path.rQuadTo(60f, -90f, 130f, -50f) // 新起点（110f, 0f）
        canvas?.drawPath(path, mPaint)
    }
}

/** TODO 使用 贝济埃曲线 优化 手势轨迹捕捉
 *  path.quadTo 函数绘制
 */
class BezierGestureTrackView: View {
    val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    val mPath = Path()
    var mPreX = 0f
    var mPreY = 0f
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context):this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr) {
        mPaint.strokeWidth = 2f
        mPaint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(mPath, mPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN ->{
                mPreX = event.x
                mPreY = event.y
                mPath.moveTo(mPreX, mPreY)
                return@onTouchEvent true
            }
            MotionEvent.ACTION_MOVE ->{
//                mPath.lineTo(event.x, event.y)
                // 使用 贝济埃曲线 优化，把上一次位置设置为控制点，上一次位置和当前位置的中间的设置为终点
                val endX = (mPreX + event.x) / 2
                val endY = (mPreY + event.y) / 2
                mPath.quadTo(mPreX, mPreY, endX, endY)
                mPreX = event.x
                mPreY = event.y
                postInvalidate() // 重新绘制
            }
        }
//        return true
        return super.onTouchEvent(event)
    }
}

/** TODO 使用 贝济埃曲线 完成波浪动画
 *  使用path.rQuadTo函数完成
 */
class BezierAnimWareView: View {
    val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    val mPath = Path()
    // 单个波长值（一个起伏，即一上一下 例：~）
    val mItemWareLength = 1200
    var dX = 0f

    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)

    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mPaint.strokeWidth = 2f
        mPaint.color = Color.GREEN
        mPaint.style = Paint.Style.FILL
        val valueAnim = ValueAnimator.ofFloat(0f, mItemWareLength.toFloat())
        valueAnim.addUpdateListener {
            dX = it.getAnimatedValue() as Float
            postInvalidate()
        }
        valueAnim.duration = 2000
        // 设置匀速插值器，是波浪动画更为丝滑
        valueAnim.setInterpolator(LinearInterpolator())
        valueAnim.repeatCount = ValueAnimator.INFINITE
        valueAnim.repeatMode = ValueAnimator.RESTART
        valueAnim.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPath.reset()
        val originY = 300
        // 半个波的长度
        val halfWareLen = mItemWareLength / 2 .toFloat()
        mPath.moveTo(-mItemWareLength.toFloat() + dX, originY.toFloat())
        for (i in -mItemWareLength .. width + mItemWareLength step mItemWareLength) {
            mPath.rQuadTo(halfWareLen/2, -100f, halfWareLen, 0f) // 前半部分曲线
            mPath.rQuadTo(halfWareLen/2, 100f, halfWareLen, 0f) // 后半部分曲线
        }
        // 设置满屏波浪
        mPath.lineTo(width.toFloat(), height.toFloat())
        mPath.lineTo(0f, height.toFloat())
        canvas?.drawPath(mPath, mPaint)
    }
}

/** TODO 弹绳
 * 遗留问题：处理滑动事件
 */
class BezierRobeView:View{
    val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    val mPath = Path()
    val basicLineY = 200f
    var controlX = 0f
    var controlY = basicLineY
    var downY = 0f

    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mPaint.color = Color.BLACK
        mPaint.strokeWidth = 4f
        mPaint.style = Paint.Style.STROKE

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mPath.moveTo(0f, basicLineY)
        mPath.quadTo(controlX, controlY, width.toFloat(), basicLineY)
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPath.reset()
        mPath.moveTo(0f, basicLineY)
        mPath.quadTo(controlX, controlY, width.toFloat(), basicLineY)
        canvas?.drawPath(mPath, mPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                downY = event.y.toFloat()
                return@onTouchEvent true
            }
            MotionEvent.ACTION_MOVE -> {
                val moveH = event.y - downY
                controlX = if (event.x < 0) 0f else if (event.x > width) width.toFloat() else event.x
                controlY = basicLineY + moveH

                postInvalidate()
            }
            MotionEvent.ACTION_UP -> {
                val valueAnim = ValueAnimator.ofFloat(controlY, basicLineY)
                valueAnim.addUpdateListener {
                    controlY = it.animatedValue as Float
                    postInvalidate()
                }
                valueAnim.duration = 1000
                valueAnim.setInterpolator(OvershootInterpolator())
//                valueAnim.setInterpolator(BounceInterpolator())
                valueAnim.start()
            }
        }
        return true
    }

}