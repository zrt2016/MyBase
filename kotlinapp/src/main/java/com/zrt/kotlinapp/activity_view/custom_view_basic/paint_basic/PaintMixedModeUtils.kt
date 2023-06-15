package com.zrt.kotlinapp.activity_view.custom_view_basic.paint_basic

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.zrt.kotlinapp.R

/**
 * @author：Zrt
 * @date: 2023/3/25
 * 一、Paint 进阶 混合模式，能够实现两张图片的无缝结合
 *     Paint.setXfermode(Xfermode xfermode) 实现
 * 二、使用 Xfermode 需做2件事
 *     1、禁用硬件加速：setLayerType(LAYER_TYPE_SOFTWARE, null)
 *     2、使用离屏绘制（在onDraw中）：
 *         ① 新建图层：val  layerID = canvas?.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
 *         ② 核心绘制代码：绘制的核心代码放在canvas.save()和canvas.resotre()函数之间
 *         ③ 还原图层：canvas.restoreToCount(layerID)
 * 三、Xfermode的子类：AvoidXfermode、PixelXorXfermode 和 PorterDuffXfermode
 *     AvoidXfermode：API16中已过时,不支持硬件加速 （未查询到该类）
 *          原理：先把选定区域的颜色值清空，然后在替换为目标颜色
 *     PixelXorXfermode：API16中已过时，不支持硬件加速。 针对像素的简单异或运算（op^src^dst）,
 *              返回的Alpha始终是255，对于操作颜色混合不是特别有效很少用（未查询到该类）
 *     PorterDuffXfermode：部分支持硬件加速
 * 四、AvoidXfermode
 * @see PaintAvoidXfermodeView AvoidXfermode 找不到该类，此处仅为代码实现
 *     AvoidXfermode(int opColor,int tolerance ,Mode mode)
 *         opColor：一个十六进制的AARRGGBB形式的颜色值
 *         tolerance：表示容差，任何颜色值差异都在 0~255之间。为0时，只选择 opColor 颜色。255时，即所有颜色都被选中。
 *         mode：取值为Mode.TARGET和Mode.AVOID。前者表示将指定颜色替换掉，后者表示将Mode.TARGET的相反区域颜色替换掉。
 * 五、PorterDuffXfermode
 *     PorterDuffXfermode(PorterDuff.Mode mode)
 * ******** 在设置Xfermode之前绘制的图像叫做目标图像，之后绘制的是源图像
 *         PorterDuff.Mode mode：表示混合模式，枚举值有18种
 *             计算公式（括号内为别称）：
 *                 SourceAlpha（Sa）（alpha_{src}）：源图形的 Alpha 通道
 *                 DestinationAlpha（Da）（alpha_{dst}）：目标图像的 Alpha 通道
 *                 SourceColor（Sc）（C_{src}）：源图像的颜色
 *                 DestinationColor（Dc）（C_{dst}）：目标图像的颜色
 *             每种模式又分为2部分[alpha_{out}, C_{out}]
 *                 alpha_{out}：计算后的 Alpha 通道
 *                 C_{out}：计算后的颜色值
 *        例：LIGHTEN 模式计算公式
 *           alpha_{out} = alpha_{src} + alpha_{dst} - alpha_{src} * alpha_{dst}
 *           C_{out} = (1 - alpha_{dst}) * C_{src} + (1 - alpha_{src}) * C_{dst} + max(C_{src}, C_{dst})
 *        模式：
 *        CLEAR /** [0, 0] */
 *          显示空白，因为所有像素点的alpha和color都是0
 *        SRC /** [Sa, Sc] */
 *          只保留源图像的color和alpha
 *        DST /** [Da, Dc] */
 *          只保留目标图像的color和alpha
 *        SRC_OVER /** [Sa + (1 - Sa)*Da, Rc = Sc + (1 - Sa)*Dc] */
 *          绘制源图像，在源图像像素点的其他地方绘制目标图像，源颜色，源图像其他地方用目标颜色
 *        DST_OVER /** [Sa + (1 - Sa)*Da, Rc = Dc + (1 - Da)*Sc] */
 *          绘制源图像，在源图像像素点的其他地方绘制目标图像，目标颜色，目标图像的其他方用源颜色
 *        SRC_IN /** [Sa * Da, Sc * Da] */
 *          绘制源图像和目标图像的交集，用源图像颜色，颜色受目标图像的透明度影响
 *        DST_IN /** [Sa * Da, Sa * Dc] */
 *          绘制源图像和目标图像的交集，用目标图像颜色，颜色受源图像的透明度影响
 *        SRC_OUT (7) /** [Sa * (1 - Da), Sc * (1 - Da)] */
 *          在没有目标图像的地方绘制源图像，用源图像颜色，颜色的透明度与目标图像的透明度相反
 *        DST_OUT (8) /** [Da * (1 - Sa), Dc * (1 - Sa)] */
 *          在没有源图像的地方绘制目标图像，用目标图像颜色，颜色的透明度与源图像的透明度相反
 *        SRC_ATOP (9) /** [Da, Sc * Da + (1 - Sa) * Dc] */
 *          正常绘制目标图像，（相交部分）源图颜色受目标透明度影响，目标颜色部分与源图透明度相反
 *        DST_ATOP (10) /** [Sa, Sa * Dc + Sc * (1 - Da)] */
 *          正常绘制源图像，（相交部分）目标图颜色受源图像透明度影响，源图像颜色部分与目标图透明度相反
 *        XOR (11) /** [Sa + Da - 2 * Sa * Da, Sc * (1 - Da) + (1 - Sa) * Dc] */
 *          正常绘制两图不相交的部分，（相交部分）源图像颜色部分与目标图透明度相反，目标颜色部分与源图透明度相反
 *        DARKEN /** [Sa + Da - Sa*Da , Sc*(1 - Da) + Dc*(1 - Sa) + min(Sc, Dc)] */
 *          绘制图像，（相交部分，先如XOR相交部分显示）最后混合一层较暗的颜色(非完全不透明时按上面公式叠加一层暗的颜色，完全不透明则显示黑色)
 *        LIGHTEN /** [Sa + Da - Sa*Da , Sc*(1 - Da) + Dc*(1 - Sa) + max(Sc, Dc)] */
 *          绘制图像，（相交部分，先如XOR相交部分显示）最后混合一层较亮的图像（非完全不透明时按上面公式叠加一层亮的颜色，完全不透明则显示白色）
 *        MULTIPLY /** [Sa * Da, Sc * Dc] */
 *          绘制两图相交的部分，颜色为两图颜色的叠加，eg：蓝+红=紫
 *        SCREEN /** [Sa + Da - Sa * Da, Sc + Dc - Sc * Dc] */
 *          绘制两张图，重叠的部分保留较白的一方（感觉像较白的一方显示在上面，较黑的颜色显示在下面）
 *        ADD /** Saturate(S + D) */
 *          饱和度叠加
 *        OVERLAY
 *          叠加。像素是进行 Multiply （正片叠底）混合还是 Screen （屏幕）混合，取决于底层颜色，但底层颜色的高光与阴影部分的亮度细节会被保留；
 * @see PaintPDXLightenView 实现开灯效果，使用 LIGHTEN 模式
 * @see PaintTwitterMultiplyView  使用用 PorterDuff.Mode.MULTIPLY 完成2张图片合成小鸟轮廓图
 * @see PaintInvertedSrcInView PorterDuff.Mode.SRC_IN 实现倒影图
 * @see PaintEraserSrcOutView PorterDuff.Mode.SRC_OUT 实现橡皮擦效果
 * ******************************************************************************
 * 从以上可以看出 DST 与 SRC 模式正好相反的，DST 完全以目标图像为主，SRC 以原图像为主
 * SRC_OVER 与 DST_OVER 相反
 * SRC_IN 与 DST_IN 相反 上述例子中 PaintInvertedSrcInView 可以将目标图片和源图片相互替换，使用 DST_IN 实现
 * SRC_OUT 与 DST_OUT 相反 上述例子中 PaintEraserSrcOutView 可以将目标图片和源图片相互替换，使用 DST_OUT 实现
 * SRC_ATOP 与 DST_ATOP 相反
 * ******************************************************************************
 * @see PaintPDXWaveDstInView PorterDuff.Mode.DST_IN 实现文字区域波浪效果
 * @see PaintPDXIrregularDstInView PorterDuff.Mode.DST_IN 实现区域 不规则波纹
 *
 * PorterDuff.Mode 模式总结：
 *  1、目标图像与源图像混合，需要生成颜色叠加的特效，则从颜色叠加相关模式选择：ADD（饱和度相加）、DARKEN（变暗）
 *      、LIGHTEN（变亮）、MULTIPLY（正片叠加）、OVERLAY（叠加）、SCREEN（滤色）
 *  2、目标图像与源图像混合，不需要生成颜色叠加特效，而需要根据某张图片的透明像素剪裁时，从 SRC 和 DST 相关模式选择。
 *       SRC 和 DST 相关模式是想通的，唯一不同的是决定当前哪个图像时目标图像，哪个是源图像
 *  3、需要清空图像时，使用 CLEAR 模式
 */

