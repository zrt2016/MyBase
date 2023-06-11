package com.zrt.kotlinapp.activity_view.custom_basic.paint_basic

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.zrt.kotlinapp.R

/**
 * @author：Zrt
 * @date: 2023/3/16
 * 使用Paint绘制文字常用函数
 *   1、文字基准线：
 *      canvas.drawText(String text, float x, float y, Paint paint)
 *      String text: 文字内容
 *      float x, 文字X轴起始位置
 *      float y, 文字y轴基线位置
 *      Paint paint 画笔
 *   2、设置文字开始绘制点的位置，即X轴在文字位置的那一侧
 *      setTextAlign(Align align)
 *          Paint.Align.LEFT：x坐标在文字左侧
 *          Paint.Align.CENTER：x坐标在文字中间
 *          Paint.Align.RIGHT：x坐标在文字右侧
 *   3、文字绘制四格线与 FontMetrics
 *     ① 四格线分别：ascent、descent、top 和 bottom 以及baseLine（文字基准线）
 *       ascent： 绘制单个字符时，字符应当的最高高度所在线
 *       descent： 绘制单个字符时，字符应当的最低高度所在线
 *       top：可绘制最高高度所在线
 *       bottom：可绘制最低高度所在线
 *       baseLine：文字基准线
 *       一般：top > ascent > baseLine > descent > bottom
 *     ② FontMetrics 中 ascent、descent、top 和 bottom 的值计算
 *       ascent(负值) = ascent 线y坐标 - baseLine 线y坐标
 *       descent = descent 线y坐标 - baseLine 线y坐标
 *       top(负值) = top 线y坐标 - baseLine 线y坐标
 *       bottom = bottom 线y坐标 - baseLine 线y坐标
 *       FontMetrics 中的变量一般只用于计算真实四格线的位置
 *     ③ 根据 FontMetrics 推测四格线位置
 *       ascent 线y坐标 = baseLine 线y坐标 + FontMetrics.ascent 即 baseLine 线y坐标 - |FontMetrics.ascent| （|表示绝对值|）
 *       descent 线y坐标 = baseLine 线y坐标 + FontMetrics.descent
 *       top 线y坐标 = baseLine 线y坐标 + FontMetrics.top
 *       bottom 线y坐标 = baseLine 线y坐标 + FontMetrics.bottom
 *     ④ FontMetrics 对象获取
 *       Paint mPaint = Paint()
 *       Paint.FontMetrics fmFloat = mPaint.getFontMetrics() // 值是float类型
 *       Paint.FontMetricsInt fmInt = mPaint.getFontMetricsInt() // 值是Int类型
 *   4、Paint 常用函数
 *       val mPaint = Paint()
 *     ① 字符串所占区域高度
 *       val fm = mPaint.getFontMetrics()
 *       val height = fm.bottom - fm.top
 *     ② 字符串所占区域宽度
 *       mPaint.measureText(String) // 获取宽度
 *     ③ 获取字符串所对应的最小矩形，以（0, 0）点所在位置为基线
 *       mPaint.getTextBounds(String text, int start, int end, Rect bounds)
 *       text: 文本内容
 *       start:字符起始位置
 *       end：字符终点位置
 *       bounds：接收测量结果的Rect
 *     ④ 基本函数
 *       mPaint.reset() : 重置画笔
 *       mPaint.setColor(int)：设置画笔颜色
 *       mPaint.setARGB(int a, int r, int g, int b) ：分开设置画笔的颜色
 *       mPaint.setAlpha(int) : 设置画笔透明度
 *       mPaint.setStyle(Paint.Style) ：设置画笔样式
 *          Paint.Style.STROKE   // 仅描边  画圆环
 *          Paint.Style.FILL   // 仅填充内部  填充圆环内，环边不填充
 *          Paint.Style.FILL_AND_STROKE   // 填充内部和描边，包含圆环和圆环内均进行填充
 *       mPaint.setStrokeWidth(int)：画笔宽度
 *       mPaint.setAntiAlias(boolean) ：设置画笔是否抗锯齿
 *       mPaint.setStrokeMiter(float)：设置画笔倾斜度，例90度拿画笔和30度拿画笔绘制
 *       mPaint.setPathEffect(PathEffect)：设置路径样式，取值类型为PathEffect的子类
 * @see PaintPathEffectView  setPathEffect 不同路径样式绘制路径
 *         CornerPathEffect(圆形拐角)、DashPathEffect（虚线）、DiscretePathEffect（离散路径）、
 *         PathDashPathEffect（印章路径）、ComposePathEffect（合并虚线和圆形拐角）、
 *         SumPathEffect（合并路径虚线与圆形拐角路径效果）
 *       mPaint.setStrokeCap(Paint.Cap) 设置线帽样式
 *          Paint.Cap.ROUND(圆形线帽)、Paint.Cap.SQUARE（方形线帽）、Paint.Cap.BUTT(无线帽)
 *       mPaint.setStrokeJoin(Paint.Join):设置路径拐角样式
 *          Paint.Join.MITER(结合处为锐角) 、Paint.Join.ROUND(结合处为圆角) 、Paint.Join.BECEL(结合处为直线)
 *       mPaint.setDither(boolean):设置绘制图像时的抗抖动效果，对图像中相邻颜色值进行中和，是图片 显示更为细腻
 *     ⑤ 字体相关函数
 *      setTextSize(float) 文字大小
 *      setFakeBoldText(boolean): 是否设置粗体文字
 *      setStrikeThruText(boolean):是否设置删除线效果
 *      setUnderlineText(boolean)：是否设置下划线
 *      setTextAlgin(Paint.Align):设置开始绘图点
 *      setTextScaleX(float):设置水平拉伸
 *      setTextSkewX(float):设置字体水平倾斜度，普通斜体字设为-0.25. 负值向右倾斜，正值向左倾斜。
 *      setTypeface(Typeface): 设置字体样式
 *      setLinearText(boolean)：是否打开线性文本标识，是否取消提前缓存在显存中。（可以节省内存）
 *      setSubpixelText(boolean):是否打开亚像素设置绘制文本（增强文本显示清晰度，但会降低性能）
 *      setShadowLayer(float radius, float dx, float dy, @ColorInt int shadowColor): 设置阴影效果
 * @see PaintShadowLayerView 注：阴影效果不支持硬件加速
 *          float radius ： 模糊半径，值越大越模糊，值越小越清晰，等于 0 则阴影小时
 *          float dx, float dy ： 阴影的偏移距离，正值向右和向下，负值向左和向上
 *          @ColorInt int shadowColor：绘制阴影的画笔颜色，即阴影的颜色（对图片阴影无需）
 *          文字和绘制的图形阴影会使用定义的阴影画笔颜色绘制。图片的阴影则是产生一张相同的图片，对图片边缘进行模糊
 *      clearShadowLayer() 清除阴影效果
 *      setMaskFilter(MaskFilter maskfilter) 添加发光效果，颜色取边缘颜色， 不支持硬件加速。
 * @see PaintMaskFilterView  实现 BlurMaskFilter 发光效果
 *      MaskFilter 有两个派生类： BlurMaskFilter（发光效果） 和 EmbossMaskFilter（实现浮雕效果）
 *          maskfilter = null 不设置发光效果
 *      BlurMaskFilter(float radius, Blur style)
 *      float radius: 定义模糊半径，即发光半径
 *      Blur style：发光样式，有四种：
 *          BlurMaskFilter.Blur.INNER ： 内发光，像内扩散 radius 距离
 *          BlurMaskFilter.Blur.SOLID ：外发光，向外扩散 radius 距离
 *          BlurMaskFilter.Blur.NORMAL ：内外发光 ，内外同时扩散 radius 距离
 *          BlurMaskFilter.Blur.OUTER ：仅显示发光效果
 * @see PaintBitmapShadowLayerView 给图片添加纯色阴影效果
 *          1、Bitmap.extractAlpha() ： 获取一张与原图片一样Alpha值的空白图片
 *          2、通过Canvas.drawBitmap 传入画笔的颜色控制阴影显示的颜色
 *      setShader(Shader shader): 使用印章的样式来填充图片
 * @see PaintBitmapShaderView  Shader 的伴生类 BitmapShader 的基本使用
 *      从左上角开始绘制，先填充Y轴，然后在整体向X轴填充
 *      BitmapShader(Bitmap bitmap,TileMode tileX,TileMode tileY):
 *          Bitmap bitmap：指定图片
 *          TileMode tileX：指定当X轴超出图片大小时，使用的重复策略
 *          TileMode tileY：指定当Y轴超出图片大小时，使用的重复策略
 *          Shader.TileMode.CLAMP（使用边缘色彩填充）、Shader.TileMode.REPEAT（重复原图填充）
 *          、Shader.TileMode.MIRROR（重复使用镜像模式的图像填充）
 * @see PaintBSTelescopeView 使用 BitmapShader 实现望远镜效果
 * @see PaintBSAvatorView 使用 BitmapShader 生成不规则头像
 *   LinearGradient 实现线性渐变效果， 父类 Shader
 *      LinearGradient(float x0, float y0, float x1, float y1,int color0, int color1,TileMode tile)
 *      LinearGradient(float x0, float y0, float x1, float y1,int[] colors, float[] positions,TileMode tile)
 *          (x0, y0) 起始渐变点坐标； (x1, y1) 终点渐变点坐标；
 *          int color0：起始点颜色值，包含透明度AA值, int color1：终点颜色值，包含透明度AA值
 *          int[] colors：指定渐变的颜色数组，多个渐变颜色点，长度与positions一致。
 *          float[] positions：对应每个渐变颜色数值的进度（即百分比位置），长度与int[] colors一致，一一对应关系。
 *          TileMode tile：与BitmapShader 一样，指定区域控件大于指定的渐变区域时，空白区域颜色填充模式
 * @see PaintLinearGradient 使用 LinearGradient 实现线性渐变
 * @see PaintLGShimmerTextView 使用 LinearGradient 实现文字颜色渐变动画
 *   RadialGradient 实现线性渐变效果，父类 Shader
 *      RadialGradient(float centerX, float centerY, float radius,int centerColor, int edgeColor, TileMode tileMode)
 *      RadialGradient(float centerX, float centerY, float radius,int[] colors, float[] stops, TileMode tileMode)
 *          (centerX, centerY) 渐变中心点坐标；   radius：渐变半径
 *          centerColor：渐变中心起始点颜色，包含透明度AA值；      edgeColor：渐变结束时的颜色，包含透明度AA值
 *          int[] colors：指定渐变的颜色数组，多个渐变颜色点，长度与 stops 一致,
 *          float[] stops：对应每个渐变颜色数值的进度（即百分比位置），长度与int[] colors一致，一一对应关系。
 *          TileMode tile：与BitmapShader 一样，指定区域控件大于指定的渐变区域时，空白区域颜色填充模式
 * @see PaintRadialGradientView 使用 RadialGradient 实现放射性渐变
 */

