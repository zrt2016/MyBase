package com.zrt.kotlinapp.animator.property_animation

import android.animation.*
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.animator.*
import kotlinx.android.synthetic.main.activity_property_animation.*

/**
 * android3.0引入
 * 属性动画（Property Animation），包含以下两种：
 *      ① ValueAnimator
 *      ② ObjectAnimator
 *  属性动画优点：属性动画可处理一个View在1分钟内从绿色变为红色，而补间动画不行。
 *  1、ValueAnimation（属性动画），通过修改控件属性完成动画
 *  @see valueAnimator()
 *     动画流程：ofInt(0,400) --> 插值器(Interpolator) --> Evaluator(转换器) --> 监听返回（addUpdateListener）
 *      常用属性：
 *      ValueAnimator ofInt(int... values)：传入值表示动画时的可变范围，例如（2,89,30）表示从2变化到90再变化到30
 *      ValueAnimator ofFloat(float... values)：传入值表示动画时的可变范围，含义与ofInt()函数相同，只有参数变为了float类型
 *      Object getAnimatedValue()：获取当前运动点的值
 *      setDuration(long duration)：设置动画时长
 *      start()：开始动画
 *      cancel()：取消动画
 *      setRepeatCount(int value)：设置循环次数，设置为Infinite表示无限循环
 *      setRepeatMode(int value)：设置循环模式
 *          ① ValueAnimation.RESTART：表示正序重新开始
 *          ① ValueAnimation.REVERSE：表示倒序重新开始
 *      不常用函数：
 *      setStartDelay(long startDelay)：延时指定时间后，开始动画
 *      clone()：完全克隆一个ValueAnimator实例，包括所有设置以及监听代码的处理
 *      监听事件：
 *      addUpdateListener(AnimatorUpdateListener listener)：监听动画过程中值的变化
 *          ① void onAnimationUpdate(ValueAnimator animation)
 *      addListener(Animator.AnimatorListener)：监听动画变化过程中的4个状态
 *          onAnimationStart(animation: Animator)：开始
 *          onAnimationEnd(animation: Animator)：结束
 *          onAnimationCancel(animation: Animator)：取消
 *          onAnimationRepeat(animation: Animator)：重复
 *      setInterpolator():设置插值器，控制动画进度值，详情可查看InterpolatorCustomUtils
 *      setEvaluator():设置转换器，将进度值转换为对应数值，详情可查看InterpolatorCustomUtils
 *  @see com.zrt.kotlinapp.animator.property_animation.InterpolatorCustomUtils
 *
 *  2、ObjectAnimator：派生自ValueAnimator类，ValueAnimation类使用的函数ObjectAnimator均可使用
 *  @see objectAnimator()
 *  动画流程：ofInt(view, "scaleY", 0,2,1) --> 插值器(Interpolator) --> Evaluator(转换器) --> 调用set函数（根据属性拼装set函数并反射调用）
 *      常用函数（基本与ValueAnimation一致）：
 *      ofFloat(Object target, String propertyName, float... values)
 *          参数1：指定动画操控的控件
 *          参数2：指定动画操作的属性，例：alpha、rotation、translate等,注意该值在View中必须存在对应的set函数
 *          参数3：可变长参数，操作属性值的变化
 *  3、AnimatorSet（组合动画）
 *  @see animatorSet()
 *      常用函数：
 *      playSequentially(Animator... items):可变长参数，依次取出动画进行播放
 *      playSequentially(List<Animator> items)：依次取list中的动画对象进行播放
 *      playTogether(Animator... items)：所有动画一起播放
 *      playTogether(List<Animator> items):所有动画一起播放
 *    AnimatorSet.Builder():自由组合动画播放，例A\B\C 三个动画，先播放A，在播放B和C
 *      常用函数：
 *      play(animator)：播放指定动画
 *      with(animator)：和前面动画一起执行
 *      before(animator)：先执行这个动画，在执行前面的动画 与play共用时，先播放before
 *      after(animator)：在前面动画执行完成后，执行该动画. 与play共用时，先播放after
 *      after(long)：延时n毫秒后 ，执行动画
 *      setDuration(duration)：单次动画时长，会覆盖子动画设置的动画时长
 *      setInterpolator(TimeInterpolator)：设置插值器
 *      setTarget(Object)：设置动画目标控件，在animatorSet中设置，会覆盖单个ObjectAnimator中的设置
 *      setStartDelay(delay)：针对AnimatorSet激活延时，不会影响子View设置的延时时间。
 *          AnimatorSet的激活延时时间 = AnimatorSet.StartDelay + 第一个动画.StartDelay
 *   AnimatorSet.addListener(Animator.AnimatorListener): 监听事件，只监听AnimatorSet的状态，不会监听子动画的状态
 *   animatorSet.addListener(object : Animator.AnimatorListener{
 *      override fun onAnimationStart(animation: Animator?) {} // 开始
 *      override fun onAnimationEnd(animation: Animator?) {} // 结束
 *      override fun onAnimationCancel(animation: Animator?) {} // 关闭
 *      override fun onAnimationRepeat(animation: Animator?) {} // 重复 需要对AnimatorSet设置循环才会监听到
 *   })
 * 4、xml实现Animator动画
 *  @see xmlAnimator
 *  在xml中与animator对应的有三个标签
 *  ① <animator/> 对应ValueAnimator 参考
 *  @see res.animator.xml_value_animator
 *  ② <objectAnimator/> 对应ObjectAnimator
 *  @see res.animator.xml_object_animator
 *  ③ <set/> 对应AnimatorSet
 *   valueAnim=AnimatorInflater.loadAnimator(this, R.animator.xml_value_animator) as ValueAnimator
 *   常用属性：
 *      android:duration="int" 动画时长
 *      android:valueFrom="Float|Int|color" 初始动画值
 *      android:valueTo ="Float|Int|color" 结束动画值
 *      android:startOffset="int" 动画激活延时
 *      android:repeatCount="int" 动画重复次数
 *      android:repeatMode="repeat" or "reverse" 重复模式，repeat（正序重播），reverse（倒序重播）
 *      android:valueType="intType" or "floatType" 表示参数类型，取值为intType或floatType，与valueFrom和valueTo对应。
 *          如果valueFrom和valueTo取值为color，则不需要设置该属性 或设置colorType（高版本存在）
 *          如果valueFrom和valueTo取值为int，则徐亚设置为intType
 *      android:interpolator="@android:interpolator/XXX" 设置插值器
 *   objectAnim=AnimatorInflater.loadAnimator(this, R.animator.xml_value_animator) as ObjectAnimator
 *   常用属性
 *      android:propertyName="string" 字段对应属性名，与ObjectAnimator所需要操作的属性名一致
 *      android:propertyXName="string" 字段对应属性名，与ObjectAnimator所需要操作的属性名一致
 *      android:propertyYName="string"字段对应属性名，与ObjectAnimator所需要操作的属性名一致
 *      其他属性与ValueAnimator基本一致
 */
class PropertyAnimationActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun getLayoutResID(): Int {
        return R.layout.activity_property_animation
    }

    override fun initData() {
        valueAnimator()
        objectAnimator()
        animatorSet()
        xmlAnimator()
    }

    var valueAnimator:ValueAnimator? = null
    private fun valueAnimator() {

        // 设置view平移
        a_p_a_btn.setOnClickListener {
            doAnimator(0, 400, 200, duration = 1000) {
                val curValue = it.getAnimatedValue() as Int
                // 通过layout函数改变TextView的位置
                a_p_a_tv_1.layout(curValue, a_p_a_tv_1.top, a_p_a_tv_1.width + curValue, a_p_a_tv_1.bottom)
            }
        }
        // 动画循环以及手动结束
        a_p_a_btn_start.setOnClickListener {
            valueAnimator?.cancel()
            valueAnimator = doAnimator2(0, 400, duration = 2000,
                    blockListener = {
                        val curValue = it.getAnimatedValue() as Int
                        a_p_a_tv_value.layout(curValue, a_p_a_tv_value.top,
                                curValue + a_p_a_tv_value.width, a_p_a_tv_value.bottom)
                    }, blockSet = {
                it.repeatMode = ValueAnimator.REVERSE
                it.repeatCount = ValueAnimator.INFINITE
                it.start()
            })
        }
        a_p_a_btn_cancel.setOnClickListener {
            valueAnimator?.let {
                it.cancel()
            }
        }

        var intReStart = 0;
        var intCount = 3
        // 循环动画时，更换图片
        a_p_a_btn_update.setOnClickListener {
            val anima = doAnimator2(0, 100, 0, duration = 2000,
                    blockListener = {
                        val curValue = it.getAnimatedValue() as Int
                        a_p_a_iv_update.layout(curValue, a_p_a_iv_update.top,
                                a_p_a_iv_update.width + curValue, a_p_a_iv_update.bottom)
                    }, blockSet = {
                it.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {
                        intReStart = 0
                    }

                    override fun onAnimationEnd(animation: Animator?) {}
                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationRepeat(animation: Animator?) {
                        intReStart++
                        when (intReStart % intCount) {
                            1 -> {
                                a_p_a_iv_update.setImageBitmap(BitmapFactory.decodeResource(resources, R.mipmap.pic_2))
                            }
                            2 -> {
                                a_p_a_iv_update.setImageBitmap(BitmapFactory.decodeResource(resources, R.mipmap.pic_3))
                            }
                            else -> { //默认0
                                a_p_a_iv_update.setImageBitmap(BitmapFactory.decodeResource(resources, R.mipmap.pic_1))
                            }
                        }
                    }
                })
                it.repeatMode = ValueAnimator.REVERSE
                it.repeatCount = ValueAnimator.INFINITE
                it.start()
            })
        }
        // 自定义实现循环加载变换图片
        a_p_a_btn_custom_update.setOnClickListener {
            a_p_a_iv_custom_update?.startAnim()
        }
        // 自定义插值器（interpolator）
        a_p_a_btn_custom_interpolator1.setOnClickListener {
            animatedValueDemo(MyInterpolator()){
                val curValue = it.animatedValue as Int
                a_p_a_tv_custom_interpolator1.layout(curValue, a_p_a_tv_custom_interpolator1.top,
                        a_p_a_tv_custom_interpolator1.width + curValue, a_p_a_tv_custom_interpolator1.bottom)
            }
        }
        // 自定义转化器（evaluator）
        a_p_a_btn_custom_evaluator1.setOnClickListener {
            animatedValueDemo2(MyInterpolator(), {
                it.setEvaluator(MyEvluator())
            }){
                val curValue = it.animatedValue as Int
                a_p_a_tv_custom_evaluator1.layout(curValue, a_p_a_tv_custom_evaluator1.top,
                        a_p_a_tv_custom_evaluator1.width + curValue, a_p_a_tv_custom_evaluator1.bottom)
            }
        }
        // 自定义倒序转化器（evaluator）
        a_p_a_btn_custom_evaluator2.setOnClickListener {
            animatedValueDemo2(MyInterpolator(), {
                it.setEvaluator(ReverseEvaluator())
            }){
                val curValue = it.animatedValue as Int
                a_p_a_tv_custom_evaluator2.layout(curValue, a_p_a_tv_custom_evaluator2.top,
                        a_p_a_tv_custom_evaluator2.width + curValue, a_p_a_tv_custom_evaluator2.bottom)
            }
        }
        // 自定义字符转化器（evaluator） object转换
        a_p_a_btn_custom_evaluator3.setOnClickListener {
            animatedObjectDemo({}){
                val curValue = it.animatedValue as Char
                Log.i(">>>>", "##animatedObjectDemo value=$curValue")
                a_p_a_tv_custom_evaluator3.setText(curValue.toString())
            }
        }
        var viewPoint:Point? = null
        // 抛物线动画
        a_p_a_btn_custom_1.setOnClickListener {
            if (viewPoint == null)
                viewPoint = Point(a_p_a_tv_custom_1.left, a_p_a_tv_custom_1.top)
            animatedFallingDemo(viewPoint) {
                val curPoint = it.getAnimatedValue() as Point
                a_p_a_tv_custom_1.layout(curPoint.x, curPoint.y,
                        curPoint.x + a_p_a_tv_custom_1.width,
                        curPoint.y + a_p_a_tv_custom_1.height)
            }
        }
    }

    fun doAnimator(vararg values: Int, duration: Long, block: (animation: ValueAnimator) -> Unit){
        //        val valueAnima = ValueAnimator()
        // 创建一个值从0到400的动画,对指定区间进行运算
        val valueAnima = ValueAnimator.ofInt(*values) // *values:展开可变参数
        valueAnima.duration = duration
        // 添加动画监听，监听运算过程，然后自己对控件执行动画操作
        valueAnima.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator) {
                Log.i(">>>>", "##animator value=${animation?.animatedValue}")
                block(animation)
            }
        })
        valueAnima.start()
    }
    fun doAnimator2(
            vararg values: Int, duration: Long,
            blockListener: (animation: ValueAnimator) -> Unit,
            blockSet: (valueAnima: ValueAnimator) -> Unit
    ): ValueAnimator{
        val valueAnima = ValueAnimator.ofInt(*values) // *values:展开可变参数
        valueAnima.duration = duration
        // 添加动画监听，监听运算过程，然后自己对控件执行动画操作
        valueAnima.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator) {
                Log.i(">>>>", "##animator value=${animation?.animatedValue}")
                blockListener(animation)
            }
        })
        // 监听动画变化时的4个状态
