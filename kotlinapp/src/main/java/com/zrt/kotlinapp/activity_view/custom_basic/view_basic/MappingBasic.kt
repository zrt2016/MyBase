package com.zrt.kotlinapp.activity_view.custom_basic.view_basic

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import com.zrt.kotlinapp.R

/**
 * @author：Zrt
 * @date: 2022/11/24
 * 绘图基础
 * 一、View的工作流程
 *     工作流程指：measure、layout和draw。measure测量View的宽高，layout确定View的位置，draw绘制View。
 *      onDraw():该函数中请勿创建变量，因为该函数会调用很频繁。
 * 二、ViewGroup的绘制流程
 *    三步：测量（onMeasure()）、布局（onLayout()）、绘制（onDraw()）
 *      onMeasure():测量当前控件大小，为正式布局提供建议（只建议，是否使用由onLayout()函数决定）
 *      onLayout()：使用layout函数对所有子控件进行布局
 *      onDraw():根据布局的位置绘图
 * @see onSizeChanged() View发生变化时，会通过onSizeChange通知我们控件的大小
 *      流程之一：layout() --> setFrame() --> sizeChange() --> onSizeChanged()
 * @see invalidate() // 重绘控件，一定要在主线中执行
 * @see postInvalidate() // 重绘控件，可异步调用
 *
 * @see MPaintView: 了解Paint的常用方法以及属性
 *      mPaint.isAntiAlias = true   表示是否开起抗锯齿功能，一般处理不规则的图形时使用，比如圆形、文字等。是所绘图形可以产生平滑的边缘
 * @see PaintTextView Paint与文字设置
 *      Paint()对象不要在onDraw方法中创建，onDraw会频繁调用。此处仅方便
 *      获取文字高度
 *      val fm = textPaint.getFontMetrics()
 *      var textHeight1 = fm.descent - fm.ascent // 文字高度
 *      val textHeight2 = fm.bottom - fm.top + fm.leading // 行高
 *      文字位置，从基准线出发，向上为负，向下为正
 *      fm.descent：文字基准线到文字底部的距离。
 *      fm.ascent:文字基准线到文字顶部的距离。
 *      fm.leading：表示行间距
 *      文字宽度：textPaint.measureText("文字内容")
 * @see PathView: 路径View
 *      path.moveTo(0f, 0f)             ：起始点
 *      path.lineTo(width.toFloat(), height.toFloat()) ：终点，也是下一次绘制点的起始点
 *      path.lineTo(width.toFloat(), 0f)  ：终点，也是下一次绘制点的起始点
 *      path.close()                   ：闭环，将路径首尾点连接，行成闭环
 *      canvas?.drawPath(path, mPaint) : 画路径
 *      path.arcTo(arcRect2, 0f, 180f, true) ：画一个圆弧路径，参2：弧开始的角度，以X轴正方向为0° .参3、弧持续角度。参4：弧线的起点与path的起始点的连接
 *      ovalPath.addOval(rectF, Path.Direction.CCW) ：添加一条椭圆路径，CCW逆时针，CW顺时针
 *      path.addArc(arcRect2, 0f, 180f) : 添加一个曲线路径，不需要考虑首尾连接
 *   path的填充模式
 *      fillPath.fillType = Path.FillType.WINDING  // 默认值 ， 取两图形所在区域
 *      fillPath.fillType = Path.FillType.EVEN_ODD  // 取path所在并不相交的区域
 *      fillPath.fillType = Path.FillType.INVERSE_WINDING  // 取path的外部区域
 *      fillPath.fillType = Path.FillType.INVERSE_EVEN_ODD  // 取path的外部和相交区域
 * @see CobwebView 蛛网图,使用Path绘制蜘蛛网状图
 * @see argb():颜色合成
 * @see RegionView:了解区域属性常用方法
 * @see drawRegion(): 绘画区域的方法
 * @see CanvasView： 了解画布的属性和方法
 *      canvas.save():保存当前画布，将其放入特定的栈中
 *      canvas.restore()：获取栈中顶层的画布状态，并按照该状态恢复当前画布
 *      drawBitmap(Bitmap, Rect1, Rect2, mPaint)
 *          截取 Rect2 中的 Rect1 矩形部分展示
 *
 * @see RoundHeadView：圆形头像View
 * @see RectPointView: 自定义一个点击View区域内，自动变化颜色的View。
 *      mRect.contains(mX, mY): 当前位置(mX,mY)是否在矩形范围内
 *      mRect.contains(Rect(0,0,10,10)): 判断矩形是否在当前矩形范围内
 *      Rect.intersects(mRect, Rect(0,0,10,10)): 判断两个矩形是否相交
 *      mRect.intersects(Rect(0,0,10,10))：判断两个矩形是否相交
 *      mRect.intersect(Rect(0,0,10,10))：判断两个矩形是否相交，如果相交将当前相交的部分赋值给mRect
 *      mRect.union(Rect(0,0,10,10))：合并两个矩形，分别取两个矩形最小左上角点和最大右下角点绘制一个新的矩形
 * @see ClipRgnView 剪裁动画
 * @see AddView 控件添加，以及控件的使用
 */
