package com.zrt.kotlinapp.activity_view.custom_basic.matrix_basic

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.SeekBar
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_matrix_basic.*

class MatrixBasicActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int {
         return  R.layout.activity_matrix_basic
    }

    override fun initData() {
        // 色彩饱和度
        saturationColorMatrixBmp()
        // 色彩缩放
        scaleColorMatrixBmp()
        // 色彩旋转
        rotateColorMatrixBmp()
    }

    private fun saturationColorMatrixBmp() {
        val bitmapDrawable = a_m_b_iv_saturation.drawable as BitmapDrawable
        var mOriginBitmap = bitmapDrawable.bitmap
        var tempBitmap = Bitmap.createBitmap(mOriginBitmap.width, mOriginBitmap.height, Bitmap.Config.ARGB_8888)
        a_m_b_sk_saturation.max = 200
        a_m_b_iv_saturation.setImageBitmap(handleColorMatrixSaturationBmp(mOriginBitmap, tempBitmap, a_m_b_sk_saturation.progress))
        a_m_b_sk_saturation.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                a_m_b_iv_saturation.setImageBitmap(handleColorMatrixSaturationBmp(mOriginBitmap, tempBitmap, progress))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun scaleColorMatrixBmp() {
        val bitmapDrawable = a_m_b_iv_scale.drawable as BitmapDrawable
        var mOriginBitmap = bitmapDrawable.bitmap
        var tempBitmap = Bitmap.createBitmap(mOriginBitmap.width, mOriginBitmap.height, Bitmap.Config.ARGB_8888)
        a_m_b_sk_scale.max = 100 // 最大进度
        a_m_b_sk_scale.progress = 50 // 当前进度
        a_m_b_iv_scale.setImageBitmap(handleColorMatrixScaleBmp(mOriginBitmap, tempBitmap, a_m_b_sk_scale.progress))
        a_m_b_sk_scale.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                a_m_b_iv_scale.setImageBitmap(handleColorMatrixScaleBmp(mOriginBitmap, tempBitmap, progress))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

    }

    private fun rotateColorMatrixBmp() {
        val bitmapDrawable = a_m_b_iv_rotate.drawable as BitmapDrawable
        var mOriginBitmap = bitmapDrawable.bitmap
        var tempBitmap = Bitmap.createBitmap(mOriginBitmap.width, mOriginBitmap.height, Bitmap.Config.ARGB_8888)
        a_m_b_sk_rotate.max = 360 // 最大进度
        a_m_b_sk_rotate.progress = 180 // 当前进度
        a_m_b_iv_rotate.setImageBitmap(handleColorMatrixRotateBmp(mOriginBitmap, tempBitmap, a_m_b_sk_rotate.progress))
        a_m_b_sk_rotate.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                a_m_b_iv_rotate.setImageBitmap(handleColorMatrixRotateBmp(mOriginBitmap, tempBitmap, progress))
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

    }


}