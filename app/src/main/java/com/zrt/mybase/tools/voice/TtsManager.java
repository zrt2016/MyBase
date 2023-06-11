package com.zrt.mybase.tools.voice;

/**
 * 语音播报管理器
 */
public interface TtsManager {

    boolean isInit();

    void startSpeaking(String text, boolean maxVolumeFlag);

    void stopSpeaking();

    void release();

}
