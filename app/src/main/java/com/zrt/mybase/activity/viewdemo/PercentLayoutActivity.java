package com.zrt.mybase.activity.viewdemo;

import android.os.Bundle;

import com.zrt.mybase.R;
import com.zrt.mybase.base.BaseActivity;

/**
 * 百分比布局
 * 百分比属性
 * layout_widthPercent、layout_heightPercent、            // 百分比宽高
 * layout_marginPercent、layout_marginLeftPercent、       // 百分比边距
 * layout_marginTopPercent、layout_marginRightPercent、
 * layout_marginBottomPercent、layout_marginStartPercent、layout_marginEndPercent。
 *
 *  app:layout_widthPercent = "30%"
 *  app:layout_heightPercent = "20%"
 *  android:layout_alignParentLeft="true"
 *  android:layout_alignParentTop="true"
 */
public class PercentLayoutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_percent_layout;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}