package com.zrt.mybase.utils;

import com.google.gson.Gson;

/**
 * @author：Zrt
 * @date: 2022/10/18
 */
public class JsonUtils {

    public static <T> String toJson(T t, Class<T> classz){
        Gson gson = new Gson();
        String s = gson.toJson(t, classz);
        return s;
    }
    /**
     * {"state":"ok","error":"200","msg":""}
     */
    public static <T> T parseGsonObject(String json, Class<T> classz) {
        Gson gson = new Gson();
        T t = gson.fromJson(json, classz);
        return t;
    }
}
