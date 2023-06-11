package com.zrt.mybase.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.zrt.mybase.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class TopTabActivity extends BaseActivity {

    public RadioGroup top_tab_rg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_top_tab;
    }

    @Override
    protected void initView() {
        top_tab_rg = findViewById(R.id.top_tab_rg);

    }
    @Override
    protected void initData() {
        initTabData();
    }
    public abstract List<TabInfo> getTabList();
    public void initTabData(){
        List<TabInfo> tabInfoList = getTabList();
//        tabInfoList.add(getTabInfo("待配液"));
//        tabInfoList.add(getTabInfo("已配液"));
//        tabInfoList.add(getTabInfo("已校对"));
        for (int i=0;i<tabInfoList.size(); i++) {
            TabInfo tabInfo = tabInfoList.get(i);
            RadioButton view = (RadioButton) getLayoutInflater().inflate(R.layout.tab_radio_button, null);
            view.setText(tabInfo.tabName);
            view.setId(i);
            view.setBackgroundResource(R.drawable.blue_underline);
            view.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
//            view.setButtonDrawable(0);
//            top_tab_rg.addView(view);
            top_tab_rg.addView(view, new RadioGroup.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            tabInfo.id = view.getId();
            tabInfo.view = view;
        }
        top_tab_rg.check(0);
    }

    public TabInfo getTabInfo(String state) {
        TabInfo tabOne = new TabInfo();
        tabOne.tabName = state;
        tabOne.state = state;
        return tabOne;
    }


}