// TODO 使用 AvoidXfermode （AvoidXfermode 找不到该类，此处仅为代码实现）
class PaintAvoidXfermodeView: View {
    val mPaint: Paint = Paint()
    lateinit var mBmp:Bitmap
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mPaint.setColor(Color.RED)
        mBmp = BitmapFactory.decodeResource(context.resources, R.mipmap.dog)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var newW = width/2
        var newH = newW * mBmp.height / mBmp.width
        // 新建图层
        val layerID = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        // 核心绘制代码
        canvas?.drawBitmap(mBmp, null, Rect(0,0,newW,newH), mPaint)
        // 一、设置 Xfermode 选择区域，即清空选定区域的颜色值。模式 TARGET 替换指定颜色
//        mPaint.setXfermode(AvoidXfermode(Color.WHITE, 100, AvoidXfermode.Mode.TARGET))
        // 替换选定区域内的内容
        // 1、把白色小狗替换为红色的小狗，mPaint.setColor(Color.RED)
        canvas?.drawRect(Rect(0,0,newW, newH), mPaint)
        // 2、也可以给白色小狗添加皮肤图片
        canvas?.drawBitmap(BitmapFactory.decodeResource(resources, R.mipmap.flower), null,Rect(0,0,newW,newH),mPaint)
        // 二、设置 Xfermode 选择区域，即清空选定区域的颜色值。模式 AVOID 取指定颜色区域相反的区域
////        mPaint.setXfermode(AvoidXfermode(Color.WHITE, 100, AvoidXfermode.Mode.AVOID))
//        // 替换相反的区域内的内容
//        // 1、把白色小狗周边容差100以上的颜色替换为红色，mPaint.setColor(Color.RED)
//        canvas?.drawRect(Rect(0,0,newW, newH), mPaint)

