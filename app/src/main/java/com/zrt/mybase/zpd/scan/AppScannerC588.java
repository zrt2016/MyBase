package com.zrt.mybase.zpd.scan;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

import com.capipad.scan.ScanManager;
import com.capipad.scan.ScanResult;
import com.zrt.mybase.MyApplication;

/**
 * @author Yuan
 * @date 2017-06-14
 * 北京系统PDA工程师提供新的SDK扫描，适配扫描问题
 *   绿光的588终端，使用中会出现连续执行的问题（按键一次，执行两次）
 *   提供的该版本SDK适配 568+、588终端扫描，不兼容 568终端
 */
public class AppScannerC588 implements IScanner {
	private MyApplication application;
	private final Context mContext;
	private IScanListener mListener;
	private final ScanManager mScan;
	
	private boolean saomiaoFlag = true;
	
	public AppScannerC588(Context paramContext) {
		this.mContext = paramContext;
		this.mScan = new ScanManager(this.mContext);
		this.mScan.setScanState(true);
		this.mScan.setScanBroadCastMode();
		
//		CapipadInterfaceManager cm = new CapipadInterfaceManager(mContext);
//		application = (MyApplication) mContext.getApplicationContext();
//		cm.setScanVoiceState(false);
//		cm.setHomekeyandStatusbarState(application.mHomekeyandStatusbarState);
	}
	
	/**
	 * 添加扫描监听
	 */
	public void addListener(IScanListener paramIScanListener) {
		this.mListener = paramIScanListener;
		this.mScan.registScanDataListen(new ScanResult() {
			public void onResult(String paramAnonymousString) {
				if ((paramAnonymousString != "") && (!paramAnonymousString.isEmpty()))
					AppScannerC588.this.mListener.handleScanResult(paramAnonymousString);
			}
		});
		this.mScan.setScanState(true);
	}

	public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
		int keyCode = paramKeyEvent.getKeyCode();
		int eventType = paramKeyEvent.getAction();
		boolean processResult = false;
		if (eventType == KeyEvent.ACTION_UP) {
			saomiaoFlag = true;
		} else if (eventType == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case 140:
				processResult = true;
				if (saomiaoFlag) {
					mScan.startScan();
				}
				break;
			case 141:
				processResult = true;
				if (saomiaoFlag) {
					mScan.startScan();
				}
				break;
			case 142:
				processResult = true;
				if (saomiaoFlag) {
					mScan.startScan();
				}
				break;
			}
		}
		return processResult;
	}
	
	public String getScanResult() {
		return null;
	}
	
	public int scanExit() {
		return 1;
	}
	
	@Override
	public int scanExitAll() {
//		this.mScan.setScanState(false);
//		this.mScan.setScanTextMode();
//		this.mScan.unregistScanDataListen();
//		this.mScan.finalize();
		Log.i("", "## 588退出扫描模式...");
		return 0;
	}

	public void startScan() {
	}

	public void stopScan() {
	}

	@Override
	public int getKeyCode(KeyEvent paramKeyEvent) {
		int return_value = KeyMapperCode.DEVICE_KEY_ERROR;
		switch (paramKeyEvent.getKeyCode()) {
		case 7:
			return_value = KeyMapperCode.DEVICE_KEY_0;
			break;
		case 8:
			return_value = KeyMapperCode.DEVICE_KEY_1;
			break;
		case 9:
			return_value = KeyMapperCode.DEVICE_KEY_2;
			break;
		case 10:
			return_value = KeyMapperCode.DEVICE_KEY_3;
			break;
		case 11:
			return_value = KeyMapperCode.DEVICE_KEY_4;
			break;
		case 12:
			return_value = KeyMapperCode.DEVICE_KEY_5;
			break;
		case 13:
			return_value = KeyMapperCode.DEVICE_KEY_6;
			break;
		case 14:
			return_value = KeyMapperCode.DEVICE_KEY_7;
			break;
		case 15:
			return_value = KeyMapperCode.DEVICE_KEY_8;
			break;
		case 16:
			return_value = KeyMapperCode.DEVICE_KEY_9;
			break;
		case 82:
			return_value = KeyMapperCode.DEVICE_KEY_MENU;
			break;
		case 4:
			return_value = KeyMapperCode.DEVICE_KEY_RETRUN;
			break;
		case 67:
			return_value = KeyMapperCode.DEVICE_KEY_DEL;
			break;
		case 131:
			return_value = KeyMapperCode.DEVICE_KEY_F1;
			break;
		case 132:
			return_value = KeyMapperCode.DEVICE_KEY_F2;
			break;
		case 17:
			return_value = KeyMapperCode.DEVICE_KEY_STAR;
			break;
		case 18:
			return_value = KeyMapperCode.DEVICE_KEY_SHAP;
			break;
		case 23:
			return_value = KeyMapperCode.DEVICE_KEY_SAVE;
			break;
		case 133:
			return_value = KeyMapperCode.DEVICE_KEY_SOS;
			break;
		case 3:
			return_value = KeyMapperCode.DEVICE_KEY_HOME;
			break;
		}
		return return_value;
	}
}
