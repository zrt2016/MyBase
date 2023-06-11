package com.zrt.nfcapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.zrt.nfcapp.nfc.NfcBasicWrapper;
import com.zrt.nfcapp.nfc.NfcResult;
import com.zrt.nfcapp.utils.Callback;

public class MainNfcActivity extends AppCompatActivity {
    /** NFC */
    NfcBasicWrapper nfcBasicWrapper;
    TextView a_m_nfc_title;
    TextView a_nfc_b_card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nfc);
        nfcBasicWrapper = NfcBasicWrapper.getInstance();
        a_m_nfc_title = findViewById(R.id.a_m_nfc_title);
        a_nfc_b_card = findViewById(R.id.a_nfc_b_card);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(20f);
        drawable.setStroke(2, Color.LTGRAY, 10, 10);
        a_nfc_b_card.setBackgroundDrawable(drawable);
    }

    @Override
    protected void onStart() {
        super.onStart();
        nfcBasicWrapper.initNfc(this, callback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcBasicWrapper.enableForegroundDispatch(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcBasicWrapper.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        nfcBasicWrapper.onNewIntent(this, intent, callback);
    }

    Callback<NfcResult> callback = new Callback<NfcResult>() {
        @Override
        public void onCallback(NfcResult s) {
            Log.i(">>>>", "##Nfc result="+s.toString());
            a_m_nfc_title.setText(s.result);
            Toast.makeText(MainNfcActivity.this, "NFC:"+s.toString(), Toast.LENGTH_SHORT).show();
        }
    };

}