// TODO Paint
class MPaintView: View {
    val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var radius:Float
    @ColorInt var mBackgroundColor:Int = Color.BLACK
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context):this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){
        val mTypeArray = context.obtainStyledAttributes(attrs, R.styleable.MPaintView)
        radius = mTypeArray.getDimension(R.styleable.MPaintView_mRadius, 0f)
        mBackgroundColor = mTypeArray.getColor(R.styleable.MPaintView_backgroundColor, Color.BLACK)
        mTypeArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 设置画笔颜色
        mPaint.setColor(mBackgroundColor)
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//            mPaint.setColor(0xFFFF0000) // 设置画笔颜色
//        }
        // 设置填充样式
        mPaint.style = Paint.Style.STROKE   // 仅描边  画圆环
//        mPaint.style = Paint.Style.FILL   // 仅填充内部  填充圆环内，环边不填充
//        mPaint.style = Paint.Style.FILL_AND_STROKE   // 填充内部和描边，包含圆环和圆环内均进行填充

        // 设置画笔宽度，单位px
        mPaint.strokeWidth = 10f
        val width = width - paddingLeft - paddingRight
        val height = height - paddingTop - paddingBottom
        canvas?.let {
            it.drawCircle((width / 4).toFloat(), (height / 4).toFloat(), radius, mPaint)
            it.save()
        }
        // 表示是否开起抗锯齿功能，一般处理不规则的图形时使用，比如圆形、文字等。
        // 是所绘图形可以产生平滑的边缘
        mPaint.isAntiAlias = true
        mPaint.setColor(Color.RED)
        canvas?.let {
            it.drawCircle((width / 4).toFloat(), (height / 4).toFloat(), radius - 10, mPaint)
            it.save()
        }
        // 设置画布颜色
//        canvas?.drawColor(Color.BLUE)
//        canvas?.drawARGB(255, 255, 255, 255) // 1、透明度。2、红色值。3、绿色值。4、蓝色值
//        canvas?.drawRGB(255, 255, 255) // 1、红色值。2、绿色值。3、蓝色值
        mPaint.setColor(Color.YELLOW)
        // 画直线
        canvas?.drawLine(0f, (height / 2).toFloat(), width.toFloat(), (height / 2).toFloat(), mPaint)
        mPaint.strokeWidth = 4f
        mPaint.setColor(Color.RED)
        var lineH = (height / 2).toFloat()+50f;
        // 画虚线
        // 方式1
        canvas?.drawLines(floatArrayOf(0f, lineH, 10f, lineH, 20f, lineH, 30f, lineH, 40f, lineH, 50f, lineH), mPaint)
        mPaint.setColor(Color.BLACK)
        lineH = lineH + 10f
        // 方式2
        // 参数2 offset:集合中跳过的数值个数。此处为2，则从索引为2的数值开始绘制
        // 参数3 count：此处为8，则表示有8个数值参与绘制
        canvas?.drawLines(floatArrayOf(0f, lineH, 10f, lineH, 20f, lineH, 30f, lineH, 40f, lineH, 50f, lineH, 60f, lineH, 70f, lineH),
                2, 8, mPaint)
        mPaint.setColor(Color.RED)
        // 画点
        canvas?.drawPoint(0f, 0f, mPaint)
        lineH = lineH + 10f
        // 多个点
        canvas?.drawPoints(floatArrayOf(0f, lineH, 10f, lineH, 20f, lineH, 40f, lineH, 80f, lineH, 160f, lineH), mPaint)
        lineH = lineH + 10f
        // 参数2：索引为2的数值点开始， 连续4个数值，即2个点
        canvas?.drawPoints(floatArrayOf(0f, lineH, 10f, lineH, 20f, lineH, 40f, lineH, 80f, lineH, 160f, lineH),
                2, 4, mPaint)

        // 画矩形
        // 保存Int类型数值的矩形
        val rect = Rect(0, 0, width / 2, height / 2)
        // 保存float类型数值的矩形
        val rectF = RectF(10f, 10f, width / 2.toFloat() - 10f, height / 2.toFloat() - 10f)
        canvas?.drawRect(rect, mPaint)
        mPaint.setColor(Color.GRAY)
        canvas?.drawRect(rectF, mPaint)
        // 圆角矩形
        // 参数2：rx 圆角x轴半径。 参数3：ry 圆角Y轴半径
        canvas?.drawRoundRect(RectF(width / 2 + 20.toFloat(), height / 2 + 20.toFloat(),
                width.toFloat(), height.toFloat()), 50f, 50f, mPaint)
    }
}

/**
 * TODO Paint 与文字设置
 */
class PaintTextView: View{
    val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    lateinit var mContext:Context
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context):this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){
        mContext = context
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPaint.strokeWidth = 5f // 设置画笔宽度
        mPaint.isAntiAlias = true // 使用抗锯齿功能，但会降低绘图速度
        mPaint.style = Paint.Style.FILL // 绘图样式，对文字和几何图形都有效
        // center:起始点在文字中间。Left：起始点在文字左边。Right：起始点在文字右边
        mPaint.textAlign = Paint.Align.CENTER // 设置文字对齐方式，分别有：CENTER、LEFT和RIGHT
        mPaint.textSize = 18f // 设置文字大小
        // 样式设置
        mPaint.isFakeBoldText = true // 设置是否为粗体文字
        mPaint.isUnderlineText = true // 设置下划线
        mPaint.textSkewX = -0.25f // 设置字体水平倾斜度，普通斜体字设为-0.25. 负值向右倾斜，正值向左倾斜。
        mPaint.isStrikeThruText = true // 设置担忧删除线效果
        mPaint.textScaleX = 2f // 设置字体水平方向拉伸，高度不变，默认值1.0。大于1.0则将文本拉伸更宽，小于1.0则缩小文本宽度。
        // 此处设置起始点在文字左边
        mPaint.textAlign = Paint.Align.LEFT
        //@param2 x所绘制文本原点的x坐标
        //@param3 y所绘制文本基线的y坐标
        canvas?.drawText("床前明月光", 200f, 50f, mPaint)
        mPaint.textAlign = Paint.Align.RIGHT
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 1f
        mPaint.isFakeBoldText = false
        mPaint.isUnderlineText = false // 设置下划线
        mPaint.textSkewX = 0f // 设置字体水平倾斜度，普通斜体字设为-0.25
        mPaint.isStrikeThruText = false // 设置担忧删除线效果
        mPaint.textScaleX = 1f
        canvas?.drawText("床前明月光", 200f, 100f, mPaint)
        canvas?.drawLine(200f, 100f, 200f, 150f, mPaint)
        // start参数2：表示从字符串中索引处开始绘制。end参数2：表示字符串中文字绘制在该索引前结束。
        // 即：start <= length < end 此处绘制文字内容为：床前
        canvas?.drawText("床前明月光", 0, 2, 200f, 150f, mPaint)
        mPaint.textAlign = Paint.Align.LEFT
        // 逐个指定文字位置
        val textPos = floatArrayOf(10f, 160f, 30f, 160f, 80f, 180f, 130f, 200f, 150f, 200f)
        // 参数2：绘制文字起始索引。参数3：绘制文字个数。参数4：要绘制文字的位置，两两一组
        canvas?.drawPosText("床前明月光".toCharArray(), 0, 5, textPos, mPaint)

        // 沿路径绘制
        val textPath = Path()
        textPath.addCircle(400f, 100f, 50f, Path.Direction.CCW) // 逆时针
        canvas?.drawPath(textPath, mPaint)
        // hOffset参数3：水平偏移量，即左右便宜。 vOffset参数4：垂直偏移量，即上下便宜
        canvas?.drawTextOnPath("逆时针绘制", textPath, 0f, 0f, mPaint)
        textPath.reset()
        textPath.addCircle(500f, 100f, 50f, Path.Direction.CW) // 顺时针
        canvas?.drawPath(textPath, mPaint)
        // hOffset参数3：水平偏移量，即左右便宜。 vOffset参数4：垂直偏移量，即上下便宜
        // 此处顺时针绘制，因此50f则按顺时针方向便宜。10f，正常向下便宜，此处是圆 向内偏移
        canvas?.drawTextOnPath("顺时针绘制", textPath, 50f, 10f, mPaint)

        // 设置文本样式
        // 一、常规设置
//        mPaint.setTypeface(Typeface.SANS_SERIF) // Typeface有3种自带的字体样式：SANS_SERIF、SERIF和MONOSPACE
        // 二、国内用于，系统默认的字体一般是DroidSansFallback，一般不会使用Google的三种字体写中文，因为看不出差别
        // NORMAL（正常字体）、BOLD（粗体）、ITALIC（斜体）、BOLD_ITALIC（粗斜体）
        val typeface = Typeface.defaultFromStyle(Typeface.BOLD_ITALIC)
        mPaint.setTypeface(typeface)
        // 三、通过指定字体名加载系统自带的字体样式，如果不存在则使用系统样式替代。
        mPaint.setTypeface(Typeface.create("宋体", Typeface.NORMAL))
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 1f
        mPaint.textSize = 30f
        mPaint.setColor(Color.RED)
        canvas?.drawText("设置文本样式SERIF", 10f, 250f, mPaint)
        // TODO 自定义文本暂未测试
        // 自定义文本样式，常用函数createFromAsset()、createFromFile()
        // 首先得在assets文件夹下新建一个fonts文件夹，然后放入字体文件xxx.ttf格式文件（字体样式）
//        mPaint.setTypeface(Typeface.createFromAsset(mContext.assets, "fonts/xxx.ttf")); // 方式11
//        mPaint.setTypeface(Typeface.createFromFile("fonts/xxx.ttf")) // 方式2

    }
}

