package com.zrt.mybase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zrt.mybase.activity.ActionBarActivity;
import com.zrt.mybase.activity.DateActivity;
import com.zrt.mybase.activity.jisuanqi.Calculator2Activity;
import com.zrt.mybase.activity.jisuanqi.CalculatorActivity;
import com.zrt.mybase.activity.IntentActivity;
import com.zrt.mybase.activity.SettingActivity;
import com.zrt.mybase.activity.TextViewActivity;
import com.zrt.mybase.activity.viewdemo.ToolbarActivity;
import com.zrt.mybase.activity.viewmodels.ViewModelActivity;
import com.zrt.mybase.activity.ViewShowActivity;
import com.zrt.mybase.activity.viewdemo.WebViewActivity;
import com.zrt.mybase.activity.viewpagers.ViewPagerActivity;
import com.zrt.mybase.activity.voice.VoiceActivity;
import com.zrt.mybase.base.BaseActivity;
import com.zrt.mybase.base.BaseListAdapter;
import com.zrt.mybase.base.DataHandle;
import com.zrt.mybase.customviewdemo.SurfaceItemActivity;
import com.zrt.mybase.demo.TabDemoActivity;
import com.zrt.mybase.utils.DensityUtil;
import com.zrt.mybase.utils.MyLogger;
import com.zrt.mybase.utils.ScreenUtils;
import com.zrt.mybase.activity.viewdemo.ConstraintLayoutActivity;
import com.zrt.mybase.activity.viewdemo.ImageBase64Activity;
import com.zrt.mybase.activity.viewdemo.PercentLayoutActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    DataHandle dataHandle;

    ListView main_start_lv;
    MainAdapter mainAdapter;
    List<DataState> mList = new ArrayList<>();
    @Nullable
    public String a;
    @NonNull
    public String b;

//    DataState dataState = new;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dataHandle = new DataHandle.Builder(this)
                .setCallable(myCallables)
                .create();
        super.onCreate(savedInstanceState);
//        startActivity(new Intent(this, TabDemoActivity.class));
        MyLogger.Log().i("##dp-5="+DensityUtil.dip2px(this, 5));
        MyLogger.Log().i("##getLocalClassName()="+this.getLocalClassName());
        MyLogger.Log().i("##getPackageName()="+this.getPackageName());
        MyLogger.Log().i("##getComponentName()1="+this.getComponentName().getPackageName());
        MyLogger.Log().i("##getComponentName()2="+this.getComponentName().getClassName());
        runHandle();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ScreenUtils.onWindowFocusChanged(this, (MyApplication) getApplicationContext());
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        main_start_lv = findViewById(R.id.main_start_lv);
    }

    @Override
    protected void initData() {
        mainAdapter = new MainAdapter(this, mList, R.layout.list_item_tv);
        main_start_lv.setAdapter(mainAdapter);
        main_start_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainActivity.this
                        , mainAdapter.getItem(position).start_activity));
            }
        });
        dataHandle.getDataHandler(null);
    }

    DataHandle.Callables myCallables = new DataHandle.Callables<List<DataState>, String>() {

        @Override
        public List<DataState> call(String state) throws Exception {
            List<DataState> list = new ArrayList<>();
            // 设置界面
            list.add(new DataState("START_SETTING_ACTIVITY", SettingActivity.class));
            list.add(new DataState("START_TAB_DEMO_ACTIVITY", TabDemoActivity.class));
            list.add(new DataState("START_IMAGE_BASE64_ACTIVITY", ImageBase64Activity.class));
            list.add(new DataState("START_PERCENT_LAYOUT_ACTIVITY", PercentLayoutActivity.class));
            list.add(new DataState("START_CONSTRAINT_LAYOUT_ACTIVITY", ConstraintLayoutActivity.class));
            list.add(new DataState("START_SURFACE_ITEM_ACTIVITY", SurfaceItemActivity.class));
            list.add(new DataState("START_VIEW_SHOW_ACTIVITY", ViewShowActivity.class));
            list.add(new DataState("START_WEB_VIEW_ACTIVITY", WebViewActivity.class));
            list.add(new DataState("START_ACTION_BAR_ACTIVITY", ActionBarActivity.class));
            list.add(new DataState("START_TOOLBAR_ACTIVITY", ToolbarActivity.class));
            list.add(new DataState("START_INTENT_ACTIVITY", IntentActivity.class));
            // ViewModel or LiveData
            list.add(new DataState("START_VIEW_MODEL_ACTIVITY", ViewModelActivity.class));
//            // ViewPager
            list.add(new DataState("START_VIEW_PAGER_ACTIVITY", ViewPagerActivity.class));
//            // 动态更换APP图标
//            list.add(new DataState("START_APP_ICON_REPLACE_ACTIVITY", AppIconReplaceActivity.class));
            list.add(new DataState("START_TEXTVIEW_ACTIVITY", TextViewActivity.class));
            // 计算器
            list.add(new DataState("计算器1", CalculatorActivity.class));
            list.add(new DataState("计算器2", Calculator2Activity.class));
            list.add(new DataState("语音识别播报", VoiceActivity.class));
            list.add(new DataState("时间", DateActivity.class));
            MyLogger.Log().i("##list="+list.size());
            return list;
        }

        @Override
        public void onCallback(String state, List<DataState> list) {
            mList.clear();
            if (null != list){
                mList.addAll(list);
            }
            if (mainAdapter != null){
                mainAdapter.refreshList(list);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        dataHandle.sendMsg();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataHandle.removeMsg();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataHandle.close();
    }
//
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.main_start_top_tab:
////                startActivity(new Intent(this, TopTabActivity.class));
//                startActivity(new Intent(this, TabDemoActivity.class));
//                break;
//        }
//    }

    public static class DataState{
        public String title;
        public Class<?> start_activity;

        public DataState(String title, Class<?> start_activity) {
            this.title = title;
            this.start_activity = start_activity;
        }
    }

    class MainAdapter extends BaseListAdapter<DataState>{

        public MainAdapter(Context context, List<DataState> t, int layoutID) {
            super(context, t, layoutID);
        }

        @Override
        public void convert(ViewHolder mHolder, DataState dataState, int position) {
            mHolder.setText(R.id.list_item_title, dataState.title);
        }
    }

    int i = 0;
    public void runHandle() {
//                主线程运行
        MyLogger.Log().i("##Main run1="+Thread.currentThread().getName()+"##id="+Thread.currentThread().getId());

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
//                主线程运行
                MyLogger.Log().i("##Main run handler2="+Thread.currentThread().getName()+"##id="+Thread.currentThread().getId());
                ++i;
                if (i == 1){
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.postDelayed(runnable, 1000);
        new Thread(new Runnable() {
            @Override
            public void run() {
//                子线程运行
                MyLogger.Log().i("##Main run Thread3="+Thread.currentThread().getName()+"##id="+Thread.currentThread().getId());
            }
        }).start();
    }
}