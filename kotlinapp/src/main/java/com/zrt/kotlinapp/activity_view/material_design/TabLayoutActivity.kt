package com.zrt.kotlinapp.activity_view.material_design

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.activity_view.material_design.utils.FragmentAdapter
import com.zrt.kotlinapp.activity_view.material_design.utils.VPFragment
import kotlinx.android.synthetic.main.activity_tab_layout.*
import kotlinx.android.synthetic.main.mytoolbar.*

/**
 * TabLayoutActivity:
 * app:tabIndicatorColor="@color/pale_yellow" 下划方块颜色
 * app:tabMode="scrollable" scrollable：可滑动的， fixed：不可滑动的
 */
class TabLayoutActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int = R.layout.activity_tab_layout

    override fun initData() {
        setSupportActionBar(my_toolbar)
        val titles = initList()
        var fragments: MutableList<Fragment> = ArrayList<Fragment>()
        for (i in 0 until titles.size){
            a_tl_tab.addTab(a_tl_tab.newTab().setText(titles[i]))
            fragments.add(VPFragment())
        }
        val fgAdapter = FragmentAdapter(supportFragmentManager, fragments, titles)
        a_tl_viewpager.adapter = fgAdapter
        a_tl_tab.setupWithViewPager(a_tl_viewpager)
        // 过时，已被setupWithViewPager替换
//        a_tl_tab.setTabsFromPagerAdapter(fgAdapter)

    }

    fun initList():List<String>{
        val titles: MutableList<String> = ArrayList()
        titles.add("精选")
        titles.add("体育")
        titles.add("巴萨")
        titles.add("购物")
        titles.add("明星")
        titles.add("视频")
        titles.add("健康")
        titles.add("励志")
        titles.add("图文")
        titles.add("本地")
        titles.add("动漫")
        titles.add("搞笑")
        titles.add("精选")
        return titles
    }
}