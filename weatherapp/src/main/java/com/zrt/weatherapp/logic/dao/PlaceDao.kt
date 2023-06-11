package com.zrt.weatherapp.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.zrt.weatherapp.MyApplication
import com.zrt.weatherapp.logic.model.Place

/**
 * @author：Zrt
 * @date: 2022/8/22
 */
object PlaceDao {
    val KEY_PLACE = "place"
    fun savePlace(place:Place){
        sharedPreferences().edit {
            putString(KEY_PLACE, Gson().toJson(place))
        }
    }
    fun getSavedPlace():Place{
        val placeJson = sharedPreferences().getString(KEY_PLACE, "")
        return Gson().fromJson(placeJson, Place::class.java)
    }
    fun isPlaceSaved() = sharedPreferences().contains(KEY_PLACE)

    private fun sharedPreferences() = MyApplication.context.
        getSharedPreferences("my_weather", Context.MODE_PRIVATE)

}