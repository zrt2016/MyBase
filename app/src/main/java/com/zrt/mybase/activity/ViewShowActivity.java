package com.zrt.mybase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.widget.CheckBox;

import com.zrt.mybase.R;

/**
 * 使用shape绘画圆形checkBox
 */
public class ViewShowActivity extends AppCompatActivity {
    CheckBox item_patient_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_show);
        item_patient_check = findViewById(R.id.item_patient_check);
        item_patient_check.setButtonDrawable(new StateListDrawable());
    }
}