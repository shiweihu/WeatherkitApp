package com.unisa.weatherkitapp.data.currentweather

data class Past9Hours(
    override val Imperial: Imperial = Imperial(),
    override val Metric: Metric = Metric()
):BaseParemeters