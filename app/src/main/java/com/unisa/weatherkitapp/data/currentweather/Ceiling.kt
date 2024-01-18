package com.unisa.weatherkitapp.data.currentweather

data class Ceiling(
    override val Imperial: Imperial = Imperial(),
    override val Metric: Metric = Metric()
):BaseParemeters