        // 还原图层
        canvas.restoreToCount(layerID)
    }
}

// TODO PorterDuffXfermode
class PaintPorterDuffXfermodeView: View {
    val mPaint: Paint = Paint()
    lateinit var srcBmp:Bitmap // 源图像
    lateinit var dstBmp:Bitmap // 目标图像
    var srcColor: Int = Color.argb(255,102, 170, 255)
    var dstColor: Int = Color.argb(255,255, 204, 68)
    val w = 200
    val h = 200

    val porterDuffModeStr = arrayOf("CLEAR", "SRC", "DST", "SRC_OVER", "DST_OVER", "SRC_IN",
            "DST_IN", "SRC_OUT", "DST_OUT", "SRC_ATOP", "DST_ATOP", "XOR", "DARKEN", "LIGHTEN",
            "MULTIPLY", "SCREEN", "ADD", "OVERLAY")
    val porterDuffMode = arrayOf(PorterDuff.Mode.CLEAR, PorterDuff.Mode.SRC, PorterDuff.Mode.DST,
            PorterDuff.Mode.SRC_OVER, PorterDuff.Mode.DST_OVER, PorterDuff.Mode.SRC_IN,
            PorterDuff.Mode.DST_IN, PorterDuff.Mode.SRC_OUT, PorterDuff.Mode.DST_OUT,
            PorterDuff.Mode.SRC_ATOP, PorterDuff.Mode.DST_ATOP, PorterDuff.Mode.XOR,
            PorterDuff.Mode.DARKEN, PorterDuff.Mode.LIGHTEN, PorterDuff.Mode.MULTIPLY,
            PorterDuff.Mode.SCREEN, PorterDuff.Mode.ADD, PorterDuff.Mode.OVERLAY)
    var mode = porterDuffMode[0]
        set(value) {
            field = value
            postInvalidate()
        }
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.PaintPorterDuffXfermodeView)
        srcColor = typeArray.getColor(R.styleable.PaintPorterDuffXfermodeView_srcColor, Color.argb(255,102, 170, 255))
        dstColor = typeArray.getColor(R.styleable.PaintPorterDuffXfermodeView_dstColor, Color.argb(255,255, 204, 68))
        typeArray.recycle()
        // 矩形图片
        srcBmp = makeBmp(w, h) { canvas, paint ->
            paint.setColor(srcColor)
            canvas.drawRect(Rect(0,0,w,h),paint)
        }
        // 圆形图片
        dstBmp = makeBmp(w, h){canvas, paint ->
            paint.setColor(dstColor)
            canvas.drawCircle(w/2f,h/2f,w/2f,paint)
        }
    }
    private fun makeBmp(w: Int, h: Int, block:(Canvas, Paint)->Unit): Bitmap {
        val  bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        block(canvas, paint)
        return bmp
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val layerID = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        // 先绘制目标圆形图片
        canvas?.drawBitmap(dstBmp,0f,0f,mPaint)
        mPaint.setXfermode(PorterDuffXfermode(mode))
        canvas?.drawBitmap(srcBmp, w/2f, h/2f, mPaint)
        mPaint.setXfermode(null)
        canvas.restoreToCount(layerID)
        mPaint.setColor(srcColor)
        canvas.drawRect(w+10f,0f,w+50f,50f, mPaint)
        mPaint.setColor(dstColor)
        canvas.drawRect(w+10f,50f,w+50f,100f, mPaint)
    }
}
// TODO PorterDuffXfermode 实现图片变亮效果
class PaintPDXLightenView:View{
    val mPaint: Paint = Paint()
    lateinit var srcBmp:Bitmap // 源图像
    lateinit var dstBmp:Bitmap // 目标图像
    val w = 200
    val h = 200
    // 控制是否开起是否变亮
    var isShowLighten = true
        set(value) {
            field = value
            postInvalidate()
        }
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        srcBmp = BitmapFactory.decodeResource(resources, R.mipmap.book_light)
        dstBmp = BitmapFactory.decodeResource(resources, R.mipmap.book_bg)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val layerID = canvas.saveLayer(0f, 0f, width.toFloat(),height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        // 绘制目标背景图
        canvas.drawBitmap(dstBmp,0f,0f,mPaint)
        if (isShowLighten) {
            mPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.LIGHTEN))
            // 源图片添加
            canvas.drawBitmap(srcBmp, 0f, 0f, mPaint)
        }
        mPaint.setXfermode(null)
        canvas.restoreToCount(layerID)
    }
}