/**
 * alpha shl 24 左移24位：11111111 00000000 00000000 00000000
 * red shl 16 左移16位：00000000 11111111 00000000 00000000
 * green shl 8 左移8位：00000000 00000000 11111111 00000000
 * blue：00000000 00000000 00000000 11111111
 * 取|, 最终合并值为对应颜色值
 */
@ColorInt
fun argb(alpha: Int, red: Int, green: Int, blue: Int): Int {
    return alpha shl 24 or (red shl 16) or (green shl 8) or blue
}
// TODO Path
class PathView:View{
    val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    constructor(context: Context):this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            :super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 直线路径：Path
        mPaint.strokeWidth = 4f
        mPaint.style = Paint.Style.STROKE
        mPaint.setColor(Color.BLUE)
        val path = Path()
        path.moveTo(0f, 0f)             // 起始点
        path.lineTo((width / 3).toFloat(), 0f)  // 终点，也是下一次绘制点的起始点
        path.lineTo(width / 3.toFloat(), 30f)         // 终点，也是下一次绘制点的起始点
        path.close()                    // 闭环，将路径首尾点连接，行成闭环
        canvas?.drawPath(path, mPaint)
        mPaint.setColor(Color.RED)
        // 画弧线
        val arcPath = Path()
        arcPath.moveTo(0f, 0f)
        val arcRect = RectF(0f, 0f, width / 3.toFloat(), height / 6.toFloat())
        // 顺时针方向为正方向
        // 1、生成椭圆的矩形。 2、弧开始的角度，以X轴正方向为0° .3、弧持续角度
        arcPath.arcTo(arcRect, 0f, 180f)
        canvas?.drawPath(arcPath, mPaint)
        mPaint.setColor(Color.GREEN)
        // 取消弧线的起点与path的起始点的连接
        val arcPath2 = Path()
        arcPath2.moveTo(0f, 0f)
        val arcRect2 = RectF(0f, 10f, width / 3.toFloat(), height / 6.toFloat() + 10f)
        arcPath2.arcTo(arcRect2, 0f, 180f, true)
//        arcPath2.arcTo(0f, 10f, width/3.toFloat(), height/6.toFloat()+10f, 0f, 180f, true)
        canvas?.drawPath(arcPath2, mPaint)
        mPaint.setColor(Color.MAGENTA)
        val addPath = Path()
        addPath.moveTo(0f, 0f)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            addPath.addArc(0f, 10f, width / 3.toFloat() + 20f, height / 6.toFloat() + 10f, 0f, 90f)
        }
        canvas?.drawPath(addPath, mPaint)
        // 矩形路径
        val rectPath = Path()
        //CCW逆时针，CW顺时针
        rectPath.addRect(RectF(width / 3f + 10f, 0f, width / 1.5f, 50f), Path.Direction.CW)
        canvas?.drawPath(rectPath, mPaint)
        mPaint.setColor(Color.RED)
        mPaint.textSize = 24f
        mPaint.strokeWidth = 1f
        // 字体方向由rectPath生成绘画的方向决定，此处是顺时针，因此字体方向也是有顺时针生成
        // 参3：hOffset沿路径添加到文本起始位置的距离
        // 参4：vOffset定位文本的路径上方（-）或下方（+）的距离
        canvas?.drawTextOnPath("我爱工作，我爱加班！", rectPath, 0f, 24f, mPaint)
        // 画不同大小的圆角矩形
        val roundRPath = Path()
        val rectR = RectF(width / 3f + 10f, 60f, width / 1.5f, 110f)
        val radii = floatArrayOf(0f, 0f, 10f, 10f, 30f, 10f, 30f, 30f)
        // 参2：float[] radii 必须传入8个值，分别对应左上，右上，右下，左下的横纵半径值
        roundRPath.addRoundRect(rectR, radii, Path.Direction.CW)
        canvas?.drawPath(roundRPath, mPaint)
        mPaint.setColor(Color.GRAY)
        mPaint.strokeWidth = 4f
        // 画一个椭圆
        val ovalRectF = RectF(0f, 30f, width / 3.toFloat(), height / 6.toFloat() + 30f)
        canvas?.drawOval(ovalRectF, mPaint)

        mPaint.color = Color.YELLOW
        mPaint.style = Paint.Style.FILL
        // path的填充模式
        val fillPath = Path()
        fillPath.addRect(RectF(10f, height / 4.toFloat(), 110f, height / 3.toFloat()), Path.Direction.CW)
        fillPath.addCircle(60f, height / 3.toFloat(), 20f, Path.Direction.CW)
