package com.zrt.kotlinapp.activity_view.custom_view_basic.view_basic

import android.os.Bundle
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R

/**
 * 自定义 View 绘图进阶，于： 自定义View控件实战开发第7章
 * @see MappingAdvancedUtils
 *
 *
 */
class MappingAdvancedActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_mapping_advanced
    }

    override fun initData() {

    }
}