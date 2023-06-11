package com.zrt.mybase.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;

/**
 * 自定义View
 * 
 * @author Yuan
 *
 */
public class ViewUtil {

	/**
	 * 自定义加载框1
	 */
//	private CustomLoadingDialog loading;
	
	/**
	 * 自定义加载框2
	 */
	private ProgressDialog progressTipsDialog = null;

	/**
	 * 自定义ProgressDialog
	 */
	public void showProgressTips(Context context, String tipMsg) {
		// 显示进度对话框
		dismissProgressTips();
		if (isValidContext(context)){
			progressTipsDialog = new ProgressDialog(context);
			progressTipsDialog.setProgressStyle(0);
			progressTipsDialog.setMessage((null==tipMsg || "".equals(tipMsg))?"正在努力加载中...":tipMsg);
			progressTipsDialog.setIndeterminate(false);
			progressTipsDialog.setCancelable(true);
			try {
				progressTipsDialog.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void dismissProgressTips() {
		if (progressTipsDialog != null && progressTipsDialog.isShowing()) {
			Context context = ((ContextWrapper)progressTipsDialog.getContext()).getBaseContext();
			if(context instanceof Activity) {
				if(!((Activity)context).isFinishing() && !((Activity)context).isDestroyed())
					progressTipsDialog.dismiss();
			} else {
				progressTipsDialog.dismiss();
			}
		}
		progressTipsDialog = null;
	}

	/**
	 * 创建加载框
	 */
//	public void showLoading(final Context context, final String message) {
//		dismissLoading();
//		if (isValidContext(context)){
//			// 显示进度对话框
//			loading = new CustomLoadingDialog(context, message);
//			loading.setCancelable(false);
//			loading.show();
//		}
//	}
	
	/**
	 * 关闭加载框
	 */
//	public void dismissLoading() {
//		if (loading != null && loading.isShowing()) {
//			loading.dismiss();
//		}
//		loading = null;
//	}

	//-------------------------------带图片加载中
	// My进度
//	public static ViewUtilPDialog pDialog;
//	public static void showMyprogress(Context context, String msg) {
//		if (pDialog == null) {
//			pDialog = ViewUtilPDialog.createDialog(context, false);
//			pDialog.setMessage(msg);
//		} else {
//			pDialog.setMessage(msg);
//		}
//		if (null != pDialog && isValidContext(context)) {
//			pDialog.show();
//			pDialog.setCancelable(false);// 点击或返回键， 不消失！
//		}
//	}

//	public static void dismissMyprogress() {
//		if (pDialog != null) {
//			pDialog.dismiss();
//			pDialog = null;
//		}
//	}
	
	
	/**
	 * 显示正在加载中，可关闭
	 * @param context
	 * @param msg
	 * @param showClose
	 */
//	static ViewUtilPDialog dialog;
//	public static void showMyprogress2(Context context, String msg, boolean showClose) {
//		if (isValidContext(context)){
//			ViewUtilPDialogBuilder.Builder customBuilder = new ViewUtilPDialogBuilder.Builder(context);
//			customBuilder.setMessage(msg).setCloseImageViewShow(new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//				}
//			});
//			dialog = customBuilder.create();
//			dialog.setCancelable(false);// 点击或返回键， 不消失！
//			dialog.show();
//		}
//	}
	
	private static boolean isValidContext(Context c) {
		Activity a = (Activity) c;
		if (null != a && a.isFinishing()) {
			return false;
		} else {
			return true;
		}
	}
	
//	public static void dismissMyprogress2() {
//		if (dialog != null && dialog.isShowing()) {
//			dialog.dismiss();
//			dialog = null;
//		}
//	}

	/**
	 * 动态添加标记控件
	 */
//	public static void dynamicAddFlagView(Context context, FlowLayout flowLayout, String app_show_tag_list, String app_show_color_list){
//		if(flowLayout == null){
//			return;
//		}
//		if (flowLayout.getChildCount() > 0) {
//			flowLayout.removeAllViews();
//		}
//		int size = DensityUtils.dip2px(context, 20);
//		float radius = DensityUtils.dip2px(context, 90);
//
//		if(!TextUtils.isEmpty(app_show_tag_list)){
//			String[] tagAry = app_show_tag_list.split(",");
//			String[] colorAry = null;
//			if(!TextUtils.isEmpty(app_show_color_list)){
//				colorAry = app_show_color_list.split(",");
//			}
//			for (int i = 0; i < tagAry.length; i++) {
//				if(TextUtils.isEmpty(tagAry[i])){
//					continue;
//				}
//				if("红河州第三人民医院".equals(Constants.APP_REGION) && tagAry[i].length()>=2){
//					// 目前默认只显示1个字
//					continue;
//				}
//				RoundTextView roundTextView = new RoundTextView(context);
//				if ("枣庄妇幼保健院".equals(Constants.APP_REGION)){
//					//枣庄妇幼，术后1-3天，显示图标
//					Drawable drawable = null;
//					if ("术后1天".equals(tagAry[i])){
//						drawable = context.getResources().getDrawable(R.drawable.shuhou_1);
//					}else if ("术后2天".equals(tagAry[i])){
//						drawable = context.getResources().getDrawable(R.drawable.shuhou_2);
//					}else if ("术后3天".equals(tagAry[i])){
//						drawable = context.getResources().getDrawable(R.drawable.shuhou_3);
//					}
//					if (null != drawable){
//						drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//						roundTextView.setBackground(drawable);
//						flowLayout.addView(roundTextView, new ViewGroup.LayoutParams(size, size));
//						continue;
//					}
//				}
//				roundTextView.setText(tagAry[i]);
//				roundTextView.setTextSize(13);
//				roundTextView.setGravity(Gravity.CENTER);
//				roundTextView.setTextColor(Color.WHITE);
//				String color = "#FF000000";
//				if(colorAry!=null && i<colorAry.length && !TextUtils.isEmpty(colorAry[i])){
//					color = colorAry[i];
//				}
//				roundTextView.setBackgroundColor(Color.parseColor(color), radius);
////				if("红河州第三人民医院".equals(Constants.APP_REGION) && tagAry[i].length()>=2){
////					width = (int) roundTextView.getPaint().measureText(tagAry[i]) + 10;
////					if (width > flowLayout.getWidth()){
////						height = (int) Math.ceil((double)width / flowLayout.getWidth()) * size;
////						width = flowLayout.getWidth();
////					}
////				}
//				flowLayout.addView(roundTextView, new ViewGroup.LayoutParams(size, size));
//			}
//		}
//	}

	/**
	 * 动态添加标记控件
	 */
//	public static void dynamicAddFlagView(Context context, FlowLayout flowLayout, Drawable drawable){
//		if(flowLayout == null){
//			return;
//		}
//		int size = DensityUtils.dip2px(context, 20);
//		float radius = DensityUtils.dip2px(context, 90);
//		RoundTextView roundTextView = new RoundTextView(context);
//		roundTextView.setBackground(drawable);
//		flowLayout.addView(roundTextView, new ViewGroup.LayoutParams(size, size));
//	}
}
