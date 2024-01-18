package com.unisa.weatherkitapp.data.currentweather

data class Precip1hr(
    override val Imperial: Imperial = Imperial(),
    override val Metric: Metric = Metric()
):BaseParemeters