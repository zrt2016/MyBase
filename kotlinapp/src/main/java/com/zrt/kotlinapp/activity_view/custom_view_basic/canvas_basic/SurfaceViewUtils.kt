package com.zrt.kotlinapp.activity_view.custom_view_basic.canvas_basic

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.zrt.kotlinapp.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * @author：Zrt
 * @date: 2023/5/5
 *
 * 一、SurfaceView：
 *      1、android屏幕刷新时间间隔是16ms，超过16ms就会出现界面卡顿的情况和警告。一般卡顿原因在绘制过程中不止执行
 *        了绘制操作，还夹杂了很多逻辑处理。导致在指定的16ms内未完成绘制。
 *         警告：skipped 60 frames! The application may be doing too much work on its main thread
 *          （翻译：跳过了60帧！应用程序可能在其主线程上做了太多工作）
 *      2、因此引入了 SurfaceView，在2个方面改造了View 的绘图操作：
 *             ① 使用双缓冲技术：即新增了一块缓冲画布，当需要执行绘图操作时，先在缓冲画布上绘制，绘制好后直接将缓冲
 *          画布上的内容更新到主画布上，就不会存在逻辑处理时间的问题，也就解决了超时绘制的问题。
 *             ② 自带画布，支持在子线程中更新画布内容：内部存在自带的 Canvas（即缓冲 Canvas），支持在子线程中更新
 *          Canvas中的内容。
 *      3、SurfaceView 缺点：事件同步，例：多次触摸屏幕，SurfaceView就会调用多个线程处理，线程过多是就需要使用
 *        线程队列保存触摸时间，一个个进行处理，因此会稍显复杂，在该场景处理下不如普通的View。
 *      4、View 和 SurfaceView 各自应用场景：
 *             ① 界面需要被动更新时，使用 View 较好。例如：与手势交互的场景，因为画面依赖于 onTouch 完成，所以直接
 *         使用invalidate()更新。
 *             ② 界面需要主动更新时，使用 SurfaceView 较好。例如一个物品一直在移动，这就需要一个单独的线程不停
 *         重绘其状态,避免阻塞主线程。
 *             ③ 当界面绘制需要频繁刷新，或者刷新时数据处理量比较大时，就可以使用 SurfaceView来实现，例如视频播放及Camera。
 *       5、基本用法
 *  @see GesturePathSurfaceView 使用 SurfaceView完成手势绘制。
 *  @see com.zrt.kotlinapp.activity_view.custom_view_basic.bezier_basic.BezierGestureTrackView 参考该类完成
 *            1）setWillNotDraw(boolean willNotDraw)：主要用于 View 派生子类的初始化中。
 *         即让控件显示告诉系统，在重绘屏幕时，当前哪些控件需要绘制，哪些不需要。
 *              willNotDraw = true ： 表示控件没有绘制内容，屏幕重绘时，该控件不需要绘制。
 *              willNotDraw = false ：表示空间每次在重绘时，都需要绘制该控件
 *            2）getHolder().lockCanvas()：获取 SurfaceView 自带的缓冲画布，并加锁，防止其他线程更改。
 *         如果有其他线程获取时，会返回null，需要判空处理。
 *            3）getHolder().lockCanvas(Rect):获取指定区域的画布，画布以外的区域会保持与屏幕内容一致，画布以内的区域保持原画布内容
 *            4）getHolder().unlockCanvasAndPost(lockCanvas):将缓冲画布释放，并将所画内容更新到主线程的画布上。
 *       6）Surface的声明周期：
 *            SurfaceView缓冲画布分为三个概念：Surface（Model）、SurfaceView（View）、SurfaceHolder（Controller）
 *         即典型的 MVC 模式，Surface 保存缓冲画布和绘图内容相关的信息。SurfaceView负责视图交互。SurfaceHolder 负责
 *         操作 Surface 中的数据。（Surface 中的数据是不允许直接用来操作的）
 *            A、getHolder() 获取 SurfaceHolder
 *            B、addCallback(new SurfaceHolder.Callback(){}) 对 Surface 的生命周期进行监听，只有在 Surface 存在时，
 *         才能获取到 surface，才能操作缓存 Canvas，否则会出现获取到的 Canvas=null的情况。
 *                ① surfaceCreated(SurfaceHolder)：Surface 对象创建时，该函数会被调用
 *                ② surfaceChanged(SurfaceHolder)：Surface 发生结构性变化（格式或大小），该函数会被立即调用
 *                ③ surfaceDestroyed(SurfaceHolder)：Surface 对象要销毁时，会被立即调用
 *  @see AnimaSurfaceView 使用  SurfaceView 实现动态左右移动的背景图
 *  @see DoubleBufferSurfaceView 双缓冲技术，多个缓存画布
 *  @see RectRefreshSurfaceView 双缓存技术局部更新原理：lockCanvas(Rect)
 */


