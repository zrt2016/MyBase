package com.zrt.mybase.tools.voice;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.cloud.util.ResourceUtil;

import com.zrt.mybase.MyApplication;
import com.zrt.mybase.R;
import com.zrt.mybase.tools.voice.speech.setting.IatSettings;
import com.zrt.mybase.tools.voice.speech.util.JsonParser;


/**
 * 语音识别
 * 添加Msc.jar到lib包下
 * 1)项目 libs\armeabi-v7a 下 添加libmsc.so
 * 2)项目 libs\arm64-v8a 下添加libmsc.so （注：该so文件才能支持arm64位系统——C688终端）
 * 3)assets目录下添加 iat 和 iflytek
 */
public class IflytekVoiceManager {

	private static final String TAG = "IflytekVoiceManager";
	
	public static final int PRIMARY_CODE_VOICE = 1001;

	// 语音听写对象
	private SpeechRecognizer mIat;
	// 语音听写UI
	private RecognizerDialog mIatDialog;

	private EditText currentEditText;

	private SharedPreferences mSharedPreferences;
	private boolean mTranslateEnable = false;
	private String mEngineType = "cloud";

	public IflytekVoiceManager(Context context) {
		// 初始化识别无UI识别对象
		// 使用SpeechRecognizer对象，可根据回调消息自定义界面；
		mIat = SpeechRecognizer.createRecognizer(context, mInitListener);

		// 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
		// 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
		mIatDialog = new RecognizerDialog(context, mInitListener);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			mIatDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
		}else{
			mIatDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		}
		mSharedPreferences = context.getSharedPreferences(IatSettings.PREFER_NAME, Activity.MODE_PRIVATE);

		/**
		 * 初始化默认离线中文听写
		 */
		// showTip("离线听写只支持中文普通话");
		// 离线听写不支持联系人/热词上传
		mEngineType = SpeechConstant.TYPE_LOCAL;
		
		// 设置参数
		setParam(context);
	}

	public interface RecognizerCallback {
		void onCallback(String result);
	}
	public RecognizerCallback callback;

	public void voiceWrite(Context context, EditText editText) {
		voiceWrite(context, editText, null);
	}

	public void voiceWrite(Context context, EditText editText, RecognizerCallback callback) {
		this.callback = callback;
		this.currentEditText = editText;
		if(currentEditText!=null){
			currentEditText.setText(null);// 清空显示内容
		}
		
		boolean isShowDialog = mSharedPreferences.getBoolean(context.getString(R.string.pref_key_iat_show), true);
		if (isShowDialog) {
			// 显示听写对话框
			mIatDialog.setListener(mRecognizerDialogListener);
			mIatDialog.show();
			showTip(context.getString(R.string.text_begin));
		} else {
			// 不显示听写对话框
			int ret = mIat.startListening(mRecognizerListener);
			if (ret != ErrorCode.SUCCESS) {
				showTip("听写失败,错误码：" + ret);
			} else {
				showTip(context.getString(R.string.text_begin));
			}
		}
	}

	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("初始化失败，错误码：" + code);
			}
		}
	};

	/**
	 * 听写监听器。
	 */
	private RecognizerListener mRecognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			// 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
