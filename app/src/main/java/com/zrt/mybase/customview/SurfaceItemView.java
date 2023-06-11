package com.zrt.mybase.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zrt.mybase.R;

/**
 * @author：Zrt
 * @date: 2021/10/31
 * 表单Item：左侧标题，右侧输入框
 */
public class SurfaceItemView extends LinearLayout {
    TextView item_title;
    EditText item_content;
    public SurfaceItemView(Context context) {
        this(context, null);
    }

    public SurfaceItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.surface_item_layout, this);
        item_title = view.findViewById(R.id.surface_item_title);
        item_content = view.findViewById(R.id.surface_item_content);
    }

//    public SurfaceItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public TextView getItem_title() {
        return item_title;
    }

    public EditText getItem_content() {
        return item_content;
    }
    public <T> void setContentTag(T position){
        item_content.setTag(position);
    }

    public void setTitleText(String title){
        item_title.setText(title);
    }

}