/*
 * TODO 使用 SurfaceView 实现手势绘制
 * 可参考 BezierGestureTrackView
 *
 */
class GesturePathSurfaceView : SurfaceView {
    lateinit var mPaint: Paint
    lateinit var mPath: Path
    var mPreX = 0f
    var mPreY = 0f
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        // 显示告诉系统，当前控件需要绘制
        setWillNotDraw(false)
        mPaint = Paint()
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 5f
        mPaint.color = Color.RED
        mPath = Path()
        holder.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceCreated(holder: SurfaceHolder) {

            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {

            }
        })
    }

//    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
//        canvas?.drawPath(mPath, mPaint)
//    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPreX = x
                mPreY = y
                mPath.moveTo(x, y)
                return@onTouchEvent true
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = (mPreX + x) / 2
                val endY = (mPreY + y) / 2
                mPath.quadTo(mPreX, mPreY, endX, endY)
                mPreX = x
                mPreY = y
//                postInvalidate()
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        drawCanvas()
        return super.onTouchEvent(event)
    }

    private fun drawCanvas() {
        // 2023-04-24 待修改至子线程中运行
        // 2023-04-25 使用协程
        GlobalScope.launch {
            val surfaceHolder: SurfaceHolder = holder
            val lockCanvas = surfaceHolder.lockCanvas()
            lockCanvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            lockCanvas?.drawPath(mPath, mPaint)
            surfaceHolder.unlockCanvasAndPost(lockCanvas)
        }
//        thread {
//            val surfaceHolder:SurfaceHolder = holder
//            val lockCanvas = surfaceHolder.lockCanvas()
//            lockCanvas?.drawPath(mPath, mPaint)
//            surfaceHolder.unlockCanvasAndPost(lockCanvas)
//        }
    }
}
// TODO 使用  SurfaceView 实现动态左右移动的背景图
class AnimaSurfaceView: SurfaceView {
    lateinit var bitmapBg: Bitmap
    var mSWidth = 0
    var mSHeight = 0
    var flag:Boolean = false
    var mCanvas: Canvas? = null
    var mBitPosX = 0 //开始绘制图片X的坐标
    val BITMAP_STEP = 1 // 背景画布移动步伐
    var state:State = State.LEFT // 默认背景图向左滑动
    enum class State{
        LEFT, RIGHT
    }
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                flag = true
                startAnimation()
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                flag = false
            }
        })
    }
    fun startAnimation() {
        mSWidth = width
        mSHeight = height
        val mWidth = mSWidth * 3 / 2
        val bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.scenery)
        // 将图片宽度缩放至屏幕的3/2倍，高度充满屏幕
        bitmapBg = Bitmap.createScaledBitmap(bitmap, mWidth, mSHeight, true)
        GlobalScope.launch {
            while (flag) {
                // 加锁
                mCanvas = holder.lockCanvas()
                drawView()
                holder.unlockCanvasAndPost(mCanvas)
                delay(50) // 延时 50ms 运行
            }
        }
    }

    private fun drawView() {
        mCanvas?.let {
            it.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR) //清空屏幕
            it.drawBitmap(bitmapBg, mBitPosX.toFloat(), 0f, null)
            when(state) {
                State.LEFT -> {
                    mBitPosX -= 1 // 起始点向左平移
                }
                State.RIGHT -> {
                    mBitPosX += 1 // 起始点向右平移
                }
            }
            if (mBitPosX <= - mSWidth / 2) {
                state = State.RIGHT
            }
            if (mBitPosX >= 0) {
                state = State.LEFT
            }
        }
    }
}
// TODO 双缓存技术 缓冲画布数量是根据需求动态分配的
class DoubleBufferSurfaceView: SurfaceView {
    lateinit var mPaint: Paint
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mPaint = Paint()
        mPaint.color = Color.RED
        mPaint.textSize = 30f
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                drawText(holder)
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
            }
        })
    }
    val mInts = mutableListOf<Int>()
    private fun drawText(holder: SurfaceHolder) {
        // 一、当前显示：0、3、6、9 （3个画布，A、B、C，此时显示的A画布的内容）
        // 原因：存在多个Canvas缓存画布，导致绘制多个画布绘制的内容不一致，最后只显示其中一个画布的内容
        // 多个画布时，会交替显示在屏幕上。
        // 例：A 和 B 两个画布，目前A是屏幕画布，那么第一次获取的缓存画布则是 B，在更新后会将B画布更新到屏幕上。
        // 再次获取缓存画布则会获取到 A 画布，因此会导致A画布和B画布的内容不一致
        // 画布A：0、3、6、9；画布B：1、4、7、10；画布C：2、5、8
//        for (i in 0 until 10) {
//            val canvas = holder.lockCanvas()
//            canvas?.drawText(i.toString(), i*30f, 50f, mPaint)
//            holder.unlockCanvasAndPost(canvas)
//        }
        // 二、使用线程绘制，每次绘制完等待800ms，可查看每块画布的内容
//        GlobalScope.launch {
//            for (i in 0 until 10) {
//                val canvas = holder.lockCanvas()
//                canvas?.drawText(i.toString(), i*30f, 100f, mPaint)
//                holder.unlockCanvasAndPost(canvas)
//                delay(800)
//            }
//        }
        // 三、解决方案1：将要绘制的数据一次画出
//        GlobalScope.launch {
//            val canvas = holder.lockCanvas()
//            for (i in 0 until 10) {
//                canvas?.drawText(i.toString(), i*30f, 50f, mPaint)
//            }
//            holder.unlockCanvasAndPost(canvas)
//        }
        // 四、解决方案2：将要绘制的数据缓存，然后一次绘制
        GlobalScope.launch {
            for (i in 0 until 10) {
                val canvas = holder.lockCanvas()
                mInts.add(i)
                for ((index, value) in mInts.withIndex()) {
                    canvas?.drawText(value.toString(), index * 30f, 50f, mPaint)
                }
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }
}
/**
 * TODO 双缓存技术局部更新原理：lockCanvas(Rect)
 * 注：在ScrollView 中存在显示缓存Canvas显示异常的情况
 */
class RectRefreshSurfaceView:SurfaceView {
    lateinit var mPaint: Paint
    var flag = false
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mPaint = Paint()
        mPaint.color = Color.argb(0x1F, 0xFF, 0xFF, 0xFF)
        mPaint.textSize = 30f
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                flag = true
                drawText(holder)
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                flag = false
            }
        })
    }

    private fun drawText(holder: SurfaceHolder) {
        GlobalScope.launch {
            // 先进行清屏操作
            while (true) {
                val rect = Rect(0, 0, 1, 1)
                val canvas = holder.lockCanvas(rect)
                val canvasRect = canvas.clipBounds
                Log.i(">>>>", "##left=${canvasRect.left}；top=${canvasRect.top}；right=${canvasRect.right}；bottom=${canvasRect.bottom}")
                if (width == canvasRect.width() && height == canvasRect.height()) {
                    // 1、默认获取的是这块画布，将整块画布绘制完成后，才会按指定区域返回画布大小
                    canvas.drawColor(Color.BLACK)
                    holder.unlockCanvasAndPost(canvas)
                }else {
                    // 2、此时返回指定区域大小
                    holder.unlockCanvasAndPost(canvas)
                    break
                }
            }
            // 方式一： 画图 在ScrollView中会出现不同显示
            for (i in 0 .. 9) {
                when(i) {
                    0 -> {
                        // 画大方
                        val canvas = holder.lockCanvas(Rect(10, 10, 600, 600))
                        canvas.drawColor(Color.RED)
                        holder.unlockCanvasAndPost(canvas)
                    }
                    1 -> {
                        // 画中方
                        val canvas = holder.lockCanvas(Rect(50, 50, 550, 550))
                        canvas.drawColor(Color.GREEN)
                        holder.unlockCanvasAndPost(canvas)
                    }
                    2 -> {
                        // 画小方
                        val canvas = holder.lockCanvas(Rect(100, 100, 500, 500))
                        canvas.drawColor(Color.BLUE)
                        holder.unlockCanvasAndPost(canvas)
                    }
                    3 -> {
                        // 画圆形
                        val canvas = holder.lockCanvas(Rect(150, 150, 450, 450))
                        mPaint.color = Color.argb(0x3F, 0xFF, 0xFF, 0xFF)
                        canvas.drawCircle(300f, 300f, 100f, mPaint)
                        holder.unlockCanvasAndPost(canvas)
                    }
                    4 -> {
                        // 写数字
                        val canvas = holder.lockCanvas(Rect(250, 250, 350, 350))
                        mPaint.color = Color.RED
                        canvas.drawText(i.toString(), 300f, 300f, mPaint)
                        holder.unlockCanvasAndPost(canvas)
                    }
                }
                delay(800)
            }
        }
    }
}

/**
 * Created by Administrator on 2016/3/18.
 */
class CaremaSurfaView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {
    private val mSurfaceHolder: SurfaceHolder
    private var camera: android.hardware.Camera? = null
    override fun surfaceCreated(holder: SurfaceHolder) {
        camera = android.hardware.Camera.open(0)
        try {
            //将摄像头捕获的画面透过Holder对象绘制到SurfaceView中
            camera?.setPreviewDisplay(holder)
            //设置角度
            camera?.setDisplayOrientation(90)
            //开始预览（打开摄像头）
            camera?.startPreview()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    override fun surfaceDestroyed(holder: SurfaceHolder) {}

    init {
        mSurfaceHolder = holder
        mSurfaceHolder.addCallback(this)
    }
}