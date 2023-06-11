package com.zrt.kotlinapp.activity_view.recyclerview.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalArgumentException

/**
 * @author：Zrt
 * @date: 2022/8/30
 * 定义RecycleView的分割线
 */
class DividerItemDecoration(): RecyclerView.ItemDecoration() {
    companion object{
        private val ATTRS = intArrayOf(
                android.R.attr.listDivider
        )
        val HORIZONTAL_LIST= LinearLayoutManager.HORIZONTAL
        val VERTICAL_LIST= LinearLayoutManager.VERTICAL
    }
    lateinit var mDivider: Drawable
    var mOrientation: Int = VERTICAL_LIST
    constructor(context:Context, orientation: Int): this(){
        val typedArray = context.obtainStyledAttributes(ATTRS)
        mDivider = typedArray.getDrawable(0)!!
        typedArray.recycle()
        setOrientation(orientation)
    }

    fun setOrientation(orientation: Int) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST){
            throw IllegalArgumentException("invalid orientation")
        }
        mOrientation = orientation
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
//        super.onDraw(c, parent, state)
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent)
        }else{
            drawHorizontal(c, parent)
        }
    }
    //横向分割线
    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        // 左起点
        val left = parent.paddingLeft
        // 右侧终点
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val childAt = parent.getChildAt(i)
            val param = childAt.layoutParams as RecyclerView.LayoutParams
            // 分割线的顶部起点为，item的底部位置
            val top = childAt.bottom + param.bottomMargin
            // 分割线的底部位置：分割线的起点+分割线的高度
            val bottom = top + mDivider.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }
    //竖向分割线
    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        // 顶部起点
        val top = parent.paddingTop
        // 底部终点
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val childAt = parent.getChildAt(i)
            val param = childAt.layoutParams as RecyclerView.LayoutParams
            val left = childAt.right + param.rightMargin
            val right = left + mDivider.intrinsicWidth
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    /**
     * 设置Item的padding属性
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//        super.getItemOffsets(outRect, view, parent, state)
        if (mOrientation == VERTICAL_LIST){
            outRect.set(0,0,0,mDivider.intrinsicHeight)
        }else{
            outRect.set(0,0,mDivider.intrinsicWidth,0)
        }
    }

}