package com.zrt.mybase.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zrt.mybase.R;

/**
 *
 */
public class ActionBarActivity extends AppCompatActivity {
    MenuItem menuAll;
    State state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar);
        // 如果Activity继承的AppCompatActivity，则需要调用getSupportActionBar()或者ActionBar
        // 否则返回null
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("ActionBar");
        actionBar.setCustomView(R.layout.common_header);
        actionBar.setDisplayOptions(16);
        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(false);
        TextView title_textView = findViewById(R.id.title_textView);
        title_textView.setText("ActionBar");
        state = new State();
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


    /**
     * 在onResume生命周期后调用
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        // 记录显示在顶部的menuAll
        menuAll = menu.findItem(R.id.menu_all);
        return super.onCreateOptionsMenu(menu);
    }

    static class State{
        String timeType = "A";
    }

    public void goMain(View view){

    }
}