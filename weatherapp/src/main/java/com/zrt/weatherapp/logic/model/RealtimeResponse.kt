package com.zrt.weatherapp.logic.model

import com.google.gson.annotations.SerializedName

/**
 * @author：Zrt
 * @date: 2022/8/20
 * @SerializedName("ari_quality") 使Json字段与Kotlin字段建立映射关系。因为Kotlin中命名规范与Json不太一致。
 * RealtimeResponse 对应Json数据格式
 * {
 *      "status":"ok",
 *      "result":{
 *          "realtime":{
 *              "temperature":23.16,
 *              "skycon":"WIND",
 *              "ari_quality":{
 *                  "aqi":{"chn" : 17.0}
 *              }
 *          }
 *      }
 * }
 */
class RealtimeResponse(val status:String, val result:Result) {
    class Result(val realtime:Realtime)
    class Realtime(val skycon:String, val temperature:Double,
            @SerializedName("air_quality") val airQuality:AirQuality)
    class AirQuality(val aqi: AQI)
    class AQI(val chn: Float)
}