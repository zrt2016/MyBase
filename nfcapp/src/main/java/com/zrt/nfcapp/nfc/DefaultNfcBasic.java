package com.zrt.nfcapp.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.util.Log;

import com.zrt.nfcapp.utils.Callback;
import com.zrt.nfcapp.utils.HexUtil;
import com.zrt.nfcapp.utils.M1CardUtils;
import com.zrt.nfcapp.utils.StringHelper;

import java.io.IOException;

/**
 * @author：Zrt
 * @date: 2023/4/25
 */
public class DefaultNfcBasic extends NfcBasic {
    /** NFC */
    private static NfcAdapter mAdapter;
    private static PendingIntent mPendingIntent;
    private static IntentFilter[] mFilters;
    private static String[][] mTechLists;
    @Override
    public void initNfc(Context context, Callback callback) {
        mAdapter = NfcAdapter.getDefaultAdapter(context);
        mPendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        mFilters = new IntentFilter[] { ndef };
        mTechLists = new String[][] { new String[] { MifareClassic.class.getName() } };

        Intent intent = ((Activity) context).getIntent();
        resolveIntent(context, intent, callback);
    }

    @Override
    public void enableForegroundDispatch(Context context) {
        if(mAdapter != null){
            mAdapter.enableForegroundDispatch((Activity) context, mPendingIntent, mFilters, mTechLists);
        }
    }

    @Override
    public void disableForegroundDispatch(Context context) {
        if(mAdapter != null){
            mAdapter.disableForegroundDispatch((Activity) context);
        }
    }

    @Override
    public void onNewIntent(Context context, Intent intent, Callback callback) {
        resolveIntent(context, intent, callback);
    }

    @Override
    public void resolveIntent(Context context, Intent intent, Callback callback) {
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            MifareClassic mfc = MifareClassic.get(tagFromIntent);
            try {
                byte[] data = M1CardUtils.readBlock(mfc, 2, 0, null);
                String hexStr = HexUtil.bytesToHexString(data);
                if(!StringHelper.isEmpty(hexStr)){
                    String lastData = HexUtil.filter(HexUtil.convertHexToString(hexStr)).trim();
//					ToastUtil.showMsgLong("读取结果是："+lastData);
//                    MyLogger.Log().i("nfc result:"+lastData);
                    NfcResult nfcResult = new NfcResult();
                    nfcResult.result = lastData;
                    callback.onCallback(nfcResult);
                }else{
                    Log.i(">>>>", "##未读取到患者信息，请先绑定患者！");
//                    ToastUtil.showMsgLong("未读取到患者信息，请先绑定患者！");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
