package com.zrt.weatherapp.logic.network

import com.zrt.weatherapp.MyApplication
import com.zrt.weatherapp.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author：Zrt
 * @date: 2022/8/15
 */
interface PlaceService {
    /**
     * 定义一个搜索天气城市的Retrofit接口
     */
    @GET("v2/place?token=${MyApplication.TOKEN_WEATHER}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>

}