package com.zrt.kotlinapp.activity_view.custom_view_basic.view_basic

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Scroller
import android.widget.TextView
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.learnkotlin.log

/**
 * @author：Zrt
 * @date: 2022/9/12
 *
 * android 左边原点在屏幕左上方，X轴正方向向右，Y轴正方向向下
 * 一、View 的坐标系
 *   1、View获取自身的宽和高
 *     a、width = getRight() - getLeft()
 *     b、height = getBottom() - getTop()
 *   2、View自身的坐标：View到其父控件的距离
 *     a、getTop() ：View自身顶部到其父布局顶部的距离
 *     b、getLeft() ：View自身左边到其父布局左边的距离
 *     c、getRight() ：View自身右边到其父布局左边的距离
 *     d、getBottom() ：View自身底边到其父布局顶部的距离
 *   3、MotionEvent提供获取焦点坐标的各种方法
 *     a、获取点击事件在屏幕中的坐标（getRowX(), getRowY()）
 *     b、获取点击事件在View控件中的坐标（getX(), getY()）
 *
 * 二、View的滑动：当点击时间传到View时，系统记下触摸点坐标，手指移动时，系统记下移动后触摸的坐标并算出偏移量，
 *      通过偏移量改变View的位置。以下有6种滑动方法：layout()、offsetLeftAndRight()、offsetTopAndBottom()、
 *      layoutParams、动画、scrollTo与scrollBy以及Scroller
 *   Scroller:不会对view进行滑动，而是计算动画时间内，View移动的距离，通过invalidate()重绘View，调用draw(),
 *      触发computeScroll()，在该方法内进行滑动
 *   @see ScrollLayoutView
 *
 * 三、自定义View分为三种：自定义View、自定义ViewGroup 和 自定义组合控件。
 *     a、自定义View又被分为继承系统控件（例：TextView）和继承View 两种
 *     b、自定义ViewGroup同样被分为继承系统特定的ViewGroup（例RelativeLayout）和继承ViewGroup
 *  不同例子：
 *  @see InvalidTextView 继承系统控件TextView的 InvalidTextView。添加红色横线。
 *  @see RectView 添加自定义属性：在values目录下创建attrs.xml
 *  @see TitleBar  自定义组合控件：自定义一个顶部标题栏
 *  @see HorizontalView 自定义ViewGroup，横向滑动View布局
 * 四、View的工作流程
 *   @see WorkView
 *      工作流程指：measure、layout和draw。measure测量View的宽高，layout确定View的位置，draw绘制View。
 *      DecorView的创建：调用startActivity，最终会调用ActivityThread的handleLaunchActivity方法创建Activity
 * 五、自定义属性标签：declare-styleable
 *   attrs.xml该文件下的 name 属性名不能有重复，否则会编译报错
 *      1. reference：参考某一资源ID。例：android:background = "@drawable/图片ID" @color/xxx等
 *      2. color：颜色值。例：android:textColor = "#00FF00"
 *      3. boolean：布尔值。例：android:focusable = "true"
 *      4. dimension：尺寸值。例：android:layout_height = "42dip"
 *      5. float：浮点值。例：android:toAlpha = "0.7"
 *      6. integer：整型值。例：android:frameDuration = "100"
 *      7. string：字符串。例：android:apiKey = "0jOkQ"
 *      8. fraction：百分数。例： android:pivotX = "200%"
 *      9. enum：枚举值。例： android:orientation = "vertical"
 *          <declare-styleable name="名称">
 *              <attr name="orientation">
 *                  <enum name="horizontal" value="0" />
 *                  <enum name="vertical" value="1" />
 *              </attr>
 *          </declare-styleable>
 *      10. flag：位或运算。例： android:windowSoftInputMode = "stateUnspecified | stateUnchanged"
 *      自己定义，类似于android:gravity="top"
 *          <attr name="age">
 *              <flag name="child" value="10"/>
 *              <flag name="young" value="18"/>
 *              <flag name="old" value="60"/>
 *          </attr>
 *      属性定义：支持多种类型定义，例：<attr name = "background" format = "reference|color" /> android:background = "@drawable/图片ID|#00FF00"
 * 六、自定义属性集导入：
 *      在根布局添加：
 *          方式一：xmlns:app="http://schemas.android.com/apk/res/com.harvic.com.trydeclarestyle"
 *          方式二：xmlns:app="http://schemas.android.com/apk/res-auto"
 *      xmlns:app:这里的app是自定义的，但是在定义xml控件属性时，也要通过该标识符访问
 *          例：app:header=""
 * 七、代码中获取自定义属性的值：
 *      通过 TypedArray 获取某个属性值的，使用完后必须调用TypedArray类的 recycle()函数释放资源
 *          getInt(@StyleableRes int index, int defValue)
 *          getDimension(@StyleableRes int index, float defValue) ：尺寸值
 *          getBoolean(@StyleableRes int index, boolean defValue)
 *          getColor(@StyleableRes int index, @ColorInt int defValue)：获取颜色值
 *          getString(@StyleableRes int index)
 *          getDrawable(@StyleableRes int index)
 *          getResources()
 *          参数一：@StyleableRes int index，通过获取attrs资源文件中attr属性的name 。
 *              例：R.styleable.PaintTextBasicView_textPWith
 *          参数二：float defValue则为默认值
 * 八、ViewGroup的绘制流程
 *  @see BasicViewGroup
 *    1、三步：测量（onMeasure()）、布局（onLayout()）、绘制（onDraw()）
 *      onMeasure():测量当前控件大小，为正式布局提供建议（只建议，是否使用由onLayout()函数决定）
 *      onLayout()：使用layout函数对所有子控件进行布局
 *      onDraw():根据布局的位置绘图
 *    2、onMeasure()中的测量模式 MeasureSpec
 *    3、getMeasuredWidth() 和 getWidth() 的区别，getMeasuredWidth()在 onMeasure() 后就可以获取宽度值
 *          ，getWidth() 是在 onLayout() 后才能获取到值
 *          getMeasuredWidth()的值：通过 setMeasuredDimension 设置
 *          getWidth()的值通过 layout(l,t,r,b) 设置
 *   @see BasicViewGroup 使用测量模式，计算View高宽，竖向添加VIew
 *   @see com.zrt.kotlinapp.tools.MyMeasureSpec MeasureSpec测量模式可参考该类
 *
 * 九、GestureDetector 手势检测：
 *    1、GestureDetector 对外提供了两个接口和一个外部类
 *      接口：OnGestureListener 和 OnDoubleTapListener
 *      外部类：SimpleOnGestureListener 该外部类包含了2个接口的所有必须实现的函数，且均已重写。该类为静态类，
 *          可在外部继承该类，重写里面的手势处理函数、
 *    2、GestureDetector.OnGestureListener
 *  @see MyGestureListener 实现 OnGestureListener
 *      ① 创建实例：
 *          GestureDetector(OnGestureListener)
 *          GestureDetector(Context, OnGestureListener)
 *          GestureDetector(Context, GestureDetector.SimpleOnGestureListener())
 *      ② 在 onTouch(View v, MotionEvent event) 中进行拦截
 *          return mGestureDetector.onTouchEvent(event)
 *      ③ 绑定控件
 *          textView.setOnTouchListener(this)
 *    3、GestureDetector.OnDoubleTapListener
 *  @see MyOnDoubleTapListener 实现 OnDoubleTapListener
 *      ① 创建实例：
 *          方式一：GestureDetector(OnGestureListener) 在构造函数中创建时，需注意实现
 *              OnDoubleTapListener 接口时，需要同时实现 OnGestureListener 接口，否则会报错
 *          方式二：GestureDetector(OnGestureListener).setOnDoubleTapListener(OnDoubleTapListener())
 */