//        fillPath.fillType = Path.FillType.WINDING  // 默认值 ， 取两图形所在区域
        fillPath.fillType = Path.FillType.EVEN_ODD  // 取path所在并不相交的区域
//        fillPath.fillType = Path.FillType.INVERSE_WINDING  // 取path的外部区域
//        fillPath.fillType = Path.FillType.INVERSE_EVEN_ODD  // 取path的外部和相交区域
        canvas?.drawPath(fillPath, mPaint)
        /**
         * 重置路径： reset() 和 rewind(), 共同点清除内部所有路径
         * reset(): 类似于新建一个路径对象，他的所有数据空间都会被回收并重新分配，单不会清除FillType
         * rewind():会清除FillType及所有的直线、曲线、点的数据等，但是会保留数据结构。
         *      可以快速实现重用，提高一定的性能。例如重复绘制一类线段，点的数量相等，使用
         *      使用该函数可以保留转载点的数据结构，效率会更高。
         *      （注：只有重复绘制相同路径时，数据结构才可以复用）
         */
//        fillPath.reset() // 未重置FillType
        fillPath.rewind() // 重置了FillType
        mPaint.color = Color.GREEN
        fillPath.addRect(RectF(120f, height / 4.toFloat() + 10f, 200f, height / 3.toFloat()), Path.Direction.CW)
        fillPath.addCircle(160f, height / 3.toFloat(), 20f, Path.Direction.CW)
        canvas?.drawPath(fillPath, mPaint)
    }
}
// TODO CobwebView 蛛网图
class CobwebView:View{
    /** 网格画笔 */
    lateinit var radarPaint:Paint
    /** 结果图画笔 */
    lateinit var valuePaint:Paint
    /** 文字属性画笔 */
    lateinit var textPaint:Paint
    /** 网格半径*/
    var radius:Float = 0f
    /** 中心点 (x, y)*/
    var centerX:Float = 0f;
    var centerY:Float = 0f;
    /** 网格层数*/
    var cobwebCount = 1;
    /** 属性*/
    var cobwebAttribute:Array<String> = arrayOf("A", "B", "C")
    /** 属性点个数，即网格夹角数， 一般最小3个*/
    var cobwebAttributeNum = 3;
    /** 属性最大值 */
    var attributeMax = 0
    /** 多维属性值 数组长度与属性点个数保持一致*/
    var attributeData:FloatArray = FloatArray(cobwebAttributeNum)
    /** 夹角度数 */
    var angle:Double = 0.0
    constructor(context: Context):this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){
        init()
    }

    private fun init() {
        // 网格 仅描边
        radarPaint = Paint()
        radarPaint.style = Paint.Style.STROKE
        radarPaint.color = Color.GREEN
        radarPaint.isAntiAlias = true
        // 对结果图进行填充
        valuePaint = Paint()
        valuePaint.style = Paint.Style.FILL
        valuePaint.color = Color.BLUE
        valuePaint.isAntiAlias = true
        //文字画笔设置
        textPaint = Paint()
        textPaint.style = Paint.Style.FILL
        textPaint.color = Color.RED
        textPaint.textSize = 23f
        textPaint.isAntiAlias = true
        cobwebCount = 6 // 网格层数

        // 此处设置网络层数为6层，属性个数为6个
        cobwebAttribute = arrayOf("力量", "敏捷", "体力", "智力", "速度", "运气")
        cobwebAttributeNum = cobwebAttribute.size
        attributeMax = 6 // 属性值最大值
        attributeData = floatArrayOf(6f, 5f, 2f, 1f, 3f, 4f)
        // 把数字60转换成60度
        angle = Math.toRadians((360 / cobwebAttributeNum).toDouble())
    }

    // View发生变化时，会通过onSizeChanged通知我们控件的大小
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = Math.min(w, h) / 2 * 0.9f
        // 中心坐标
        centerX = (w / 2).toFloat()
        centerY = (h / 2).toFloat()
        postInvalidate()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 绘制蛛网格
        drawCobwebBg(canvas)
        // 画网格线 和 属性名
        drawCobwebLine(canvas)
        // 画数据图
        drawDataRegion(canvas)
    }

    private fun drawCobwebBg(canvas: Canvas?) {
        val path = Path()
        val r = radius/cobwebCount // 每层网格间的间距
        for (i in 1..cobwebCount){ // 从第1层开始，不绘制中点
            val curR = r * i // 当前网格半径
            path.reset()
            for (j in 0 until cobwebAttributeNum){
                if(j == 0){
                    path.moveTo(centerX + curR, centerY)
                }else {
                    val newX = centerX + curR * Math.cos(angle * j)
                    val newY = centerY + curR * Math.sin(angle * j)
                    path.lineTo(newX.toFloat(), newY.toFloat())
                }
//                Log.i(">>>>", "## i=$i; j=$j; angle=${angle*j}; curR=$curR; cosX=${curR * Math.cos(angle*j)}; sinY=${curR * Math.sin(angle*j)}")
            }
            path.close()
            canvas?.drawPath(path, radarPaint)
        }
    }

    private fun drawCobwebLine(canvas: Canvas?) {
        val path = Path()
        for (i in 0 until cobwebAttributeNum){
            path.reset()
            path.moveTo(centerX, centerY)
            val newX = centerX + radius * Math.cos(angle * i).toFloat()
            val newY = centerY + radius * Math.sin(angle * i).toFloat()
            path.lineTo(newX, newY)
            canvas?.drawPath(path, radarPaint)

            // 画文字
            // 获取文字高度 ascent:文字基准线到文字顶部的距离。descent：文字基准线到文字底部的距离。
            val fm = textPaint.getFontMetrics()
            var textHeight1 = fm.descent - fm.ascent // 文字高度
            val textHeight2 = fm.bottom - fm.top + fm.leading // 行高
            val textBaseHeight = Math.abs(fm.ascent) // 文字基准线到顶部的距离的绝对值
            // 文字宽度
            val textWidth = textPaint.measureText(cobwebAttribute[i])
            val textY = centerY + (radius + textBaseHeight) * Math.sin(angle * i).toFloat()
            if (newX == centerX && newY == (centerY - radius)){ // angle = 90
                textPaint.textAlign = Paint.Align.CENTER
                canvas?.drawText(cobwebAttribute[i], newX, newY, textPaint)
            }else if (newX == centerX && newY == (centerY + radius)) {// angle = 270
                textPaint.textAlign = Paint.Align.CENTER
                canvas?.drawText(cobwebAttribute[i], newX, textY, textPaint)
            }else if (newX > centerX){ // 0 <= angle <90 || 270 < angle <= 360
                textPaint.textAlign = Paint.Align.LEFT // 起始点在文字左侧
                if (newY > centerY){ // 270 < angle <= 360 处理文字处于下方
                    canvas?.drawText(cobwebAttribute[i], newX , textY, textPaint)
                }else {
                    canvas?.drawText(cobwebAttribute[i], newX, newY, textPaint)
                }
            }else { // 90 < angle <270
                textPaint.textAlign = Paint.Align.RIGHT // 起始点在文字右侧
                if (newY > centerY){ // 180 < angle < 270 处理文字处于下方
                    canvas?.drawText(cobwebAttribute[i], newX , textY, textPaint)
                }else {
                    canvas?.drawText(cobwebAttribute[i], newX, newY, textPaint)
                }
            }

        }
    }

    private fun drawDataRegion(canvas: Canvas?) {
        val path = Path()
        valuePaint.alpha = 130
        for (i in 0 until cobwebAttributeNum){
            val percent = attributeData[i]/attributeMax
            val newX = centerX + radius * percent * Math.cos(angle * i).toFloat()
            val newY = centerY + radius * percent * Math.sin(angle * i).toFloat()
            if (i == 0){
                path.moveTo(newX, newY)
            }else {
                path.lineTo(newX, newY)
            }
            canvas?.drawCircle(newX, newY, 5f, valuePaint)
        }
        path.close()
        valuePaint.style = Paint.Style.FILL_AND_STROKE
        canvas?.drawPath(path, valuePaint)
    }
    // 文字绘制
