package com.zrt.mybase.zpd.scan;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.zrt.mybase.utils.MyLogger;


public class DeviceDriverFactory {
	public static String current_device_type;
	private static IScanner scanner = null;
	public static IScanner createScanner(Context paramContext) {
		if ("800".equalsIgnoreCase(Build.MODEL) || Build.MODEL.contains("800")) {
			scanner = new AppScannerC800(paramContext);
		}else {
			scanner = new AppScannerC588(paramContext);
		}
//		MyLogger.Log().i("## init scan Build.MODEL : "+ Build.MODEL);
		Log.i(">>>>", "## init scan Build.MODEL : "+ Build.MODEL);
		current_device_type = Build.MODEL;
		return scanner;

	}
}