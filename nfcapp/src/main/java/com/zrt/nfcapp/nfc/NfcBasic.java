package com.zrt.nfcapp.nfc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.zrt.nfcapp.utils.Callback;

/**
 * @author：Zrt
 * @date: 2023/4/25
 */
public abstract class NfcBasic {
    public abstract void initNfc(Context context, Callback callback);
    public abstract void enableForegroundDispatch(Context context);
    public abstract void disableForegroundDispatch(Context context);
    public void onNewIntent(Context context, Intent intent, Callback callback) {}
    public void resolveIntent(Context context, Intent intent, Callback callback) {}

}
