package com.zrt.mybase.tools.voice;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.zrt.mybase.tools.voice.speech.setting.TtsSettings;
import com.zrt.mybase.utils.MyLogger;

/**
 * https://blog.csdn.net/qq_61963074/article/details/126556997
 * https://www.xfyun.cn/doc/tts/offline_tts/Android-SDK.html#_2%E3%80%81sdk%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
 * 讯飞语音播报管理器
 * 1)项目 libs\armeabi-v7a 下 添加libmsc.so
 * 2)项目 libs\arm64-v8a 下添加libmsc.so （注：该so文件才能支持arm64位系统——C688终端）
 * 3)assets目录下添加 tts
 */
public class IflytekTtsManager implements TtsManager {

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 默认本地发音人
    private static String voicerLocal = "xiaoyan";

    private AudioManager mAudioManager;

    public IflytekTtsManager(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(TtsSettings.PREFER_NAME, Context.MODE_PRIVATE);
        mTts = SpeechSynthesizer.createSynthesizer(mContext, new InitListener() {
            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS) {
                    MyLogger.Log().e("初始化失败,错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
                } else {
                    // 初始化成功，之后可以调用startSpeaking方法
                    // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                    // 正确的做法是将onCreate中的startSpeaking调用移至这里

                    // 设置参数
                    setParam();
                }
            }
        });
    }

    /**
     * 参数设置
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        //设置合成
        //设置使用本地引擎
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        //设置发音人资源路径
        mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath());
        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, voicerLocal);


        //mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY,"1");//支持实时音频流抛出，仅在synthesizeToUri条件下支持
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
        //设置合成音量
//        mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        mTts.setParameter(SpeechConstant.VOLUME, "100");
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        //	mTts.setParameter(SpeechConstant.STREAM_TYPE, AudioManager.STREAM_MUSIC+"");

        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");

        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");

    }

    /**
     * 获取发音人资源路径
     *
     * @return
     */
    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        String type = "tts";
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, type + "/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, type + "/" + voicerLocal + ".jet"));
        return tempBuffer.toString();
    }

    @Override
    public boolean isInit() {
        if(mTts!=null){
            return true;
        }
        MyLogger.Log().e("tts init fail, mTts is null");
        return false;
    }

    /**
     * 开始播报
     *
     * @param text
     */
    @Override
    public void startSpeaking(String text, final boolean maxVolumeFlag) {
        if (!isInit()) {
            return;
        }

        stopSpeaking();

        final int curStreamVolume;
        final boolean streamVolumeChanged;

        if(maxVolumeFlag){
            if(mAudioManager == null){
                mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            }

            //音量调到最大
            curStreamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int maxStreamVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            streamVolumeChanged = curStreamVolume!=maxStreamVolume;
            if(streamVolumeChanged){
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxStreamVolume, AudioManager.FLAG_PLAY_SOUND);
            }
            
//            if(baseActivity != null && "兴化市新区分院".equals(Constants.APP_REGION)){
//            	baseActivity.playMedia("warn");
//            }
            
        }else{
            curStreamVolume = 0;
            streamVolumeChanged = false;
        }

        int code = mTts.startSpeaking(text, new SynthesizerListener() {

            @Override
            public void onSpeakBegin() {
//                Log.d(TAG, "开始播放");
            }

            @Override
            public void onSpeakPaused() {
//                Log.d(TAG, "暂停播放");
            }

            @Override
            public void onSpeakResumed() {
//                Log.d(TAG, "继续播放");
            }

            @Override
            public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
                // 合成进度
//                Log.d(TAG, String.format("缓冲进度为%d%%", percent));
            }

            @Override
            public void onSpeakProgress(int percent, int beginPos, int endPos) {
                // 播放进度
//                Log.d(TAG, String.format("播放进度为%d%%", percent));
            }

            @Override
            public void onCompleted(SpeechError error) {
                if (error == null) {
//                    Log.d(TAG, "播放完成");
                } else if (error != null) {
                    MyLogger.Log().e("startSpeaking onCompleted error:" + error.getPlainDescription(true));
                }

                if(maxVolumeFlag) {
                    //恢复修改前的音量
                    if (streamVolumeChanged) {
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, curStreamVolume, AudioManager.FLAG_PLAY_SOUND);
                    }
                }
            }

            @Override
            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
                // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
                // 若使用本地能力，会话id为null
//                if (SpeechEvent.EVENT_SESSION_ID == eventType) {
//                    String sid = obj.getString(SpeechEvent.KEY_EVENT_AUDIO_URL);
//                    Log.d(TAG, "session id =" + sid);
//                }
            }
        });

        if (code != ErrorCode.SUCCESS) {
            MyLogger.Log().e("语音合成失败,错误码: " + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
        }
    }

    /**
     * 停止播报
     */
    @Override
    public void stopSpeaking() {
        if (mTts != null && mTts.isSpeaking()) {
            mTts.stopSpeaking();
        }
    }

    /**
     * 释放资源
     */
    @Override
    public void release() {
        if (mTts != null) {
            stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
            mTts = null;
        }
        mAudioManager = null;
        mContext = null;
    }

}
