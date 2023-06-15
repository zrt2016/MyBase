package com.zrt.kotlinapp.activity_view.custom_view_basic.paint_basic

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_paint_mixed_mode.*

/**
 * @see com.zrt.kotlinapp.activity_view.custom_view_basic.paint_basic.PaintMixedModeUtils
 * Paint 进阶 混合模式，能够实现两张图片的无缝结合
 *     Paint.setXfermode(Xfermode xfermode) 实现
 * 使用 Xfermode 时需禁用硬件加速 setLayerType(LAYER_TYPE_SOFTWARE, null)
 * Xfermode的子类：AvoidXfermode、PixelXorXfermode 和 PorterDuffXfermode
 *      PixelXorXfermode：API16中已过时，针对像素的简单异或运算（op^src^dst）,
 *          返回的Alpha始终是255，对于操作颜色混合不是特别有效很少用
 *
 */
class PaintMixedModeActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun getLayoutResID(): Int = R.layout.activity_paint_mixed_mode

    override fun initData() {
        a_p_m_m_lighten.setOnClickListener{
            a_p_m_m_lighten.isShowLighten = !a_p_m_m_lighten.isShowLighten
        }
        a_p_m_m_sp_mode.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, a_p_m_m_porterduff.porterDuffModeStr)
        a_p_m_m_sp_mode.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                a_p_m_m_porterduff.mode = a_p_m_m_porterduff.porterDuffMode[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        a_p_m_m_TwitterMultiply.setOnClickListener {
            a_p_m_m_TwitterMultiply.isShowXfermode = !a_p_m_m_TwitterMultiply.isShowXfermode
        }

    }
//    val porterDuffModeStr = arrayOf("CLEAR", "SRC", "DST", "SRC_OVER", "DST_OVER", "SRC_IN",
//            "DST_IN", "SRC_OUT", "DST_OUT", "SRC_ATOP", "DST_ATOP", "XOR", "DARKEN", "LIGHTEN",
//            "MULTIPLY", "SCREEN", "ADD", "OVERLAY")
//    val porterDuffMode = arrayOf(PorterDuff.Mode.CLEAR, PorterDuff.Mode.SRC, PorterDuff.Mode.DST,
//            PorterDuff.Mode.SRC_OVER, PorterDuff.Mode.DST_OVER, PorterDuff.Mode.SRC_IN,
//            PorterDuff.Mode.DST_IN, PorterDuff.Mode.SRC_OUT, PorterDuff.Mode.DST_OUT,
//            PorterDuff.Mode.SRC_ATOP, PorterDuff.Mode.DST_ATOP, PorterDuff.Mode.XOR,
//            PorterDuff.Mode.DARKEN, PorterDuff.Mode.LIGHTEN, PorterDuff.Mode.MULTIPLY,
//            PorterDuff.Mode.SCREEN, PorterDuff.Mode.ADD, PorterDuff.Mode.OVERLAY)
}