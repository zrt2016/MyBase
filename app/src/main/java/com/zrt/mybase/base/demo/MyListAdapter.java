package com.zrt.mybase.base.demo;

import android.content.Context;

import com.zrt.mybase.base.BaseListAdapter;

import java.util.List;
import java.util.Map;

/**
 * @author：Zrt
 * @date: 2021/7/27
 */
public class MyListAdapter extends BaseListAdapter<Map<String, String>> {

    public MyListAdapter(Context context, List<Map<String, String>> t, int layoutID) {
        super(context, t, layoutID);
    }

    @Override
    public void convert(BaseListAdapter.ViewHolder mHolder, Map<String, String> map, int position) {

    }
}
