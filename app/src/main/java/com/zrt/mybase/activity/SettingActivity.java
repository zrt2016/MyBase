package com.zrt.mybase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.zrt.mybase.R;
import com.zrt.mybase.base.BaseActivity;
import com.zrt.mybase.utils.PhoneSettingUtils;

public class SettingActivity extends BaseActivity {
    Switch switch_wifi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        switch_wifi = findViewById(R.id.switch_wifi);
    }

    @Override
    protected void initData() {
        switch_wifi.setChecked(PhoneSettingUtils.getWIFIState(this));
        switch_wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PhoneSettingUtils.setWIFI(SettingActivity.this, isChecked);
            }
        });
    }
}