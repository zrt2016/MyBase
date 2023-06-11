package com.zrt.kotlinapp.activity_view.recyclerview.decoration

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * @author：Zrt
 * @date: 2022/8/31
 */
class DividerGridItemDecoration(val context:Context): RecyclerView.ItemDecoration() {
    private val ATTRS = intArrayOf(R.attr.listDivider)
    val mDivider by lazy {
        val typedArray = context.obtainStyledAttributes(ATTRS)
        val drawable = typedArray.getDrawable(0)
        typedArray.recycle()
        drawable
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    /**
     * 绘制竖向间隔线
     * ##drawVertical i=0, left=200, right=202, top=0, bottom=135
     * ##drawVertical i=1, left=402, right=404, top=0, bottom=331
     * ##drawVertical i=2, left=604, right=606, top=0, bottom=247
     * ##drawVertical i=3, left=808, right=810, top=0, bottom=499
     */
    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        for (i in 0 until childCount){
            val childAt = parent.getChildAt(i)
            val params = childAt.layoutParams as RecyclerView.LayoutParams
            val top = childAt.top - params.topMargin
            val bottom = childAt.getBottom() + params.bottomMargin
            val left = childAt.getRight() + params.rightMargin
            val right = left + (mDivider?.getIntrinsicWidth() ?: 0)
//            Log.i(">>>>","##drawVertical i=$i, left=$left, right=$right, top=$top, bottom=$bottom")
            mDivider?.let {
                it.setBounds(left, top, right, bottom)
                it.draw(c)
            }

        }
    }

    /**
     * 绘制横向间隔线
     * ##drawHorizontal i=0, left=0, right=202, top=135, bottom=137
     * ##drawHorizontal i=1, left=202, right=404, top=331, bottom=333
     * ##drawHorizontal i=2, left=404, right=606, top=247, bottom=249
     * ##drawHorizontal i=3, left=606, right=810, top=499, bottom=501
     */
    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            val params = child.getLayoutParams() as RecyclerView.LayoutParams
            val left: Int = child.getLeft() - params.leftMargin
            val right: Int = (child.getRight() + params.rightMargin
                    + (mDivider?.intrinsicWidth?:0))
            val top: Int = child.getBottom() + params.bottomMargin
            val bottom = top + (mDivider?.intrinsicHeight ?:0)
//            Log.i(">>>>","##drawHorizontal i=$i, left=$left, right=$right, top=$top, bottom=$bottom")
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(c)
        }
    }

//    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
//        super.getItemOffsets(outRect, itemPosition, parent)
//    }
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val spanCount = getSpanCount(parent)
        val itemCount = (parent.adapter?.itemCount)?:0
        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        if (isLastRaw(parent, itemPosition,spanCount,itemCount)){
            // 如果是最后一行，则不需要绘制底部
            outRect.set(0, 0, mDivider?.intrinsicWidth?:0, 0);
        }else if (isLastColum(parent, itemPosition, spanCount, itemCount)) {
            // 如果是最后一列，则不需要绘制右边
            outRect.set(0, 0, 0, mDivider?.intrinsicHeight?:0);
        }else {
            outRect.set(0, 0, mDivider?.intrinsicWidth?:0, mDivider?.intrinsicHeight?:0);
        }
//        Log.i(">>>>","##getItemOffsets spanCount=$spanCount, itemCount=$itemCount" +
//                ", itemPosition=$itemPosition, width=${mDivider?.intrinsicWidth?:0}" +
//                ", height=${mDivider?.intrinsicHeight?:0}")

    }

    /**
     * 获取列数
     */
    fun getSpanCount(parent: RecyclerView): Int{
        // 列数
        var spanCouunt = 1;
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager){
            spanCouunt = layoutManager.spanCount
        }else if (layoutManager is StaggeredGridLayoutManager){
            spanCouunt = layoutManager.spanCount
        }
        return spanCouunt
    }

    /**
     * 是否为最后一列
     * 例：childCount = 10, spanCount = 3 则（position+1）% 3 == 0 当前position处于最右边的位置
     */
    fun isLastColum(parent: RecyclerView, position:Int, spanCount:Int,
            childCont: Int): Boolean{
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager){
            // 最后一列,不绘制右边
            if ((position+1)%spanCount == 0){
                return true;
            }
        }else if (layoutManager is StaggeredGridLayoutManager){
            val orientation = layoutManager.orientation
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                // 垂直方向，最后一列,不绘制右边
                if ((position + 1) % spanCount == 0) {
                    return true
                }
            }else {
                var count = childCont - childCont%spanCount
                // StaggeredGridLayoutManager 且横向滚动
                // 如果是最后一列，则不需要绘制右边
                if (position >= count){
                    return true
                }
            }
        }
        return false
    }
    /**
     * 是否为最后一行
     * 例：childCont=10, spanCount=3 则会有10%3 = 1个不需要绘制 底部
     * 因此：position >= 10-1  即第10个不需要绘制底部
     */
    fun isLastRaw(parent: RecyclerView, position:Int, spanCount:Int,
                    childCont: Int):Boolean{
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager){
            // 如果是最后一行，则不需要绘制底部
            var count = childCont - childCont%spanCount
            if (position >= count){
                return true;
            }
        }else if (layoutManager is StaggeredGridLayoutManager){
            val orientation = layoutManager.orientation
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                // 如果是最后一行，则不需要绘制底部
                var count = childCont - childCont%spanCount
                if (position >= count) {
                    return true
                }
            }else {
                // StaggeredGridLayoutManager 且横向滚动
                // 如果是最后一行，则不需要绘制底部
                if ((position+1)%spanCount == 0){
                    return true
                }
            }
        }
        return false
    }

}