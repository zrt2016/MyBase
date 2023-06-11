package com.zrt.mybase.base;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zrt.mybase.utils.MyLogger;
import com.zrt.mybase.zpd.scan.DeviceDriverFactory;
import com.zrt.mybase.zpd.scan.IScanListener;
import com.zrt.mybase.zpd.scan.IScanner;


/**
 * @author Zrt
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        initView();
        initData();
    }

    protected abstract int getLayoutResID();
    protected abstract void initView();
    protected abstract void initData();

    public IScanner scanner;
    @Override
    protected void onStart() {
        super.onStart();
        this.scanner = DeviceDriverFactory.createScanner(getApplicationContext());
        this.scanner.addListener(new IScanListener()
        {
            public void handleScanResult(String paramAnonymousString)
            {
                String str = paramAnonymousString.trim();
//                MyLogger.Log().i("##Scan code="+str);
                Log.i(">>>>", "##Scan code="+str);
            }
        });
    }
}