/* TODO
 * ScrollView
 * layout() 方法滑动
 * 让view跟随者我们的手指进行滑动
 */
class ScrollLayoutView : View {
    var lastX = 0f
    var lastY = 0f
    var mScroller:Scroller
    constructor(context: Context):this(context, null)
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){
        mScroller = Scroller(context)
    }
    //点击事件
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.getX()
        val y = event.getY()
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                lastX = x
                lastY = y
            }
            MotionEvent.ACTION_MOVE -> {
                // 计算移动距离
                var offsetX = x - lastX
                var offsetY = y - lastY
                // 1、调用layout方法重新放置其位置
//                layout((left+offsetX).toInt(), (top+offsetY).toInt(),
//                        (right+offsetX).toInt(), (bottom+offsetY).toInt())
                // 2、offsetLeftAndRight()、offsetTopAndBottom()
//                offsetLeftAndRight(offsetX.toInt()) // 偏移X轴位置
//                offsetTopAndBottom(offsetY.toInt()) // 偏移Y轴位置
                // 2、layoutParams
//                val layoutParams = layoutParams as LinearLayout.LayoutParams
//                layoutParams.leftMargin = (left + offsetX).toInt()
//                layoutParams.topMargin = (top + offsetY).toInt()
//                setLayoutParams(layoutParams)
                // 3、scrollBy：移动偏移量。scrollTo:移动的具体坐标点
                // 4、手机屏幕进行平移，画布不动，因此要添加-
                (parent as View).scrollBy((-offsetX).toInt(), (-offsetY).toInt())
            }
        }
        return true
    }
    // 给scrollBy滑动添加一个滑动效果
    // 2000ms内沿着X轴平移delta像素
    fun smoothScrollTo(destX: Int, destY: Int){
        val scrollX = scrollX
        val delta = destX - scrollX
        //2000秒内滑向destX
        mScroller.startScroll(scrollX, 0, delta, 0, 2000)
        // invalidate会对view进行重绘，重绘就会调用draw()方法，而draw方法又会调用computeScroll()方法
        // 我们重写computeScroll方法
        invalidate()
    }
    // 重写该方法与Scroller配合使用，完成弹性滑动的效果
    override fun computeScroll() {
        super.computeScroll()
        // 返回true，说明动画还未结束，则继续滑动
        if (mScroller.computeScrollOffset()){
            // 调用scrollTo完成滑动
            (parent as View).scrollTo(mScroller.currX, mScroller.currY)
            //然后继续触发重绘，不断的调用computeScroll方法
            invalidate()
        }
    }
}

