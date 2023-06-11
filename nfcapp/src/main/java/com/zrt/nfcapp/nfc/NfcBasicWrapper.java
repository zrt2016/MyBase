package com.zrt.nfcapp.nfc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.zrt.nfcapp.utils.Callback;

/**
 * @author：Zrt
 * @date: 2023/4/25
 */
public class NfcBasicWrapper extends NfcBasic{
    NfcBasic nfcBasic;
    public static NfcBasicWrapper instance;
    public static NfcBasicWrapper getInstance() {
        if (null == instance) {
            instance = new NfcBasicWrapper();
        }
        return instance;
    }

    private NfcBasicWrapper() {
//        if ("".equals("")) {
            nfcBasic = new XibuNfcBasic();
//        }else {
//            nfcBasic = new DefaultNfcBasic();
//        }
    }
    @Override
    public void initNfc(Context context, Callback callback) {
        nfcBasic.initNfc(context, callback);
    }

    @Override
    public void enableForegroundDispatch(Context context) {
        nfcBasic.enableForegroundDispatch(context);
    }

    @Override
    public void disableForegroundDispatch(Context context) {
        nfcBasic.disableForegroundDispatch(context);
    }

    @Override
    public void onNewIntent(Context context, Intent intent, Callback callback) {
        nfcBasic.onNewIntent(context, intent, callback);
    }

    @Override
    public void resolveIntent(Context context, Intent intent, Callback callback) {
        nfcBasic.resolveIntent(context, intent, callback);
    }
}
