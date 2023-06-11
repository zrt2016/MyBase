package com.zrt.weatherapp.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zrt.weatherapp.logic.Repository
import com.zrt.weatherapp.logic.model.Place

/**
 * @author：Zrt
 * @date: 2022/8/16
 */
class PlaceViewModel: ViewModel() {
    private val searchLiveData = MutableLiveData<String>()
    val placeList = ArrayList<Place>()
    val placeLiveData = Transformations.switchMap(searchLiveData){
        query->
        Repository.searchPlaces(query)
    }
    fun searchPlaces(query:String){
        searchLiveData.value = query
    }

    fun savePlace(place: Place) = Repository.savePlace(place)

    fun getSavedPlace() = Repository.getSavedPlace()

    fun isPlaceSaved() = Repository.isPlaceSaved()
}