// TODO 画笔绘制文字
class PaintTextBasicView:View{
    val mPaint:Paint = Paint()
    var text: String = ""
    var textSize: Float = 20f
    var textColor: Int = Color.BLACK
    var textPWith:Float = 1f
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context):this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.PaintTextBasicView)
        textColor = typeArray.getColor(R.styleable.PaintTextBasicView_textPColor, Color.BLACK)
        textPWith = typeArray.getDimension(R.styleable.PaintTextBasicView_textPWith, 2f)
        text = typeArray.getString(R.styleable.PaintTextBasicView_text) ?: ""
        textSize = typeArray.getDimension(R.styleable.PaintTextBasicView_android_textSize, 20f)
        typeArray.recycle()

        mPaint.setColor(textColor)
        mPaint.strokeWidth = textPWith
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 获取宽高设置模式
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        // 获取宽高值
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        Log.i(">>>>", "##widthSpecSize=$widthSpecSize; heightSpecSize=$heightSpecSize")
        setMeasuredDimension(widthSpecSize,
                heightSpecSize)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val baseLineX = 0f
        var baseLineY = 50f
        // 字体宽度
        val textWidth = mPaint.measureText(text)
        // 绘制文字基线
        mPaint.setColor(Color.GRAY)
        mPaint.strokeWidth = 1f
        mPaint.style = Paint.Style.FILL
        canvas?.drawLine(baseLineX,baseLineY, textWidth, baseLineY, mPaint)
        // 绘制文字
        mPaint.setColor(textColor)
        mPaint.strokeWidth = textPWith
        mPaint.textSize = textSize
        mPaint.textAlign = Paint.Align.LEFT
        canvas?.drawText(text, baseLineX, baseLineY, mPaint)

        baseLineY += 100f
        // 绘制文字基线
        mPaint.setColor(Color.GRAY)
        mPaint.strokeWidth = 1f
        canvas?.drawLine(baseLineX,baseLineY, textWidth, baseLineY, mPaint)
        // 绘制文字
        mPaint.setColor(textColor)
        mPaint.strokeWidth = textPWith
        mPaint.textSize = textSize
        mPaint.textAlign = Paint.Align.CENTER
        canvas?.drawText(text, baseLineX, baseLineY, mPaint)

        baseLineY += 100f
        // 绘制文字
        mPaint.setColor(textColor)
        mPaint.strokeWidth = textPWith
        mPaint.textSize = textSize
        mPaint.textAlign = Paint.Align.LEFT
        canvas?.drawText(text, baseLineX, baseLineY, mPaint)
        // 绘制文字的四格线 ascent、descent、top 和 bottom 以及 baseLine 线
        val fm = mPaint.getFontMetrics()
        val ascent = baseLineY + fm.ascent
        val descent = baseLineY + fm.descent
        val top = baseLineY + fm.top
        val bottom = baseLineY + fm.bottom

        mPaint.strokeWidth = 1f
        mPaint.setColor(Color.BLUE)
        canvas?.drawLine(baseLineX,ascent, textWidth, ascent, mPaint) // ascent 线
        mPaint.setColor(Color.BLUE)
        canvas?.drawLine(baseLineX,descent, textWidth, descent, mPaint) // descent 线
        mPaint.setColor(Color.RED)
        canvas?.drawLine(baseLineX,top, textWidth, top, mPaint) // top 线
        mPaint.setColor(Color.RED)
        canvas?.drawLine(baseLineX,bottom, textWidth, bottom, mPaint) // bottom 线
        mPaint.setColor(Color.GRAY)
        canvas?.drawLine(baseLineX,baseLineY, textWidth, baseLineY, mPaint) // baseLine 线


        val textRect = Rect()
        mPaint.setColor(Color.DKGRAY)
        mPaint.style = Paint.Style.STROKE
        mPaint.getTextBounds(text, 0, text.length, textRect)
        // 该矩形默认已(0,0)为基线位置，因此，需要平移到当前文字基线所处位置
        textRect.top = textRect.top + baseLineY.toInt()
        textRect.bottom = textRect.bottom + baseLineY.toInt()
        canvas?.drawRect(textRect, mPaint)

        // 根据给定的Top值，绘制文字
        val currentTop = baseLineY + 50f
        mPaint.style = Paint.Style.FILL
        // 绘制 top 线
        mPaint.setColor(Color.RED)
        canvas?.drawLine(0f, currentTop, textWidth, currentTop, mPaint)
        // 绘制基线位置
        mPaint.setColor(Color.GRAY)
        val blX = currentTop - fm.top
        canvas?.drawLine(baseLineX,blX, textWidth, blX, mPaint) // baseLine 线

        mPaint.setColor(textColor)
        mPaint.strokeWidth = textPWith
        mPaint.textSize = textSize
        mPaint.textAlign = Paint.Align.LEFT
        canvas?.drawText(text, baseLineX, blX, mPaint)

    }
}

