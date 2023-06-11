package com.zrt.mybase.zpd.scan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.KeyEvent;

import com.zrt.mybase.utils.MyLogger;


/**
 * 郑州方舱医院，Honeywell终端适配
 * @date20220513
 */
public class AppScannerHoneywell implements IScanner {

    private static final String TAG = AppScannerHoneywell.class.getSimpleName();

    // 扫描数据广播监听
    private static final String ACTION_DISPLAY_SCAN_RESULT = "com.honeywell.scan.broadcast";

    // 广播接收Action 体温广播
    private static final String INTENT_ACTION_TEMPRERATURE="com.honeywell.android.meter.temp";
    private static final String Extra_Target_Key="temp_data"; //目标问题--即体温
    private static final String Extra_Ambient_Key="temp_data2"; // 环境温度

    // 扫描监听
    private IScanListener mListener;
    // 测温监听
    private ITempListener iTempListener;


    private Context mContext;

    public AppScannerHoneywell(Context context) {
        this.mContext = context;
        registerScanReceiver();
    }

    @Override
    public void addListener(IScanListener paramIScanListener) {
        this.mListener = paramIScanListener;
    }

    public void addTempListener(ITempListener paramITempListener){
        this.iTempListener = paramITempListener;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
        return false;
    }

    @Override
    public int getKeyCode(KeyEvent paramKeyEvent) {
        return 0;
    }

    @Override
    public String getScanResult() {
        return null;
    }

    @Override
    public int scanExit() {
        return 0;
    }

    @Override
    public void startScan() {

    }

    @Override
    public void stopScan() {

    }

    @Override
    public int scanExitAll() {
        mListener = null;
        unRegisterScanReiver();
        return 0;
    }

    private void registerScanReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DISPLAY_SCAN_RESULT); // 扫描数据广播
        intentFilter.addAction(INTENT_ACTION_TEMPRERATURE); // 测量体温广播
        this.mContext.registerReceiver(mScannerReceiver, intentFilter);
    }

    private void unRegisterScanReiver() {
        this.mContext.unregisterReceiver(mScannerReceiver);
    }

    private BroadcastReceiver mScannerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive " + intent);
            // 扫描结果监听转发
            if (ACTION_DISPLAY_SCAN_RESULT.equals(intent.getAction())) {
                String result = intent.getStringExtra("data");
                if (mListener != null) mListener.handleScanResult(result);
            }
            // 测温结果监听转发
            if (INTENT_ACTION_TEMPRERATURE.equals(intent.getAction())) {
                final String tempdata = intent.getStringExtra(Extra_Target_Key); //获取人体温度
                final String tempdata2 = intent.getStringExtra(Extra_Ambient_Key); //获取环境温度
                if (iTempListener != null) iTempListener.handleTempResult(tempdata);
                MyLogger.Log().i("测温结果， 体温 : " + tempdata + "； 环境温度 ： " + tempdata2 + "\n");
            }

        }
    };
}
