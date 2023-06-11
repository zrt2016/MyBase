package com.zrt.nfcapp.utils;

import com.google.gson.Gson;


/**
 * json�ַ��������࣬Gson����ת�ɶ���
 */
public class GsonUtil {
	/**
	 * ת��Object����ΪJson����
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
	 * ת��Json����ΪObject
	 * {"state":"ok","error":"200","msg":""}
	 */
	public static <T> T parseGsonObject(String json, Class<T> classz) {
		Gson gson = new Gson();
		T t = gson.fromJson(json, classz);
		return t;
	}

}
