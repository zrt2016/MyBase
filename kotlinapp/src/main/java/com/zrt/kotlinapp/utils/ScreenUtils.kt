package com.zrt.kotlinapp.utils

import android.app.Activity
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.view.View
import android.view.Window
import androidx.core.app.ActivityCompat

/**
 * @author：Zrt
 * @date: 2022/12/5
 */
/**
 * 特殊节日，设置屏幕灰度
 * @param window activity.getWindow()
 * @param gray 0：灰色显示。 1:取消灰色
 */
fun setGraySheme(window: Window, gray: Int) {
    val decorView: View = window.getDecorView()
    val paint = Paint()
    val cm = ColorMatrix()
    cm.setSaturation(gray.toFloat()) //灰度效果 取值gray（0 - 1）  0是灰色，1取消灰色
    paint.colorFilter = ColorMatrixColorFilter(cm)
    decorView.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
}