//    fun drawTextData(canvas: Canvas?) {
//        val textRadius = radius + 10
//        for (i in 0 until cobwebAttributeNum){
//            val newX = centerX + radius  * Math.cos(angle * i).toFloat()
//            val newY = centerY + radius  * Math.sin(angle * i).toFloat()
//            if (newX > centerX){ // 0 ~ 90
//
//            }else if (newX == centerX){ // 90
//            }else if (newX < centerX){ // 90 ~ 180
//            }else if (newX == centerX){
//            }else if (newX == centerX){
//
//            }
//        }
//    }
}

/** TODO Region
 * 区域View
 * 使用Region构造任意图形
 *
 * Region1.op(Region2, Op.xx)
 * public enum Op {
 *     DIFFERENCE(0), 取rg1与rg2不同区域，即rg1上未与rg2相交的区域
 *     INTERSECT(1),  取相交区域
 *     UNION(2),      取组合区域
 *     XOR(3),        取相交之外的区域
 *     REVERSE_DIFFERENCE(4),   取Region2与Region1不同的区域，即取R2上与R1未相交的区域
 *     REPLACE(5);      最终取R2的区域
 * }
 * region.setPath(ovalPath, Region(10, 10, 50, 50)) // setPath传入一个比椭圆小的矩形区域，取域交集
 * 判断方法：
 *      isEmpty():判断该区域是否为空
 *      isRect()：判断该区域是否为一个矩形
 *      isComplex()：判断该区域是否是多个矩形的组合
 * getBound 系列函数
 *      getBounds()、getBounds(Rect)：返回一个Region的边界
 *      getBoundaryPath()、getBoundaryPath(Path)：返回能够包裹当前路径的最小矩形
 * 是否包含报个点或某个矩形
 *      contains(int x, int y)：是否包含某个点
 *      quickContains(Rect):是否包含某个矩形
 *      quickContains(left, top,right, bottom):是否包含某个矩形
 * 是否相交
 *      quickReject(Rect)：判断该区域是否没有和指定矩形相交
 *      quickReject(left, top,right, bottom)：判断该区域是否没有和指定矩形相交
 *      quickReject(Region)：判断该区域是否没有和指定区域相交
 * 平移变换
 *      translate(dx, dy)：Region对象向X轴平移dx距离，向Y轴平移dy距离，并将结果赋给Region对象
 *      translate(dx, dy, dst:Region )：Region对象向X轴平移dx距离，向Y轴平移dy距离, 该函数会将结果赋给dst对象，当前的Region保持不变。
 */
