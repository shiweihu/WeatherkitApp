package com.unisa.weatherkitapp.data.locationdata

data class GeoPosition(
    val Elevation: Elevation = Elevation(),
    val Latitude: Double = 0.0,
    val Longitude: Double = 0.0
)