/** TODO 绘制不同路径样式
 *
 */
class PaintPathEffectView:View {
    val mPaint: Paint = Paint()
    val path = Path()
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)

    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        mPaint.strokeWidth = 4f
        mPaint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var baseY = 30f

        path.reset()
        // 原始路径
        mPaint.setPathEffect(PathEffect())
        drawCustomPath(baseY, canvas)
        baseY += 100
        path.reset()
        // CornerPathEffet 圆形拐角路径
        mPaint.setPathEffect(CornerPathEffect(5f))
        drawCustomPath(baseY, canvas)
        baseY += 100
        path.reset()
        // DashPathEffect 虚线路径
        mPaint.setPathEffect(DashPathEffect(floatArrayOf(4f, 4f), 0f))
        drawCustomPath(baseY, canvas)
        baseY += 100
        path.reset()
        // DashPathEffect 离散路径
        // float segmentLength (切断长度), float deviation （线段偏移量,取随机数，小于deviation）
        mPaint.setPathEffect(DiscretePathEffect(4f, 5f))
        drawCustomPath(baseY, canvas)
    }

    private fun drawCustomPath(baseY: Float, canvas: Canvas?) {
        path.moveTo(0f, baseY)
        path.lineTo(100f, baseY - 30)
        path.lineTo(150f, baseY + 50)
        path.lineTo(250f, baseY - 40)
        path.lineTo(400f, baseY)
        canvas?.drawPath(path, mPaint)
    }
}

