package com.unisa.weatherkitapp.data.currentweather

data class Past24HourRange(
    val Maximum: Maximum = Maximum(),
    val Minimum: Minimum = Minimum()
)