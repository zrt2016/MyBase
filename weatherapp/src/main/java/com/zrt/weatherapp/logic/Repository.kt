package com.zrt.weatherapp.logic

import androidx.lifecycle.liveData
import com.zrt.weatherapp.logic.dao.PlaceDao
import com.zrt.weatherapp.logic.model.Place
import com.zrt.weatherapp.logic.model.Weather
import com.zrt.weatherapp.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

/**
 * @author：Zrt
 * @date: 2022/8/16
 */
object Repository {
    //liveData是lifecycle-livedata-ktx提供的，可以自动返回一个LiveData对象，然后在代码中提供一个挂起函数的上下文
    fun searchPlaces(query:String) = fire<List<Place>>(Dispatchers.IO){
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        }else{
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    fun refreshWeather(lng: String, lat: String) = fire<Weather>(Dispatchers.IO) {
        coroutineScope {
            val  deferredRealtime =  async {
                SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDialy = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dialyResponse = deferredDialy.await()
            if (realtimeResponse.status == "ok" && dialyResponse.status == "ok"){
                val weather = Weather(realtimeResponse.result.realtime, dialyResponse.result.daily)
                Result.success(weather)
            }else {
                Result.failure(
                    RuntimeException(
                            "realtime response status is ${realtimeResponse.status}" +
                            "dialy response status is ${dialyResponse.status}"
                    )
                )
            }
        }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
            liveData<Result<T>>(context) {
                val result = try {
                    block()
                } catch (e: Exception) {
                    Result.failure<T>(e)
                }
                emit(result)
            }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

}