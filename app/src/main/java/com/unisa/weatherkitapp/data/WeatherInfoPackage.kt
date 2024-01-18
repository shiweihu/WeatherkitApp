package com.unisa.weatherkitapp.data

import com.unisa.weatherkitapp.data.currentweather.CurrentWeatherResponse
import com.unisa.weatherkitapp.data.forecast.ForecastResponse
import com.unisa.weatherkitapp.data.hourlyforecast.HourlyForecastResponse
import com.unisa.weatherkitapp.data.locationdata.LocationInfo

data class WeatherInfoPackage(
    val currentWeatherResponse: CurrentWeatherResponse,
    val forecastResponse: ForecastResponse,
    val hourlyForecastResponse: HourlyForecastResponse,
    val locationInfo: LocationInfo
)
