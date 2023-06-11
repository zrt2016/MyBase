package com.zrt.mybase.tools.voice;

import android.content.Context;

/**
 * @author：Zrt
 * @date: 2022/12/7
 */
public class VoiceInputManager {
    public TtsManager mTtsManager;

    public void initTtsManager(Context context){
        releaseTtsManager();
        mTtsManager = new IflytekTtsManager(context);
    }

    public void releaseTtsManager(){
        if(mTtsManager!=null){
            mTtsManager.release();
            mTtsManager = null;
        }
    }
}
