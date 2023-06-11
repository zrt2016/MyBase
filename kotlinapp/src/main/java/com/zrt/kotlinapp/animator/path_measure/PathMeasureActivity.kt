package com.zrt.kotlinapp.animator.path_measure

import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_path_measure.*

/**
 * PathMeasure类似一个计算器，可以计算出指定路径的一些信息，
 * 例如：路径总长指定长度所对应的坐标点等
 * 初始化方式：
 *      val pm = PathMeasure()
 *      pm.setPath(Path path, boolean forceClosed)
 *      val pm = PathMeasure(Path path, boolean forceClosed)
 *      参数Path path：对添加的path和PathMeasure进行绑定
 *      参数boolean forceClosed：true则对path长度计算包含闭合的长度，不会影响path本身的绘制，只对测量结果有影响。
 * getLength()函数：获取计算的路径长度
 *      boolean forceClosed = true ：计算长度包含闭合的长度(未闭合的path也会计算闭合的长度)
 * isClose()：判断测量Path时是否计算闭合，forceClosed为true，则一定返回true。如果path本身已闭合，则也会返回true
 * @see PMLengthView getLength()和isClose()可查看该实例
 *
 * nextContour(): Path可以由多条曲线构成，不论是getLength还是getSegment还是其他函数，都只会针对第一条计算。
 *      而nextContour就是用于跳转下一条曲线的行数，跳转成功返回true
 *
 * getSegment(float startD, float stopD, Path dst, boolean startWithMoveTo)：截取path的一定长度
 * @see PMSegmentView getSegment可参考该实例
 *      ① 通过参数startD和stopD来控制截取的长度，并将截取后的Path保存到参数dst中。
 *      ② startWithMoveTo: true起始点使用pm中当前path的起始点，false：使用dst的末尾点为起始点
 *      ③ 使用该函数需先禁用硬件加速功能setLayerType(LAYER_TYPE_SOFTWARE, null)
 * getPosTan() 用于得到路径上某一长度的位置以及该位置的正切值(即根据)
 * @see PMLoadCircleView getPosTan可参考该实例中箭头图标 方式一
 *      float distance：距离path起始点的长度值
 *      float pos[]：根据长度值获取到的该长度的位置坐标（x,y）
 *      float tan[]:根据长度值获取到的该长度的位置正切值 y/x
 *      可通过Math.atan或Math.atan2获得对应正切角度
 * getMatrix();获取路径上某一长度的位置以及该位置的正切矩阵
 * @see PMLoadCircleView getPosTan可参考该实例中箭头图标 方式二
 *      float distance：距离path起始点的长度值
 *      Matrix matrix, 根据flag封装好的matrix会根据flag的设置而存入不同的内容
 *      int flags：指定哪些信息存入matrix中，flag有2个值，多个可用 | 同时指定
 *          PathMeasure.POSITION_MATRIX_FLAG：获取位置信息
 *          PathMeasure.TANGENT_MATRIX_FLAG：获取切边信息，使图片按path旋转
 * 2、SVG动画：android在5.0添加了对SVG图形的支持
 *    Html中的SVG
 *    <svg>
 *    <rect x="25" y="25" width="200" height="200" fill="lime" stroke-width="2" stroke="pink"/>
 *    </svg>
 *    对5.0以下的机型，可以通过引入 com.android.support:appcompat-v7:23.4.0 及以上版本进行支持
 *    vector可适用于android2.1以上的所有系统以显示SVG图形
 *    vector标签与图像显示，参考：res/drawable/vector_**。例res/drawable/vector_line
 * ** ① <vector/>的属性：
 *      android:width="200dp" 表示SVG图像的具体宽度
 *      android:height="100dp" 表示SVG图像的具体高度
 *      android:viewportWidth="100" 指定width分为多少个点，path的x坐标以其点数为准
 *      android:viewportHeight="50dp" 指定height分为多少个点，path的y坐标以其点数为准
 * ** ② <path/>的属性
 *      android:name="bar" 声明一个标记，类似于id，便于对其做动画的时候顺利找到该节点
 *      android:pathData="" 对矢量图的描述
 *      android:strokeWidth="2" 画笔的宽度
 *      android:strokeColor="@color/blue" 画笔颜色
 *      android:strokeAlpha="" 画笔透明度
 *      android:fillColor="" 填充颜色
 *      android:fillAlpha="" 填充颜色透明度
 *      android:strokeLineJoin 指定折现拐角形状，取值 miter（结合处为锐角）、round（结合处为圆弧）、bevel（结合处为直线）
 *          对应java：setStrokeJoin(Paint.Join)
 *      android:strokeLineCap 画出线条终点的形状，取值 butt（无线帽）、round（圆形线帽）、square（方形线帽）
 *          对应java：setStrokeCap(Paint.Join)
 *      android:strokeMiterLimit 设置斜角的上限，当strokeLineJoin=miter时，即绘制的两条线段相交处为锐角，
 *          所得斜面可能相当长。斜面太长会导致变得不协调。该属性为斜面的长度设置了一个上限，这个属性表示斜面长度
 *          和线条长度的比值，默认值=10，表示一个斜面的长度不应该超过线条宽度的10倍。斜面如果达到了这个长度，就变成斜角了。
 *          只针对strokeLineJoin=miter时有效
 *      android:trimPathStart="0.2" 指定路径从哪开始，取值 0~1，表示路径开始位置的百分比。
 *          等于0时，从头开始。等于1时，路径不可见。此处等于0.2，表示路径的起始点从 length*0.2 处开始
 *      android:trimPathEnd="0.8" 指定路径的结束位置，取值 0~1，表示路径结束位置的百分比。
 *          等于0时，表示开始位置就已结束了。等于1时，表示路径正常结束。此处等于0.8，表示路径结束点在 length*0.8 的位置
 *      android:trimPathOffset="" 指定结果路径的位移距离，取值 0~1。等于0时，不进行位移。等于1时，位移整个路径长度
 *      android:pathData="" 通过该属性指定SVG图像的显示内容
 *          pathData中主要有几种类型，M表示移动到某个点，L表示画线，A表示弧线，
 *          Q是二阶贝塞尔，C是三阶贝塞尔，Z表示闭合，V表示垂直，H表示水平
 *          M = moveto(M X,Y) ：将画笔移动到指定的坐标位置
 *          L = lineto(L X,Y) ：画直线到指定的坐标位置
 *          H = horizontal lineto(H X)：画水平线到指定的X坐标位置
 *          V = vertical lineto(V Y)：画垂直线到指定的Y坐标位置
 *          C = curveto(C X1,Y1,X2,Y2,ENDX,ENDY)：三阶贝赛曲线
 *          S = smooth curveto(S X2,Y2,ENDX,ENDY):三阶贝赛曲线, 将上一条指令的终点作为这条指令的起始点
 *          Q = quadratic Belzier curve(Q X,Y,ENDX,ENDY)：二次贝赛曲线
 *          T = smooth quadratic Belzier curveto(T ENDX,ENDY)：映射
 *          A = elliptical Arc(A RX,RY,XROTATION,FLAG1,FLAG2,X,Y)：弧线
 *              RX,RY = 所属椭圆的半轴大小
 *              XROTATION = X轴和水平方向顺时针的夹角，椭圆按顺时针旋转XROTATION角度
 *              FLAG1 = 1 or 0 ; 1表示大角度弧度。0表示小角度弧度
 *              FLAG2 = 1 or 0 ；确定起始点到终点的方向。1（顺时针）、0（逆时针）
 *              X,Y 终点坐标
 *          Z = closepath()：关闭路径
 *         注：坐标轴以(0,0)为中心点，X周水平向右，Y轴水平向下。
 *         所有指令大小写均可。大写表示绝对定位（参照全局坐标系）。小写表示相对定位，参照父容器坐标系。
 * ** ③ <group/> 可对path路径进行分组，一个vector下可以有多个path和group标签，一个group下可以有多个path标签
 *      android:name="group_bar" 组的名字，用于与动画相关联
 *      android:rotation="90" 该组图像旋转度数,顺时针旋转90度
 *      android:pivotX="" 缩放和旋转时中心点 X轴的参考点，该值相对于vector的viewport
 *      android:pivotY="" 缩放和旋转时中心点 Y轴的参考点，该值相对于vector的viewport
 *      android:scaleX="" 该组X轴缩放大小
 *      android:scaleY="" 该组Y轴缩放大小
 *      android:translateX="" 该组X轴平移
 *      android:translateY="" 该组Y轴平移
 *  SVG图片转换为Vector图像: res/drawable -> 右键 -> New -> Vector Asset
 *     Local SVG file: 选择本地SVG文件
 *     Material Icon：选择IDE自带的SVG文件
 *     Resource name 当前SVG文件名称
 *     Size 图片大小
 *     Override default size from material Design：勾选后替代默认大小
 *     Opactity：调节透明度
 *     Enable atuo mirroring for RTL layout：中国显示习惯从左向右，当出现相反情况时，通过RTL可以水平反转图标，镜像显示。
 *  SVG相关配置：
 *  Android 5.0之后是默认支持的，之前的话需要添加库支持
 *  android {
 *      defaultConfig {
 *          vectorDrawables.useSupportLibrary = true
 *      }
 *  }
 *  dependencies {
 *      compile 'com.android.support:appcompat-v7:23.2.0'
 *  }
 *  java代码中需要添加
 *  companion object {
 *      init {
 *      // 23.2.0版本的appcompat包存在bug，23.4.0进行了修复，为了区别旧版，添加了该标签，解决旧版bug问题
 *      AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
 *      }
 *  }
 *  ImageView中使用SVG图片：app:srcCompat="@drawable/vector_line" 或 setImageResource(R.drawable.vector_line)
 *  Button\RadioButton中使用svg图片需要通过 selector 标签来使用
 *  <animated-vector/> 标签，用于将Vector图像与动画相关联
 *  参考res/drawable/vector_line_anim.xml 关联 vector_line 和 object_vector_trimpathstart_anim 动画
 *  <animated-vector xmlns:android="http://schemas.android.com/apk/res/android"
 *      android:drawable="@drawable/vector_line"> 指定svg图像
 *      <target
 *          android:name="v_line" // 指定path标签的name，与vector文件中的path标签对应
 *          android:animation="@animator/object_vector_trimpathstart_anim"/> // 指定path的动画
 *  </animated-vector>
 */
