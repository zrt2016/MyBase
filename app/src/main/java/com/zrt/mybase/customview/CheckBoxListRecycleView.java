package com.zrt.mybase.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.zrt.mybase.R;

/**
 * @author：Zrt
 * @date: 2021/12/10
 */
public class CheckBoxListRecycleView extends LinearLayout {
    CheckBox item_all_check;
    RecyclerView leftRecyclerView;
    public CheckBoxListRecycleView(Context context) {
        this(context, null);
    }

    public CheckBoxListRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckBoxListRecycleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.checkbox_list_recycleview, this);
        item_all_check = view.findViewById(R.id.item_all_check);
        leftRecyclerView = findViewById(R.id.leftRecyclerView);
    }
}