/**
 * View的工作流程
 * 工作流程指：measure、layout和draw。measure测量View的宽高，layout确定View的位置，draw绘制View。
 *
 * DecorView的创建：调用startActivity，最终会调用ActivityThread的handleLaunchActivity方法创建Activity
 * */
class WorkView: View{
    constructor(context: Context):this(context, null)
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun layout(l: Int, t: Int, r: Int, b: Int) {
        super.layout(l, t, r, b)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
    }
}

/** TODO
 * 继承系统控件TextView 的InvalidTextView
 * 添加红色横线
 * 单继承TextView会报错，提示：This custom view should extend androidx.appcompat.widget.AppCompatTextView instead
 * 添加@SuppressLint("AppCompatCustomView")
 */
@SuppressLint("AppCompatCustomView")
class InvalidTextView: TextView{
    // 设置画笔
    val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    constructor(context: Context):this(context, null)
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){
        initDraw()
    }

    private fun initDraw() {
        mPaint.setColor(Color.RED) // 设置画笔颜色
        mPaint.strokeWidth = 1.5f  // 设置画笔宽度
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        val height = height
        val width = width
        // 画线
        canvas?.drawLine(0f, (height / 2).toFloat(), width.toFloat(), (height / 2).toFloat(), mPaint)
    }
}

/** TODO
 * 继承View的 RectView
 * 添加自定义属性：在values目录下创建attrs.xml
 * 画一个矩形
 */
class RectView: View{
    // 设置画笔
    val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var mColor= Color.RED
    constructor(context: Context):this(context, null)
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){
        // 获取自定义属性：
        val mTypeArray = context.obtainStyledAttributes(attrs, R.styleable.RectView)
        // 提前RectView属性计划的rect_color属性，如果没设置，采用默认值Color.RED
        mColor = mTypeArray.getColor(R.styleable.RectView_rect_color, Color.RED)
        // 资源获取完成后进行回收
        mTypeArray.recycle()
        initDraw()
    }
    private fun initDraw() {
        mPaint.setColor(mColor) // 设置画笔颜色
        mPaint.strokeWidth = 1.5f  // 设置画笔宽度
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 设置该View宽度wrap_content与match_patient的效果保持一致
        // 获取宽高设置模式
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        // 获取宽高值
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        when(widthSpecMode){
            MeasureSpec.AT_MOST -> log("##width mode=AT_MOST")
            MeasureSpec.EXACTLY -> log("##width mode=EXACTLY")
            else -> log("##width mode=UNSPECIFIED")
        }
        when(heightSpecMode){
            MeasureSpec.AT_MOST -> log("##height mode=AT_MOST")
            MeasureSpec.EXACTLY -> log("##height mode=EXACTLY")
            else -> log("##height mode=UNSPECIFIED")
        }
        Log.i(">>>>", "##width mode=${widthSpecMode == MeasureSpec.AT_MOST}, height mode=${heightSpecMode == MeasureSpec.AT_MOST}")
        Log.i(">>>>", "##width size=${widthSpecSize}, height size=${heightSpecSize}")
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(100, 100)
        }else if (widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(100, heightSpecSize)
        }else if (heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize, 100)
        }else {
            setMeasuredDimension(widthSpecSize, heightSpecSize)
        }
    }
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        // 添加padding
        val width = width - paddingLeft - paddingRight
        val height = height - paddingTop - paddingBottom
        // 画矩形
        canvas?.drawRect(0f + paddingLeft, 0f + paddingTop,
                width.toFloat() + paddingLeft, height.toFloat() + paddingTop, mPaint)
    }
}

/** TODO
 * 自定义组合控件：TitleBar
 * 自定义一个顶部标题栏
 */
