package com.zrt.nfcapp.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.widget.Toast;

import com.zrt.nfcapp.MainNfcActivity;
import com.zrt.nfcapp.nfc.read.NdefMessageParser;
import com.zrt.nfcapp.nfc.read.NfcUtil;
import com.zrt.nfcapp.nfc.read.ParsedNdefRecord;
import com.zrt.nfcapp.utils.Callback;
import com.zrt.nfcapp.utils.GsonUtil;
import com.zrt.nfcapp.utils.StringHelper;

import android.annotation.SuppressLint;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author：Zrt
 * @date: 2023/4/25
 */
public class XibuNfcBasic extends NfcBasic {
    /** NFC */
    private static NfcAdapter mNfcAdapter;
    private static PendingIntent mPendingIntent;
    private static IntentFilter[] mFilters;
    private static String[][] mTechLists;
    @Override
    public void initNfc(Context context, Callback callback) {
        Intent intent = ((Activity) context).getIntent();
        resolveIntent(context, intent, callback);

        //初始化nfc
        mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
        mPendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, context.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if (mNfcAdapter == null) {
            Toast.makeText(context, "nfc is not available", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void enableForegroundDispatch(Context context) {
        if (mNfcAdapter != null) { //有nfc功能
            if (mNfcAdapter.isEnabled()) {
                //nfc功能打开了
                //隐式启动
                mNfcAdapter.enableForegroundDispatch((Activity) context, mPendingIntent, null, null);
            } else {
                Toast.makeText(context, "请打开nfc功能", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void disableForegroundDispatch(Context context) {
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch((Activity) context);
        }
    }


    @Override
    public void onNewIntent(Context context, Intent intent, Callback callback) {
        if (mNfcAdapter != null) { //有nfc功能
            if (mNfcAdapter.isEnabled()) {//nfc功能打开了
                resolveIntent(context, intent, callback);
            } else {
                Toast.makeText(context, "请打开nfc功能", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void resolveIntent(Context context, Intent intent, Callback callback) {
        NdefMessage[] msgs = NfcUtil.getNdefMsg(intent); //重点功能，解析nfc标签中的数据

        if (msgs == null) {
            Toast.makeText(context, "非NFC启动", Toast.LENGTH_SHORT).show();
        } else {
            setNFCMsgView(msgs, callback);
        }
    }
    @SuppressLint("SetTextI18n")
    private void setNFCMsgView(NdefMessage[] ndefMessages, Callback callback) {
        if (ndefMessages == null || ndefMessages.length == 0) {
            return;
        }

//        tvNFCMessage.setText("Payload:" + new String(ndefMessages[0].getRecords()[0].getPayload()) + "\n");
        StringBuffer sb = new StringBuffer();
//        Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//        sb.append(hour + ":" + minute + "\n");
        List<String> lsit = new ArrayList<>();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(ndefMessages[0]);
        final int size = records.size();
        for (int i = 0; i < size; i++) {
            ParsedNdefRecord record = records.get(i);
            lsit.add(record.getViewText());
        }
        if(lsit.size() > 0) {
            NfcResult nfcResult = GsonUtil.parseGsonObject(lsit.get(0), NfcResult.class);
            nfcResult.error = "200";
            callback.onCallback(nfcResult);
        }else {
            NfcResult nfcResult = new NfcResult();
            nfcResult.error = "300";
            callback.onCallback(nfcResult);
        }
    }
}
