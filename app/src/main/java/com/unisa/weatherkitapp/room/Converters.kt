package com.unisa.weatherkitapp.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.unisa.weatherkitapp.data.locationdata.LocationInfo

class Converters {
    val gson = Gson()
    @TypeConverter fun converterLocationResponseToString(locationResponse: LocationInfo):String{
        return gson.toJson(locationResponse)
    }
    @TypeConverter fun converterStrToLocationResponse(str:String):LocationInfo{
        return gson.fromJson(str,LocationInfo::class.java)
    }
}