class TitleBar: RelativeLayout{
    lateinit var layout_titlebar_rootlayout: RelativeLayout
    lateinit var iv_titlebar_left: ImageView
    lateinit var tv_titlebar_title: TextView
    lateinit var iv_titlebar_right: ImageView
    var mColor= Color.BLUE
    var mTextColor= Color.WHITE
    var mTitleName:String?
    constructor(context: Context):this(context, null)
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){
        // 获取自定义属性：
        val mTypeArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar)
        // 修改背景色
        mColor = mTypeArray.getColor(R.styleable.TitleBar_title_bg, Color.BLUE)
        mTitleName = mTypeArray.getString(R.styleable.TitleBar_title_text)
        // 修改title字体颜色
        mTextColor = mTypeArray.getColor(R.styleable.TitleBar_title_text_color, Color.WHITE)
        // 资源获取完成后进行回收
        mTypeArray.recycle()
        initView(context)
    }
    private fun initView(context: Context) {
        // 加载组合控件至该View布局
        LayoutInflater.from(context).inflate(R.layout.view_custom_title, this, true)
        iv_titlebar_left = findViewById(R.id.iv_titlebar_left)
        tv_titlebar_title = findViewById(R.id.tv_titlebar_title)
        iv_titlebar_right = findViewById(R.id.iv_titlebar_right)
        layout_titlebar_rootlayout = findViewById(R.id.layout_titlebar_rootlayout)
        // 设置背景色
        layout_titlebar_rootlayout.setBackgroundColor(mColor)
        // 设置标题文字颜色
        tv_titlebar_title.setTextColor(mTextColor)
        setTitle(mTitleName)
    }

    fun setTitle(titleName: String?){
        if (!titleName.isNullOrEmpty()){
            tv_titlebar_title.text = titleName
        }
    }
    fun setLeftListener(onClickListener: OnClickListener){
        iv_titlebar_left.setOnClickListener(onClickListener)
    }
    fun setRightListener(onClickListener: () -> Unit){
        iv_titlebar_right.setOnClickListener{
            onClickListener()
        }
    }
}

/** TODO
 * 横向滑动View布局
 */
class HorizontalView: ViewGroup{
    var currentIndex = 0
    private var childWidth = 0
    var lastX:Int = 0
    var lastY:Int = 0
    private var lastInterceptX = 0
    private var lastInterceptY = 0
    lateinit var mScroller:Scroller
    // 测试滑动速度
    lateinit var tracker: VelocityTracker
    constructor(context: Context):this(context, null)
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){
        init()
    }
    private fun init() {
        mScroller = Scroller(context)
        tracker = VelocityTracker.obtain()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 获取宽高设置模式
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        // 获取宽高值
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        // 测量子View
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        if (childCount == 0){
            setMeasuredDimension(0, 0)
        }else if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            var sumWidth = 0 // 所有自View宽度和
            var maxHeight = 0; // 取子view中高度最大的
            for (i in 0 until childCount){
                val childAt = getChildAt(i)
                val measuredWidth = childAt.measuredWidth
                val measuredHeight = childAt.measuredHeight
                sumWidth += measuredWidth
                if (measuredHeight > maxHeight)
                    maxHeight = measuredHeight
            }
            setMeasuredDimension(sumWidth, maxHeight)
        }else if (widthSpecMode == MeasureSpec.AT_MOST){
            var sumWidth = 0 // 所有自View宽度和
            for (i in 0 until childCount){
                val childAt = getChildAt(i)
                val measuredWidth = childAt.measuredWidth
                sumWidth += measuredWidth
            }
            setMeasuredDimension(sumWidth, heightSpecSize)
        }else if (heightSpecMode == MeasureSpec.AT_MOST){
            var maxHeight = 0; // 取子view中高度最大的
            for (i in 0 until childCount){
                val childAt = getChildAt(i)
                val measuredHeight = childAt.measuredHeight
                if (measuredHeight > maxHeight)
                    maxHeight = measuredHeight
            }
            setMeasuredDimension(widthSpecSize, maxHeight)
        }
