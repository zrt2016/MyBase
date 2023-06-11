package com.zrt.kotlinapp.activity_view.material_design

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.activity_view.material_design.utils.VPFragment2Adapter
import com.zrt.kotlinapp.activity_view.material_design.utils.VPTransformer
import com.zrt.kotlinapp.utils.LogUtil
import kotlinx.android.synthetic.main.activity_view_pager2.*

/**
 * https://blog.csdn.net/vitaviva/article/details/111409797
 *
 * ViewPager2最显著的特点是基于RecyclerView实现，RecyclerView是目前Android端最成熟的AdapterView解决方案，这带来诸多好处：
 *   抛弃传统的PagerAdapter，统一了Adapter的API
 *   通过LinearLayoutManager可以实现类似抖音的纵向滑动
 *   支持DiffUitl，可以通过diff实现局部刷新
 *   支持RTL（right-to-left）布局，对于一些有出海需求的APP非常有用
 *   支持ItemDecorator
 * ViewPager2跟ViewPager一样，除了View以外，ViewPager2更多的是配合Fragment的使用，
 * 这需要借助FragmentStateAdapter：
 * ViewPager适配器：FragmentStatePagerAdapter、PagerAdapter（27已废弃）
 * ViewPager2适配器：FragmentStateAdapter
 *
 * ViewPager2与TabLayout的绑定需要用到TabLayoutMediator控件
 *   TabLayoutMediator，这个类是在 material-1.2.0 中新增的一个类
 *   依赖：implementation 'com.google.android.material:material:1.2.0-alpha03'
 *
 * PageTransformer：ViewPager2可通过其设置页面动画
 * CompositePageTransformer：实现了PageTransformer接口，内部维护了一个 PageTransformer的List集合
 */
class ViewPager2Activity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int = R.layout.activity_view_pager2

    override fun initData() {
        a_vp2_1.adapter = VPFragment2Adapter(this, 10)
        // 监听Viewpager2的页面滑动
        a_vp2_1.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                LogUtil.i("", "##ViewPager2 position:$position")
            }
        })
        // 设置Viewpager禁止滑动
//        a_vp2_1.isUserInputEnabled = false
        // fakeDragBy:模拟拖拽效果：参数值为正数时表示向前一个页面滑动，当值为负数时表示向下一个页面滑动
//        a_vp2_1_fakeDragBy.setOnClickListener {
//            a_vp2_1.beginFakeDrag();
//            if (a_vp2_1.fakeDragBy(-100f)) {
//                a_vp2_1.endFakeDrag();
//            }
//        }
        // 添加滑动动画效果
        val pageTransformer = CompositePageTransformer()
//        pageTransformer.addTransformer(VPTransformer())
//        pageTransformer.addTransformer(VPTransformer(VPTransformer.TYPE_SCALE))
//        pageTransformer.addTransformer(VPTransformer(VPTransformer.TYPE_ZOOM))
        pageTransformer.addTransformer(VPTransformer(VPTransformer.TYPE_ZOOM2))
        // 边界
//        pageTransformer.addTransformer(MarginPageTransformer(50))
        a_vp2_1.setPageTransformer(pageTransformer)

        a_vp2_2.adapter = VPFragment2Adapter(this, 10)
        // 设置ViewPager2垂直滑动
        a_vp2_2.orientation = ViewPager2.ORIENTATION_VERTICAL

        val titles = initList()
        a_vp2_3.adapter = VPFragment2Adapter(this, titles.size)
        for (i in 0 until titles.size){
            a_vp2_tab.addTab(a_vp2_tab.newTab().setText(titles[i]))
        }
        //预加载页面数
        a_vp2_3.offscreenPageLimit = 3
//        a_vp2_tab.setupWithViewPager(a_vp2_3)
        // 绑定ViewPager2和TabLayout
        TabLayoutMediator(a_vp2_tab, a_vp2_3){
            tab, position ->
            tab.setText(titles[position])
        }.attach()

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