package com.zrt.mybase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zrt.mybase.R;
import com.zrt.mybase.base.BaseActivity;
import com.zrt.mybase.tools.DateHelper;

public class DateActivity extends BaseActivity {
    Button a_date_get;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_date;
    }

    @Override
    protected void initView() {
        a_date_get = findViewById(R.id.a_date_get);
    }

    @Override
    protected void initData() {
        a_date_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a_date_get.setText(DateHelper.getToday("HH:mm"));
            }
        });
    }
}