package com.zrt.kotlinapp.activity_view.custom_view.loading_view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.zrt.kotlinapp.R

/**
 * @author：Zrt
 * @date: 2023/5/31
 */

class BounceLoadingView: LinearLayout {
    lateinit var mContext: Context
    // 动画view
    lateinit var shapeLoadingView:ImageView
    // 阴影view
    lateinit var indication:ImageView
    constructor(context: Context):this(context, null)
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr) {
        View.inflate(context, R.layout.view_loading_bounce, null)
        mContext = context
        shapeLoadingView = findViewById(R.id.shapeLoadingView)
        indication = findViewById(R.id.indication)
    }

}