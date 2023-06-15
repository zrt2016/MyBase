package com.zrt.kotlinapp.activity_view.custom_view.zuhe_view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.IdRes
import com.zrt.kotlinapp.R

/**
 * 组合View
 * @author：Zrt
 * @date: 2023/6/12
 */

class TabListLayout: LinearLayout {
    lateinit var mContext: Context
    lateinit var tabTop: RadioGroup
    lateinit var tabBottom: RadioGroup
    lateinit var tabList: ListView
    var mAdapter: BaseAdapter? = null
    constructor(context: Context):this(context, null)
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr) {
        val view = View.inflate(context, R.layout.tab_list_layout, this)
        mContext = context
        tabTop = view.findViewById(R.id.tab_top)
        tabBottom = view.findViewById(R.id.tab_bottom)
        tabList = view.findViewById(R.id.tab_list)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabListLayout)
        setTabTopVisibility(typedArray.getInt(R.styleable.TabListLayout_topVisibility, View.VISIBLE))
        setTabBottomVisibility(typedArray.getInt(R.styleable.TabListLayout_bottomVisibility, View.VISIBLE))
        typedArray.recycle()
    }
    // 设置顶部 Tab 是否显示
    fun setTabTopVisibility(visibility:Int) {
        tabTop.visibility = visibility
    }

    // 设置顶部 Tab 是否隐藏
    fun setTabBottomVisibility(visibility:Int) {
        tabBottom.visibility = visibility
    }

    fun setAdapter(adapter:BaseAdapter) {
        this.mAdapter = adapter
        tabList.adapter = this.mAdapter
    }
    fun addTabTopListener(listener: RadioGroup.OnCheckedChangeListener) {
        tabTop.setOnCheckedChangeListener(listener)
    }
    fun addTabBottomListener(listener: RadioGroup.OnCheckedChangeListener) {
        tabBottom.setOnCheckedChangeListener(listener)
    }
    /**
     * 添加Tab按钮
     * @param tabInfoList
     */
    fun addTabTopRadio(tabInfoList: List<TabTopBasicInfo>) {
        tabTop.removeAllViews()
        for (i in tabInfoList.indices) {
            val tabInfo: TabTopBasicInfo = tabInfoList[i]
            val view = LayoutInflater.from(mContext).inflate(R.layout.tab_list_top_radio, null) as RadioButton
            view.setText(tabInfo.tabName)
            view.setTextColor(Color.WHITE)
            view.id = i
            view.setBackgroundResource(R.drawable.tab_top_titile_bj_selector)
            view.buttonDrawable = ColorDrawable(Color.TRANSPARENT)
            val layoutParams = RadioGroup.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f)
            layoutParams.setMargins(10, 10, 10, 10)
            tabTop.addView(view, layoutParams)
            tabInfo.id = view.id
            tabInfo.view = view
        }
    }

    /**
     * 添加Tab按钮
     * @param tabInfoList
     */
    fun addTabBottomRadio(tabInfoList: List<TabBottomBasicInfo>) {
        tabBottom.removeAllViews()
        for (i in tabInfoList.indices) {
            val tabInfo: TabBottomBasicInfo = tabInfoList[i]
            val view = LayoutInflater.from(mContext).inflate(R.layout.tab_list_top_radio, null) as RadioButton
            view.setText(tabInfo.tabName)
            view.setTextColor(Color.WHITE)
            view.id = i
            view.setBackgroundResource(R.drawable.tab_top_titile_bj_selector)
            view.buttonDrawable = ColorDrawable(Color.TRANSPARENT)
            val layoutParams = RadioGroup.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f)
            layoutParams.setMargins(10, 10, 10, 10)
            tabBottom.addView(view, layoutParams)
            tabInfo.id = view.id
            tabInfo.view = view
        }
    }

    /**
     * 顶部tab
     */
    open class TabTopBasicInfo {
        var tabName = ""
        @IdRes var id = 0
        var view: RadioButton? = null
        constructor(_tabName:String) {
            tabName = _tabName
        }
    }

    /**
     * 底部tab
     */
    open class TabBottomBasicInfo {
        var tabName = ""
        @IdRes var id = 0
        var view: RadioButton? = null
        constructor(_tabName:String) {
            tabName = _tabName
        }
    }
}