package com.zrt.mybase.utils.support.async;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;

import java.lang.ref.WeakReference;

/**
 * Created by zhoubo on 2017/4/6.
 */

@SuppressWarnings("hiding")
public abstract class CommonAsyncTask<Params, Progress, Result, AsyncTaskOwner> extends AsyncTask<Params, Progress, Result> {

	private WeakReference<AsyncTaskOwner> asyncTaskReference;

	public CommonAsyncTask(AsyncTaskOwner owner){
		asyncTaskReference = new WeakReference<AsyncTaskOwner>(owner);
	}

	@Override
	public void onPreExecute() {
		final AsyncTaskOwner owner = asyncTaskReference.get();
		if (!isDestroyed(owner)) onPreExecute(owner);
	}

	@Override
	public Result doInBackground(Params... params) {
		final AsyncTaskOwner owner = asyncTaskReference.get();
		if(!isDestroyed(owner)){
			return doInBackground(owner, params);
		}
		return null;
	}

	@Override
	public void onPostExecute(Result result) {
		final AsyncTaskOwner owner = asyncTaskReference.get();
		if (!isDestroyed(owner)) {
			onPostExecute(owner, result);
			onTaskComplete(owner);
		}
	}

	@Override
	public void onCancelled() {
		super.onCancelled();
		final AsyncTaskOwner owner = asyncTaskReference.get();
		if (!isDestroyed(owner)) {
			onTaskComplete(owner);
		}
	}

	public void onPreExecute(AsyncTaskOwner owner){}

	public abstract Result doInBackground(AsyncTaskOwner owner, Params... params);

	public void onPostExecute(AsyncTaskOwner owner, Result result){}

	public void onTaskComplete(AsyncTaskOwner owner){};

	public boolean isDestroyed(AsyncTaskOwner owner){
		if (owner instanceof Activity) {
			Activity activity = (Activity) owner;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
				return (activity.isFinishing() || activity.isDestroyed());
			} else {
				return activity.isFinishing();
			}
		}

		return owner == null;
	}
}
