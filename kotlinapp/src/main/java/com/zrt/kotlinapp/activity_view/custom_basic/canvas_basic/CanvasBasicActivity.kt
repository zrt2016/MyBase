package com.zrt.kotlinapp.activity_view.custom_basic.canvas_basic

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_canvas_basic.*

class CanvasBasicActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int = R.layout.activity_canvas_basic

    override fun initData() {
        // 使用自定义圆角 Drawable 绘制
        val bitmap:Bitmap = BitmapFactory.decodeResource(resources, R.mipmap.avator)
        val drawable = DrawableCustomRound(bitmap)
//        drawable.setBounds(a_c_b_img_round_center.left + 20, a_c_b_img_round_center.top + 10
//                , a_c_b_img_round_center.right - 20, a_c_b_img_round_center.bottom - 10)
        a_c_b_img_round_center.setImageDrawable(drawable)
        a_c_b_img_round_fitStart.setImageDrawable(drawable)
        a_c_b_img_round_fitXY.setImageDrawable(drawable)
        a_c_b_img_round_centerInside.setImageDrawable(drawable)
        a_c_b_img_round_centerCrop.setImageDrawable(drawable)


        a_c_b_img_1.setOnClickListener {
            getBitmapFactoryOption(resources, R.mipmap.scenery, "", a_c_b_img_1, a_c_b_img_2)
        }
        a_c_b_img_3.setImageBitmap(getBitmapCreate6(resources))
        // 增加 Bitmap 的 density 值，会缩小图片
        getBitmapDensity(resources, a_c_b_img_4,a_c_b_img_5)
        // 修改 Bitmap 的每个 Pixel 值，改变图片
        getBitmapPixel(resources, a_c_b_img_6, a_c_b_img_7)
        // 展示不同格式压缩后的图片依次 原图片，JPEG、PNG、WEBP
        getBitmapCompress(resources, a_c_b_img_8, a_c_b_img_9, a_c_b_img_10, a_c_b_img_11)
    }


}