/** TODO 文字图片添加阴影效果
 *
 */
class PaintShadowLayerView:View {
    val mPaint: Paint = Paint()
    var bitmap:Bitmap? = null
    var isShadowLayer: Boolean = false
        set(value) {
            field = value
            postInvalidate()
        }
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        mPaint.strokeWidth = 4f
        mPaint.style = Paint.Style.FILL
        mPaint.textSize = 23f
        mPaint.setColor(Color.BLACK)
        bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.grape)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        if(isShadowLayer) {
            mPaint.setShadowLayer(2f, 10f, 10f, Color.GRAY)
        }else {
            mPaint.clearShadowLayer()
        }
        canvas?.drawText("ShadowLayer 图片阴影 点击", 20f, 80f, mPaint)
        canvas?.drawCircle(350f, 50f, 40f, mPaint)
        bitmap?.let {
            canvas?.drawBitmap(it, null, Rect(10, 120, 410, 420), mPaint)
        }
    }

}

/** TODO 设置发光效果
 *
 */
class PaintMaskFilterView: View {
    val mPaint: Paint = Paint()
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPaint.reset()
        mPaint.strokeWidth = 4f
        mPaint.style = Paint.Style.FILL
        mPaint.textSize = 25f
        mPaint.setColor(Color.BLACK)
        mPaint.setMaskFilter(null) // 取消发光设置
        canvas?.drawText("Inner 内发光", 20f, 30f, mPaint)
        canvas?.drawText("SOLID 外发光", 180f, 30f, mPaint)
        canvas?.drawText("NORMAL 内外发光", 340f, 30f, mPaint)
        canvas?.drawText("OUTER 仅发光", 540f, 30f, mPaint)
        mPaint.setMaskFilter(BlurMaskFilter(20f, BlurMaskFilter.Blur.INNER))
        canvas?.drawCircle(80f, 100f, 40f, mPaint)

