package com.unisa.weatherkitapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.unisa.weatherkitapp.data.locationdata.LocationInfo

@Entity
data class LocationEntity(
    @PrimaryKey
    val locationId:String,
    val locationInfo: LocationInfo,
    var selected:Int = 0,
    val insertTime:Long = System.currentTimeMillis()
)
