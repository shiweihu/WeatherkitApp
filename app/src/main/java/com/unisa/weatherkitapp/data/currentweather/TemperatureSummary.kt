package com.unisa.weatherkitapp.data.currentweather

data class TemperatureSummary(
    val Past12HourRange: Past12HourRange = Past12HourRange(),
    val Past24HourRange: Past24HourRange = Past24HourRange(),
    val Past6HourRange: Past6HourRange = Past6HourRange()
)