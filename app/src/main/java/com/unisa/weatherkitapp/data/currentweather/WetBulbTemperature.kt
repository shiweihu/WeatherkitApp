package com.unisa.weatherkitapp.data.currentweather

data class WetBulbTemperature(
    override val Imperial: Imperial = Imperial(),
    override val Metric: Metric = Metric()
):BaseParemeters