//        else {
//            setMeasuredDimension(widthSpecSize, heightSpecSize)
//        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childCount = childCount
        var left = 0
        for (i in 0 until childCount){
            val childAt = getChildAt(i)
            if (childAt.visibility != View.GONE){
                var width = childAt.measuredWidth
                childWidth = width
                childAt.layout(left, 0, left + width, childAt.measuredHeight)
                left += width
            }
        }
    }

    //处理滑动冲突事件
    // 返回false继续传递给子View处理，true：传递给自己的OnTouch处理
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var intercep:Boolean = false;
        val x: Float = ev?.getX() ?: 0f
        val y: Float = ev?.getY() ?: 0f
        when (ev?.action){
            MotionEvent.ACTION_DOWN -> {
                intercep = false
                // 再次触摸。如果上次滑动未执行完毕，则中断
                if (!mScroller.isFinished){
                    mScroller.abortAnimation()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - lastInterceptX
                val deltaY = y - lastInterceptY
                // x轴平移距离大于y轴平移距离，说明是横向滑动，则交由自己的onTouch事件处理
                if ((Math.abs(deltaX) - Math.abs(deltaY)) > 0) {
                    intercep = true
                }else{
                    intercep = false
                }
            }
            MotionEvent.ACTION_UP -> {
                intercep = false
            }
        }
        lastX = x.toInt()
        lastY = y.toInt()
        lastInterceptX = x.toInt()
        lastInterceptY = y.toInt()
        return intercep
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        tracker.addMovement(event)
        val x: Float = event?.getX() ?: 0f
        val y: Float = event?.getY() ?: 0f
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                // 再次触摸。如果上次滑动未执行完毕，则中断
                if (!mScroller.isFinished){
                    mScroller.abortAnimation()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - lastX
                scrollBy((-deltaX).toInt(), 0)
            }
            MotionEvent.ACTION_UP -> {
                val distance = scrollX - getLeftWidth(currentIndex)
                log("## ontouch 1 distance=$distance , getLeftWidth(currentIndex)=${getLeftWidth(currentIndex)}")
                val childAt = getChildAt(currentIndex)
                log("## ontouch childAt.measuredWidth=${childAt.measuredWidth},currentIndex=$currentIndex")
                if (Math.abs(distance) > childAt.measuredWidth/2){
                    if (distance > 0){
                        currentIndex++
                    }else {
                        currentIndex--
                    }
                }else{
                    tracker.computeCurrentVelocity(1000)
                    val xVelocity = tracker.getXVelocity()
                    // 速度的绝对值大于50。表示快速滑动
                    if (Math.abs(xVelocity) > 50){
                        // 切换上一个页面
                        if (xVelocity > 0) {
                            currentIndex--
                        }else{
                            // 切换下一个页面
                            currentIndex++
                        }
                    }
                }
                log("## ontouch a currentIndex=$currentIndex")
                currentIndex = if (currentIndex < 0){
                    0
                } else {
                    if (currentIndex > getChildCount() - 1) getChildCount() - 1 else currentIndex
                }
                log("## ontouch b currentIndex=$currentIndex")
                log("## ontouch 2 distance=$distance , getLeftWidth(currentIndex)=${getLeftWidth(currentIndex)}")
                smoothScrollTo(getLeftWidth(currentIndex), 0)
                tracker.clear()
            }
        }
        lastX = x.toInt()
        lastY = y.toInt()
        return true
    }

    fun getLeftWidth(currentIndex:Int): Int{
        var leftWidth = 0
        for (i in 0 until currentIndex){
            val childAt = getChildAt(i)
            val measuredWidth = childAt.measuredWidth
            leftWidth += measuredWidth
        }
        return leftWidth
    }

    // 给scrollBy滑动添加一个滑动效果
    // X ms内沿着X轴平移delta像素
    fun smoothScrollTo(destX: Int, destY: Int){
        mScroller.startScroll(scrollX, scrollY, destX-scrollX, destY-scrollY, 1000)
        invalidate()
    }
    override fun computeScroll() {
        super.computeScroll()
        // 返回true，说明动画还未结束，则继续滑动
        if (mScroller.computeScrollOffset()){
            // 调用scrollTo完成滑动
            scrollTo(mScroller.currX, mScroller.currY)
            //然后继续触发重绘，不断的调用computeScroll方法
            postInvalidate()
        }
    }
}

/** TODO 竖向添加VIew
 *
 */
