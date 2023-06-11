package com.zrt.mybase.activity.voice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.zrt.mybase.R;
import com.zrt.mybase.base.BaseActivity;
import com.zrt.mybase.tools.voice.IflytekTtsManager;
import com.zrt.mybase.tools.voice.IflytekVoiceManager;
import com.zrt.mybase.tools.voice.VoiceInputManager;

public class VoiceActivity extends BaseActivity {
    EditText a_v_input;
    Button a_v_play;
    // 语音识别
    public IflytekVoiceManager voiceManager;
    // 语音播报
    IflytekTtsManager mTtsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_voice;
    }

    @Override
    protected void initView() {
        a_v_input = findViewById(R.id.a_v_input);
        a_v_play = findViewById(R.id.a_v_play);
    }

    @Override
    protected void initData() {
        // 应用程序入口处调用,避免手机内存过小,杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
        // 参数间使用“,”分隔。
        // 设置你申请的应用appid
        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误
        StringBuffer param = new StringBuffer();
//        param.append("appid="+getString(support_tts?R.string.app_id_tts:R.string.app_id_iat));
        param.append("appid="+getString(R.string.app_id_tts)); // 语音播报
//        param.append("appid="+getString(R.string.app_id_iat)); // 医院识别
        param.append(",");
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE+"="+SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(this, param.toString());

        initVoiceManager();
        initTtsManager();
        a_v_input.setText("覃");
        a_v_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strings = a_v_input.getText().toString();
                speakErrorInfo(strings, false);
            }
        });
    }
    /** 初始化语音识别 */
    public void initVoiceManager(){
        voiceManager = new IflytekVoiceManager(getApplicationContext());
    }
    /** 初始化语音识别 */
    public void initTtsManager(){
        mTtsManager = new IflytekTtsManager(getApplicationContext());
    }
    public void speakErrorInfo (String text,boolean maxVolumeFlag) {
        // 多音字处理，汉字后面添加[]，
        if (text.contains("覃")){
            text = text.replaceAll("覃", "覃[=qin2]");
        }
        if(mTtsManager != null && mTtsManager.isInit()) {
            mTtsManager.startSpeaking(text, maxVolumeFlag);
        }
    }

}