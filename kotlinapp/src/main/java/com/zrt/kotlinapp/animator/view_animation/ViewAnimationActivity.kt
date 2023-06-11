package com.zrt.kotlinapp.animator.view_animation

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.animation.*
import androidx.annotation.AnimRes
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_view_animation.*

/**
 * 视图动画
 * 一、Android动画由5种类型组成：
 *   1、alpha：渐变透明度
 *   2、scale：尺寸伸缩动画
 *   3、translate：平移动画
 *   4、rotate：旋转动画
 *   5、set：定义动画集
 * 二、XML动画文件配置实现
 *   @see xmlAnimation()
 *   在res/anim目录下配置动画文件，访问时使用R.anim.XXX。也可以存放在res/drawable文件夹下，使用R.drawable.XXX
 *   1、Animation继承属性，所有动画都继承与Animation类，通用属性：
 *      duration="1000"：动画时长（单位毫秒）
 *      fillAfter="true"：表示动画结束时，保持动画结束时的状态
 *      fillBefore="true"：表示动画结束时，将还原到初始化状态
 *      fillEnabled="true"：表示动画结束时，将还原到初始化状态
 *      repeatCount="2"：指定动画重复次数，取值为infinite时，表示无限循环
 *      repeatMode="reverse"：设定重复的类型，取值有2种。
 *          reverse表示倒序回放。
 *          restart表示重放，并且需要与repeatCount绑定使用。
 *      interpolator：设定插值器，指定动画效果，例如弹跳效果等
 *          AccelerateDecelerateInterpolator 在动画开始与结束的地方速率改变比较慢，在中间的时候加速
 *          AccelerateInterpolator 在动画开始的地方速率改变比较慢，然后开始加速
 *          AnticipateInterpolator 开始的时候向后然后向前甩
 *          AnticipateOvershootInterpolator 开始的时候向后然后向前甩一定值后返回最后的值
 *          BounceInterpolator 动画结束的时候弹起
 *          CycleInterpolator 动画循环播放特定的次数，速率改变沿着正弦曲线
 *          DecelerateInterpolator 在动画开始的地方快然后慢
 *          LinearInterpolator 以常量速率改变
 *          OvershootInterpolator 向前甩一定值后再回到原来位置
 *   2、scale属性（示例：res.anim.scaleanim）：
 *      fromXScale="0.0"：动画起始控件在X轴的缩放比例。1.0无变化、小于1.0代表缩小、大于1.0代表放大（浮点值）
 *      toXScale="1.4"：动画结束时，控件在X轴上的缩放比例。1.0无变化、小于1.0代表缩小、大于1.0代表放大（浮点值）
 *      fromYScale="0.0"：动画起始控件在Y轴的缩放比例。1.0无变化、小于1.0代表缩小、大于1.0代表放大（浮点值）
 *      toYScale="1.4"：动画结束时，控件在Y轴上的缩放比例。1.0无变化、小于1.0代表缩小、大于1.0代表放大（浮点值）
 *      pivotX="50"或"50%"或"50%p"：缩放起始点X轴坐标，可以是数值、百分数和百分数p三种样式。
 *          ① 数值（50）：表示在当前视图的左上角，即原点处加上50px，作为X轴的缩放起始点
 *          ② 百分数（50%）：表示在当前视图的左上角加上自己50%的宽度作为缩放起始点X轴的坐标
 *          ③ 百分数p（50%p）：表示在当前控件的左上角加上父控件宽度的50%作为缩放起始点X轴坐标
 *      pivotY="50"或"50%"或"50%p"：缩放起始点Y轴坐标，取值级含义与pivotX相同。
 *   3、alpha属性（示例：res.anim.alphaanim）
 *      android:fromAlpha="0.0"：动画开始时的透明度，取值范围0.0~1.0，0.0表示全透明
 *      android:toAlpha="1.0"：动画结束时的透明度，取值范围0.0~1.0，1.0表示完全不透明
 *   4、rotate属性（示例：res.anim.rotateanim）
 *      android:fromDegrees="0"：动画开始旋转时的角度位置，正值代表顺时针方向的度数，负值代表逆时针方向的度数。
 *      android:toDegrees="-650"：动画结束时旋转到的角度位置，正值代表顺时针方向的度数，负值代表逆时针方向的度数。
 *      android:pivotX="50"或"50%"或"50%p"：旋转中心点X轴的坐标，默认旋转中心点是控件坐标锁上角为原点。
 *        可以是数值、百分数和百分数p三种样式。
 *          ① 数值（50）：原点加上50px，作为X轴的旋转坐标点。
 *          ② 百分数（50%）：原点加上自己50%的宽度，作为X轴的旋转坐标点。
 *          ③ 百分数p（50%p）：原点加上父控件宽度的50%，作为X轴的旋转坐标点。
 *      android:pivotY="50"或"50%"或"50%p"：旋转中心点Y轴的坐标，默认旋转中心点是控件坐标锁上角为原点。取值级含义与pivotX相同。
 *   5、translate属性（示例：res.anim.translateanim）
 *      android:fromXDelta="50"或"50%"或"50%p"：起点X轴坐标，可以是数值，百分数，百分数p三种样式，
 *          ① 数值（50）：表示当前视图的左上角，即原始点出加上50px。
 *          ② 百分数（50%）：表示在当前控件的左上角加上自己宽度的50%。
 *          ③ 百分数p（50%p）：表示在当前控件的左上角加上父控件宽度的50%
 *      android:fromYDelta="50"或"50%"或"50%p":起点Y轴坐标，取值级含义与fromXDelta相同。
 *      android:toXDelta="50"或"50%"或"50%p" 终点X轴坐标
 *      android:toYDelta="50"或"50%"或"50%p" 终点Y轴坐标
 *   6、set动画集合（示例：res.anim.setanim））：set标签中是在repeatCount属性是无效的，必须对每个动画单独设置。
 *      android:startOffset="500"：设置延迟500毫秒后执行。
 * 三、代码实现动画配置
 *   @see codeAnimation()
 *   Animation设置属性
 *      setRepeatMode():设定重复的类型，取值有2种。
 *          Animation.RESTART: 表示重放，并且需要与repeatCount绑定使用。
 *          Animation.REVERSE: 表示倒序回放。
 *
 *   1、ScaleAnimation（参数）
 *   @see scaleAnimation()
 *      float fromX：动画起始控件在X轴的缩放比例。
 *      float toX：动画结束时，控件在X轴上的缩放比例。
 *      float fromY：动画起始控件在Y轴的缩放比例。
 *      float toY：动画结束时，控件在Y轴上的缩放比例。
 *      int pivotXType：指定pivotXValue的数值类型，有三种
 *          Animation.ABSOLUTE（数值）：pivotXValue值为50时等于50px
 *          Animation.RELATIVE_TO_SELF（百分数）：pivotXValue值为0.5f时等于 50%
 *          Animation.RELATIVE_TO_PARENT（百分数p）:pivotXValue值为0.5f时等于 50%p
 *      float pivotXValue：根据pivotXType类型确定值
 *      int pivotYType：指定pivotYValue的数值类型，有三种，与pivotXType含义相同
 *      float pivotYValue：根据pivotXType类型确定值
 *   2、AlphaAnimation（参数）
 *   @see alphaAnimation()
 *      float fromAlpha：动画开始时的透明度
 *      float toAlpha：动画结束时的透明度
 *   3、RotateAnimation（参数）
 *   @see rotateAnimation()
 *      float fromDegrees：动画开始旋转时的角度位置，正值代表顺时针方向的度数，负值代表逆时针方向的度数。
 *      float toDegrees：动画结束时旋转到的角度位置，正值代表顺时针方向的度数，负值代表逆时针方向的度数。
 *      int pivotXType：指定pivotXValue的数值类型，有三种
 *          Animation.ABSOLUTE（数值）：pivotXValue值为50时等于50px
 *          Animation.RELATIVE_TO_SELF（百分数）：pivotXValue值为0.5f时等于 50%
 *          Animation.RELATIVE_TO_PARENT（百分数p）:pivotXValue值为0.5f时等于 50%p
 *      float pivotXValue：指定旋转中心点X轴上的值，根据pivotXType类型确定值。
 *      int pivotYType：指定pivotYValue的数值类型，有三种，与pivotXType含义相同
 *      float pivotYValue：指定旋转中心点Y轴上的值，根据pivotXType类型确定值
 *   4、TranslateAnimation（参数）
 *   @see translateAnimation()
 *      int fromXType：指定fromXValue的数值类型，有以下三种
 *          Animation.ABSOLUTE（数值）：pivotXValue值为50时等于50px
 *          Animation.RELATIVE_TO_SELF（百分数）：pivotXValue值为0.5f时等于 50%
 *          Animation.RELATIVE_TO_PARENT（百分数p）:pivotXValue值为0.5f时等于 50%p
 *      float fromXValue：起始X轴的坐标值，根据fromXType类型确定值类型。
 *      int toXType：指定toXValue的数值类型,类型同上
 *      float toXValue：终点X洲的坐标值，根据toXType类型确定值类型。
 *      int fromYType：指定fromYValue的数值类型,类型同上
 *      float fromYValue：起点Y轴的坐标值，根据fromYType类型确定值类型。
 *      int toYType：指定toYValue的数值类型,类型同上
 *      float toYValue：终点Y轴的坐标值，根据toYType类型确定值类型。
 *   5、AnimationSet(参数)
 *   @see setAnimation()
 *   boolean shareInterpolator：是否公用同一个插值器动画
 *      true：可在AnimationSet中设置一个插值器（），其下面所有动画公用该插值器
 *      false：表示其下动画各自定义插值器
 *   6、Animation（动画常用函数以及监听）
 *   @see attributeAnimation()
 *      cancel()：取消动画
 *      reset()：重置到动画开始前的状态
 *      setAnimationListener(Animation.AnimationListener listener)：设置动画监听
 *          onAnimationEnd(Animation animation)：动画结束时回调该函数
 *          onAnimationRepeat(Animation animation)：动画重复时会调用该函数通知
 *          onAnimationStart(Animation animation)：动画开始时，会调用该函数通知
 * 四、插值器（Interpolator）：指定动画的变化速率
 *   1、可通过xml或者java代码中设置：
 *   xml：android:interpolator="@android:anim/linear_interpolator"
 *   java：animation.setInterpolator(new LinearInterpolator())
 *   2、系统自带插值器：
 *      AccelerateDecelerateInterpolator 在动画开始与结束的地方速率改变比较慢，在中间的时候加速
 *      AccelerateInterpolator 在动画开始的地方速率改变比较慢，然后开始加速
 *      AnticipateInterpolator 开始的时候向后然后向前甩
 *      AnticipateOvershootInterpolator 开始的时候向后然后向前甩一定值后返回最后的值
 *      BounceInterpolator 动画结束的时候弹起
 *      CycleInterpolator 动画循环播放特定的次数，速率改变沿着正弦曲线
 *      DecelerateInterpolator 在动画开始的地方快然后慢
 *      LinearInterpolator 以常量速率改变
 *      OvershootInterpolator 向前甩一定值后再回到原来位置
 * 逐帧动画（Frame Animation）
 * @see frameAnimation()
 *    XML设置动画，以及获取AnimationDrawable
 *    1、android:src="@drawable/frame_anim_list"  取出方式getDrawable()
 *    2、android:background="@drawable/frame_anim_list" 取出方式getBackground()
 *    AnimationDrawable常用函数
 *    1、start()：开始播放逐帧动画
 *    2、stop()：停止播放逐帧动画
 *    3、getDuration(index)：得到指定index帧的持续时间
 *    4、getFrame(index)：得到指定index帧所对应的Drawable对象
 *    5、getNumberOfFrames():得到当前AnimationDrawable的所有帧数量
 *    6、isRunning()：判断当前AnimationDrawable是否正在播放
 *    7、setOneShot(oneSer)：设置AnimationDrawable是否执行一次，设置true表示执行一次，false表示循环播放
 *    8、isOneShot():判断当前AnimationDrawable是否执行一次，true表示执行一次，false表示循环播放
 *    9、addFrame(drawable, duration)：为AnimationDrawable添加1帧，并设置持续时间
 */
class ViewAnimationActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_view_animation
    }

    override fun initData() {
        xmlAnimation() // xml配置实现动画
        codeAnimation() // java代码实现动画
        attributeAnimation() // 动画常用方法和监听
        interpolatorAnimation() // 插值器
        frameAnimation() // 逐帧动画
    }

    /** TODO
     * 使用XML配置完成动画
     */
    fun xmlAnimation() {
        // 缩放
        a_v_a_scale_btn.setOnClickListener {
            a_v_a_scale_tv_1.startAnimation(getAnimation(R.anim.scaleanim))
            a_v_a_scale_tv_2.startAnimation(getAnimation(R.anim.scaleanim_pivot_50))
            a_v_a_scale_tv_3.startAnimation(getAnimation(R.anim.scaleanim_pivot_50bf))
            a_v_a_scale_tv_4.startAnimation(getAnimation(R.anim.scaleanim_pivot_50bfp))
        }
        // 淡化
        a_v_a_alpha_btn.setOnClickListener {
            a_v_a_alpha_tv_1.startAnimation(getAnimation(R.anim.alphaanim))
        }
        // 旋转动画
        a_v_a_rotate_btn.setOnClickListener {
            a_v_a_rotate_tv_1.startAnimation(getAnimation(R.anim.rotateanim))
            a_v_a_rotate_tv_2.startAnimation(getAnimation(R.anim.rotateanim_pivot_50))
            a_v_a_rotate_tv_3.startAnimation(getAnimation(R.anim.rotateanim_pivot_50bf))
            a_v_a_rotate_tv_4.startAnimation(getAnimation(R.anim.rotateanim_pivot_50bfp))
        }
        // 平移
        a_v_a_translate_btn.setOnClickListener {
            a_v_a_translate_tv_1.startAnimation(getAnimation(R.anim.translateanim))
            a_v_a_translate_tv_2.startAnimation(getAnimation(R.anim.translateanim_fromxdelta_50))
            a_v_a_translate_tv_3.startAnimation(getAnimation(R.anim.translateanim_fromxdelta_50bf))
            a_v_a_translate_tv_4.startAnimation(getAnimation(R.anim.translateanim_fromxdelta_50bfp))
        }
        // set动画集合
        a_v_a_set_btn.setOnClickListener {
            a_v_a_set_tv_1.startAnimation(getAnimation(R.anim.setanim))
        }
    }

    fun getAnimation(@AnimRes animId: Int):Animation{
        return AnimationUtils.loadAnimation(this, animId)
    }

    /** TODO
     * 使用java代码完成动画
     */
    fun codeAnimation() {
        // 缩放
        a_v_a_scale_btn_code.setOnClickListener {
            a_v_a_scale_tv_code_1.startAnimation(scaleAnimation())
        }
        // 淡化
        a_v_a_alpha_btn_code.setOnClickListener {
            a_v_a_alpha_tv_code_1.startAnimation(alphaAnimation())
        }
        // 旋转
        a_v_a_rotate_btn_code.setOnClickListener {
            a_v_a_rotate_tv_code_1.startAnimation(rotateAnimation())
        }
        // 平移
        a_v_a_translate_btn_code.setOnClickListener {
            a_v_a_translate_tv_code_1.startAnimation(translateAnimation())
        }
        // 动画集合
        a_v_a_set_btn_code.setOnClickListener {
            a_v_a_set_tv_code_1.startAnimation(setAnimation())
        }
    }
    /**
     * 缩放
     */
    fun scaleAnimation():Animation{
        // 指定缩放点为控件中心点
        val scaleAnimation = ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
        scaleAnimation.duration = 4000
        return scaleAnimation
    }
    /**
     * 淡化
     */
    fun alphaAnimation():Animation{
        val alphaAnimation = AlphaAnimation(0.0f, 1.0f)
        alphaAnimation.duration = 3000
        alphaAnimation.fillAfter = true
        alphaAnimation.interpolator = AccelerateInterpolator()
        return alphaAnimation
    }

    /**
     * 旋转
     */
    fun rotateAnimation():Animation {
        val rotateAnimation = RotateAnimation(0f, 720f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.duration = 4000
        return rotateAnimation
    }

    fun translateAnimation():Animation{
        val translateAnimation = TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f)
        translateAnimation.duration = 4000
        return translateAnimation
    }
    fun setAnimation():Animation{
        val animationSet = AnimationSet(false)
        animationSet.duration = 3000
        animationSet.addAnimation(scaleAnimation())
        animationSet.addAnimation(alphaAnimation())
        animationSet.addAnimation(rotateAnimation())

        val animationSet2 = AnimationSet(false)
        animationSet2.startOffset = 3000 //延时3秒后播放
        animationSet2.addAnimation(translateAnimation())
        animationSet.addAnimation(animationSet2)
        animationSet.fillAfter = true
        return animationSet
    }
