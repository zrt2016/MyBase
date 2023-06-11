package com.zrt.weatherapp.logic.model

/**
 * @author：Zrt
 * @date: 2022/8/20
 */
data class Weather(val realtime: RealtimeResponse.Realtime, val daily: DailyResponse.Daily) {
}