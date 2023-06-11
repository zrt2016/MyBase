package com.zrt.kotlinapp.activity_view.actionbar

import android.app.Activity
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.title_layout.view.*

/**
 * @author：Zrt
 * @date: 2022/6/12
 */
class TitleLayout(context: Context, attrs: AttributeSet):LinearLayout(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.title_layout, this)
        title_back.setOnClickListener {
//            val activity = context as Activity
//            activity.finish()
            onBack()
        }
        title_edit.setOnClickListener {
            Toast.makeText(context, "You clicked Edit button", Toast.LENGTH_SHORT).show()
        }
    }
    fun onBack() {
        val activity = context as Activity
        activity.finish()
    }


}