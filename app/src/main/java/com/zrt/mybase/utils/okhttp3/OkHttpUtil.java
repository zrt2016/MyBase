package com.zrt.mybase.utils.okhttp3;

import android.util.Log;

import com.zrt.mybase.utils.Constants;
import com.zrt.mybase.utils.MyLogger;
import com.zrt.mybase.utils.compression.GlibCompression;
import com.zrt.mybase.utils.compression.ICompression;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * OkHttp工具类
 */
public class OkHttpUtil {

    private volatile static OkHttpUtil okHttpUtil = null;

    private static ICompression mCompress = new GlibCompression();

    public interface OkHttpListener {
        void requestSucess(String reponse);
        void requestError(Exception e);
    }

    private OkHttpUtil() {}

    public static OkHttpUtil getInstance() {
        if (okHttpUtil == null) {
            synchronized (OkHttpUtil.class) {
                if (okHttpUtil == null) {
                    okHttpUtil = new OkHttpUtil();
                }
            }
        }
        return okHttpUtil;
    }

    /**
     * Get请求方法
     * @param url
     * @param mListener
     */
    public void getRequestMethod(String url, Map<String, Object> addtionalParams, final OkHttpListener mListener) {

        if(null != addtionalParams) {
            StringBuilder strs = new StringBuilder();
            Iterator<String> localIterator = addtionalParams.keySet().iterator();
            while (localIterator.hasNext()) {
                String key = (String) localIterator.next();
                String value = (String) addtionalParams.get(key);
                strs.append(key).append("=").append(value).append("&");
            }
            if(url.contains("?")) {
                url += "&"+strs.toString();
            }else {
                url += "?"+strs.toString();
            }
        }
        Request request = new Request.Builder().url(url).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mListener.requestError(e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mListener.requestSucess(response.body().string());
            }
        });
    }

    /**
     * Get请求方法
     */
    public static Response getRequestMethod(String url, Map<String, Object> addtionalParams) {
        if(null != addtionalParams) {
            StringBuilder strs = new StringBuilder();
            Iterator<String> localIterator = addtionalParams.keySet().iterator();
            while (localIterator.hasNext()) {
                String key = (String) localIterator.next();
                String value = (String) addtionalParams.get(key);
                strs.append(key).append("=").append(value).append("&");
            }
            if(url.contains("?")) {
                url += "&"+strs.toString();
            }else {
                url += "?"+strs.toString();
            }
        }
        Request request = new Request.Builder().url(url).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        }catch(IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Get请求方法
     */
    public static Response getRequestMethod(String url) {
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = new OkHttpClient().newCall(request).execute();
        }catch(IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Post 请求方法
     */
    public static void postRequestMethod(String url, Map<String, Object> bodyParams, final OkHttpListener mListener) {
        RequestBody body = setRequestBodyMap(bodyParams);
        Request request = new Request.Builder().url(url).post(body)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("cs", "error："+e.getMessage());
                mListener.requestError(e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mListener.requestSucess(response.body().string());	// body().string() 只能调用一次
            }
        });
    }

    /**
     * Post 请求方法
     */
    public static String postRequestMethod(String url, Map<String, String> bodyParams) throws IOException {
        return postRequestMethod(url, bodyParams, true);
    }

    public static String postRequestMethod(String url, Map<String, String> bodyParams, boolean http_data_compression_flag) throws IOException {
        RequestBody requestBody = setRequestBody(bodyParams);
        Response response = oKHttp3RequestResponse(url, requestBody);
        String result = "";
        if(null != response && response.isSuccessful()) {
            if(http_data_compression_flag) {
                byte[] arrayOfByte = response.body().bytes();
                //Log.e("zpd", "开始解压数据,解压前数据大小: " + arrayOfByte.length);
                result = mCompress.decompress(url,arrayOfByte);
                //Log.e("zpd", "开始解压数据,解压后数据大小: " + result.getBytes().length);
            }else {
                result = response.body().string();
            }
        }
        return result;
    }

    /**
     * Okhttp3 Client 封装
     * @param url
     * @param requestBody
     * @return
     * @throws IOException
     */
    public static Response oKHttp3RequestResponse(String url, RequestBody requestBody){
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
//		OkHttpClient okHttpClient = new OkHttpClient();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//				.hostnameVerifier((hostname, session) -> true)
                .connectTimeout(Constants.HTTP_REQUEST_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.HTTP_REQUEST_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.HTTP_REQUEST_WRITE_TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)

                //自定义连接池最大空闲连接数和等待时间大小，否则默认最大5个空闲连接
                .connectionPool(new ConnectionPool(32,5, TimeUnit.MINUTES))

                .build();

        Call call = okHttpClient.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        }catch (Exception e) {
            MyLogger.Log().e("## error : "+ e.getMessage() + "; url"+url);
        }
        return response;
    }

    /**
     * Okhttp3 post请求基础方法封装
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */
    public static String postRequestNameValuePairMethod(String url, Map<String, String> paramMap) throws IOException {
        String result = null;
        FormBody.Builder formBody = null;
        try {
            formBody = OkHttpUtil.setRequestFormBodyParams(paramMap);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        RequestBody requestBody = formBody.build();
        Response response = oKHttp3RequestResponse(url, requestBody);
        if (response != null) {
            // 读取服务器响应的数据
            if(response.isSuccessful()) {
//        		result = response.body().string();
                if (Constants.http_data_compression_flag) {
                    byte[] arrayOfByte = response.body().bytes();
                    //Log.e("zpd", "开始解压数据,解压前数据大小: " + arrayOfByte.length);
                    result = mCompress.decompress(url,arrayOfByte);
                    //Log.e("zpd", "开始解压数据,解压后数据大小: " + result.getBytes().length);
                } else {
                    result = response.body().string(); // 未使用压缩，直接返回result 结果
                }
            }else {
                result = null;
            }
        }
        return result;
    }

    /**
     * Okhttp3 post请求基础方法封装
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */
    public static String postRequestBasicNameValuePairMethod(String url, Map<String, String> paramMap) throws IOException {
        return postRequestBasicNameValuePairMethod(url, paramMap, true);
    }

    public static String postRequestBasicNameValuePairMethod(String url, Map<String, String> paramMap, boolean http_data_compression_flag) throws IOException {
        String result = null;
        FormBody.Builder formBody = null;
        try {
            formBody = OkHttpUtil.setRequestFormBodyParams(paramMap);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        RequestBody requestBody = formBody.build();
        Response response = oKHttp3RequestResponse(url, requestBody);
        if (response != null) {
            // 读取服务器响应的数据
            if(response.isSuccessful()) {
                if(http_data_compression_flag) {
                    byte[] arrayOfByte = response.body().bytes();
//        				Log.e("zpd", "开始解压数据,解压前数据大小: " + arrayOfByte.length);
                    result = mCompress.decompress(url,arrayOfByte);
//        				Log.e("zpd", "开始解压数据,解压后数据大小: " + result.getBytes().length);
                }else {
                    result = response.body().string(); // 未使用压缩，直接返回result 结果
                }
            }else {
                result = null;
                MyLogger.Log().e("## response is fail error "+url);
            }
        }else {
            MyLogger.Log().e("## response is null error "+url);
        }
        return result;
    }

    /**
     * post的请求参数，构造RequestBody
     * @param paramMap
     * @return
     * @throws JSONException
     */
    public static FormBody.Builder setRequestFormBodyParams(Map<String, String> paramMap) throws JSONException {
        okhttp3.FormBody.Builder formEncodingBuilder=new okhttp3.FormBody.Builder();
        if(paramMap != null){
            Set<Map.Entry<String, String>> entries = paramMap.entrySet();
            for (Map.Entry<String, String> en : entries){
                formEncodingBuilder.add(en.getKey(), notEmpty(en.getValue()));
            }
        }
        return formEncodingBuilder;
    }

    public static String notEmpty(String src) {
        return (src == null || src.equals("null")) ? "" : src.toString();
    }

    /**
     * post的请求参数，构造RequestBody
     */
    private static RequestBody setRequestBodyMap(Map<String, Object> bodyParams){
        RequestBody body=null;
        okhttp3.FormBody.Builder formEncodingBuilder=new okhttp3.FormBody.Builder();
        if(bodyParams != null){
            Iterator<String> iterator = bodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                formEncodingBuilder.add(key, bodyParams.get(key)+"");
            }
        }
        body=formEncodingBuilder.build();
        return body;
    }

    private static RequestBody setRequestBody(Map<String, String> bodyParams){
        RequestBody body=null;
        okhttp3.FormBody.Builder formEncodingBuilder=new okhttp3.FormBody.Builder();
        if(bodyParams != null){
            Iterator<String> iterator = bodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                formEncodingBuilder.add(key, notEmpty(bodyParams.get(key)));
            }
        }
        body=formEncodingBuilder.build();
        return body;
    }
}
