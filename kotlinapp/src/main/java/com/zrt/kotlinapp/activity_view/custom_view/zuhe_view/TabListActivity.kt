package com.zrt.kotlinapp.activity_view.custom_view.zuhe_view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_tab_list.*

class TabListActivity : BasicActivity() {
    val tabTopList = arrayListOf<TabListLayout.TabTopBasicInfo>()
    val tabBottomList = arrayListOf<TabListLayout.TabBottomBasicInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_tab_list
    }

    override fun initData() {
        initTabList()
        // 添加顶部 tab
        a_t_l_tablist.addTabTopRadio(tabTopList)
        a_t_l_tablist.addTabTopListener(object :RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {

            }
        })
        // 添加底部 tab
        a_t_l_tablist.addTabBottomRadio(tabBottomList)
        a_t_l_tablist.addTabBottomListener(object :RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {

            }
        })
    }

    private fun initTabList() {
        // 初始化顶部 tab 数据
        tabTopList.add(TabListLayout.TabTopBasicInfo("全部"))
        tabTopList.add(TabListLayout.TabTopBasicInfo("输液"))
        tabTopList.add(TabListLayout.TabTopBasicInfo("注射"))
        tabTopList.add(TabListLayout.TabTopBasicInfo("口服"))
        tabTopList.add(TabListLayout.TabTopBasicInfo("雾化"))
        tabTopList.add(TabListLayout.TabTopBasicInfo("其他"))
        // 初始化底部 tab 数据
        tabBottomList.add(TabListLayout.TabBottomBasicInfo("未执行"))
        tabBottomList.add(TabListLayout.TabBottomBasicInfo("开始执行"))
        tabBottomList.add(TabListLayout.TabBottomBasicInfo("执行完毕"))
    }
}