class RegionView: View{
    val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    constructor(context: Context):this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 设置画笔颜色
        mPaint.setColor(Color.RED)
        // 设置填充样式
        mPaint.style = Paint.Style.STROKE   // 填充内部
        // 设置画笔宽度，单位px
        mPaint.strokeWidth = 1f
        val ovalPath = Path()
        val rectF = RectF(10f, 10f, 50f, 90f)
        // 构造一条椭圆路径，CCW逆时针，CW顺时针
        ovalPath.addOval(rectF, Path.Direction.CCW)
        val region = Region()
        // setPath传入一个比椭圆小的矩形区域，取域交集
        region.setPath(ovalPath, Region(10, 10, 50, 50))
        drawRegion(canvas, region, mPaint)
        mPaint.style = Paint.Style.STROKE   // 描边线
        mPaint.setColor(Color.GRAY)
        canvas?.drawPath(ovalPath, mPaint)
//        canvas?.drawRect(Rect(10, 10, 50, 50), mPaint)
        drawRegion(canvas, Region(10, 10, 50, 50), mPaint)
        mPaint.style = Paint.Style.FILL   // 填充内部
        mPaint.setColor(Color.RED)
        mPaint.strokeWidth = 10f
        // union取并区域
        val unionRegion = Region(70, 10, 90, 90)
        unionRegion.union(Rect(60, 10, 100, 50))
//        val unionOvalPath = Path()
//        unionOvalPath.addOval(RectF(60f, 10f, 100f, 90f), Path.Direction.CCW)
        drawRegion(canvas, unionRegion, mPaint)
        // 构造两个矩形的轮廓
        val rect1 = Rect(110, 35, 210, 85) // 横
        val rect2 = Rect(135, 10, 185, 110) // 竖
        mPaint.style = Paint.Style.STROKE  // 描边
        mPaint.strokeWidth = 1f
        canvas?.drawRect(rect1, mPaint)
        canvas?.drawRect(rect2, mPaint)
        // 构造两个区域
        val rg1 = Region(rect1)
        val rg2 = Region(rect2)
        // 方式一
        rg1.op(rg2, Region.Op.DIFFERENCE) // 取rg1与rg2不同区域，即rg1上未与rg2相交的区域
//        rg1.op(rg2, Region.Op.INTERSECT) // 取相交区域
//        rg1.op(rg2, Region.Op.UNION) // 取组合区域
//        rg1.op(rg2, Region.Op.XOR) // 取相交之外的区域
//        rg1.op(rg2, Region.Op.REVERSE_DIFFERENCE) // 取Region2与Region1不同的区域，即取R2上与R1未相交的区域
//        rg1.op(rg2, Region.Op.REPLACE) // 最终取R2的区域
        // 方式二
//        val rg = Region()
//        rg.op(rg1, rg2, Region.Op.DIFFERENCE)
        val paintFill = Paint()
        paintFill.setColor(Color.GREEN)
        paintFill.style = Paint.Style.FILL
        drawRegion(canvas, rg1, paintFill)
        // 平移
        mPaint.color = Color.rgb(0, 200, 0)
        val rg = Region(Rect(230, 35, 330, 85))
        drawRegion(canvas, rg, mPaint) // 平移前
        mPaint.color = Color.rgb(0, 0, 200)
        val dst = Region()
        rg.translate(150, 0, dst) // 平移后
        drawRegion(canvas, dst, mPaint)

    }
}
//
/**
 * 绘画Region函数
 * 绘制Region对象时，先将其转换成一个矩形集合，然后利用画笔逐个画出
 */
fun drawRegion(canvas: Canvas?, region: Region, paint: Paint){
    val regionIterator = RegionIterator(region)
    val rect = Rect()
    while (regionIterator.next(rect)){
        canvas?.drawRect(rect, paint)
    }
}

/** TODO Canvas
 * Canvas 画布处理
 * 1、translate(dx,dy)：对画布进行平移， 平移时，坐标系也会跟着平移
 *      参数dx：水平方向平移，正数向右平移，负数向左平移
 *      参数dy：垂直方向平移，正数向下平移，负数向上平移
 * 2、drawXXX()系列函数，每次调用时，都会先产生一个透明图层，然后在该图层上作画，画完之后覆盖着屏幕上显示。
 *          如果之前已存在其他绘制的图形，发生平移后，不会影响之前已经绘制的图形。
 * 3、rotate(degrees, px, py):画布旋转，围绕画布的坐标原点进行旋转
 *      参数degrees：旋转的度数，正数指顺时针旋转，负数指逆时针旋转。（一个参数时，默认中心点（0,0））
 *      参数(px, py):指定旋转的中心点坐标
 * 4、scale(sx, sy)：缩放，用于变更坐标轴密度
 *      参数sx：水平方向伸缩的比例，大于等于1时，表示放大。小于1时，表示缩小
 *      参数sy：垂直方向伸缩的比例。大于等于1时，表示放大。小于1时，表示缩小
 * 5、skew(sx, sy)：扭曲，这里的参数都是倾斜角度的正切值，比如在X轴上倾斜60°，tan60=1.732.
 *      参数sx：将互不在X轴方向上倾斜相应的角度，sx为倾斜角度的正切值。
 *      参数sy：将画布在Y轴方向上倾斜相应的角度，sy为倾斜角度的正切值。
 * 6、clip系列函数，裁剪函数，通过与Rect、Path、Region去交、并等集合运算来获取最新的画布图形。
 *  clipPath()、
 *  除调用save（）、restore（）函数以外，这个操作是不可逆的，一旦被裁剪，就不能恢复。
 *  注：使用clip系列函数，需要禁用加速功能。setLayerType(Layer_TYPE_SOFTWARE, null)
 * 7、save():每次调用save函数，都会保存当前画布的状态，然后放入栈中。
 * 8、restore()：每次调用restore函数，都会把栈中顶层的画布状态取出来，并按照这个状态恢复当前的画布，然后在这个画布上作画。
 * 9、restoreToCount(save())： 连续出栈到指定的save索引处（每次save都会返回一个索引），
 */
class CanvasView:View {
    val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    constructor(context: Context):this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr)

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        // 设置画笔颜色
        mPaint.setColor(Color.RED)
        // 设置填充样式
        mPaint.style = Paint.Style.STROKE   // 填充内部
        // 设置画笔宽度，单位px
        mPaint.strokeWidth = 1f
        mPaint.setColor(Color.BLACK)
        canvas?.drawLine(0f, 0f, 200f, 0f, mPaint)
        canvas?.drawLine(0f, 0f, 0f, 200f, mPaint)
        mPaint.setColor(Color.GREEN)
        val rect = Rect(0, 0, 80, 40)
        //平移前
        canvas?.drawRect(rect, mPaint)
        // 平移画布
        canvas?.translate(20f, 20f)
        mPaint.setColor(Color.BLUE)
        //平移后, 绿色矩形在左上角不变，蓝色矩形发生了平移。
        // 原因是:canvas?.drawXXX()系列函数绘制时，都会先产生一个透明的绘制图层，绘制完成后将图层覆盖至屏幕上。
        // 因此发现蓝色矩形发生了位移，而绿色矩形未发生变化
        canvas?.drawRect(rect, mPaint)

        mPaint.setColor(Color.RED)
        canvas?.rotate(45f)
//        Math.toRadians() Math.toRadians(45.00).toFloat()
        canvas?.drawRect(rect, mPaint)
        mPaint.setColor(Color.CYAN)
        // 缩放
        canvas?.scale(1.5f, 1.5f)
        canvas?.drawRect(rect, mPaint)
        canvas?.rotate(-45f)
        // 扭曲，x周上倾斜60°
        canvas?.skew(1.732f, 0f)
        canvas?.drawRect(rect, mPaint)

        // 画布剪裁 clip
        // 在使用画布剪裁系列函数时，需要禁用硬件加速功能
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        canvas?.skew(0f, 0f)
        val save = canvas?.save()
        canvas?.clipRect(Rect(10, 10, 200, 110))