/**
 * TODO 使用 PorterDuff.Mode.MULTIPLY 完成小鸟轮廓图
 */
class PaintTwitterMultiplyView: View {
    val mPaint: Paint = Paint()
    lateinit var srcBmp:Bitmap // 源图像
    lateinit var dstBmp:Bitmap // 目标图像
    // 控制是否开起是否变亮
    var isShowXfermode = true
        set(value) {
            field = value
            postInvalidate()
        }
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        srcBmp = BitmapFactory.decodeResource(resources, R.mipmap.twiter_light)
        dstBmp = BitmapFactory.decodeResource(resources, R.mipmap.twiter_bg)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val layerID = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null,Canvas.ALL_SAVE_FLAG)
        canvas.drawBitmap(dstBmp, 0f, 0f, mPaint)
        // 一方透明则结果就是透明的
        if (isShowXfermode) {
            mPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.MULTIPLY))
            canvas.drawBitmap(srcBmp, 0f, 0f, mPaint)
        }
        mPaint.setXfermode(null)
        canvas.restoreToCount(layerID)
    }
}
// TODO 使用不同的 mode 处理目标图片和原图片
class PaintPDXBasicView:View {
    val mPaint: Paint = Paint()
    lateinit var srcBmp:Bitmap // 源图像
    lateinit var dstBmp:Bitmap // 目标图像
    var pdxMode:PorterDuff.Mode = porterDuffMode[14]
    // 控制是否开起是否变亮
    var isShowXfermode = true
        set(value) {
            field = value
            postInvalidate()
        }
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.PaintPDXBasicView)
        val srcID = typeArray.getResourceId(R.styleable.PaintPDXBasicView_srcBitmap, R.mipmap.twiter_light)
        val dstID = typeArray.getResourceId(R.styleable.PaintPDXBasicView_dstBitmap, R.mipmap.twiter_bg)
        val modeIndex = typeArray.getInt(R.styleable.PaintPDXBasicView_pdx_mode, 0)
        // TypedValue 获取值