        mPaint.setMaskFilter(BlurMaskFilter(20f, BlurMaskFilter.Blur.SOLID))
        canvas?.drawCircle(240f, 100f, 40f, mPaint)

        mPaint.setMaskFilter(BlurMaskFilter(20f, BlurMaskFilter.Blur.NORMAL))
        canvas?.drawCircle(400f, 100f, 40f, mPaint)

        mPaint.setMaskFilter(BlurMaskFilter(20f, BlurMaskFilter.Blur.OUTER))
        canvas?.drawCircle(600f, 100f, 40f, mPaint)
    }
}

// TODO Bitmap 添加纯色阴影效果
class PaintBitmapShadowLayerView: View{
    val mPaint: Paint = Paint()
    lateinit var mBitmap: Bitmap
    lateinit var mAlphaBitmap: Bitmap
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mBitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.cat_dog)
        mAlphaBitmap = mBitmap.extractAlpha() // 获取一张空白图片，该图片具有与原图片一样的 Alpha 值
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPaint.reset()
        val width = 200
        val height = width * mAlphaBitmap.height / mAlphaBitmap.width
        // 给原图添加灰色阴影效果
        mPaint.setMaskFilter(BlurMaskFilter(10f, BlurMaskFilter.Blur.NORMAL))
        canvas?.drawBitmap(mAlphaBitmap, null, Rect(15, 15, width+15, height+15), mPaint)
        mPaint.setMaskFilter(null)
        canvas?.drawBitmap(mBitmap, null, Rect(10, 10, width+10, height+10), mPaint)
        mPaint.color = Color.RED
        canvas?.drawBitmap(mAlphaBitmap, null, Rect(width+30, 10, 2*width+30, height+10),mPaint)
        mPaint.color = Color.GRAY
        mPaint.setMaskFilter(BlurMaskFilter(10f, BlurMaskFilter.Blur.NORMAL))
        canvas?.drawBitmap(mAlphaBitmap, null, Rect(2*width+60, 10, 3*width+60, height+10),mPaint)
    }
}
// TODO BitmapShader
class PaintBitmapShaderView: View {
    val mPaint: Paint = Paint()
    lateinit var mBitmap: Bitmap
    lateinit var bsRepeat:BitmapShader
    lateinit var bsClamp:BitmapShader
    lateinit var bsMirror:BitmapShader
    lateinit var bsRepeatMirror:BitmapShader
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mBitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.dog)
        bsRepeat = BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        bsClamp = BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        bsMirror = BitmapShader(mBitmap, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR)
        bsRepeatMirror = BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.MIRROR)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPaint.setShader(bsClamp)
        canvas?.drawRect(Rect(0,0,width,height/4),mPaint)
        mPaint.setShader(bsRepeat)
        canvas?.drawRect(Rect(0,height/4,width,height/4*2),mPaint)
        // 绘制镜像图，同时截取中间的一部分，可以看到图片默认从左上角绘制，然后截取中间的一部分显示
        mPaint.setShader(bsMirror)
        canvas?.drawRect(Rect(20,height/4*2+20,width-20,height/4*3-20),mPaint)
        mPaint.setShader(bsRepeatMirror)
        canvas?.drawRect(Rect(0,height/4*3,width,height),mPaint)
    }
}
// TODO 使用 BitmapShader实现望远镜效果
class PaintBSTelescopeView:View{
    val mPaint: Paint = Paint()
    lateinit var mBitmap: Bitmap
    var mBitmapBG: Bitmap? = null
    // 记录手指中心点
    var mDx: Float = -1f;
    var mDy: Float = -1f;

    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        mBitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.scenery)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mBitmapBG == null) {
            // 创建一个空白的 bitmap，大小与控件大小一致
            mBitmapBG = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            mBitmapBG?.let{
                // 将创建的 bitmap 作为画布
                val canvasBG = Canvas(it)
                // 在画布上绘制原始图片（mBitmap）, 完整绘制大小与控件大小一致
                canvasBG.drawBitmap(mBitmap, null, Rect(0, 0, width, height), mPaint)
            }
        }
        if (mDx != -1f && mDy != -1f) {
            mBitmapBG?.let {
                // 将 mBitmapBG 作为 BitmapShader 设置给 Paint
                mPaint.setShader(BitmapShader(it, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT))
                // 在手指点击出绘制一个圆圈，该圆圈会显示部分图像
                canvas?.drawCircle(mDx, mDy, 150f, mPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDx = event.x
                mDy = event.y
                postInvalidate()
                return@onTouchEvent true
            }
            MotionEvent.ACTION_MOVE -> {
                mDx = event.x
                mDy = event.y
            }
            MotionEvent.ACTION_UP -> {
                mDx = -1f
                mDy = -1f
            }
        }
        postInvalidate()
        return super.onTouchEvent(event)
    }
}
// TODO 使用BitmapShader 生成不规则头像
class PaintBSAvatorView:View {
    val mPaint: Paint = Paint()
    lateinit var mBitmap: Bitmap
    var mBitmapBG: Bitmap? = null
    lateinit var mBitmapShader: BitmapShader

    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)

    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mBitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.avator)
        mBitmapShader = BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 将 mBitmapShader 缩放到与控件宽高一致
        val matrix = Matrix()
        val scale = width / mBitmap.width .toFloat()
        matrix.setScale(scale, scale)
        mBitmapShader.setLocalMatrix(matrix)
        mPaint.setShader(mBitmapShader)
        val half = width/2.toFloat()
        canvas?.drawCircle(half, half, half, mPaint)
    }
}
// PaintLinearGradient 线性渐变
class PaintLinearGradient: View {
    val mPaint: Paint = Paint()

    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)

    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val mLinearGradient1 = LinearGradient(0f, 25f, width.toFloat(), 25f,
            Color.argb(255, 255, 0, 0), Color.argb(255, 0, 0, 255), Shader.TileMode.REPEAT)
        mPaint.setShader(mLinearGradient1)
        canvas?.drawRect(Rect(0, 0, width, 50), mPaint)
        val mLinearGradient2 = LinearGradient(0f, 75f, width.toFloat()/4*3, 75f,
                intArrayOf(Color.argb(255, 255, 0, 0), Color.argb(255, 255, 0, 0),
                        Color.argb(255, 0, 255, 0), Color.argb(255, 0, 255, 0), Color.argb(255, 0, 0, 255)),
                floatArrayOf(0.0f, 0.2f,0.6f, 0.8f, 1f),
                Shader.TileMode.REPEAT)
        mPaint.setShader(mLinearGradient2)
        canvas?.drawRect(Rect(0, 50, width, 100), mPaint)

        val mLinearGradient3 = LinearGradient(0f, 75f, width.toFloat()/2, 75f,
                intArrayOf(Color.argb(255, 200, 0, 0), Color.argb(255, 0, 255, 0),
                        Color.argb(255, 0, 0, 200)),
                floatArrayOf(0.0f, 0.5f, 1f),
                Shader.TileMode.MIRROR)
        mPaint.setShader(mLinearGradient3)
        mPaint.textSize = 40f
        val basicLineY = 100 - mPaint.fontMetrics.top
        canvas?.drawText("Text 文字颜色渐变效果", 50f, basicLineY, mPaint)
    }
}
// TODO LinearGradient 文字颜色渐变动画效果
class PaintLGShimmerTextView: AppCompatTextView {
    lateinit var mPaint: Paint
    var mDx:Float = 0f
    lateinit var mLinearGradient: LinearGradient
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)

    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mPaint = paint
        // 获取文字长度
        val length = mPaint.measureText(text.toString())
        // 添加动画
        createAnim(length)
        mLinearGradient = LinearGradient(-length.toFloat(), 0f, 0f, length.toFloat(),
                intArrayOf(Color.argb(255, 255, 0, 0), Color.argb(255, 0, 255, 0), Color.argb(255, 0, 0, 255)),
                floatArrayOf(0.0f, 0.5f, 1f), Shader.TileMode.CLAMP)
    }
    fun createAnim(length:Float) {
        val valueAnim = ValueAnimator.ofFloat(0f, length*2)
        valueAnim.addUpdateListener {
            mDx = it.getAnimatedValue() as Float
            postInvalidate()
        }
        valueAnim.duration = 2000
        valueAnim.repeatCount = ValueAnimator.INFINITE
        valueAnim.repeatMode = ValueAnimator.RESTART
        valueAnim.start()
    }

    override fun onDraw(canvas: Canvas?) {
        val matrix = Matrix()
        // 设置位图进行平移
        matrix.setTranslate(mDx, 0f)
        mLinearGradient.setLocalMatrix(matrix)
        mPaint.setShader(mLinearGradient)
        super.onDraw(canvas)
    }
}
// TODO 使用 RadialGradient 实现放射性渐变
class PaintRadialGradientView: View {
    val mPaint: Paint = Paint()
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val mRadialGradient = RadialGradient(width/8.toFloat(), height/2.toFloat(), height/2.toFloat(),
                Color.argb(255, 255, 0, 0), Color.argb(255, 0, 0, 255), Shader.TileMode.CLAMP)
        mPaint.setShader(mRadialGradient)
        canvas?.drawCircle(width/8.toFloat(), height/2.toFloat(), height/2.toFloat(), mPaint)

