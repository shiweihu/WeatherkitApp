package com.unisa.weatherkitapp.data.hourlyforecast

data class Wind(
    val Direction: Direction = Direction(),
    val Speed: Speed = Speed()
)