//        val typeValue = TypedValue()
//        val isMode = typeArray.getValue(R.styleable.PaintPDXBasicView_pdx_mode, typeValue)
//        pdxMode = porterDuffMode[if (isMode) typeValue.data else 0]
        typeArray.recycle()
        pdxMode = porterDuffMode[modeIndex]
        srcBmp = BitmapFactory.decodeResource(context.resources, srcID)
        dstBmp = BitmapFactory.decodeResource(context.resources, dstID)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val layerID = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null,Canvas.ALL_SAVE_FLAG)
        canvas.drawBitmap(dstBmp, null, Rect(0,0,width,height), mPaint)
        // 一方透明则结果就是透明的
        if (isShowXfermode) {
            mPaint.setXfermode(PorterDuffXfermode(pdxMode))
            canvas.drawBitmap(srcBmp, null, Rect(0,0,width,height), mPaint)
        }
        mPaint.setXfermode(null)
        canvas.restoreToCount(layerID)
    }
}

// TODO PorterDuff.Mode.SRC_IN实现倒影图
class PaintInvertedSrcInView:View{
    val mPaint: Paint = Paint()
    lateinit var srcBmp:Bitmap // 源图像
    lateinit var dstBmp:Bitmap // 目标图像
    lateinit var revertBmp:Bitmap // 还原图像
    var pdxMode:PorterDuff.Mode = porterDuffMode[14]
    // 控制是否开起是否变亮
    var isShowXfermode = true
        set(value) {
            field = value
            postInvalidate()
        }
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        srcBmp = BitmapFactory.decodeResource(context.resources, R.mipmap.dog)
        dstBmp = BitmapFactory.decodeResource(context.resources, R.mipmap.dog_invert_shade)
        // 利用矩阵翻转小狗图片，创建小狗的倒置图，作为倒影的原图像
        val matrix = Matrix()
        matrix.setScale(1f, -1f)
        revertBmp = Bitmap.createBitmap(srcBmp, 0, 0, srcBmp.width, srcBmp.height, matrix, true)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width/2
        val h = w * dstBmp.height/dstBmp.width
        // 画出小狗图
        canvas.drawBitmap(srcBmp,  null, Rect(0,0,w,h), mPaint)
        val layerID = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null,Canvas.ALL_SAVE_FLAG)
        // 画布平移后 在画出小狗的倒影图
        canvas.translate(0f, h.toFloat())
        canvas.drawBitmap(dstBmp, null, Rect(0,0,w,h), mPaint)
        mPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(revertBmp, null, Rect(0,0,w,h), mPaint)
        mPaint.setXfermode(null)
//        canvas.drawBitmap(srcBmp, 0f, 0f, mPaint)
        canvas.restoreToCount(layerID)
    }
}

