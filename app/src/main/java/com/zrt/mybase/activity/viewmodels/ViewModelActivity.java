package com.zrt.mybase.activity.viewmodels;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.zrt.mybase.R;
import com.zrt.mybase.base.BaseActivity;

public class ViewModelActivity extends BaseActivity {
    TextView title_timer;
    TextView title_timer2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //  方式一： 通过监听事件回调
//        MyViewModel myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
//        myViewModel.setOnTimeChangeListener(new MyViewModel.OnTimeChangeListener() {
//            @Override
//            public void onTimeChanged(final int second) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        title_timer.setText("Time="+second);
//                    }
//                });
//            }
//        });
//        myViewModel.startMing();

        // 方式二：通过liveData回调
        TimerLDViewModel ldViewModel = ViewModelProviders.of(this).get(TimerLDViewModel.class);
        MutableLiveData<Integer> currentSecond = (MutableLiveData<Integer>) ldViewModel.getCurrentSecond();
        //通过LiveData.observe()实现对ViewModel中数据变化的观察
        currentSecond.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                //收到回调后更新UI界面
                title_timer.setText("Time="+integer);
            }
        });
        ldViewModel.startMing();
        final MutableLiveData<Integer> currentSecond2 = (MutableLiveData<Integer>) ldViewModel.getCurrentSecond2();
        currentSecond2.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.i(">>>>", "##currentSecond2="+integer);
                title_timer2.setText("## Num =" + integer);
            }
        });
        title_timer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                currentSecond2.postValue(1);
//                currentSecond2.postValue(2);
                currentSecond2.postValue(3);// 重复调用，只打印最后一个
                /**
                 *  主线程中重复调用只会调用一次：ArchTaskExecutor.getInstance().postToMainThread(mPostValueRunnable);
                 *  注释：If you called this method multiple times before a main thread executed a posted task, only the last value would be dispatched.
                 *  翻译：如果在主线程执行已发布任务之前多次调用此方法，则只会调度最后一个值。
                 *  原因：
                 *  1. postValue方法内部其实是将值的回调逻辑放到了Runnable中，再post给Handler，
                 *      利用Handler在主线程中更新，因此从postValue到执行Runnable，中间是存在时间差的。
                 *  2. 在执行Runnable之前，因为是连续调用的postValue，第一次mPendingData的值被第二次调用时的值覆盖掉了，
                 *      最后执行Runnable时，mPendingData的值只能是最新的那个，也就是带着第二次的值触发onChange回调给UI。
                 */
            }
        });

    }

    int i = 0;
    @Override
    protected int getLayoutResID() {
        return R.layout.activity_view_model;
    }

    @Override
    protected void initView() {

        title_timer = findViewById(R.id.title_timer);
        title_timer2 = findViewById(R.id.title_timer2);
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) finish();
        return super.onKeyDown(keyCode, event);
    }
}