class BasicViewGroup:ViewGroup {
    constructor(context: Context):this(context, null)
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){

    }

    /**
     * @param widthMeasureSpec
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // widthMeasureSpec: Int, heightMeasureSpec: Int 父类传过来给当前View的一个建议值
        // int specMode = MeasureSpec.getMode(measureSpec); 获取模式
        // int specSize = MeasureSpec.getSize(measureSpec); 获取大小
        // 三种模式：
        // 1、UNSPECIFIED（未指定）：父元素不对子元素施加束缚，子元素可获取任意大小
        // 2、EXACTLY（完全）：父元素决定子元素大小，子元素被限制在给定的边界中。
        // 3、AT_MOST（至多）：子元素至多达到指定大小的值
        // 分别对应二进制值：
        // UNSPECIFIED:00000000000000000000000000000000  --》十进制：0
        // EXACTLY:01000000000000000000000000000000  --》十进制：1
        // AT_MOST:10000000000000000000000000000000  --》十进制：2
        // 前二位代表模式，后30位代表数值
        val specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        val specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        val specHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        val specHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        var newW = 0
        var newH = 0
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            // 测量子控件的高度和宽度
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            // 获取子View的Margin，并添加进去
            val margin = child.layoutParams as MarginLayoutParams
            val childW = child.measuredWidth + margin.leftMargin + margin.rightMargin
            val childH = child.measuredHeight + margin.topMargin + margin.bottomMargin

            newH += childH
            newW = Math.max(newW, childW)
            Log.i(">>>>", "##onMeasure 1 childW=${childW}；childH=${childH}")
            Log.i(">>>>", "##onMeasure 2 newW=${newW}；newH=${newH}")
        }
        setMeasuredDimension(if(specWidthMode == MeasureSpec.EXACTLY) specWidthSize else newW
                , if(specHeightMode == MeasureSpec.EXACTLY) specHeightSize else newH)
    }
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // 对container内部的各个控件进行排列，此处实现垂直排列
        var top = 0
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            // 获取子View的Margin，并添加进去
            val margin = child.layoutParams as MarginLayoutParams
            val childW = child.measuredWidth + margin.leftMargin + margin.rightMargin
            val childH = child.measuredHeight + margin.topMargin + margin.bottomMargin
            // 确定子View的位置
            child.layout(margin.leftMargin, top, childW, top + childH)
            top += childH
            Log.i(">>>>", "##onLayout 1 childW=${childW}；childH=${childH}")
            Log.i(">>>>", "##onLayout 2 top=${top}")
        }
    }
    // 重写 generateLayoutParams 和 generateDefaultLayoutParams 获取Margin
    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        // 从XML中获取长宽值
//        return LayoutParams(context, attrs)
        // 如果需要Margin 则 返回 MarginLayoutParams 即可从XML中获取Margin
        return MarginLayoutParams(context, attrs)
    }
    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }
}

/** TODO 流式布局
 *  问题：
 *     1、何时换行：当前行放不下一个控件，就移动至下一行
 *     2、获取FlowLayout的宽度，记录每一行所占据的宽度值，进而获取最大值
 *     3、获取FlowLayout的高度
 */
class CustomFlowLayout: ViewGroup {

