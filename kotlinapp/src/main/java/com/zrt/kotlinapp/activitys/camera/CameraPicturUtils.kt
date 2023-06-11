package com.zrt.kotlinapp.activitys.camera

import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface

/**
 * @author：Zrt
 * @date: 2022/7/11
 */

/**
 * 获取图片角度，并对图片旋转
 * path:outputImage.path
 */
fun rotateIfRequired(bitmap: Bitmap, path:String): Bitmap {
    val exif = ExifInterface(path)
    val attributeInt = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
    return when(attributeInt){
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
        else -> bitmap
    }
}
// 旋转图片
private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degree.toFloat())
    val createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    bitmap.recycle()
    return createBitmap
}