package com.zrt.nfcapp.utils;

import com.google.gson.Gson;


/**
 * json字符串处理类，Gson可以转成对象
 */
public class GsonUtil {
	/**
	 * 转换Object对象为Json数据
	 * @param t
	 * @param classz
	 * @param <T>
	 * @return
	 */
	public static <T> String toJson(T t, Class<T> classz){
		Gson gson = new Gson();
		String s = gson.toJson(t, classz);
		return s;
	}
	/**
	 * 转换Json对象为Object
	 * {"state":"ok","error":"200","msg":""}
	 */
	public static <T> T parseGsonObject(String json, Class<T> classz) {
		Gson gson = new Gson();
		T t = gson.fromJson(json, classz);
		return t;
	}

}
