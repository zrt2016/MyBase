package com.zrt.kotlinapp.java;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author：Zrt
 * @date: 2022/9/23
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    public static int[] ATTRS = {android.R.attr.listDivider};
    public static int HORIZONTAL_LIST= LinearLayoutManager.HORIZONTAL;
    public static int VERTICAL_LIST= LinearLayoutManager.VERTICAL;
    Drawable mDivider;
    private int mOrientation = VERTICAL_LIST;

    public DividerItemDecoration(Context context, int orientation){
        TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        mDivider = typedArray.getDrawable(0);
        typedArray.recycle();
        setOrientation(orientation);
    }

    private void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST){
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        }else{
            drawHorizontal(c, parent);
        }
    }
    //横向分割线
    private void drawVertical(Canvas c, RecyclerView parent) {
        // 左起点
        int left = parent.getPaddingLeft();
        // 右侧终点
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i=0; i<childCount; i++) {
            View childAt = parent.getChildAt(i);
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) childAt.getLayoutParams();
            // 分割线的顶部起点为，item的底部位置
            int top = childAt.getBottom() + param.bottomMargin;
            // 分割线的底部位置：分割线的起点+分割线的高度
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
    //竖向分割线
    private void drawHorizontal(Canvas c, RecyclerView parent) {
        // 顶部起点
        int top = parent.getPaddingTop();
        // 底部终点
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount();
        for (int i=0; i<childCount; i++) {
            View childAt = parent.getChildAt(i);
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) childAt.getLayoutParams();
            int left = childAt.getRight() + param.rightMargin;
            int right = left + mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
    /**
     * 设置Item的padding属性
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrientation == VERTICAL_LIST){
            outRect.set(0,0,0,mDivider.getIntrinsicHeight());
        }else{
            outRect.set(0,0,mDivider.getIntrinsicWidth(),0);
        }
    }
}