//			showTip("开始说话");
		}

		@Override
		public void onError(SpeechError error) {
			// Tips：
			// 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
			if (mTranslateEnable && error.getErrorCode() == 14002) {
				showTip(error.getPlainDescription(true) + "\n请确认是否已开通翻译功能");
			} else {
				showTip(error.getPlainDescription(true));
			}
		}

		@Override
		public void onEndOfSpeech() {
			// 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
//			showTip("结束说话");
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			if (mTranslateEnable) {
				printTransResult(results);
			} else {
				String text = JsonParser.parseIatResult(results.getResultString());
				if (callback != null) callback.onCallback(text);
				if (null != currentEditText) {
					currentEditText.append(text);
					currentEditText.setSelection(currentEditText.length());
				}
				// else{
				// mResultText.append(text);
				// mResultText.setSelection(mResultText.length());
				// }
			}

			if (isLast) {
				// TODO 最后的结果
			}
		}

		@Override
		public void onVolumeChanged(int volume, byte[] data) {
			showTip("当前正在说话，音量大小：" + volume);
			Log.d(TAG, "返回音频数据：" + data.length);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			// 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
			// 若使用本地能力，会话id为null
			// if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			// String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			// Log.d(TAG, "session id =" + sid);
			// }
		}
	};

	/**
	 * 听写UI监听器
	 */
	private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.d(TAG, "recognizer result：" + results.getResultString());

			if (mTranslateEnable) {
				printTransResult(results);
			} else {
				String text = JsonParser.parseIatResult(results.getResultString());

//				if (null != currentEditText) {
//					currentEditText.append(text);
//					currentEditText.setSelection(currentEditText.length());
//				} else {
//					// mResultText.append(text);
//					// mResultText.setSelection(mResultText.length());
//				}
				
				if(isLast){
					if (callback != null) {
						callback.onCallback(text);
					}
					if (null != currentEditText) {
						currentEditText.setText(NumberUtils.numberCN2Arab(text));
						currentEditText.setSelection(currentEditText.length());
					}
				}
			}
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {
			if (mTranslateEnable && error.getErrorCode() == 14002) {
				showTip(error.getPlainDescription(true) + "\n请确认是否已开通翻译功能");
			} else {
				showTip(error.getPlainDescription(true));
			}
		}

	};

	private void showTip(final String str) {
//		ToastUtil.showMsg(str);
		Toast.makeText(MyApplication.application, str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 参数设置
	 * 
	 * @return
	 */
	public void setParam(Context context) {
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);
		String lag = mSharedPreferences.getString("iat_language_preference", "mandarin");
		// 设置引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

		this.mTranslateEnable = mSharedPreferences.getBoolean(context.getString(R.string.pref_key_translate), false);
		if (mEngineType.equals(SpeechConstant.TYPE_LOCAL)) {
			// 设置本地识别资源
			mIat.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath(context));
		}
		if (mEngineType.equals(SpeechConstant.TYPE_CLOUD) && mTranslateEnable) {
			Log.i(TAG, "translate enable");
			mIat.setParameter(SpeechConstant.ASR_SCH, "1");
			mIat.setParameter(SpeechConstant.ADD_CAP, "translate");
			mIat.setParameter(SpeechConstant.TRS_SRC, "its");
		}
		// 设置语言，目前离线听写仅支持中文
		if (lag.equals("en_us")) {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
			mIat.setParameter(SpeechConstant.ACCENT, null);

			if (mEngineType.equals(SpeechConstant.TYPE_CLOUD) && mTranslateEnable) {
				mIat.setParameter(SpeechConstant.ORI_LANG, "en");
				mIat.setParameter(SpeechConstant.TRANS_LANG, "cn");
			}
		} else {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			mIat.setParameter(SpeechConstant.ACCENT, lag);

			if (mEngineType.equals(SpeechConstant.TYPE_CLOUD) && mTranslateEnable) {
				mIat.setParameter(SpeechConstant.ORI_LANG, "cn");
				mIat.setParameter(SpeechConstant.TRANS_LANG, "en");
			}
		}

		// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
		mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

		// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
		mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

		// 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
		mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "0"));

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
	}

	private String getResourcePath(Context context) {
		StringBuffer tempBuffer = new StringBuffer();
		// 识别通用资源
		tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "iat/common.jet"));
		tempBuffer.append(";");
		tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "iat/sms_16k.jet"));
		// 识别8k资源-使用8k的时候请解开注释
		return tempBuffer.toString();
	}

	private void printTransResult(RecognizerResult results) {
		String trans = JsonParser.parseTransResult(results.getResultString(), "dst");
		String oris = JsonParser.parseTransResult(results.getResultString(), "src");

		if (TextUtils.isEmpty(trans) || TextUtils.isEmpty(oris)) {
			showTip("解析结果失败，请确认是否已开通翻译功能。");
		} else {

			if (currentEditText != null) {
				currentEditText.setText("原始语言:\n" + oris + "\n目标语言:\n" + trans);
			} else {
				// mResultText.setText( "原始语言:\n"+oris+"\n目标语言:\n"+trans );
			}
		}
	}

	private void requestPermissions(Context context) {
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				int permission = ActivityCompat.checkSelfPermission(context,
						Manifest.permission.WRITE_EXTERNAL_STORAGE);
				if (permission != PackageManager.PERMISSION_GRANTED) {
					ActivityCompat.requestPermissions((Activity) context, new String[]{
							Manifest.permission.WRITE_EXTERNAL_STORAGE,
							Manifest.permission.LOCATION_HARDWARE, Manifest.permission.READ_PHONE_STATE,
							Manifest.permission.WRITE_SETTINGS, Manifest.permission.READ_EXTERNAL_STORAGE,
							Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CONTACTS}, 0x0010);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
