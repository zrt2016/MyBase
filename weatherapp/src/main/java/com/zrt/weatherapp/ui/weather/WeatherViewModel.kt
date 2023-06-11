package com.zrt.weatherapp.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zrt.weatherapp.logic.Repository
import com.zrt.weatherapp.logic.model.Location

/**
 * @author：Zrt
 * @date: 2022/8/20
 */
class WeatherViewModel :ViewModel() {
    private val localtionLiveData = MutableLiveData<Location>()
    var locationLng = ""
    var locationLat = ""
    var placeName = ""
    val weatherLiveData = Transformations.switchMap(localtionLiveData){
        location ->
        Repository.refreshWeather(location.lng, location.lat)
    }
    fun refreshWeather(lng:String, lat:String){
        localtionLiveData.value = Location(lng, lat)
    }
}