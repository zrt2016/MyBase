package com.zrt.mybase.base;

import android.view.View;

import androidx.annotation.IdRes;

public class TabInfo {
    /**
     * 顶部tab显示内容
     */
    public String tabName = "";
    /**
     * tab内容tag，一般为字母
     */
    public String tabNameTag = "";
    /**
     * tab对应状态
     */
    public String state = "";
    /**
     * 是否存在下一个状态
     */
    public boolean isNextState = false;
    /**
     * 下一个状态
     */
    public String nextState = "";

    @IdRes public int id;

    View view;
}
