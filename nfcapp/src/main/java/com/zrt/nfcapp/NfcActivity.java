package com.zrt.nfcapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.zrt.nfcapp.nfc.read.NdefMessageParser;
import com.zrt.nfcapp.nfc.read.NfcUtil;
import com.zrt.nfcapp.nfc.read.ParsedNdefRecord;

import java.util.Calendar;
import java.util.List;

/**
 * TODO:功能说明
 *
 * @author: chenqiuyang
 * @date: 2018-07-12 11:18
 */
public class NfcActivity extends AppCompatActivity {

    private static final String TAG = "NfcActivity";
    private TextView tvNFCMessage;
    private PendingIntent mPendingIntent;
    private NfcAdapter mNfcAdapter;
    private Button btnClean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        Log.i(TAG, "onCreate: ");
        btnClean = findViewById(R.id.btn_clean);
        tvNFCMessage = findViewById(R.id.tv_show_nfc);


        resolveIntent(getIntent());

        //初始化nfc
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if (mNfcAdapter == null) {
            Toast.makeText(NfcActivity.this, "nfc is not available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        btnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvNFCMessage.setText("");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        if (mNfcAdapter != null) { //有nfc功能
            if (mNfcAdapter.isEnabled()) {
                //nfc功能打开了
                //隐式启动
                mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            } else {
                Toast.makeText(NfcActivity.this, "请打开nfc功能", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG, "onNewIntent: ");
        setIntent(intent);
        if (mNfcAdapter != null) { //有nfc功能
            if (mNfcAdapter.isEnabled()) {//nfc功能打开了
                resolveIntent(getIntent());
            } else {
                Toast.makeText(NfcActivity.this, "请打开nfc功能", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    //初次判断是什么类型的NFC卡
    private void resolveIntent(Intent intent) {
        NdefMessage[] msgs = NfcUtil.getNdefMsg(intent); //重点功能，解析nfc标签中的数据

        if (msgs == null) {
            Toast.makeText(NfcActivity.this, "非NFC启动", Toast.LENGTH_SHORT).show();
        } else {
            setNFCMsgView(msgs);
        }

    }

    /**
     * 显示扫描后的信息
     *
     * @param ndefMessages ndef数据
     */
    @SuppressLint("SetTextI18n")
    private void setNFCMsgView(NdefMessage[] ndefMessages) {
        if (ndefMessages == null || ndefMessages.length == 0) {
            return;
        }

//        tvNFCMessage.setText("Payload:" + new String(ndefMessages[0].getRecords()[0].getPayload()) + "\n");

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        tvNFCMessage.append(hour + ":" + minute + "\n");
        List<ParsedNdefRecord> records = NdefMessageParser.parse(ndefMessages[0]);
        final int size = records.size();
        for (int i = 0; i < size; i++) {
            ParsedNdefRecord record = records.get(i);
            tvNFCMessage.append("ktr:" + record.getViewText() + "\n");
        }
    }

}

