package com.zrt.weatherapp.logic.model

import com.google.gson.annotations.SerializedName

/**
 * @author：Zrt
 * @date: 2022/8/15
 */
class PlaceResponse(val status: String, val places: List<Place>)

class Place(val name: String, val location: Location, @SerializedName("formatted_address") val address: String)

class Location(val lng: String, val lat: String)