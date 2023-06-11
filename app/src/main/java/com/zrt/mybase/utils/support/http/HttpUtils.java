package com.zrt.mybase.utils.support.http;

import android.content.ContentValues;
import android.util.Log;

import com.zrt.mybase.utils.Constants;
import com.zrt.mybase.utils.MyLogger;
import com.zrt.mybase.utils.StringHelper;
import com.zrt.mybase.utils.compression.GlibCompression;
import com.zrt.mybase.utils.compression.ICompression;
import com.zrt.mybase.utils.okhttp3.OkHttpUtil;
import com.zrt.mybase.utils.support.async.AsyncTaskOwner;
import com.zrt.mybase.utils.support.async.AsyncTaskUtil;
import com.zrt.mybase.utils.support.async.Callable;
import com.zrt.mybase.utils.support.async.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoubo on 2017/4/6.
 */

public class HttpUtils {

	private static final String TAG = HttpUtils.class.getSimpleName();

	private volatile static HttpUtils mInstance;
	private ICompression mCompress = new GlibCompression();

	public static final int GET = 0;
	public static final int POST = 1;

	public HttpUtils() {
	}

	public static HttpUtils getInstance() {
		HttpUtils temp = mInstance;
		if (temp == null) {
			synchronized (HttpUtils.class) {
				temp = mInstance;
				if (temp == null) {
					temp = new HttpUtils();
					mInstance = temp;
				}
			}
		}
		return temp;
	}
//
//	private ArrayList<BasicNameValuePair> setPostRequestBody(Map<String, String> bodyParams) {
//		ArrayList<BasicNameValuePair> paramsList = new ArrayList<BasicNameValuePair>();
//		if (bodyParams != null) {
//			Iterator<String> iterator = bodyParams.keySet().iterator();
//			String key = "";
//			while (iterator.hasNext()) {
//				key = iterator.next().toString();
//				paramsList.add(new BasicNameValuePair(key, bodyParams.get(key)));
//			}
//		}
//		// Log.i(">>>>", "##BasicNameValuePair="+paramsList.toString());
//		return paramsList;
//	}
//
//	/**
//	 * get方法连接拼加参数
//	 *
//	 * @param mapParams
//	 * @return
//	 */
//	private String setGetUrlParams(Map<String, String> mapParams) {
//		String strParams = "";
//		if (mapParams != null) {
//			Iterator<String> iterator = mapParams.keySet().iterator();
//			String key = "";
//			while (iterator.hasNext()) {
//				key = iterator.next().toString();
//				strParams += "&" + key + "=" + mapParams.get(key);
//			}
//		}
//		return strParams;
//	}
//
//	private JSONArray setPostRequestBodyJsonArray(ArrayList<ContentValues> arrayListContentValues) {
//		JSONArray yizhuZhixingRecordJSONArray = new JSONArray();
//		if (arrayListContentValues != null) {
//			try {
//				if(null == arrayListContentValues || arrayListContentValues.isEmpty()){
//					return null;
//				}
//				for(int i = 0; i < arrayListContentValues.size(); i++){
//					JSONObject localJSONObject = new JSONObject();
//					ContentValues cvParams = arrayListContentValues.get(i);
//					for (java.util.Map.Entry<String, Object> item : cvParams.valueSet()) {
//						localJSONObject.put(item.getKey(), item.getValue());
//					}
//					yizhuZhixingRecordJSONArray.put(localJSONObject);
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//				MyLogger.Log().e("#==setPostRequestBodyJsonArray exception:"+e);
//			}
//		}
//		return yizhuZhixingRecordJSONArray;
//	}
//
//	public <T> void execute(int method, String reqUrl, Map<String, String> params, NetCallback<T> callback, AsyncTaskOwner ower) {
//		switch (method) {
//			case GET:
//				doGet(reqUrl, params, callback);
//				break;
//			case POST:
//				doPost(reqUrl, params, callback, ower);
//				break;
//		}
//	}
//
//	public <T> void execute(int method, String reqUrl, Map<String, String> params, NetCallback<T> callback, AsyncTaskOwner ower, boolean http_data_compression_flag) {
//		switch (method) {
//			case GET:
//				doGet(reqUrl, params, callback);
//				break;
//			case POST:
//				doPost(reqUrl, params, callback, ower, http_data_compression_flag);
//				break;
//		}
//	}
//
//	public <T> void execute(int method, String reqUrl, ArrayList<ContentValues> arrayListContentValues,
//							GlobalInfoApplication current_application, NetCallback<T> callback, AsyncTaskOwner ower) {
//		execute(method, reqUrl, arrayListContentValues, null, current_application, callback, ower, Constants.http_data_compression_flag);
//	}
//
//	/**
//	 * 医嘱模块专用
//	 * @param method
//	 * @param reqUrl
//	 * @param arrayListContentValues
//	 * @param current_application
//	 * @param callback
//	 * @param ower
//	 */
//	public <T> void execute(int method, String reqUrl, ArrayList<ContentValues> arrayListContentValues, List<BasicNameValuePair> pairs,
//							GlobalInfoApplication current_application, NetCallback<T> callback, AsyncTaskOwner ower,
//							boolean http_data_compression_flag) {
//		switch (method) {
//			case GET:
//				doGet(reqUrl, arrayListContentValues,current_application, callback);
//				break;
//			case POST:
//				doPost(reqUrl, arrayListContentValues, pairs, current_application, callback, ower, http_data_compression_flag);
//				break;
//		}
//	}
//
//	public <T> void execute2(int method, String reqUrl, ArrayList<ContentValues> arrayListContentValues, GlobalInfoApplication current_application, NetCallback<T> callback, AsyncTaskOwner ower) {
//		switch (method) {
//			case GET:
//				doGet(reqUrl, arrayListContentValues,current_application, callback);
//				break;
//			case POST:
//				doPost2(reqUrl, arrayListContentValues,current_application, callback, ower);
//				break;
//		}
//	}
//
//	/**
//	 * get
//	 *
//	 * @param reqUrl
//	 * @param params
//	 * @param callback
//	 */
//	private <T> void doGet(String reqUrl, Map<String, String> params, NetCallback<T> callback) {
//		String url = reqUrl + setGetUrlParams(params);
//		Log.d(TAG, url);
//	}
//
//	private <T> void doGet(String reqUrl, ArrayList<ContentValues> arrayListContentValues, GlobalInfoApplication current_application, NetCallback<T> callback) {
//	}
//
//
//
//	private <T> void doPost(final String reqUrl, final Map<String, String> params, final NetCallback<T> callback, AsyncTaskOwner ower) {
//		AsyncTaskUtil.getInstance().execute(new Callable<T>() {
//			@SuppressWarnings({ "unchecked" })
//			@Override
//			public T call() {
//				try {
//					String resultStr = OkHttpUtil.postRequestBasicNameValuePairMethod(reqUrl, setPostRequestBody(params));
//					if (!StringHelper.isEmpty(resultStr)) {
//						T t = (T) resultStr;
//						if (t instanceof String) {
//							return t;
//						}
//					}else {
//						MyLogger.Log().d(String.format("url:%s ===> httpStateCode:%s", reqUrl, resultStr));
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					MyLogger.Log().d(String.format("url:%s ===> Exception:%s", reqUrl, e.toString()));
//					return null;
//				}
//				return null;
//			}
//		}, new Callback<T>() {
//			@Override
//			public void onCallback(T result) {
//				//Log.i(">>>>", "##Callback="+result);
//				if (result != null) {
//					if (callback != null)
//						callback.onSuccess(0, result);
//				} else {
//					if (callback != null){
//						//callback.onFailure(1, new JSONObject((String) result).get(Constants.MSG_RESULT)+ "");
//						MyLogger.Log().d("===>url:"+reqUrl+"，result:"+result);
//						callback.onFailure(1, "null, 返回结果异常！");
//					}
//				}
//			}
//		}, ower);
//	}
//
//	private <T> void doPost(final String reqUrl, final Map<String, String> params, final NetCallback<T> callback, AsyncTaskOwner ower,final boolean http_data_compression_flag) {
//		AsyncTaskUtil.getInstance().execute(new Callable<T>() {
//			@SuppressWarnings({ "unchecked" })
//			@Override
//			public T call() {
//				try {
//					String resultStr = OkHttpUtil.postRequestBasicNameValuePairMethod(reqUrl, setPostRequestBody(params),http_data_compression_flag);
//					if (!StringHelper.isEmpty(resultStr)) {
//						T t = (T) resultStr;
//						if (t instanceof String) {
//							return t;
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					return null;
//				}
//				return null;
//			}
//		}, new Callback<T>() {
//			@Override
//			public void onCallback(T result) {
//				// Log.i(">>>>", "##Callback="+result);
//				if (result != null) {
//					if (callback != null)
//						callback.onSuccess(0, result);
//				} else {
//					if (callback != null){
//						//callback.onFailure(1, new JSONObject((String) result).get(Constants.MSG_RESULT)+ "");
//						callback.onFailure(1, "null, 返回结果异常！");
//					}
//				}
//			}
//		}, ower);
//	}
//
//	public String httpPost(String reqUrl, Map<String, String> params, boolean http_data_compression_flag) {
//		String resultStr = null;
//		try {
//			resultStr = OkHttpUtil.postRequestBasicNameValuePairMethod(reqUrl, setPostRequestBody(params), http_data_compression_flag);
//		} catch (IOException e) {
//			e.printStackTrace();
//			MyLogger.Log().d("##httpPost exception reqUrl:" + reqUrl+",e:"+e);
//		}
//		return resultStr;
//	}
//
//	private <T> void doPost(final String reqUrl, final ArrayList<ContentValues> arrayListContentValues, final List<BasicNameValuePair> pairs,
//							final GlobalInfoApplication current_application, final NetCallback<T> callback, AsyncTaskOwner ower,
//							final boolean http_data_compression_flag) {
//		AsyncTaskUtil.getInstance().execute(new Callable<T>() {
//			@SuppressWarnings({ "unchecked" })
//			@Override
//			public T call() {
//				try {
//					String str1 = "";
//					if(null != arrayListContentValues){
//						str1 = setPostRequestBodyJsonArray(arrayListContentValues).toString();
//					}
//					ArrayList<BasicNameValuePair> localArrayList2 = new ArrayList<BasicNameValuePair>();
//					if (http_data_compression_flag) {
//						localArrayList2.add(new BasicNameValuePair("data", new Gson().toJson(mCompress.compress(reqUrl,str1.getBytes()))));
//						localArrayList2.add(new BasicNameValuePair("compression_http_data","on"));
//					} else {
//						localArrayList2.add(new BasicNameValuePair("data", str1));
//					}
//					localArrayList2.add(new BasicNameValuePair("online_submit_data", "on")); // 标记为在线提交
//					localArrayList2.add(new BasicNameValuePair("user_number",current_application.current_user_number));
//					localArrayList2.add(new BasicNameValuePair("session_id",current_application.current_user_session_id));
//					localArrayList2.add(new BasicNameValuePair("bingqu_name",current_application.current_user_bingqu_name));
//					localArrayList2.add(new BasicNameValuePair("bingqu_id", current_application.current_user_bingqu_id));
//					localArrayList2.add(new BasicNameValuePair("keshi_id",current_application.current_patient_keshi_id));
//					localArrayList2.add(new BasicNameValuePair("keshi_name", current_application.current_patient_keshi_name));
//					localArrayList2.add(new BasicNameValuePair("shebei_id",current_application.device_id));
//					localArrayList2.add(new BasicNameValuePair("version",Constants.CURRENT_USE_VERSION));
//					localArrayList2.add(new BasicNameValuePair("yiyuan_id",Constants.YIYUAN_ID));
////					for (BasicNameValuePair basicNameValuePair : localArrayList2) {
////					 	Log.i("yzzx:::", basicNameValuePair.getName() + "/" + basicNameValuePair.getValue());
////					}
//
//					if (pairs != null && !pairs.isEmpty()) {
//						localArrayList2.addAll(pairs);
//					}
//
//					String resultStr = OkHttpUtil.postRequestBasicNameValuePairMethod(reqUrl, localArrayList2, http_data_compression_flag);
//					if (!StringHelper.isEmpty(resultStr)) {
//						StringBuffer saveResult = new StringBuffer();
//						if (new JSONObject(resultStr).get(Constants.ERROR_RESULT).equals(Constants.OK_RESULT)) {
//							// MyLogger.Log().e("## 修改皮试结果返回值："+(t.toString()));
//
//							String data = new JSONObject(resultStr).get("data")+"";
//							if (!"".equals(data)) {
//								List<Map<String, Object>> list = GsonUtil.parseMapObjectList(data);
//								if (null != list && !list.isEmpty()) {
//									for (int i = 0; i < list.size(); i++) {
//										Map<String, Object> info = list.get(i);
//										if ("1001".equals(info.get("state"))) {
//											// 1001：数据上传成功，其他状态返回操作异常！
//											saveResult = new StringBuffer().append("保存成功");
//										} else {
//											saveResult.append(info.get("state")).append(";").append(current_application.error_tip_code_map.get(info.get("state")));
//										}
//									}
//								}else{
//									saveResult = new StringBuffer().append("保存成功");
//								}
//							}
//						} else {
//							MyLogger.Log().w(resultStr);
//							String errorCode = new JSONObject(resultStr).get(Constants.ERROR_RESULT).toString();
//							if(!StringHelper.isEmpty(current_application.error_tip_code_map.get(errorCode))){
//								saveResult =  new StringBuffer().append(current_application.error_tip_code_map.get(errorCode));
//							}else{
//								saveResult = new StringBuffer().append(new JSONObject(resultStr).get(Constants.MSG_RESULT));
//							}
//						}
//						return (T)saveResult.toString();
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					return null;
//				}
//				return null;
//			}
//		}, new Callback<T>() {
//			@Override
//			public void onCallback(T result) {
//				//Log.i(">>>>", "##Callback="+result);
//				if (result != null) {
//					if (callback != null){
//						callback.onSuccess(0, result);
//					}
//				} else {
//					if (callback != null){
//						//callback.onFailure(1, new JSONObject((String) result).get(Constants.MSG_RESULT)+ "");
//						MyLogger.Log().e("## 返回结果为空，请求异常！\n reqUrl:"+reqUrl);
//						callback.onFailure(1, "请求超时，请重试！");
//					}
//				}
//			}
//		}, ower);
//	}
//
//
//	private <T> void doPost2(final String reqUrl, final ArrayList<ContentValues> arrayListContentValues, final GlobalInfoApplication current_application,
//							 final NetCallback<T> callback, AsyncTaskOwner ower) {
//		AsyncTaskUtil.getInstance().execute(new Callable<T>() {
//			@SuppressWarnings({ "unchecked" })
//			@Override
//			public T call() {
//				try {
//					String str1 = "";
//					if(null != arrayListContentValues){
//						str1 = setPostRequestBodyJsonArray(arrayListContentValues).toString();
//					}
//					ArrayList<BasicNameValuePair> localArrayList2 = new ArrayList<BasicNameValuePair>();
//					if (Constants.http_data_compression_flag) {
//						localArrayList2.add(new BasicNameValuePair("data", new Gson().toJson(mCompress.compress(reqUrl,str1.getBytes()))));
//						localArrayList2.add(new BasicNameValuePair("compression_http_data","on"));
//					} else {
//						localArrayList2.add(new BasicNameValuePair("data", str1));
//					}
//					localArrayList2.add(new BasicNameValuePair("online_submit_data", "on")); // 标记为在线提交
//					localArrayList2.add(new BasicNameValuePair("user_number",current_application.current_user_number));
//					localArrayList2.add(new BasicNameValuePair("session_id",current_application.current_user_session_id));
//					localArrayList2.add(new BasicNameValuePair("bingqu_name",current_application.current_user_bingqu_name));
//					localArrayList2.add(new BasicNameValuePair("bingqu_id", current_application.current_user_bingqu_id));
//					localArrayList2.add(new BasicNameValuePair("keshi_id",current_application.current_patient_keshi_id));
//					localArrayList2.add(new BasicNameValuePair("keshi_name", current_application.current_patient_keshi_name));
//					localArrayList2.add(new BasicNameValuePair("shebei_id",current_application.device_id));
//					localArrayList2.add(new BasicNameValuePair("version",Constants.CURRENT_USE_VERSION));
//					localArrayList2.add(new BasicNameValuePair("yiyuan_id",Constants.YIYUAN_ID));
////					for (BasicNameValuePair basicNameValuePair : localArrayList2) {
////					 	Log.i("yzzx:::", basicNameValuePair.getName() + "/" + basicNameValuePair.getValue());
////					}
//					String resultStr = OkHttpUtil.postRequestBasicNameValuePairMethod(reqUrl, localArrayList2);
//					if (!StringHelper.isEmpty(resultStr)) {
//						StringBuffer saveResult = new StringBuffer();
//						JSONObject jsonObject = new JSONObject(resultStr);
//						if (jsonObject.get(Constants.ERROR_RESULT).equals(Constants.OK_RESULT)) {
//							MyLogger.Log().e("## 修改皮试结果返回值："+(resultStr));
//
//							String data = jsonObject.get("msg")+"";
//							saveResult = new StringBuffer().append(data);
//						} else {
//							MyLogger.Log().w(resultStr);
//							String errorCode = jsonObject.get(Constants.ERROR_RESULT).toString();
//							if(!StringHelper.isEmpty(current_application.error_tip_code_map.get(errorCode))){
//								saveResult =  new StringBuffer().append(current_application.error_tip_code_map.get(errorCode));
//							}else{
//								saveResult = new StringBuffer().append(jsonObject.get(Constants.MSG_RESULT));
//							}
//						}
//						return (T)saveResult.toString();
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					return null;
//				}
//				return null;
//			}
//		}, new Callback<T>() {
//			@Override
//			public void onCallback(T result) {
//				//Log.i(">>>>", "##Callback="+result);
//				if (result != null) {
//					if (callback != null){
//						callback.onSuccess(0, result);
//					}
//				} else {
//					if (callback != null){
//						//callback.onFailure(1, new JSONObject((String) result).get(Constants.MSG_RESULT)+ "");
//						MyLogger.Log().e("## 返回结果为空，请求异常！\n reqUrl:"+reqUrl);
//						callback.onFailure(1, "请求超时，请重试！");
//					}
//				}
//			}
//		}, ower);
//	}
}