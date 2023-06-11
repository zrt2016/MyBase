package com.zrt.mybase.zpd.scan;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

import com.capipad.scan.ScanManager;
import com.capipad.scan.ScanResult;
import com.zrt.mybase.MyApplication;
import com.zrt.mybase.utils.MyLogger;

public class AppScannerC800 implements IScanner {

	private MyApplication application;
	private final Context mContext;
	private IScanListener mListener;
	private final ScanManager mScan;

	private boolean saomiaoFlag = true;

	public AppScannerC800(Context paramContext) {
		this.mContext = paramContext;
		application = (MyApplication) mContext.getApplicationContext();
		this.mScan = new ScanManager(this.mContext);
		this.mScan.setScanState(true);
		this.mScan.setScanBroadCastMode(); //设置广播模式
		// 20210610
		this.mScan.setScanVoiceState(false);	// 关闭系统模组扫描声音
//		CapipadInterfaceManager cm = new CapipadInterfaceManager(mContext);
//		cm.setTimeAutoState(false);// 互联网自动时间设置 true 打开自动时间更新,false 关闭自动时间更新
//		// 设置禁用Home键 true：使能此功能 ; false：禁止此功能
//		cm.setHomekeyandStatusbarState(application.mHomekeyandStatusbarState);	// 设置开关需要重启，暂时不用
//		//Log.i("zpd", "## C800初始化成功，并禁用Home键...");
//		// 设置任务键 true：能使用此功能； false：禁止此功能
//        cm.setTaskkeyandStatusbarState(application.mHomekeyandStatusbarState);	// 设置开关需要重启，暂时不用
		//Log.i("zpd", "## C800初始化成功，并禁用Task键...");
		Log.i("zpd", "## C800sdk初始化成功...");
	}

	/**
	 * 添加扫描监听
	 */
	public void addListener(IScanListener paramIScanListener) {
		this.mListener = paramIScanListener;
		this.mScan.registScanDataListen(new ScanResult() {
			public void onResult(String paramAnonymousString) {
//				MyLogger.Log().i("## 扫描数据结果："+paramAnonymousString);
				Log.i(">>>>", "## 扫描数据结果："+paramAnonymousString);
				if ((paramAnonymousString != "") && (!paramAnonymousString.isEmpty())){
					AppScannerC800.this.mListener.handleScanResult(paramAnonymousString);
				}
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
		try {
//			this.mScan.setScanState(false);
			this.mScan.setScanTextMode();
			this.mScan.unregistScanDataListen();
			this.mScan.finalize();
		} catch (Exception e) {
			// e.printStackTrace();
			MyLogger.Log().e(e.getMessage());
		}
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