//        valueAnima.addListener(object : Animator.AnimatorListener{
//            override fun onAnimationStart(animation: Animator?) {
//                Log.i(">>>>", "##animator onAnimationStart")
//            }
//            override fun onAnimationEnd(animation: Animator?) {
//                Log.i(">>>>", "##animator onAnimationEnd")
//            }
//            override fun onAnimationCancel(animation: Animator?) {
//                Log.i(">>>>", "##animator onAnimationCancel")
//            }
//            override fun onAnimationRepeat(animation: Animator?) {
//                Log.i(">>>>", "##animator onAnimationRepeat")
//            }
//        })
        blockSet(valueAnima)
        return valueAnima
    }


    private fun objectAnimator() {
        // 淡化
        a_p_a_btn_obj_alpha.setOnClickListener {
            doObjectAnimatorFloat(a_p_a_tv_obj_alpha, "alpha", 1f,0f,1f)
        }
        // 旋转
        a_p_a_btn_obj_rotation.setOnClickListener {
            doObjectAnimatorFloat(a_p_a_tv_obj_rotation, "rotation", 0f, 180f, 0f)
        }
        // X轴旋转
        a_p_a_btn_obj_rotationX.setOnClickListener {
            doObjectAnimatorFloat(a_p_a_tv_obj_rotationX, "rotationX", 0f, 180f, 0f)
        }
        // X平移 :translationX , Y轴平移：translationY
        a_p_a_btn_obj_translationX.setOnClickListener {
            doObjectAnimatorFloat(a_p_a_tv_obj_translationX, "translationX", 0f, 200f, -200f, 0f)
        }
        // x轴缩放：scaleX  y周缩放：scaleY
        a_p_a_btn_obj_scaleX.setOnClickListener {
            doObjectAnimatorFloat(a_p_a_tv_obj_scaleX, "scaleX", 0f, 3f, 1f)
        }
        // 抛物线
        a_p_a_btn_obj_custom_1.setOnClickListener {
            val objectAnim = ObjectAnimator.ofObject(a_p_a_tv_obj_custom_1, "fallingPos",
                    FallingBallEvaluator(),Point(0,a_p_a_btn_obj_custom_1.bottom),
                    Point(100,a_p_a_btn_obj_custom_1.bottom+50))
            objectAnim.duration = 2000
            objectAnim.start()
        }
        a_p_a_btn_obj_cus_scaleX.setOnClickListener {
            // ScaleAnimTV未设置getScaleSize()函数，且只设置了1个values时，会默认从0开始
            // 设置getScaleSize()函数,会取该函数内的默认值
            // TODO 只有当动画存在一个过度值时，系统才会取对应属性的get函数来得到动画的初始值
            val objectAnim = ObjectAnimator.ofFloat(a_p_a_tv_obj_cus_scaleX, "ScaleSize", 2f)
            objectAnim.duration = 2000
            objectAnim.start()
        }
    }

    /**
     * @param view 指定动画操作的空间
     * @param anim  动画类型
     * @param value 动画变化数值
     */
    fun doObjectAnimatorFloat(view: View, anim: String, vararg value:Float): ObjectAnimator{
        val animator = ObjectAnimator.ofFloat(view, anim, *value)
        animator.duration = 2000
        animator.start()
        return animator
    }
    fun doObjectAnimatorFloat2(view: View, anim: String, vararg value:Float): ObjectAnimator{
        val animator = ObjectAnimator.ofFloat(view, anim, *value)
        animator.duration = 2000
        return animator
    }
    fun doObjectAnimatorInt(view: View, anim: String, vararg value: Int): ObjectAnimator{
        val animator = ObjectAnimator.ofInt(view, anim, *value)
        animator.duration = 2000
        animator.start()
        return animator
    }
    fun doObjectAnimatorInt2(view: View, anim: String, vararg value: Int): ObjectAnimator{
        val animator = ObjectAnimator.ofInt(view, anim, *value)
        animator.duration = 2000
        return animator
    }

    /** TODO
     * 组合动画
     */
    private fun animatorSet() {
        // 组合动画依次播放
        a_p_a_btn_set_1.setOnClickListener {
            val anim1 = doObjectAnimatorFloat2(a_p_a_tv_set_1, "translationX", 0f, 200f)
            val anim2 = doObjectAnimatorFloat2(a_p_a_tv_set_1, "alpha", 0.2f, 1f)
            val anim3 = doObjectAnimatorFloat2(a_p_a_tv_set_2, "translationX", 0f, 250f)
            val animator = AnimatorSet()
            animator.playSequentially(anim1, anim2, anim3)
            animator.duration = 3000
            animator.start()
        }
        // 组合动画同时播放
        a_p_a_btn_set_2.setOnClickListener {
            val anim1 = doObjectAnimatorFloat2(a_p_a_tv_set_3, "translationX", 0f, 200f)
            val anim2 = doObjectAnimatorFloat2(a_p_a_tv_set_3, "alpha", 0.2f, 1f)
            val anim3 = doObjectAnimatorFloat2(a_p_a_tv_set_4, "translationX", 0f, 250f)
            val list = mutableListOf<Animator>(anim1, anim2, anim3)
            val animator = AnimatorSet()
            animator.playTogether(list)
            animator.duration = 3000
            animator.start()
        }
        // 组合动画同时播放，部分延时
        a_p_a_btn_set_3.setOnClickListener {
            val anim1 = doObjectAnimatorInt2(a_p_a_tv_set_5, "backgroundColor", 0xFFFF00FF.toInt(),
                    0xFFFFFF00.toInt(), 0xFF00FFFF.toInt())
            val anim2 = doObjectAnimatorFloat2(a_p_a_tv_set_5, "alpha", 0.2f, 1f)
            anim2.startDelay = 3000
            val anim3 = doObjectAnimatorFloat2(a_p_a_tv_set_6, "translationX", 0f, 250f)
            anim3.startDelay = 3000
            val list = mutableListOf<Animator>(anim1, anim2, anim3)
            val animator = AnimatorSet()
            animator.playTogether(list)
            animator.duration = 3000
            animator.start()
        }
        // 依次播放，中间动画循环播放,最后动画在会被循环播放的动画阻塞住无法继续播放
        a_p_a_btn_set_4.setOnClickListener {
            val anim1 = doObjectAnimatorInt2(a_p_a_tv_set_7, "backgroundColor", 0xFFFF00FF.toInt(),
                    0xFFFFFF00.toInt(), 0xFF00FFFF.toInt())
            val anim2 = doObjectAnimatorFloat2(a_p_a_tv_set_7, "alpha", 0.2f, 1f)
            anim2.repeatCount = ValueAnimator.INFINITE
            val anim3 = doObjectAnimatorFloat2(a_p_a_tv_set_8, "translationX", 0f, 250f)
            anim3.startDelay = 3000
            val list = mutableListOf<Animator>(anim1, anim2, anim3)
            val animator = AnimatorSet()
            animator.playSequentially(list)
            animator.duration = 3000
            animator.start()
        }
        //animatorSet.Build 自由组合动画
        a_p_a_btn_set_build_1.setOnClickListener {
            val anim1 = doObjectAnimatorInt2(a_p_a_tv_set_build_1, "backgroundColor", 0xFFFF00FF.toInt(),
                    0xFF00FF00.toInt())
            val anim2 = doObjectAnimatorFloat2(a_p_a_tv_set_build_1, "translationX", 0f, 250f)
            val anim3 = doObjectAnimatorFloat2(a_p_a_tv_set_build_2, "translationX", 0f, 250f)
            val anim4 = doObjectAnimatorFloat2(a_p_a_tv_set_build_2, "alpha", 1f, 0.2f)
            val animatorSet = AnimatorSet()
            // 先2号平移，然后1号平移变色 以及 2号淡化
            animatorSet.play(anim1).with(anim2).after(anim3).with(anim4)
            animatorSet.duration = 3000
            animatorSet.start()
        }
        // animatorSet的监听事件,只监听animatorSet的状态，与其中动画无关
        var animSet:AnimatorSet? = null
        a_p_a_btn_set_listener_1.setOnClickListener {
            val anim1 = doObjectAnimatorInt2(a_p_a_tv_set_listener_1, "backgroundColor", 0xFFFF00FF.toInt(),
                    0xFFFFFF00.toInt(), 0xFF00FFFF.toInt())
            val anim2 = doObjectAnimatorFloat2(a_p_a_tv_set_listener_1, "alpha", 0.2f, 1f)
            anim2.repeatCount = 10
            val anim3 = doObjectAnimatorFloat2(a_p_a_tv_set_listener_2, "translationX", 0f, 250f)
            animSet = AnimatorSet()
            // anim3 先播放，然后在播放anim1和anim2
            animSet?.let {
                it.play(anim1).with(anim2).after(anim3)
                it.addListener(object : Animator.AnimatorListener{
                    override fun onAnimationStart(animation: Animator?) {
                        Log.i(">>>>", "onAnimationStart")
                    }
                    override fun onAnimationEnd(animation: Animator?) {
                        Log.i(">>>>", "onAnimationEnd")
                    }
                    override fun onAnimationCancel(animation: Animator?) {
                        Log.i(">>>>", "onAnimationCancel")
                    }
                    override fun onAnimationRepeat(animation: Animator?) {
                        Log.i(">>>>", "onAnimationRepeat")
                    }
                })
                it.duration = 3000
                it.start()
            }
        }
        a_p_a_btn_set_listener_cancel_1.setOnClickListener {
            animSet?.cancel()
        }

        // TODO AnimatorSet的延时设置setStartDelay
        a_p_a_btn_set_delay_1.setOnClickListener {
            val anim1 = doObjectAnimatorFloat2(a_p_a_tv_set_delay_1, "translationX", 0f, 250f)
            val anim2 = doObjectAnimatorFloat2(a_p_a_tv_set_delay_2, "translationX", 0f, 250f)
            anim2.startDelay = 2000
            val animatorSet = AnimatorSet()
            // 先延时2秒后，1号开始平移，2秒后2号开始平移
//            animatorSet.play(anim1).with(anim2)
            // 延时4秒（animatorSet.startDelay+anim2.startDelay），后1和2一起平移
            animatorSet.play(anim2).with(anim1)
            animatorSet.startDelay = 2000
            animatorSet.duration = 3000
            animatorSet.start()
        }
        
    }

    fun xmlAnimator() {
        // animator标签：
        a_p_a_btn_xml_value.setOnClickListener {
            val valueAnima = AnimatorInflater.loadAnimator(this, R.animator.xml_value_animator)
                    as ValueAnimator
            valueAnima.addUpdateListener{
                val offset = it.getAnimatedValue() as Int
                a_p_a_tv_xml_value.layout(offset, a_p_a_tv_xml_value.top,
                        a_p_a_tv_xml_value.width+offset, a_p_a_tv_xml_value.bottom)
            }
            valueAnima.start()
        }
        // TODO
        //objectAnimator，translatrionX 参数必须为float类型
        a_p_a_btn_xml_object.setOnClickListener {
            val objectAnima = AnimatorInflater.loadAnimator(this, R.animator.xml_object_animator)
                    as ObjectAnimator
            // 设置操作的view
            objectAnima.target = a_p_a_tv_xml_object
            objectAnima.start()
        }
    }
}