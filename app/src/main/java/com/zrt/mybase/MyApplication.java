package com.zrt.mybase;

import android.app.Application;
import android.content.Context;

import com.zrt.mybase.activity.ForegroundCallbacks;
import com.zrt.mybase.tools.monitor.TimeMonitorConfig;
import com.zrt.mybase.tools.monitor.TimeMonitorManager;

/**
 * @author：Zrt
 * @date: 2022/2/11
 */
public class MyApplication extends Application {
    public static MyApplication application;

    public static MyApplication getInstance() {
        return application;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 启动时在Application中添加打点记录
        TimeMonitorManager.Companion
                .getInstance()
                .resetTimeMonitor(TimeMonitorConfig.INSTANCE.getTIME_MONITOR_ID_APPLICATION_START());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始后台监听，当处于后台时切换APP图标
        ForegroundCallbacks.init(this);
        application = this;
        // 记录Application启动时间
        TimeMonitorManager.Companion
                .getInstance()
                .getTimeMonitor(TimeMonitorConfig.INSTANCE.getTIME_MONITOR_ID_APPLICATION_START())
                .recordingTimeTag("Application-onCreate");
    }
}
