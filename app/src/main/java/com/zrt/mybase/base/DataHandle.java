package com.zrt.mybase.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import com.zrt.mybase.utils.MyLogger;

public class DataHandle<T, K> {
	private HandlerThread mHandlerThread;
	// 子线程中的handler
	private Handler asyncHandler;
	// UI线程中的handler
	private Handler mainHandler;
	// 以防退出界面后Handler还在执行
	private boolean hander_thread_state = true;
	// 用以表示该handler的结束
	private static final int MSG_UPDATE_INFO = 0x220;

	private Callables<T, K> callable;
	private DataHandle(){
		init();
	}
	public void init() {
		mHandlerThread = new HandlerThread(this.getClass().getName());
		mHandlerThread.start();
		asyncHandler = new Handler(mHandlerThread.getLooper());
		mainHandler = new Handler();
	}

	/**
	 * onResume()
	 */
	public void sendMsg() {
		if (asyncHandler == null) return;
		asyncHandler.sendEmptyMessage(MSG_UPDATE_INFO);
	}

	/**
	 * onPause()
	 */
	public void removeMsg() {
		if (asyncHandler == null) return;
		asyncHandler.removeMessages(MSG_UPDATE_INFO);
	}

	/**
	 * onDestroy()
	 */
	public void close() {
		if (mHandlerThread != null) {
			// 释放资源
			mHandlerThread.quit();
			mHandlerThread = null;
		}
	}

	public void setCallable(Callables<T, K> callable){
		this.callable = callable;
	}

	/**
	 * 查询数据
	 * @param state 查询条件
	 */
	public synchronized void getDataHandler(final K state) {
		if (asyncHandler == null) init();
//		if (null == UseActivity.this || UseActivity.this.isDestroyed() || UseActivity.this.isFinishing()) {
//			return;
//		}
//		viewUtil.showProgressTips(UseActivity.this, "数据加载中...");

		asyncHandler.post(new Runnable() {
			@Override
			public void run() {
				getData(state);
				if (hander_thread_state) {
					sendMsg();
				}
			}
		});
	}

	private void getData(final K state) {
		if (!hander_thread_state) {
			return;
		}
		hander_thread_state = false;
		MyLogger.Log().i("##A-----start");
		// 获取列表数据结果
		final T result = getDataList(state);
		MyLogger.Log().i("##A-----stop");
//		try {
//			Thread.sleep(30 * 1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		MyLogger.Log().i("##A-----end="+callable);
		// UI线程更新界面
		mainHandler.post(new Runnable() {
			@Override
			public void run() {
				if (callable != null){
					callable.onCallback(state, result);
				}
				hander_thread_state = true;
			}
		});
	}
	/**
	 * 查询数据
	 * @param state

	 */
	private <T> T  getDataList(K state) {
		// TODO Auto-generated method stub
		if (callable != null){
			T call = null;
			try {
				call = (T) callable.call(state);
			} catch (Exception e) {
				MyLogger.Log().i("##getDataList error："+e.toString());
			}
			return call;
		}
		return null;
	}

	public interface Callables<T, K>{
		T call(K state) throws Exception;;
		void onCallback(K state, T t);
	}

	public static class Builder<T, K> {
		Callables<T, K> callable;

		public Builder(Context context){}
		public Builder setCallable(Callables<T, K> callable){
			this.callable = callable;
			return this;
		}
		public DataHandle create(){
			DataHandle dataHandle = new DataHandle();
			dataHandle.setCallable(callable);
			return dataHandle;
		}
	}
}
