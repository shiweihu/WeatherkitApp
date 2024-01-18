package com.unisa.weatherkitapp.data.forecast

data class ForecastResponse(
    val DailyForecasts: List<DailyForecast> = listOf(),
    val Headline: Headline = Headline()
)