        val mRadialGradient2 = RadialGradient(width/8*3.toFloat(), height/2.toFloat(), height/4.toFloat(),
                intArrayOf(Color.argb(255, 255, 0, 0), Color.argb(255, 0, 255, 0), Color.argb(255, 0, 0, 255)),
                floatArrayOf(0.0f, 0.5f, 1f), Shader.TileMode.CLAMP)
        mPaint.setShader(mRadialGradient2)
        canvas?.drawCircle(width/8*3.toFloat(), height/2.toFloat(), height/2.toFloat(), mPaint)
        // 重复模式
        val mRadialGradient3 = RadialGradient(width/8*5.toFloat(), height/2.toFloat(), height/4.toFloat(),
                Color.argb(255, 255, 0, 0), Color.argb(255, 0, 255, 0), Shader.TileMode.REPEAT)
        mPaint.setShader(mRadialGradient3)
        canvas?.drawCircle(width/8*5.toFloat(), height/2.toFloat(), height/2.toFloat(), mPaint)
        // 镜像模式
        val mRadialGradient4 = RadialGradient(width/8*7.toFloat(), height/2.toFloat(), height/4.toFloat(),
                Color.argb(255, 255, 0, 0), Color.argb(255, 0, 255, 0), Shader.TileMode.MIRROR)
        mPaint.setShader(mRadialGradient4)
        canvas?.drawCircle(width/8*7.toFloat(), height/2.toFloat(), height/2.toFloat(), mPaint)
    }
}