//    var scaleAnimation:Animation? = null
    /** TODO
     * 动画常用方法和监听
     */
    fun attributeAnimation(){
        a_v_a_attribute_btn_code.setOnClickListener {
//            scaleAnimation?.cancel()
//            scaleAnimation?.reset()
            var scaleAnimation = scaleAnimation()
            scaleAnimation.fillAfter = true
            scaleAnimation?.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // 动画开始
                }

                override fun onAnimationEnd(animation: Animation?) {
                    // 动画结束- 设置新的动画
                    a_v_a_attribute_tv_code_1.startAnimation(rotateAnimation())
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // 动画重复
                }

            })
            a_v_a_attribute_tv_code_1.startAnimation(scaleAnimation)
        }
    }

    /** TODO
     * 插值器（Interpolator）：指定动画的变化速率
     */
    private fun interpolatorAnimation() {
        a_v_a_btn_adi.setOnClickListener {
            // 动画开始和结束时速率比较慢，中间会加速.
            // 即先加速后减速
            val alphaAnimation = translateAnimation()
            alphaAnimation.interpolator = AccelerateDecelerateInterpolator()
            alphaAnimation.duration = 6000
            a_v_a_tv_adi.startAnimation(alphaAnimation)
        }

        a_v_a_btn_ai.setOnClickListener {
            // AccelerateInterpolator 加速插值器
            // 会逐渐加速，然后突然停止（速率会越来越快）
            val alphaAnimation = translateAnimation()
            alphaAnimation.interpolator = AccelerateInterpolator()
            alphaAnimation.duration = 6000
            a_v_a_tv_ai.startAnimation(alphaAnimation)
        }
        a_v_a_btn_di.setOnClickListener {
            // DecelerateInterpolator 减速插值器
            // 开始的一瞬间速度加速到最大值，然后逐渐变慢，然后停止
            val alphaAnimation = translateAnimation()
            alphaAnimation.interpolator = DecelerateInterpolator()
            alphaAnimation.duration = 6000
            a_v_a_tv_di.startAnimation(alphaAnimation)
        }
        a_v_a_btn_li.setOnClickListener {
            // LinearInterpolator 匀速插值器
            // 速度保持恒定
            val alphaAnimation = translateAnimation()
            alphaAnimation.interpolator = LinearInterpolator()
            alphaAnimation.duration = 6000
            a_v_a_tv_li.startAnimation(alphaAnimation)
        }
        a_v_a_btn_bi.setOnClickListener {
            // BounceInterpolator 弹跳插值器
            // 模拟控件自由落体后回弹的效果
            val alphaAnimation = translateAnimation()
            alphaAnimation.interpolator = BounceInterpolator()
            alphaAnimation.duration = 6000
            a_v_a_tv_bi.startAnimation(alphaAnimation)
        }
        a_v_a_btn_anticipatei.setOnClickListener {
            // AnticipateInterpolator 初始偏移插值器
            // 动画开始时会向反方向偏移一段时间，然后再应用动画
            // 参数float tension默认=2，值越大，初始的偏移量越大，而且速度越快
            val alphaAnimation = translateAnimation()
            alphaAnimation.interpolator = AnticipateInterpolator(4f)
            alphaAnimation.duration = 6000
            a_v_a_tv_anticipatei.startAnimation(alphaAnimation)
        }
        a_v_a_btn_oi.setOnClickListener {
            // OvershootInterpolator 结束偏移插值器
            // 动画结束时会继续运动一段时间，然后再结束动画
            // 参数float tension默认=2，值越大，结束时向前的偏移量越大，而且速度越快
            val alphaAnimation = translateAnimation()
            alphaAnimation.interpolator = OvershootInterpolator(4f)
            alphaAnimation.duration = 6000
            a_v_a_tv_oi.startAnimation(alphaAnimation)
        }
        a_v_a_btn_aoi.setOnClickListener {
            // AnticipateInterpolator与OvershootInterpolator 的合体
            // 参数一：float tension：对应xml属性android:tension,表示张力，默认2，值越大，起始和结束时的偏移量越大，而且速度越快
            // 参数二：float extraTension：对应xml属性android:extraTension,表示额外张力值，默认值1.5
            val alphaAnimation = translateAnimation()
            alphaAnimation.interpolator = AnticipateOvershootInterpolator(4f, 4f)
            alphaAnimation.duration = 6000
            a_v_a_tv_aoi.startAnimation(alphaAnimation)
        }
        a_v_a_btn_ci.setOnClickListener {
            // CycleInterpolator 循环插值器
            // 表示动画循环播放特定的次数，速率沿正弦曲线改变
            // 参数float cycles：循环次数
            val alphaAnimation = translateAnimation()
            alphaAnimation.interpolator = CycleInterpolator(3f)
            alphaAnimation.duration = 6000
            a_v_a_tv_ci.startAnimation(alphaAnimation)
        }



    }

    /** TODO
     * 逐帧动画（Frame Animation）
     */
    private fun frameAnimation() {
        a_v_a_frame_btn.setOnClickListener {
            // 方式一： 如果是通过src设置的动画资源，则通过getDrawable()取出
            val anima = a_v_a_frame_tv.drawable as AnimationDrawable
//            方式二：如果是通过background设置动画资源，则通过getBackground()取出
//          val anima = a_v_a_frame_tv.background  as AnimationDrawable
            anima.isOneShot = false // 设置循环播放
            anima.addFrame(resources.getDrawable(R.mipmap.grape_pic), 1000)
            anima.start()
        }
        // 获取资源文件
        // String name, 资源ID打的名称
        // String defType, 资源所在的文件类型
        // String defPackage 应用包名
        resources.getIdentifier("banana_pic", "mipmap", packageName)
        resources.getIdentifier("drawer_open", "string", packageName)
        resources.getIdentifier("", "array", packageName)
    }
}