// TODO PorterDuff.Mode.SRC_OUT 实现橡皮擦效果, 利用目标图片的透明值，控制源图片显示的透明度
class PaintEraserSrcOutView:View {
    val mPaint: Paint = Paint()
    lateinit var dstBmp:Bitmap // 目标图像
    lateinit var srcBmp:Bitmap // 源图像
    lateinit var bgBmp:Bitmap // 底图
    // 绘制手指移动路线
    val dstPath = Path()
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 30f
        srcBmp = BitmapFactory.decodeResource(context.resources, R.mipmap.dog)
        // 目标图片设置一张空白图片
        dstBmp = Bitmap.createBitmap(srcBmp.width, srcBmp.height, Bitmap.Config.ARGB_8888)

        bgBmp = BitmapFactory.decodeResource(context.resources, R.mipmap.guaguaka_text)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bgBmp, null, Rect(0,0,srcBmp.width,srcBmp.height), mPaint)
        val layerID = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null,Canvas.ALL_SAVE_FLAG)
        val dstCanvas = Canvas(dstBmp)
        dstCanvas.drawPath(dstPath, mPaint)
        canvas.drawBitmap(dstBmp, 0f,0f, mPaint)
        mPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_OUT))
        canvas.drawBitmap(srcBmp, 0f,0f, mPaint)
        mPaint.setXfermode(null)

        canvas.restoreToCount(layerID)
    }
    // 记录控制点
    var mPreX = 0f
    var mPreY = 0f
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPreX = event.x
                mPreY = event.y
                dstPath.moveTo(mPreX, mPreY)
                return@onTouchEvent true
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = (mPreX + event.x) / 2
                val endY = (mPreY + event.y) / 2
                dstPath.quadTo(mPreX, mPreY, endX, endY)
                mPreX = event.x
                mPreY = event.y
            }
            MotionEvent.ACTION_UP -> {
                mPreX = 0f
                mPreY = 0f
            }
        }
        postInvalidate()
        return super.onTouchEvent(event)
    }
}
/** TODO PorterDuff.Mode.DST_IN 实现文字区域波浪
 * [Sa*Da, Sa*Dc] 源文件的透明度会影响目标图片的颜色显示
 * 所以此处 SRC 为文字图片（text_shape），DST 为波浪图
 */
class PaintPDXWaveDstInView:View{
    val mPaint: Paint = Paint()
    lateinit var dstBmp:Bitmap // 目标图像
    lateinit var srcBmp:Bitmap // 源图像
    // 绘制波浪路径
    val mPath = Path()
    // 一个完整波浪长度 ~
    val mItemWaveLength = 2000
    var dx = 0f
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mPaint.color = Color.BLUE
        mPaint.style = Paint.Style.FILL_AND_STROKE
        mPaint.strokeWidth = 1f
        srcBmp = BitmapFactory.decodeResource(context.resources, R.mipmap.text_shade)
        // 目标图片设置一张空白图片
        dstBmp = Bitmap.createBitmap(srcBmp.width, srcBmp.height, Bitmap.Config.ARGB_8888)
        startAnim()
    }
    // 波浪动画
    private fun startAnim() {
        val valueAnim = ValueAnimator.ofFloat(0f, mItemWaveLength.toFloat())
        valueAnim.duration = 2000
        valueAnim.repeatCount = ValueAnimator.INFINITE
        valueAnim.interpolator = LinearInterpolator() // 匀速波浪
        valueAnim.addUpdateListener {
            dx = it.getAnimatedValue() as Float
            postInvalidate()
        }
        valueAnim.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制波浪路径
        generageWavePath()
        val dstCanvas = Canvas(dstBmp)
        dstCanvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR)
        dstCanvas.drawPath(mPath, mPaint)

        // 先绘制文字，在绘制效果
        canvas.drawBitmap(srcBmp, 0f, 0f, mPaint)
        val layerID = canvas.saveLayer(0f,0f,width.toFloat(),height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        canvas.drawBitmap(dstBmp, 0f, 0f, mPaint)
        mPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_IN))
        canvas.drawBitmap(srcBmp, 0f, 0f, mPaint)
        mPaint.setXfermode(null)
        canvas.restoreToCount(layerID)
    }

    private fun generageWavePath() {
        mPath.reset()
        val originY = srcBmp.height / 2f
        val halfWaveLen = mItemWaveLength / 2f
        mPath.moveTo(-mItemWaveLength + dx, originY)
        for (i in -mItemWaveLength .. width+mItemWaveLength step mItemWaveLength) {
            mPath.rQuadTo(halfWaveLen/2, -50f, halfWaveLen, 0f)
            mPath.rQuadTo(halfWaveLen/2, 50f, halfWaveLen, 0f)
        }
        mPath.lineTo(srcBmp.width.toFloat(), srcBmp.height.toFloat())
        mPath.lineTo(0f, srcBmp.height.toFloat())
        mPath.close()
    }
}