class PathMeasureActivity : BasicActivity() {
    companion object {
        init {
            // 23.2.0版本的appcompat包存在bug，23.4.0进行了修复，为了区别旧版，添加了该标签，解决旧版bug问题
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_path_measure
    }

    override fun initData() {
        //SVG图像动画
        a_p_m_btn1.setOnClickListener {
            val animDrawable = AnimatedVectorDrawableCompat.create(this, R.drawable.vector_line_anim)
            a_p_m_anim_img.setImageDrawable(animDrawable)
            val anim = a_p_m_anim_img.drawable as Animatable
            anim.start()
        }
        val vector = VectorDrawableCompat.create(resources, R.drawable.vector_line, null)
        a_p_m_anim_img_startpath.setImageDrawable(vector)
        a_p_m_svg_rotator.setOnClickListener{
            val animDrawable = AnimatedVectorDrawableCompat.create(this, R.drawable.vector_group_line_anim)
            a_p_m_svg_rotator.setImageDrawable(animDrawable)
            val anim = a_p_m_svg_rotator.drawable as Animatable
            anim.start()
        }
//        a_p_m_anim_img_startpath.drawable as AnimatedVectorDrawable
//        VectorDrawableCompat.createFromPath()
        a_p_m_anim_seekBar_start.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        vectorEditSearch()
    }

    /**
     * 输入框搜索动画
     * @see R.drawable.vector_edit_search vector图片
     * @see R.animator.object_edit_line_anim vector图片中bottom_line的动画，从左到右逐渐减小
     * @see R.animator.object_edit_search_anim vector图片中right_search的动画，从无到有逐渐显示
     * @see R.drawable.vector_edit_search_anim 关联vector图片和相关动画
     */
    private fun vectorEditSearch() {
        // ImageView获取焦点
        a_p_m_anim_img_search.setFocusable(true)
        a_p_m_anim_img_search.setFocusableInTouchMode(true)
        a_p_m_anim_img_search.requestFocus()
        a_p_m_anim_img_search.requestFocusFromTouch()
        a_p_m_edit_search.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val animDrawable = AnimatedVectorDrawableCompat.create(this, R.drawable.vector_edit_search_anim)
                a_p_m_anim_img_search.setImageDrawable(animDrawable)
                val anim = a_p_m_anim_img_search.drawable as Animatable
                anim.start()
            }
        }

    }
}