package com.zrt.kotlinapp.animator.path_measure

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.zrt.kotlinapp.R

/**
 * @author：Zrt
 * @date: 2023/3/7
 *
 * @see  加载动画
 */

class PMLengthView: View{
    val paint = Paint()
    val path = Path()
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        paint.setColor(Color.BLACK)
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 平移画布
        canvas?.translate(50f, 50f)
        path.reset()
        path.moveTo(0f, 0f)
        path.lineTo(0f, 100f)
        path.lineTo(100f, 100f)
        path.lineTo(100f, 0f)
        val pmF = PathMeasure(path, false) // length = 300  isClose=false
        val pmT = PathMeasure(path, true) // length = 400  isClose=true
        Log.i(">>>>", "##pmF =${pmF.length}; pmT=${pmT.length}")
        Log.i(">>>>", "##pmF isClose=${pmF.isClosed}; pmT isClose=${pmT.isClosed}")
        canvas?.drawPath(path, paint)
        path.reset()
        path.moveTo(150f, 0f)
        path.lineTo(150f, 100f)
        path.lineTo(250f, 100f)
        path.lineTo(250f, 0f)
        path.close()
        val pmF2 = PathMeasure(path, false) // length = 300 isClose=true
        val pmT2 = PathMeasure(path, true) // length = 400 isClose=true
        Log.i(">>>>", "##pmF2 =${pmF2.length}; pmT2 =${pmT2.length}")
        Log.i(">>>>", "##pmF2 isClose=${pmF2.isClosed}; pmT2 isClose=${pmT2.isClosed}")
        canvas?.drawPath(path, paint)
    }
}

class PMNextContourView: View {
    val paint = Paint()
    val path = Path()
    val pm = PathMeasure()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        paint.setColor(Color.BLACK)
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.translate(150f, 150f)
        path.reset()
        path.addRect(RectF(-40f, -20f, 40f, 20f), Path.Direction.CW)
        path.addRect(RectF(-50f, 10f, 50f, 10f), Path.Direction.CW)
        path.addRect(RectF(-20f, -40f, 20f, 40f), Path.Direction.CW)
        canvas?.drawPath(path, paint)
        pm.setPath(path, false)
        do {
            // ##PM length=320.0; isClosed=true
            // ##PM length=240.0; isClosed=true
            // ##PM length=160.0; isClosed=true
            Log.i(">>>>", "##PM length=${pm.length}; isClosed=${pm.isClosed}")
        }while (pm.nextContour())
    }
}

class PMSegmentView:View{
    val paint = Paint()
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        paint.setColor(Color.BLACK)
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.translate(100f, 100f)
        // getSegment函数使用，必须禁止硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        val path = Path()
        // 逆时针方向截取长度为150的路径
        path.addRect(-50f, -50f, 50f, 50f, Path.Direction.CCW)
        val pm = PathMeasure(path, true)
        val dst = Path()
        pm.getSegment(0f, 150f, dst, true)
        canvas?.drawPath(dst, paint)
        val path2 = Path()
        // 顺时针方向截取长度为150的路径
        path2.addRect(100f, -50f, 200f, 50f, Path.Direction.CW)
        val pm2 = PathMeasure(path2, true)
        val dst2 = Path()
        dst2.moveTo(150f, 0f)
        dst2.lineTo(160f, 100f)
        pm2.getSegment(0f, 150f, dst2, true)
        canvas?.drawPath(dst2, paint)
        val path3 = Path()
        // 顺时针方向截取长度为150的路径, startWithMoveTo=false
        path3.addRect(250f, -50f, 350f, 50f, Path.Direction.CW)
        val pm3 = PathMeasure(path3, true)
        val dst3 = Path()
        dst3.moveTo(300f, 0f)
        dst3.lineTo(310f, 100f)
        pm3.getSegment(0f, 150f, dst3, false)
        canvas?.drawPath(dst3, paint)
    }
}

/**
 * 圆形路径加载动画
 */
class PMLoadCircleView:View {
    val paint = Paint()
    val mPM = PathMeasure()
    val mCirclePath: Path = Path()
    val mDstPath: Path = Path()
    var mCurAnimValue: Float = 0f
    lateinit var mArrawBmp: Bitmap
    val arramPos:FloatArray = FloatArray(2) // 记录图标所在的位置
    val arramTan:FloatArray = FloatArray(2) // 记录图标所在位置的正切值

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        paint.setColor(Color.BLACK)
        paint.strokeWidth = 4f
        paint.style = Paint.Style.STROKE

