package com.unisa.weatherkitapp.data.forecast

data class WetBulbTemperature(
    val Average: Average = Average(),
    val Maximum: Maximum = Maximum(),
    val Minimum: Minimum = Minimum()
)