//        canvas?.restore()
        canvas?.drawColor(Color.YELLOW)
//        save?.let { canvas?.restoreToCount(it) }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 获取宽高设置模式
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        // 获取宽高值
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(widthSpecSize,
                heightSpecSize)
    }
}

/** TODO RoundHeadView
 * 自定义圆形头像View
 */
class RoundHeadView:View{
    lateinit var mBmp:Bitmap
    lateinit var mPaint: Paint
    lateinit var mPath: Path
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context):this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){
        val mTypeArray = context.obtainStyledAttributes(attrs, R.styleable.RoundHeadView)
        val bitID = mTypeArray.getResourceId(R.styleable.RoundHeadView_header, R.mipmap.nan)
        mBmp = BitmapFactory.decodeResource(context.resources, bitID)
        mTypeArray.recycle()
        init()
    }
    private fun init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mPaint = Paint()
        mPath = Path()
        val width = mBmp.width
        val height = mBmp.height
        mPath.addCircle((width/2).toFloat(), (height/2).toFloat(), (width/2).toFloat(), Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.clipPath(mPath) // 剪裁区域，保留该部分区域
        canvas?.drawBitmap(mBmp, 0f, 0f, mPaint)
    }
}
/**
 * 定义一个矩形，点击矩形内，则边框发生变化的自定义View
 */
class RectPointView:View{
    val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    lateinit var mRect:Rect
    var mX = 0
    var mY = 0
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context):this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){
//        val mTypeArray = context.obtainStyledAttributes(attrs, R.styleable.MPaintView)
//        radius = mTypeArray.getDimension(R.styleable.MPaintView_mRadius, 0f)
//        mBackgroundColor = mTypeArray.getColor(R.styleable.MPaintView_backgroundColor, Color.BLACK)
//        mTypeArray.recycle()
        init()
    }

    private fun init() {
        mPaint.style = Paint.Style.STROKE
        mRect = Rect(10, 10, 210, 110)
    }
    // 判断当前手指是否在矩形区域内，如果在区域内即可通过event.getX()和event.getY()获取坐标
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mX = event?.getX()?.toInt()?:-1
        mY = event?.getY()?.toInt()?:-1
        if (event?.action == MotionEvent.ACTION_DOWN){
            invalidate() // 重绘控件，一定要在主线中执行
            return true
        }else if (event?.action == MotionEvent.ACTION_UP){
            mX = -1
            mY = -1
        }
        postInvalidate() // 重绘控件，可异步调用
        return super.onTouchEvent(event)
    }

    /**
     * invalidate() // 重绘控件，一定要在主线中执行
     * postInvalidate() // 重绘控件，可异步调用
     * 调用onDraw
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(mRect.contains(mX, mY)){
            mPaint.setColor(Color.RED)
        }else {
            mPaint.setColor(Color.GREEN)
        }
        canvas?.drawRect(mRect, mPaint)
    }
}

// TODO ClipRgnView 剪裁动画
class ClipRgnView:View{
    lateinit var mBitmap: Bitmap
    /** 剪裁宽度 */
    var clipWidth: Int = 0
    /** 剪裁高度 */
    var clipHeight: Int = 5
    var w: Int = 0
    var h: Int = 0
    /** 裁剪区域 */
//    lateinit var mRgn:Region
    lateinit var mPath: Path
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context):this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){
        val mTypeArray = context.obtainStyledAttributes(attrs, R.styleable.ClipRgnView)
        val bitID = mTypeArray.getResourceId(R.styleable.ClipRgnView_background, R.mipmap.ic_launcher)
        mBitmap = BitmapFactory.decodeResource(context.resources, bitID)
        mTypeArray.recycle()
        init()
    }

    private fun init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        w = mBitmap.width
        h = mBitmap.height
//        clipWidth = w/2
        mPath = Path()
        mPath.fillType = Path.FillType.WINDING // 默认值 ， 取两图形所在区域
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        mRgn.setEmpty()
        mPath.reset()
        var cuttingRgnNum:Int = h/ this.clipHeight
        for (i in 0..cuttingRgnNum){
            if (i % 2 == 0){
                mPath.addRect(RectF(0f, i * clipHeight.toFloat(), clipWidth.toFloat(), (i + 1) * clipHeight.toFloat()), Path.Direction.CCW)
//                mRgn.union()
            }else {
                mPath.addRect(RectF(w - clipWidth.toFloat(), i * clipHeight.toFloat(), w.toFloat(), (i + 1) * clipHeight.toFloat()), Path.Direction.CCW)
//                mRgn.union()
            }
        }
        val mPaint = Paint()
//        canvas?.clipRegion(mRgn)// 方法已过时
        canvas?.clipPath(mPath) // 取相交区域
        canvas?.drawBitmap(mBitmap, 0f, 0f, mPaint)
        if (clipWidth > w) return
        clipWidth += 2 // 逐渐增大剪裁区域
        invalidate()
    }
}

// TODO ClipRgnAnimView 使用动画效果完成剪裁动画
class ClipRgnAnimView:View {
    lateinit var mBitmap: Bitmap
    /** 剪裁宽度 */
    var clipWidth: Int = 0
    /** 剪裁高度 */
    var clipHeight: Int = 5
    var w: Int = 0
    var h: Int = 0

    /** 裁剪区域 */
//    lateinit var mRgn:Region
    lateinit var mPath: Path
    lateinit var mPaint: Paint

    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)

    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val mTypeArray = context.obtainStyledAttributes(attrs, R.styleable.ClipRgnAnimView)
        val bitID = mTypeArray.getResourceId(R.styleable.ClipRgnAnimView_clipAnimBg, R.mipmap.ic_launcher)
        mBitmap = BitmapFactory.decodeResource(context.resources, bitID)
        mTypeArray.recycle()
        init()
    }

    private fun init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        w = mBitmap.width
        h = mBitmap.height