        mArrawBmp = BitmapFactory.decodeResource(resources, R.mipmap.arraw)

        mCirclePath.addCircle(100f, 100f, 50f, Path.Direction.CW)
        mPM.setPath(mCirclePath, true)

        val valueAnim = ValueAnimator.ofFloat(0f, 1f)
        valueAnim.repeatCount = ValueAnimator.INFINITE
        valueAnim.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator) {
                mCurAnimValue = animation.animatedValue as Float
                invalidate()
            }
        })
        valueAnim.duration = 2000
        valueAnim.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val cLength = mPM.length
        val stop = mCurAnimValue * cLength
        val start = stop - (0.5f - Math.abs(mCurAnimValue - 0.5f)) * cLength
//        var start = 0
//        if (mCurAnimValue > 0.5){
//            start = 2 * mCurAnimValue -1
//        }
        mDstPath.reset()
        mPM.getSegment(start, stop, mDstPath, true)
        canvas?.drawPath(mDstPath, paint)
        // 方式一：使用getPosTan完成图片的平移旋转
        // 获取stop位置的坐标和正切值
//        mPM.getPosTan(stop, arramPos, arramTan)
//        //旋转图片 动作
//        val matrix = Matrix()
//        // 获取当前旋转角度
//        val angle = Math.atan2(arramTan[1].toDouble(), arramTan[0].toDouble())
//        // 旋转角度转换为弧度值
//        val degress = (angle * 180 / Math.PI).toFloat()
////        matrix.postRotate(angle.toFloat()) // 默认图片左上角为旋转中心点
//        // 设置矩阵旋转角度，同时设置其旋转中心点为图片中心
//        matrix.postRotate(degress, mArrawBmp.width/2.toFloat(), mArrawBmp.height/2.toFloat())
//        Log.i(">>>>","##posX=${arramPos[0]},poxY=${arramPos[1]}; tanX=${arramTan[0]},tanY=${arramTan[1]}")
//        matrix.postTranslate(arramPos[0] - mArrawBmp.width/2, arramPos[1] - mArrawBmp.height/2) // 平移指定位置
//        canvas?.drawBitmap(mArrawBmp, matrix, paint)

        // 方式二：使用getMatrix完成图片的平移旋转
        val matrix = Matrix()
        mPM.getMatrix(stop, matrix, PathMeasure.POSITION_MATRIX_FLAG or PathMeasure.TANGENT_MATRIX_FLAG)
        // 设置选择中心点位置
        matrix.preTranslate(-mArrawBmp.width/2.toFloat(), -mArrawBmp.height/2.toFloat())
        canvas?.drawBitmap(mArrawBmp, matrix, paint)
    }
}

/**
 * Alipay支付动画，画圆路径以及√绘制动画
 * */
class PMAliPayView:View {
    val paint = Paint()
    val mPM = PathMeasure()
    val mCirclePath: Path = Path()
    val mDstPath: Path = Path()
    var mCurAnimValue: Float = 0f
    // 圆心坐标
    val mCenterX = 100f
    val mCenterY = 100f
    val mRadius = 50f // 圆半径
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        paint.setColor(Color.BLUE)
        paint.strokeWidth = 4f
        paint.style = Paint.Style.STROKE
        // 路径1  圆
        mCirclePath.addCircle(mCenterX, mCenterY, mRadius, Path.Direction.CW)
        // 路径2  √
        mCirclePath.moveTo(mCenterX - mRadius / 2, mCenterY)
        mCirclePath.lineTo(mCenterX, mCenterY + mRadius / 2)
        mCirclePath.lineTo(mCenterX + mRadius / 2, mCenterY - mRadius / 3)
        mPM.setPath(mCirclePath, false)

        val valueAnim = ValueAnimator.ofFloat(0f, 2f)
        valueAnim.addUpdateListener {
            mCurAnimValue = it.getAnimatedValue() as Float
            invalidate()
        }
        valueAnim.setDuration(4000)
        valueAnim.start()
    }
    var isNext = false
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        if (mCurAnimValue < 1) {
            // 0~1 绘制圆路径
            val stop = mPM.length * (mCurAnimValue)
            mPM.getSegment(0f, stop, mDstPath, true)
        }else {
            // 1~2 绘制√路径
            if (!isNext){
                isNext = true
//                mPM.getSegment(0f, mPM.length, mDstPath, true)
                mPM.nextContour()
            }
            val stop = mPM.length * (mCurAnimValue - 1)
            mPM.getSegment(0f, stop, mDstPath, true)
        }
        canvas?.drawPath(mDstPath, paint)
    }
}

