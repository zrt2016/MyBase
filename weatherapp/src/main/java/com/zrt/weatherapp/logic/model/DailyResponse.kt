package com.zrt.weatherapp.logic.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * @author：Zrt
 * @date: 2022/8/20
 */
data class DailyResponse(val status:String, val result: Result) {
    class Result(val daily: Daily)
    class Daily(val temperature: List<Temperature>, val skycon: List<Skycon>,
            @SerializedName("life_index") val lifeIndex: LifeIndex)
    class Temperature(val max:Float, val min:Float)
    class Skycon(val value:String, val date: Date)
    class LifeIndex(val coldRisk: ArrayList<LifeDescription>, val carWashing: List<LifeDescription>,
            val ultraviolet: List<LifeDescription>, val dressing: List<LifeDescription>)
    class LifeDescription(val desc: String)
}