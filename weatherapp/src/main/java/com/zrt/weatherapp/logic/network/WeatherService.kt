package com.zrt.weatherapp.logic.network

import com.zrt.weatherapp.MyApplication
import com.zrt.weatherapp.logic.model.DailyResponse
import com.zrt.weatherapp.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author：Zrt
 * @date: 2022/8/20
 * 定义访问天气的接口
 */
interface WeatherService {
    @GET("v2.5/${MyApplication.TOKEN_WEATHER}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<RealtimeResponse>

    @GET("v2.5/${MyApplication.TOKEN_WEATHER}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<DailyResponse>
}