// TODO PorterDuff.Mode.DST_IN 实现区域 不规则波纹
// [Sa*Da, Sa*Dc] 源文件的透明度会影响目标图片的颜色显示
class PaintPDXIrregularDstInView: View {
    val mPaint: Paint = Paint()
    lateinit var dstBmp:Bitmap // 目标图像
    lateinit var srcBmp:Bitmap // 源图像
    // 一个完整波浪长度 ~
    var mItemWaveLength = 0
    var dx = 0f
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mPaint.color = Color.BLUE
        mPaint.style = Paint.Style.FILL_AND_STROKE
        mPaint.strokeWidth = 1f
        // 如果 src 图片的透明度不为0 则该部分会显示dst图片
        dstBmp = BitmapFactory.decodeResource(context.resources, R.mipmap.wave_bg)
        srcBmp = BitmapFactory.decodeResource(context.resources, R.mipmap.circle_shape)

        mItemWaveLength = dstBmp.width
        startAnim()
    }

    private fun startAnim() {
        val valueAnim = ValueAnimator.ofFloat(0f, mItemWaveLength.toFloat())
        valueAnim.repeatCount = ValueAnimator.INFINITE
        valueAnim.duration = 4000
        valueAnim.interpolator = LinearInterpolator()
        valueAnim.addUpdateListener {
            dx = it.getAnimatedValue() as Float
            postInvalidate()
        }
        valueAnim.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(srcBmp, 0f, 0f, mPaint)
        val layerID = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(),null, Canvas.ALL_SAVE_FLAG)
        canvas.drawBitmap(dstBmp, Rect(dx.toInt(), 0, dx.toInt() + srcBmp.width, srcBmp.height)
                , Rect(0,0,srcBmp.width,srcBmp.height), mPaint)
        mPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_IN))
        canvas.drawBitmap(srcBmp,0f,0f, mPaint)
        mPaint.setXfermode(null)

        canvas.restoreToCount(layerID)
    }
}

val porterDuffModeStr = arrayOf("CLEAR", "SRC", "DST", "SRC_OVER", "DST_OVER", "SRC_IN",
        "DST_IN", "SRC_OUT", "DST_OUT", "SRC_ATOP", "DST_ATOP", "XOR", "DARKEN", "LIGHTEN",
        "MULTIPLY", "SCREEN", "ADD", "OVERLAY")
val porterDuffMode = arrayOf(PorterDuff.Mode.CLEAR, PorterDuff.Mode.SRC, PorterDuff.Mode.DST,
        PorterDuff.Mode.SRC_OVER, PorterDuff.Mode.DST_OVER, PorterDuff.Mode.SRC_IN,
        PorterDuff.Mode.DST_IN, PorterDuff.Mode.SRC_OUT, PorterDuff.Mode.DST_OUT,
        PorterDuff.Mode.SRC_ATOP, PorterDuff.Mode.DST_ATOP, PorterDuff.Mode.XOR,
        PorterDuff.Mode.DARKEN, PorterDuff.Mode.LIGHTEN, PorterDuff.Mode.MULTIPLY,
        PorterDuff.Mode.SCREEN, PorterDuff.Mode.ADD, PorterDuff.Mode.OVERLAY)

fun search(modeStr:String): PorterDuff.Mode{
    porterDuffModeStr.find { it.equals(modeStr) }
    val i = porterDuffModeStr.binarySearch(modeStr)
    if (i >= 0){
        return porterDuffMode[i]
    }
    return porterDuffMode[0]
}