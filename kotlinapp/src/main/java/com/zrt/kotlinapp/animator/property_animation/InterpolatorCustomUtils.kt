package com.zrt.kotlinapp.animator.property_animation


import android.animation.ArgbEvaluator
import android.animation.IntEvaluator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.graphics.Point
import android.os.Build
import android.util.Log
import android.view.View
import android.view.animation.Interpolator
import androidx.annotation.RequiresApi

/**
 * @author：Zrt
 * @date: 2023/2/15
 * 自定义插值器
 * 插值器的实现，
 * 例：
 * 1、 LinearInterpolator（匀速插值器） extends BaseInterpolator
 * 2、 BaseInterpolator implements Interpolator
 * 3、 Interpolator extends TimeInterpolator
 * 4、TimeInterpolator接口中只有一个函数 float getInterpolation(float input);
 *    ① input 参数：取值范围0 ~ 1， 表示动画进度。0：表示动画刚开始。1：表示动画结束。0.5表示动画中间的位置。
 *    ② 返回值 ：表示当前动画的要显示的进度。取值可以大于1，也可以小于0.大于1表示超过目标值。小于0表示小于开始的位置
 * 5、关于addUpdateListener(ValueAnimator.AnimatorUpdateListener)的进度值
 *  animation.animatedValue如何计算可看示例animatedValueDemo()
 *  @see animatedValueDemo
 *  6、Evaluator：对插值器返回的数值进度计算当前值，然后给监听器。
 *      ① 显示设置：valueAnima.setEvaluator(IntEvaluator())
 *      ② evaluate(float fraction, Integer startValue, Integer endValue)
 *          float fraction：插值器返回的值
 *          Integer startValue, Integer endValue分别是ofInt中的start和end
 *  7、ValueAnimator完整的数值进度转换流程：
 *      ofInt(0,400) --> 插值器(Interpolator) --> Evaluator(转换器) --> 监听返回（addUpdateListener）
 *  8、ArgbEvaluator：颜色过度转化器，通过计算A、R、G、B四个颜色的值进行变化
 *  @see animatedArgbDemo  ： ValueAnimator.ofArgb(0xFFFFFF00.toInt(), 0xFF0000FF.toInt())
 *  9、ValueAnimator ofObject(TypeEvaluator evaluator, Object... values)
 *      参数1：自定义的Evaluator转化器
 *      参数2：可变长参数，属于Object类型
 *  10、抛物线动画
 *  @see animatedFallingDemo
 */

/** TODO addUpdateListener中的动画值
 * animatedValue = 100 + (400 - 100) * 显示进度
 * 第一个100：为初始值
 * 400：最大值
 * 显示进度：getInterpolation()
 * 例全程30%进度的位置：100+(400-100) * 0.3
 */
fun animatedValueDemo(interpolator: Interpolator, block: (animation: ValueAnimator) -> Unit){
    animatedValueDemo2(interpolator, {
        it.setEvaluator(IntEvaluator())
    }, block)
}

fun animatedValueDemo2(
        interpolator: Interpolator, setAnim: (animation: ValueAnimator) -> Unit,
        block: (animation: ValueAnimator) -> Unit
){
    val valueAnima = ValueAnimator.ofInt(100, 400) // *values:展开可变参数
    valueAnima.duration = 2000
    setAnim(valueAnima)
    // 添加动画监听，监听运算过程，然后自己对控件执行动画操作
    valueAnima.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
        override fun onAnimationUpdate(animation: ValueAnimator) {
            val curValue = animation.animatedValue
            block(animation)
        }
    })
    valueAnima.start()
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun animatedArgbDemo(interpolator: Interpolator, setAnim: (animation: ValueAnimator) -> Unit,
        block: (animation: ValueAnimator) -> Unit) {
    val argbAnim = ValueAnimator.ofArgb(0xFFFFFF00.toInt(), 0xFF0000FF.toInt())
    argbAnim.setDuration(3000)
    argbAnim.setEvaluator(ArgbEvaluator())
    setAnim(argbAnim)
    argbAnim.addUpdateListener{
        block(it)
    }
    argbAnim.start()
}

/** TODO Object类型动画
 * 字符变换动画
 */
fun animatedObjectDemo(setAnim: (animation: ValueAnimator) -> Unit,
        block: (animation: ValueAnimator) -> Unit){
    val objectAnim = ValueAnimator.ofObject(CharEvaluator(), 'A', 'Z')
    objectAnim.setDuration(5000)
    objectAnim.setEvaluator(CharEvaluator())
    setAnim(objectAnim)
    objectAnim.addUpdateListener{
        block(it)
    }
    objectAnim.start()
}

/**
 * 抛物线动画
 */
fun animatedFallingDemo(point: Point?, block: (animation: ValueAnimator) -> Unit){
    val fallingAnim = ValueAnimator.ofObject(FallingBallEvaluator(),
            point?.let { Point(it.x,point.y) }, point?.let { Point(it.x+150,point.y+50) })
//    val fallingAnim = ValueAnimator.ofObject(FallingBallEvaluator(),
//            Point(0,0), Point(150, 50))
    fallingAnim.duration = 5000
    fallingAnim.addUpdateListener{
        block(it)
    }
    fallingAnim.start()
}


/** TODO 自定义插值器：Interpolator
 *
 */
class MyInterpolator:Interpolator{
    override fun getInterpolation(input: Float): Float {
        // 传入0时，数值进度在完成的位置；完成的时候，数值进度在开始的位置
        return 1 - input
    }
}

/** TODO 自定义转换器：Evaluator
 *
 */
class MyEvluator:TypeEvaluator<Int>{
    override fun evaluate(fraction: Float, startValue: Int, endValue: Int): Int {
        return (200 + startValue + fraction * (endValue - startValue)).toInt()
    }
}
/** TODO 自定义倒序转换器：Evaluator
 *
 */
class ReverseEvaluator:TypeEvaluator<Int>{
    override fun evaluate(fraction: Float, startValue: Int, endValue: Int): Int {
        return (endValue - fraction * (endValue - startValue)).toInt()
    }
}

/**
 * 字符转换器
 */
class CharEvaluator:TypeEvaluator<Char>{
    // 利用ASCII码表中对应数字是连续且递增的原理，球场对应字符的数字，然后转成对应的字符
    override fun evaluate(fraction: Float, startValue: Char, endValue: Char): Char {
        val sInt = startValue.toInt()
        val eInt = endValue.toInt()
        val curInt:Int = (sInt + fraction * (eInt - sInt)).toInt()
//        Log.i(">>>>", "##curInt=$curInt+; char=${curInt.toChar()}")
        return curInt.toChar()
    }
}

/**
 * 绘制一个抛物线动画，计算每次移动点位
 */
class FallingBallEvaluator:TypeEvaluator<Point>{
    val point = Point()
    override fun evaluate(fraction: Float, startValue: Point, endValue: Point): Point {
        point.x = (startValue.x + fraction * (endValue.x - startValue.x)).toInt()
        if (fraction * 2 <= 1){
            point.y = (startValue.y + fraction * 2 * (endValue.y - startValue.y)).toInt()
        }else {
            point.y = endValue.y
        }
        return point
    }

}