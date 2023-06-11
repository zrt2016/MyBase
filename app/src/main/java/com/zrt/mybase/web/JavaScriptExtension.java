package com.zrt.mybase.web;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;

/**
 * JS调用android端方法 参数必须是final类型,每个方法上面必须添加@JavascriptInterface 才可以被JS调用
 */
public class JavaScriptExtension extends Activity implements Callback {

//	private final String TAG = "JavaScriptExtension";
	
	private Context context;

	protected Dialog loadingDlg;				// 等待提示框
	
	public JavaScriptExtension(Context context) {
		super();
		this.context = context;
	}
	
	/**
	 * 显示提示消息
	 * 
	 * @param message
	 */
	@JavascriptInterface
	public void showMessage(final String message) {
//		ToastUtil.showMsg(message);
	}

	/**
	 * 拨号
	 * @param phoneNumber 手机号
	 */
	@SuppressWarnings("null")
	@JavascriptInterface
	public void callPhone(final String phoneNumber) {
		Intent intent = null;
		if ((null != phoneNumber) || "".equalsIgnoreCase(phoneNumber.trim())) {
			/** 不直接拨号 */
			// intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + number.trim()));
			/** 直接拨号 */
			intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber.trim()));
			this.context.startActivity(intent);
//			Log.i("JavaScriptExtension_callPhone", "The mobile number = "+ phoneNumber.trim());
		} else {
			this.showMessage("号码不能为空!");
//			Log.i("JavaScriptExtension_callPhone", "号码为空!");
		}
	}
	
	/**
	 * getSoftKeyboardState
	 * 
	 * @return state
	 */
	@JavascriptInterface
	public boolean getSoftKeyboardState() {
		boolean state = false;
		if (((Activity) this.context).getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED)
		{
			state = true;
		}
//		Log.i("JavaScriptExtension_getSoftKeyboardState", "The keyboard state = " + state);
		return state;
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 调用手写签名弹出框
	 */
	@JavascriptInterface
    public void showHandSignDialog(final String type){
//		if(context instanceof TbsBrowserActivity) {
//			((TbsBrowserActivity) context).showHandSignDialog(type);
//			return;
//		}
//		((ZpdyfBrowserActivity) context).showHandSignDialog(type);
    }
	
	@JavascriptInterface
    public void healthEducationQRCode(final String id){
//		if(context instanceof TbsBrowserActivity) {
//			((TbsBrowserActivity) context).onHealthEducationQRCode(id);
//			return;
//		}
//        ((ZpdyfBrowserActivity) context).onHealthEducationQRCode(id);
    }
	
	@JavascriptInterface
    public void jumpHealthEducation(){
//		if(context instanceof TbsBrowserActivity) {
//			((TbsBrowserActivity) context).goHealthEducationListDelayed();
//			return;
//		}
//        ((ZpdyfBrowserActivity) context).goHealthEducationListDelayed();
    }
	
	@JavascriptInterface
    public void shouShuDanCheckGP(){
//		if(context instanceof TbsBrowserActivity) {
//			((TbsBrowserActivity) context).onShouShuDanCheckGP();
//			return;
//		}
//        ((ZpdyfBrowserActivity) context).onShouShuDanCheckGP();
    }

	@JavascriptInterface
    public void openGaoweiPinggu(){
//		context.startActivity(new Intent(context, GaoweiPingguActivity.class));
	}
}