//        clipWidth = w/2
        mPath = Path()
        mPath.fillType = Path.FillType.WINDING // 默认值 ， 取两图形所在区域

        mPaint = Paint()
        mPaint.setShader(BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP))
        val valueAnimator = ValueAnimator.ofFloat(clipWidth.toFloat(), w.toFloat())
        valueAnimator.setDuration(5000)
        valueAnimator.addUpdateListener {
            clipWidth = (it.getAnimatedValue() as Float).toInt()
            postInvalidate()
        }
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.repeatMode = ValueAnimator.RESTART
        valueAnimator.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPath.reset()
        var cuttingRgnNum:Int = h/ this.clipHeight
        for (i in 0 .. cuttingRgnNum) {
            if (i%2 == 0) {
                mPath.addRect(0f, i * clipHeight.toFloat(), clipWidth.toFloat(), (i+1) * clipHeight.toFloat(), Path.Direction.CW)
            }else {
                mPath.addRect(w - clipWidth.toFloat(), i * clipHeight.toFloat(), w.toFloat(), (i+1) * clipHeight.toFloat(), Path.Direction.CW)
            }
        }
        canvas?.drawPath(mPath, mPaint)
//        canvas?.clipPath(mPath)
//        canvas?.drawBitmap(mBitmap, 0f,0f,mPaint)
    }
}

/** TODO 控件添加
 *
 * 静态添加View：通过在XML文件中添加布局
 * <comn.harvic.myapp.CustomView
 *      android:layout width="match_parent"
 *      android:layout height="match_parent" />
 * 动态添加View：通过Java代码动态添加 addView()
 *  LinearLayout rootView = (LinearLayout)findViewByIid(R,id.root);
 *  CustomView customView = new CustomView(this);
 *  LinearLayout.LayoutParams layoutParams =
 *          new LinearLayout.LayoutParams(Linearlayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
 *  rootView.addView(customView,layoutParams);
 * 动态修改View的宽高属性:LayoutParams
 *   LayoutParams 的作用就是设置控件的宽和高，对应的是 XML中的 layout width 和layout height 属性。
 *   LayoutParams 有三个构造函数。
 *   public LayoutParams(int width, int height)
 *   public LayoutParams(Context c, AttributeSet attrs)
 *   public LayoutParams(LayoutParams source)
 *  第一个构造函数用于指定具体的宽和高，取值有LayoutParams.MATCH_PARENTLayoutParams.FILL_PARENT 和具体值。
 *      比如，指定 LayoutParams(LayoutParams.MATCH_PARENT,500)就是指 layout_width 设置为 match_parent，
 *      layout_height 设置为 500px。
 *  第二、三个构造函数都不常用。
 *  第二个构造函数用于从AttributeSet 中提取出layout widthlayout height 等各属性的值。
 *  第三个构造函数更简单，直接从一个现成的 LayoutParams 中复制一份。
 *  示例:RelativeLayout动态设置宽高以及View的位置
 *     val rootView = findViewById<RelativeLayout>(R.id.rootView)
 *     val cobwebView = CobwebView(context)
 *     val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
 *     RelativeLayout.LayoutParams.WRAP_CONTENT)
 *     layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.text) // 在text右边
 *     rootView.addView(cobwebView, layoutParams)
 * 设置Margin：
 *      val layoutParam = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
 *      LinearLayout.LayoutParams.MATCH_PARENT)
 *      layoutParam.setMargins(10, 10, 10, 10)
 *      imageView.setLayoutParams(layoutParam)
 * 设置layout_weight：LinertLayout.LayoutParams的特殊构造函数:LayoutParams(width:Int, Height:Int, weight:Float)
 *      val tvLike = TextView(context)
 *      方式一：
 *      val layoutParam = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
 *      LinearLayout.LayoutParams.MATCH_PARENT, 1.0f)
 *      方式二：
 *      layoutParam.weight = 1.0f
 *      tvLike.setText("赞（8）")
 *      tvLike.setTextSize(16f)
 *      linearLayout.addView(tvLike, layoutParam)
 * 设置layout_gravity：
 *      layoutParam.gravity = Gravity.TOP
 *      rootView.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
 * addView()：动态添加控件，属于ViewGroup类中的一个函数
 *    不同参数下的addView()
 *      addView(child:View)：在节点末尾添加一个View控件，布局使用默认布局。layout_width\layout_height = wrap_content
 *      addView(child:View, index:Int)：参数二index的取值有-1、0和正数。
 *          取值-1：表示在末尾添加一个View控件。 取值0：在容器顶端添加一个View控件。
 *          取值正数：表示在对应索引位置插入一个View控件
 *      addView(child:View, params:LayoutParams)：使用自定义布局参数
 *      addView(child:View, index:Int, params:LayoutParams)：允许指定控件位置和自定义布局参数
 *      addView(child:View, width:Int, height:Int)：参数2和参数3简化了params:LayoutParams参数，内部会根据传入的宽高构造一个LayoutParams对象。
 */
class AddView:View{
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context):this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){



    }
}


// TODO GridView 内容显示不全，自定义GridView。 （暂未完成）
//	@Override
//	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//		super.onLayout(changed, l, t, r, b);
//		int childCount = getChildCount();
//		for (int i=0; i< childCount; i++) {
//			View childAt = getChildAt(i);
//			if (childAt instanceof ViewGroup) {
//				ViewGroup childVG = (ViewGroup)childAt;
//				int vgCount = childVG.getChildCount();
//				for (int x=0; x< vgCount; x++) {
//					View vgChildAt = childVG.getChildAt(x);
//					if (vgChildAt instanceof CheckBox) {
//						CheckBox cb = (CheckBox)vgChildAt;
//						final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) cb.getLayoutParams();
//						int pLeft = cb.getPaddingLeft();
//						int pRight = cb.getPaddingRight();
//						int pTop = cb.getPaddingTop();
//						int pBottom = cb.getPaddingBottom();
//						float cbWidth = cb.getMeasuredWidth();
//						float cbHeight = cb.getMeasuredHeight();
//						float textWidth = cb.getPaint().measureText(cb.getText().toString());
//						int num = (int) Math.ceil(textWidth/cbWidth);
//						Paint.FontMetrics fontMetrics = cb.getPaint().getFontMetrics();
//						int textHeight = (int) (fontMetrics.bottom-fontMetrics.top);
//						int allHeight = (int) (cbHeight - textHeight + (textHeight * num));
//						cb.layout(pLeft+lp.leftMargin, pTop+lp.topMargin,
//								(int) cbWidth, allHeight);
//					}
//				}
//			}
//		}
//	}