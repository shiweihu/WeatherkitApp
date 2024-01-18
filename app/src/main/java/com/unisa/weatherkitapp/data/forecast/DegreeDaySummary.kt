package com.unisa.weatherkitapp.data.forecast

data class DegreeDaySummary(
    val Cooling: Cooling = Cooling(),
    val Heating: Heating = Heating()
)