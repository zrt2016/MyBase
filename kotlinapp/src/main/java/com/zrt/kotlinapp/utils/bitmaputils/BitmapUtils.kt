package com.zrt.kotlinapp.utils.bitmaputils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.zrt.kotlinapp.R

/**
 * @author：Zrt
 * @date: 2023/3/22
 */
object BitmapUtils {
    fun basic(context:Context) {
        val bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.circle1)
        val bitmapAlpha = bitmap.extractAlpha() // 新建一张空图片，该图片具有与原图片一样的 Alpha 值
    }
}