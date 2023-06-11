package com.zrt.kotlinapp.animator.property_animation

import android.animation.Keyframe
import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.BounceInterpolator
import android.widget.Button
import android.widget.LinearLayout
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_propert_advanced.*

/**
 * 属性动画进阶
 * 1、PropertyValuesHolder与Keyframe
 * valueAnimator和ObjectAnimator中的ofPorpertyValuesHolder(PropertyValuesHolder)函数
 * 两个类的使用方式差不多,此处主要已ObjectAnimator为主.
 * PropertyValuesHolder类含义，保存动画过程中所需要操作的属性和对应的值。
 * 在ofFloat函数中内部实现其实就是将传入的参数封装成PropertyValuesHolder实例来保存动画状态
 * PropertyValuesHolder中常用函数
 *      ofFloat(propertName:String, varvrg values:float)
 *      ofInt(propertName:String, varvrg values:Int)
 *      ofObject(propertName:String, eva:TypeEvaluator, varvrg values:float)
 *      ofKeyframe(propertName:String, varvrg values:Keyframe) 关键帧动画
 *      setEvaluator() : 设置转换器
 *      setFolotValues(varvrg values:float)：设置ofFloat对应的动画值列表
 *      setIntValues(varvrg values:Int) 设置ofInt对应的动画值列表
 *      setKeyframe(varvrg values:Keyframe) 设置ofKeyframe对应的动画值列表
 *      setObject(varvrg values:Object) 设置ofObject对应的动画值列表
 *      setPropertyName(propertyName) 设置彩织动画属性名
 * 参数说明：
 *      propertName:String：表示ObjectAnimator操作的属性名
 *      values ： 属性所对应的参数，同样是可变长参数
 *      eva:TypeEvaluator：转换器，对插值器返回的进度值进行转换，转换为当前数值
 *      values:Keyframe 关键帧 记录当前进度下的动画数值
 * @see doPorpertyValuesHolder
 * ofPropertyValuesHolder(Object target, PropertyValuesHolder... values)
 *      Object target：操作VIew
 *      PropertyValuesHolder... values：多个动画属性包装实例
 * PropertyValuesHolder.ofObject("CharText", CharEvaluator(), 'A', 'Z')：创建字符变换动画
 * PropertyValuesHolder.ofKeyframe(String propertyName, Keyframe... values)：组合多个关键帧进行动画
 * Keyframe.ofFloat(0f, 0f) 或Keyframe.ofInt(1, 0) 关键帧
 *      参数1：当前显示进度，即在插值器中getInterpolation的返回值
 *      参数2：表示动画当前所在的数值位置
 *   给Keyframe在动作期间设置插值器
 *      setInterpolator(TimeInterpolator):给keyframe设置插值器
 *      例：Keyframe有kf1,kf2,kf3,kf4 共4个帧
 *      我们给kf2设置了插值器，那么kf1到kf2会使用该插值器。
 *      给kf3设置了插值器，那么kf2到kf3会使用该插值器。
 *
 * 2、ViewPropertyAnimator android3.1提供的为View的动画操作提供更加便捷的用法
 *  @see doViewPropertyAnimator
 *      ① 通过调用View的animate()获取ViewPropertyAnimator对象，然后通过这个对象设置动画的属性
 *      ② 自动开始，不用显示调用start()函数
 *      ③ 适合同时操作View多个属性的动画
 *      TextView.animate().alpha(0.1f) 设置View的淡化效果
 *      TextView.animate().x(50).y(100) 设置view的移动
 *      常用函数：
 *      alpha(Float) 透明度
 *      scaleX(Float) X轴缩放大小
 *      scaleY(Float) Y轴缩放大小
 *      translationX(Float) X轴平移
 *      translationY(Float) Y轴平移
 *      rotation(Float) 绕Z轴旋转
 *      rotationX(Float) 绕X轴旋转
 *      rotationY(Float) 绕Y轴旋转
 *      x(Float) 相对于父容器左上角X轴的最终位置
 *      y(Float)相对于父容器左上角Y轴的最终位置
 *  ****By 表示在原基础上的增量****
 *      alphaBy(Float) 设置透明度增量
 *      rotationBy(Float) 设置Z轴旋转增量
 *      rotationXBy(Float) 设置X轴旋转增量
 *      rotationYBy(Float) 设置Y轴旋转增量
 *      translationXBy(Float) X轴平移增量
 *      translationYBy(Float) Y轴平移增量
 *      scaleXBy(Float) X轴缩放大小增量
 *      scaleYBy(Float) Y轴缩放大小增量
 *      xBy(Float) 相对于父容器左上角X轴的最终位置增量
 *      yBy(Float)相对于父容器左上角Y轴的最终位置增量
 *      setInterpolater(TimeInterpolater) 设置插值器
 *      setStartDelay(long) 设置开始延时
 *      setDuration(long) 设置动画时长
 *      setListener(Animator.AnimatorListener(){})
 * 3、ViewGroup内的组件添加动画
 *  @see doViewGroup() animateLayoutChanges属性的使用 完成添加删除
 *  @see doLayoutTransition()
 *    例：ListView中的item在入场、出场时和数据变更时添加动画可以使用ViewGroup内的组件添加动画
 *    ① layoutAnimation标签和LayoutAnimationController（API 1引入） 针对ListView添加动画 （缺点：新增数据不能播放动画）
 *    ② gridAnimation标签和 GridAnimationController（API 1引入） 针对GridView添加动画（缺点：新增数据不能播放动画）
 *    ③ android：animateLayoutChanges属性：（API 11）支持ViewGroup类控件，在添加或移除控件时自动添加动画。
 *          只要在xml中添加该属性，就能实现。但不能自定义动画
 *    ④ LayoutTransition（API 11）实现ViewGroup动态添加或删除其中指定动画，动画可自定义
 *    LayoutTransition常用函数：
 *      setAnimator(int transitionType, Animator animator)
 *          int transitionType表示应用动画的对象范围，取值如下：
 *              LayoutTransition.APPEARING：元素在容器中出现的动画
 *              LayoutTransition.DISAPPEARING：元素在容器中消失的动画
 *              LayoutTransition.CHANGE_APPEARING：容器中出现新元素，其他元素所应用的动画
 *              LayoutTransition.CHANGE_DISAPPEARINGARING：容器中某个元素消失，其他元素所应用的动画
 *      setDuration(int transitionType, long duration) : 设置动画时长
 *          参数transitionType：设置单个transitionType的动画时长
 *      setInterpolator(int transitionType, interpolator) :添加插值器
 *          参数transitionType：针对单个transitionType设置插值器
 *      setStartDelay(int transitionType, long delay)：设置动画延时
 *          参数transitionType：针对单个transitionType设置动画延时
 *      setStagger(int transitionType, long duration):设置每个item动画的时间间隔
 *          参数transitionType：针对单个transitionType设置每个item动画的时间间隔
 *      addTransitionListener(object : LayoutTransition.TransitionListener{})：添加开始和结束监控
 *          override fun startTransition 四个参数
 *          override fun endTransition 四个参数
 *          参数1：transition: LayoutTransition?：当前LayoutTransition实例
 *          参数2：container: ViewGroup? ：当前应用容器
 *          参数3：view: View? ：当前做动画的View对象
 *              APPEARING、DISAPPEARING（对应View），
 *              CHANGE_APPEARING、APPECHANGE_DISAPPEARINGARING（对应容器对象）
 *          参数4：transitionType: Int ：当前LayoutTransition类型，取值：APPEARING、DISAPPEARING、CHANGE_APPEARING、APPECHANGE_DISAPPEARINGARING
 *    LayoutTransition使用说明：
 *   @see addLayoutTransition1  APPEARING 和 DISAPPEARING动画
 *   @see addLayoutTransition2
 *      ① CHANGE_APPEARING 和 APPECHANGE_DISAPPEARINGARING 使用比较特殊，必须使用PropertyValuesHolder构造动画才会有效果
 *         且必须添加 Left 和 Top 的属性变动，如果无需变动，值为 0 即可。如下：
 *          val pvhLeft = PropertyValuesHolder.ofInt("left", 0, 0)
 *          val pvhTop = PropertyValuesHolder.ofInt("top", 0, 0)
 *      ② 在构造PropertyValuesHolder动画时，所使用的ofInt和ofFloat函数参数的第一个值和最后一个值必须相同，否则视为放弃该动画
 *          原因：1f, 0f 开始动画(0->0.5 进度)，0f, 1f结束动画（0.5—>1 进度），
 *          val pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f)
 *          val pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f)
 * 4、开源动画库NineOldAndroids，可在各个版本上使用
 *     例：mView.animatr() --在动画库中--> ViewPropertyAnimator.animate(mView)
 *     画类：ViewHelper提供了一系列的静态set/get函数操作View的各种属性，无需考虑版本兼容问题：
 *     例：getAlpha(mView)/setAlpha(mView, alphaValue) 等
 */
class PropertAdvancedActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_propert_advanced
    }

    override fun initData() {
        doPorpertyValuesHolder()
        doViewPropertyAnimator()
        doViewGroup()
        doLayoutTransition()
    }

    private fun doPorpertyValuesHolder() {
        // PropertyValuesHolder.ofFloat 完成旋转和淡化动画
        a_p_a_btn_pvh_1.setOnClickListener {
            // 旋转属性修改
            val rotationP = PropertyValuesHolder.ofFloat("Rotation", 60f, -60f, 40f, -40f, 20f, -20f, 10f, -10f, 0f)
            // 淡化输血修改
            val alphaP = PropertyValuesHolder.ofFloat("Alpha", 0.2f, 0.8f, 0.4f, 1.0f, 0.6f)
            val objectAnima = ObjectAnimator.ofPropertyValuesHolder(a_p_a_tv_pvh_1, rotationP, alphaP)
            objectAnima.duration = 3000
            objectAnima.start()
        }
        val c:Character = Character('c')
        // PropertyValuesHolder.object自定义字符变换动画
        a_p_a_btn_pvh_2.setOnClickListener {
            val charP = PropertyValuesHolder.ofObject("CharText", CharEvaluator(), 'A', 'Z')
            val objectAnima = ObjectAnimator.ofPropertyValuesHolder(a_p_a_tv_pvh_2, charP)
            objectAnima.duration = 5000
            objectAnima.interpolator = AccelerateInterpolator()
            objectAnima.start()
        }
        // 关键帧动画
        a_p_a_btn_pvh_kf1.setOnClickListener {
            val kf1 = Keyframe.ofFloat(0f, 0f)
            val kf2 = Keyframe.ofFloat(0.1f, -30f)
            val kf3 = Keyframe.ofFloat(0.2f,  30f)
            val kf4 = Keyframe.ofFloat(0.3f,  -30f)
            val kf5 = Keyframe.ofFloat(0.4f,  30f)
            val kf6 = Keyframe.ofFloat(0.5f,  -30f)
            val kf7 = Keyframe.ofFloat(0.6f,  30f)
            val kf8 = Keyframe.ofFloat(0.7f,  -30f)
            val kf9 = Keyframe.ofFloat(0.8f,  30f)
            val kf10 = Keyframe.ofFloat(0.9f,  -30f)
            val kf11 = Keyframe.ofFloat(1f,  0f)
            val ofKeyframe1 = PropertyValuesHolder.ofKeyframe("rotation", kf1, kf2, kf3, kf4, kf5, kf6,
                    kf7, kf8, kf9, kf10, kf11)
            val kf111 = Keyframe.ofFloat(1f,  20f)
            val kf12 = Keyframe.ofFloat(1.1f,  -20f)
            val kf13 = Keyframe.ofFloat(1.2f,  10f)
            val kf14 = Keyframe.ofFloat(1.3f,  -10f)
            val kf15 = Keyframe.ofFloat(1f, 0f)
            val ofKeyframe2 = PropertyValuesHolder.ofKeyframe("rotation", kf1, kf2, kf3, kf4, kf5, kf6,
                    kf7, kf8, kf9, kf10, kf111, kf12, kf13, kf14, kf15)
            val anim1 = ObjectAnimator.ofPropertyValuesHolder(a_p_a_tv_pvh_kf1, ofKeyframe1)
            anim1.duration = 5000
            anim1.start()
            val anim2 = ObjectAnimator.ofPropertyValuesHolder(a_p_a_tv_pvh_kf2, ofKeyframe2)
            anim2.duration = 7000
            anim2.start()
        }
        // Keyframe（关键帧）与Interpolator（插值器）
        a_p_a_btn_pvh_kfi1.setOnClickListener {
            val k1 = Keyframe.ofFloat(0f, 0f)
            val k2 = Keyframe.ofFloat(0.2f, 50f)
            val k3 = Keyframe.ofFloat(0.4f, 100f)
            val k4 = Keyframe.ofFloat(0.6f, 150f)
            val k5 = Keyframe.ofFloat(0.8f, 200f)
            val k6 = Keyframe.ofFloat(1f, 250f)
            // k1到k2时会出现回弹效果
            k2.interpolator = BounceInterpolator() // 结束时回弹
            // k3到k4会出现前后平移一定位置
            k4.interpolator = AnticipateOvershootInterpolator() // 开始和结束向前后走一定值
            val keyframe = PropertyValuesHolder.ofKeyframe("TranslationX", k1, k2, k3, k4, k5, k6)
            val anim = ObjectAnimator.ofPropertyValuesHolder(a_p_a_tv_pvh_kfi1, keyframe)
            anim.duration = 6000
            anim.start()
        }
        // Keyframe（关键帧）ofObject 实现字符变换
        a_p_a_btn_pvh_kf_obj1.setOnClickListener {
            val k1 = Keyframe.ofObject(0f, Character('A'))
            val k2 = Keyframe.ofObject(0.2f, Character('L'))
            val k3 = Keyframe.ofObject(1f, Character('Z'))
            val keyframe = PropertyValuesHolder.ofKeyframe("CharText", k1, k2, k3)
            // 设置字符变换插值器
            keyframe.setEvaluator(CharEvaluator())
            val anim = ObjectAnimator.ofPropertyValuesHolder(a_p_a_tv_pvh_obj1, keyframe)
            anim.duration = 6000
            anim.start()
        }
        // 完整的响铃动画
        a_p_a_btn_pvh_all.setOnClickListener {
            // 旋转动画
            val kf1 = Keyframe.ofFloat(0f, 0f)
            val kf2 = Keyframe.ofFloat(0.1f, -30f)
            val kf3 = Keyframe.ofFloat(0.2f,  30f)
            val kf4 = Keyframe.ofFloat(0.3f,  -30f)
            val kf5 = Keyframe.ofFloat(0.4f,  30f)
            val kf6 = Keyframe.ofFloat(0.5f,  -30f)
            val kf7 = Keyframe.ofFloat(0.6f,  30f)
            val kf8 = Keyframe.ofFloat(0.7f,  -30f)
            val kf9 = Keyframe.ofFloat(0.8f,  30f)
            val kf10 = Keyframe.ofFloat(0.9f,  -30f)
            val kf11 = Keyframe.ofFloat(1f,  0f)
            val ofKeyframe1 = PropertyValuesHolder.ofKeyframe("rotation", kf1, kf2, kf3, kf4, kf5, kf6,
                    kf7, kf8, kf9, kf10, kf11)
            // X轴缩放动画
            val kf21 = Keyframe.ofFloat(0f, 1f)
            val kf22 = Keyframe.ofFloat(0.3f, 0.6f)
            val kf23 = Keyframe.ofFloat(0.8f, 0.6f)
            val kf24 = Keyframe.ofFloat(1f,  1f)
            val ofKeyframe2 = PropertyValuesHolder.ofKeyframe("ScaleX", kf21, kf22, kf23, kf24)
            val ofKeyframe3 = PropertyValuesHolder.ofKeyframe("ScaleY", kf21, kf22, kf23, kf24)
            val anim = ObjectAnimator.ofPropertyValuesHolder(a_p_a_tv_pvh_all, ofKeyframe1,
                    ofKeyframe2, ofKeyframe3)
            anim.duration = 5000
            anim.start()
        }
    }

    /**
     * 使用View的animate()完成动画
     */
    private fun doViewPropertyAnimator() {
        // 完成旋转、淡化、平移、缩放
        a_p_a_btn_vpa_1.setOnClickListener {
            // 每次重新开始
            val anim: ViewPropertyAnimator = a_p_a_tv_vpa_1.animate()
            anim.translationX(100f).alpha(0.5f).rotation(180f).scaleX(0.8f)
            anim.duration = 3000
//            anim.start()  // 默认隐式开起
            // By函数在原基础上移动
            // 每次都会在最后变化的基础上进行变化
            val animBy: ViewPropertyAnimator = a_p_a_tv_vpa_2.animate()
            animBy.translationXBy(50f).alphaBy(0.3f).rotationBy(180f).scaleXBy(0.1f)
            animBy.duration = 3000
        }
    }
    var viewCount = 0
    private fun doViewGroup() {
        // 使用 animateLayoutChanges 属性完成添加删除动画效果
        a_p_a_btn_alc_add.setOnClickListener {
            val btn = Button(this)
            val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            btn.layoutParams = lp
            btn.setText("BTN-$viewCount")
            a_p_a_btn_alc_ll.addView(btn)
            viewCount++
        }
        // 删除
        a_p_a_btn_alc_del.setOnClickListener {
            if (viewCount > 0){
                a_p_a_btn_alc_ll.removeViewAt(viewCount-1)
                viewCount--
            }
        }
    }

    var ltCount = 0
    var ltChangeCount = 0
    private fun doLayoutTransition() {
        // 设置出场动画和消失动画
        addLayoutTransition1(a_p_a_btn_lt_ll)
        // 添加动画
        a_p_a_btn_lt_add.setOnClickListener {
            val btn = Button(this)
            val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            btn.layoutParams = lp
            btn.setText("BTN-$ltCount")
            a_p_a_btn_lt_ll.addView(btn)
            ltCount++
        }
        // 隐藏动画
        a_p_a_btn_lt_del.setOnClickListener {
            if (ltCount > 0){
                a_p_a_btn_lt_ll.removeViewAt(ltCount-1)
                ltCount--
            }
        }
        // 设置 CHANGE_APPEARING 和 CHANGE_DISAPPEARING 的动画，即新增和删除View时，容器内其他View的变化
        addLayoutTransition2(a_p_a_btn_lt_change_ll)
        // 添加
        a_p_a_btn_lt_ca_add.setOnClickListener {
            val btn = Button(this)
            val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            btn.layoutParams = lp
            btn.setText("BTN-$ltChangeCount")
            a_p_a_btn_lt_change_ll.addView(btn, 0)
            ltChangeCount++
        }
        // 隐藏
        a_p_a_btn_lt_cd_del.setOnClickListener {
            if (ltChangeCount > 0){
                a_p_a_btn_lt_change_ll.removeViewAt(0)
                ltChangeCount--
            }
        }

    }



    private fun addLayoutTransition1(viewGroup: ViewGroup) {
        val lt = LayoutTransition()
        // 显示动画
        val animShow = ObjectAnimator.ofFloat(null, "alpha", 0f, 1f)
        lt.setAnimator(LayoutTransition.APPEARING, animShow)
        // 消失动画
        val pvhAlpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f)
        val pvhScaleX = PropertyValuesHolder.ofFloat("ScaleX", 1f, 0f)
        val pvhScaleY = PropertyValuesHolder.ofFloat("ScaleY", 1f, 0f)
        val animHide = ObjectAnimator.ofPropertyValuesHolder(viewGroup, pvhAlpha, pvhScaleX, pvhScaleY)
        //        val animHide = ObjectAnimator.ofFloat(null, "alpha", 1f, 0f)
        lt.setAnimator(LayoutTransition.DISAPPEARING, animHide)
        lt.setDuration(LayoutTransition.APPEARING, 2000)
        lt.setDuration(LayoutTransition.DISAPPEARING, 3000)
        viewGroup.layoutTransition = lt
    }
    private fun addLayoutTransition2(viewGroup: ViewGroup) {
        val ltChange = LayoutTransition()
        // CHANGE_APPEARING 和 CHANGE_DISAPPEARING 的动画 必须使用PropertyValuesHolder构造才会有效果
        // Left 和 Top 的属性变动必写，无需变动，值为 0 即可
        val pvhLeft = PropertyValuesHolder.ofInt("left", 0, 0)
        val pvhTop = PropertyValuesHolder.ofInt("top", 0, 0)
        // 在构造PropertyValuesHolder动画时，所使用的ofInt和ofFloat函数参数的第一个值和最后一个值必须相同，否则视为放弃该动画
        val pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f)
        val pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f)
        val animApp = ObjectAnimator.ofPropertyValuesHolder(viewGroup, pvhLeft, pvhTop, pvhScaleX, pvhScaleY)
        ltChange.setAnimator(LayoutTransition.CHANGE_APPEARING, animApp)
        val kf1 = Keyframe.ofFloat(0.0f, 0f)
        val kf2 = Keyframe.ofFloat(0.2f, -40f)
        val kf3 = Keyframe.ofFloat(0.4f, 40f)
        val kf4 = Keyframe.ofFloat(0.6f, -40f)
        val kf5 = Keyframe.ofFloat(0.8f, 40f)
        val kf6 = Keyframe.ofFloat(1f, 0f)
        val pvhRotate = PropertyValuesHolder.ofKeyframe("rotate", kf1, kf2, kf3, kf4, kf5, kf6)
        val animDis = ObjectAnimator.ofPropertyValuesHolder(viewGroup, pvhLeft, pvhTop, pvhRotate)
        ltChange.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, animDis)
        ltChange.setDuration(3000)
        viewGroup.layoutTransition = ltChange
        // 添加监控
        ltChange.addTransitionListener(object : LayoutTransition.TransitionListener{
            override fun startTransition(transition: LayoutTransition?, container: ViewGroup?, view: View?, transitionType: Int) {
                // 开始
            }
            override fun endTransition(transition: LayoutTransition?, container: ViewGroup?, view: View?, transitionType: Int) {
                // 结束
            }
        })
    }

}