package com.zrt.kotlinapp.activity_view.custom_basic.paint_basic

import android.os.Bundle
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_paint_basic.*

/**
 * 一、禁用GPU硬件加速功能，
 *  ① application 层级禁用，在 AndroidManifest 文件中 applicationn 标签中添加如下属性：
 *      <applicationn android:hardwareAccelerated="true"/>
 *  ② activity 层级禁用，在 AndroidManifest 文件中， activity 标签中添加如下属性：
 *      <activity  android:hardwareAccelerated="true"/>
 *  ③ window 层级开起硬件加速（window层级上不支持关闭硬件加速）
 *      getWindow().setFlags(
 *          WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
 *          WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
 *  ④ 在 View 层级上使用如下代码关闭硬件加速（View 层级不支持开起硬件加速）
 *      setLayerType(View.LAYER_TYPE_SOFTWARE, null)
 *      或者使用 android:layerType="software" 关闭硬件加速
 * 二、画笔的基本使用
 *   ① 文字基准线：
 *   canvas.drawText(String text, float x, float y, Paint paint)
 *   String text: 文字内容
 *   float x, 文字X轴起始位置
 *   float y, 文字y轴基线位置
 *   Paint paint 画笔
 */
class PaintBasicActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int = R.layout.activity_paint_basic

    override fun initData() {
        a_p_b_psl_view.setOnClickListener {
            a_p_b_psl_view.isShadowLayer = !a_p_b_psl_view.isShadowLayer
        }
    }
}