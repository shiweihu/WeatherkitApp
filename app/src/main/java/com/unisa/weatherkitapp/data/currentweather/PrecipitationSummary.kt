package com.unisa.weatherkitapp.data.currentweather

data class PrecipitationSummary(
    val Past12Hours: Past12Hours = Past12Hours(),
    val Past18Hours: Past18Hours = Past18Hours(),
    val Past24Hours: Past24Hours = Past24Hours(),
    val Past3Hours: Past3Hours = Past3Hours(),
    val Past6Hours: Past6Hours = Past6Hours(),
    val Past9Hours: Past9Hours = Past9Hours(),
    val PastHour: PastHour = PastHour(),
    val Precipitation: Precipitation = Precipitation()
)