    constructor(context: Context):this(context, null)
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){

    }
    // 1、重写 generateLayoutParams 和 generateDefaultLayoutParams 获取Margin
    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }
    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        // 如果需要Margin 则 返回 MarginLayoutParams 即可从XML中获取Margin
        return MarginLayoutParams(context, attrs)
    }
    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    // 2、 重写 onMeasure 计算当前FlowLayout所占区域大小
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        val specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        val specHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        val specHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        // 记录每一行的宽度
        var lineW = 0
        // 记录每一行的高度
        var lineH = 0
        // 记录整个FlowLayout的宽度
        var flowW = 0
        // 记录整个FlowLayout的高度
        var flowH = 0
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            // 计算子View
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            val margin = child.layoutParams as MarginLayoutParams
            val childW = child.measuredWidth + margin.leftMargin + margin.rightMargin
            val childH = child.measuredHeight + margin.topMargin + margin.bottomMargin
            if (lineW + childW > specWidthSize) {
                // 需要换行
                flowW = Math.max(lineW, childW) // 与上一行的最大宽度进行比较，获取最大宽度
                flowH += lineH // 累加上一行的最大高度
                // 换行后，将当前控件的宽高初始化给lineW和lineH
                lineW = childW
                lineH = childH
            } else {
                lineW += childW // 累加宽度
                lineH = Math.max(lineH, childH) // 取最大高度
            }
            // 绘制最后一个控件时，需要和最后一行最大宽度进行比较以及添加最大高度
            if (i == count-1) {
                flowW = Math.max(lineW, flowW)
                flowH += lineH
            }
        }
        setMeasuredDimension(if (specWidthMode == MeasureSpec.EXACTLY) measuredWidth else flowW
                , if (specHeightMode == MeasureSpec.EXACTLY) measuredHeight else flowH)
    }
    // 3、重写onLayout
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        var lineW = 0   // 行宽
        var lineH = 0   // 行高
        var childTop = 0    // 当前控件的top位置
        var childLeft = 0   // 当前控件的 left位置
        for (i in 0 until count) {
            val child = getChildAt(i)
            val margin = child.layoutParams as MarginLayoutParams
            val childW = child.measuredWidth + margin.leftMargin + margin.rightMargin
            val childH = child.measuredHeight + margin.topMargin + margin.bottomMargin
            if (childW + lineW > measuredWidth) {
                // 换行
                childTop += childH
                childLeft = 0
                lineW = childW
                lineH = childH
            }else {
                lineW += childW
                lineH = Math.max(lineH, childH)
            }
            // 计算child的Left、Top、Right、Bottom
            val lc = childLeft + margin.leftMargin
            val tc = childTop + margin.topMargin
            val rc = lc + child.measuredWidth
            val bc = tc + child.measuredHeight
            child.layout(lc, tc, rc, bc)
            // 下一个子控件的起始点
            childLeft += childW
        }
    }
}
// 重写flowlayout 完善版
class CustomFlowLayout2: ViewGroup {
    constructor(context: Context):this(context, null)
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){

    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }
    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measureWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        val measureHeight = MeasureSpec.getSize(heightMeasureSpec)
        val measureHeightMode = MeasureSpec.getMode(heightMeasureSpec)
        var lineWidth = 0  // 当前行的行宽
        var lineHeight = 0 // 当前行的行高， 已最大的为准
        var flowWidth = 0   // 最大行宽，即 Flow 最终的宽度
        var flowHeight = 0  // 最大行高，即 Flow 最终的高度
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            // 计算子类后，才能获取到子类的宽高
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            val nowlp = child.layoutParams as MarginLayoutParams
            val childWidthNow = child.measuredWidth + nowlp.leftMargin + nowlp.rightMargin
            // 如果当前获取child 宽度超过父控件宽度，则需要重新计算，以父控件宽度进行计算
            if (childWidthNow > measureWidth) {
                val newCW = MeasureSpec.makeMeasureSpec(measureWidth-(nowlp.leftMargin + nowlp.rightMargin), MeasureSpec.EXACTLY)
                measureChild(child, newCW, heightMeasureSpec)
            }
            val lp = child.layoutParams as MarginLayoutParams
            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin

            if (childWidth + lineWidth > measureWidth) {
                // 对之前行的宽度进行比较，获取最大宽
                flowWidth = Math.max(flowWidth, lineWidth)
                // 累计上一行的高度
                flowHeight += lineHeight
                // 换行，以当前view的宽度和高度为初始值
                lineWidth = childWidth
                lineHeight = childHeight
            }else {
                // 累加宽度
                lineWidth += childWidth
                // 获取当前行的最大高度，以高度为准
                lineHeight = Math.max(lineHeight, childHeight)
            }
            if (i == count-1) {
                // 累加最后一行的高度，以及和最后一行的宽度比较
                lineWidth = Math.max(lineWidth, childWidth)
                lineHeight += childHeight
            }
        }
        // 是否是精确数值，如果是，则已设置值为准
        val mWidth = if (measureWidthMode == MeasureSpec.EXACTLY) measureWidth else flowWidth
        val mHeight = if (measureHeightMode == MeasureSpec.EXACTLY) measureHeight else flowHeight
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var childTop = 0   // child 当前上边的位置
        var childLeft = 0  // child 当前左边的位置
        var lineWidth = 0  // 当前行的行宽
        var lineHeight = 0 // 当前行的行高
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            val lp = child.layoutParams as MarginLayoutParams
            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin
            if (childWidth + lineWidth > measuredWidth) {
                // 换行
                //  child从左开始绘制，恢复0
                childLeft = 0
                // child 顶部位置累加
                childTop += lineHeight
                // 初始化当前行的宽高
                lineWidth = childWidth
                lineHeight = childHeight
            }else {
                // 当前行宽累加
                lineWidth += childWidth
                // 获取当前行的最大高度
                lineHeight = Math.max(lineHeight, childHeight)
            }
            // 计算每个child的绘制位置
            val lc = childLeft + lp.leftMargin
            val tc = childTop + lp.topMargin
            val rc = lc + child.measuredWidth
            val bc = tc + child.measuredHeight
            child.layout(lc, tc, rc, bc)
            // 获取下一个child的起点位置
            childLeft += childWidth
        }
    }

}

fun getGestureDetector() {
    // GestureDetector
//    val context:Context = Context()
//    GestureDetector(context, GestureDetector.SimpleOnGestureListener())
}

/**
 * 触发顺序
 * onDown ——》 onShowPress ——》 onLongPress
 * 单击一下非常快，不滑动，触发顺序
 * onDown ——》 onSingleTapUp ——》 onSingleTapConfirmed
 * 单击一下稍微慢一点，不滑动，触发顺序：
 * onDown ——》 onShowPress ——》 onSingleTapUp ——》onSingleTapConfirmed
 * 滑屏，手指触动屏幕后，稍微滑动后松开，触发顺序
 * onDown ——》 onScroll ——》 ...... ——》 onScroll ——》 onFling
 */
