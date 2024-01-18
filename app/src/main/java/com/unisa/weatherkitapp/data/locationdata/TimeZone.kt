package com.unisa.weatherkitapp.data.locationdata

data class TimeZone(
    val Code: String = "",
    val GmtOffset: Double = 0.0,
    val IsDaylightSaving: Boolean = false,
    val Name: String = "",
    val NextOffsetChange: String = ""
)