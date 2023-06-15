package com.zrt.kotlinapp.activity_view.custom_view_basic

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.activity_view.custom_view_basic.bezier_basic.BezierCurveActivity
import com.zrt.kotlinapp.activity_view.custom_view_basic.canvas_basic.CanvasBasicActivity
import com.zrt.kotlinapp.activity_view.custom_view_basic.canvas_basic.SurfaceViewActivity
import com.zrt.kotlinapp.activity_view.custom_view_basic.matrix_basic.MatrixBasicActivity
import com.zrt.kotlinapp.activity_view.custom_view_basic.paint_basic.PaintBasicActivity
import com.zrt.kotlinapp.activity_view.custom_view_basic.paint_basic.PaintMixedModeActivity
import com.zrt.kotlinapp.activity_view.custom_view_basic.view_basic.CustomViewActivity
import com.zrt.kotlinapp.activity_view.custom_view_basic.view_basic.CustomViewTwoActivity
import com.zrt.kotlinapp.activity_view.custom_view_basic.view_basic.MappingAdvancedActivity
import com.zrt.kotlinapp.activity_view.custom_view_basic.view_basic.MappingBasicActivity
import com.zrt.kotlinapp.activity_view.recyclerview.decoration.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_custom_view_basic_navigation.*

/**
 * 自定义基础View导航
 */
class CustomViewBasicNavigationActivity : BasicActivity() {
    var mList: MutableList<CustomViewNavigation> = ArrayList<CustomViewNavigation>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_custom_view_basic_navigation
    }

    override fun initData() {
        initList()
        addRecycleView()
    }

    fun addRecycleView() {
        val recyclerView = RecyclerView(this)
        a_cv_n_frame.addView(recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST))
        recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            inner class ViewHolder(val convertView: View) : RecyclerView.ViewHolder(convertView) {
                val i_btn: Button = convertView.findViewById(R.id.i_btn)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_btn, parent, false)
                val viewHolder = ViewHolder(view)
                viewHolder.i_btn.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        val position = viewHolder.adapterPosition
                        startActivity(Intent(this@CustomViewBasicNavigationActivity, mList[position].startActivity))
                    }
                })
                return viewHolder
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                if (holder is ViewHolder) {
                    holder.i_btn.setText(mList[position].title)
                }
            }

            override fun getItemCount(): Int {
                return mList.size
            }
        }
    }

    fun initList(){
        // 自定义View，包含自定义Rect、自定义标题栏和自定义横向滑动列表切换
        mList.add(CustomViewNavigation("CustomView", CustomViewActivity::class.java))
        // 自定义滑动View，跟随手指移动
        mList.add(CustomViewNavigation("自定义跟随手指滑动View", ViewScrollActivity::class.java))
        // 绘图基础
        mList.add(CustomViewNavigation("绘图基础", MappingBasicActivity::class.java))
        // Paint 文字绘制基础
        mList.add(CustomViewNavigation("Paint 文字绘制基础", PaintBasicActivity::class.java))
        mList.add(CustomViewNavigation("Paint 混合模式", PaintMixedModeActivity::class.java))
        // Path 绘制贝济埃曲线（Bezier Curve）
        mList.add(CustomViewNavigation("Path 绘制贝济埃曲线（Bezier Curve）", BezierCurveActivity::class.java))
        // 自定义View绘图进阶
        mList.add(CustomViewNavigation("自定义View绘图进阶 阴影、发光效果等", MappingAdvancedActivity::class.java))
        // Canvas 进阶 图层
        mList.add(CustomViewNavigation("Canvas 与 图层", CanvasBasicActivity::class.java))
        // 多Canvas的SurfaceView
        mList.add(CustomViewNavigation("SurfaceView ", SurfaceViewActivity::class.java))
        // Matrix 基础
        mList.add(CustomViewNavigation("Matrix 矩阵基础 ", MatrixBasicActivity::class.java))
        // 自定义View，
        mList.add(CustomViewNavigation("自定义VIew2 ", CustomViewTwoActivity::class.java))

    }

    data class CustomViewNavigation(val title:String, val startActivity:Class<out BasicActivity>)
}