class MyGestureListener: GestureDetector.OnGestureListener {
    // 按下屏幕就会触发
    override fun onDown(e: MotionEvent?): Boolean {
        Log.i(">>>>", "##MyGestureListener onDown")
        return false
    }
    // 按下时间超过瞬间，且没有松开和拖动，就会触发
    override fun onShowPress(e: MotionEvent?) {
        Log.i(">>>>", "##MyGestureListener onShowPress")
    }
    // 长按触摸屏，超过一定时长就会触发
    override fun onLongPress(e: MotionEvent?) {
        Log.i(">>>>", "##MyGestureListener onLongPress")
    }
    // 一次单独轻击屏幕后立刻抬起就会触发该函数。如果down以外还有其他操作，就不再算单独操作，就不会触发。
    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Log.i(">>>>", "##MyGestureListener onSingleTapUp")
        return true
    }
    // 滑动触发
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        Log.i(">>>>", "##MyGestureListener onScroll")
        return true
    }
    // 滑屏，用户按下触摸屏、快速移动后松开。由一个MotionEvent.ACTON_DOWN、多个MotionEvent.ACTION_MOVE和一个ACTION_UP触发
    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        Log.i(">>>>", "##MyGestureListener onFling")
        return true
    }

}

/**
 * 需要同时实现 OnGestureListener 和 OnDoubleTapListener
 * 单击一次触发顺序：onDown——》 onSingleTapUp ——》 onSingleTapConfirmed
 */
class MyOnDoubleTapListener: GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    /* 单击事件，用来判定该次单击是 SingleTop，而不是 DoubleTap。
     * 单击一次，系统等待一段时间后没有收到第二次单击，则会判定该次单击为 SingleTop，而不是DoubleTab时间。
     * 然后触发 onSingleTapConfirmed 函数
     * 触发顺序：onDown——》 onSingleTapUp ——》 onSingleTapConfirmed
     */
    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        Log.i(">>>>", "##MyOnDoubleTapListener OnDoubleTapListener onSingleTapConfirmed")
        return false
    }

    /* 双击事件
     */
    override fun onDoubleTap(e: MotionEvent?): Boolean {
        Log.i(">>>>", "##MyOnDoubleTapListener OnDoubleTapListener onDoubleTap")
        return false
    }
    /* 双击间隔中发生的动作，即在触发 onDoubleTap 后，在双击之间发生的其他动作，包含down、up和move时间
     *
     */
    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        Log.i(">>>>", "##MyOnDoubleTapListener OnDoubleTapListener onDoubleTapEvent")
        return false
    }

    // 按下屏幕就会触发
    override fun onDown(e: MotionEvent?): Boolean {
        Log.i(">>>>", "##MyOnDoubleTapListener OnGestureListener onDown")
        return false
    }
    // 按下时间超过瞬间，且没有松开和拖动，就会触发
    override fun onShowPress(e: MotionEvent?) {
        Log.i(">>>>", "##MyOnDoubleTapListener OnGestureListener onShowPress")
    }
    // 长按触摸屏，超过一定时长就会触发
    override fun onLongPress(e: MotionEvent?) {
        Log.i(">>>>", "##MyOnDoubleTapListener OnGestureListener onLongPress")
    }
    // 一次单独轻击屏幕后立刻抬起就会触发该函数。如果down以外还有其他操作，就不再算单独操作，就不会触发。
    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Log.i(">>>>", "##MyOnDoubleTapListener OnGestureListener onSingleTapUp")
        return true
    }
    // 滑动触发
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        Log.i(">>>>", "##MyOnDoubleTapListener OnGestureListener onScroll")
        return true
    }
    /*
     * 滑屏，用户按下触摸屏、快速移动后松开。由一个MotionEvent.ACTON_DOWN、多个MotionEvent.ACTION_MOVE和一个ACTION_UP触发
     * velocityX,velocityY :分别表示X轴上和Y轴上的滑动速度，单位：像素/秒
     * 校验向左滑和向右滑：
     *  例向左滑:滑动距离超过 FLING_MIN_DISTANCE 像素，且滑动速度超过 FLING_MIN_VELOCITY 像素/秒
     */
    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        Log.i(">>>>", "##MyOnDoubleTapListener OnGestureListener onFling")
        // 滑动方向触发条件：X周的坐标位移大于 FLING_MIN_DISTANCE，且移动速度大于 FLING_MIN_VELOCITY 像素/秒
        if (e1.x - e2.x > FLING_MIN_DISTANCE
                && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            // 向左滑
            Log.i(">>>>", "##MyOnDoubleTapListener OnGestureListener onFling 向左滑")
        }else if (e2.x - e1.x > FLING_MIN_DISTANCE
                && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            // 向右滑
            Log.i(">>>>", "##MyOnDoubleTapListener OnGestureListener onFling 向右滑")
        }
        return true
    }
    val FLING_MIN_DISTANCE = 100
    val FLING_MIN_VELOCITY = 200

}