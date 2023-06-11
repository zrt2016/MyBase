package com.zrt.mybase.activity.viewdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zrt.mybase.R;

public class ToolbarActivity extends AppCompatActivity {

    private MenuItem menuAll;
    private State state = new State();
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);
        toolbar = findViewById(R.id.toolbar);
        // 添加menu方式二：需要注释setSupportActionBar
        toolbar.inflateMenu(R.menu.menu_actionbar);
        menuAll = toolbar.getMenu().findItem(R.id.menu_all);

        //将ToolBar对象设置为当前Activity的ActionBar
//        setSupportActionBar(toolbar);

        initListener();
        initData();
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(">>>>", "##setNavigationOnClickListener");
            }
        });
        // 该监听事件会使onOptionsItemSelected失效
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i(">>>>", "##setOnMenuItemClickListener");
                switch (item.getItemId()) {
                    case R.id.menu_a:
                        // 动态修改menuAll的title显示
                        menuAll.setTitle("A");
                        state.timeType = "A";
                        break;
                    case R.id.menu_p:
                        menuAll.setTitle("P");
                        state.timeType = "P";
                        break;
                    case R.id.menu_n:
                        menuAll.setTitle("N");
                        state.timeType = "N";
                        break;
                }
                return false;
            }
        });
    }

    private void initData() {
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_a:
                // 动态修改menuAll的title显示
                menuAll.setTitle("A");
                state.timeType = "A";
                break;
            case R.id.menu_p:
                menuAll.setTitle("P");
                state.timeType = "P";
                break;
            case R.id.menu_n:
                menuAll.setTitle("N");
                state.timeType = "N";
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 添加menu方式一：需要添加setSupportActionBar();
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        // 记录显示在顶部的menuAll
        menuAll = menu.findItem(R.id.menu_all);
        return super.onCreateOptionsMenu(menu);
    }

    static class State{
        String timeType = "A";
    }
}