package com.zrt.mybase.utils;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * @author：Zrt
 * @date: 2022/2/28
 */
public class PhoneSettingUtils {

    /**
     * 设置WiFi 开起和关闭
     * @param mContext
     * @param wifiEnabled true：开起WiFi； false：关闭WIFI
     */
    public static void setWIFI(Context mContext, boolean wifiEnabled){
        WifiManager wifiManager=(WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(wifiEnabled);
    }

    /**
     * 获取WIFI状态
     * @param mContext
     */
    public static boolean getWIFIState(Context mContext){
        WifiManager wifiManager=(WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
//        wifiManager.setWifiEnabled(wifiEnabled);
        return wifiManager.isWifiEnabled();
    }
}
