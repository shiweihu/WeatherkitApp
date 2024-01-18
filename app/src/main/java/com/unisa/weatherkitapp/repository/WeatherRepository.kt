package com.unisa.weatherkitapp.repository

import com.unisa.weatherkitapp.data.WeatherInfoPackage
import com.unisa.weatherkitapp.data.currentweather.CurrentWeatherResponse
import com.unisa.weatherkitapp.data.forecast.ForecastResponse
import com.unisa.weatherkitapp.data.hourlyforecast.HourlyForecastResponse
import com.unisa.weatherkitapp.data.locationdata.LocationInfo
import com.unisa.weatherkitapp.net.CurrentWeatherProxy
import com.unisa.weatherkitapp.net.ForecastsProxy
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WeatherRepository @Inject constructor(
    private val currentWeatherProxy: CurrentWeatherProxy,
    private val forecastsProxy: ForecastsProxy
) {

    suspend fun requestHourlyWeather(locationId: String,metric:Boolean): HourlyForecastResponse {
        return forecastsProxy.requestHourlyForecasts(cityid = locationId,metric = metric)
    }

    suspend fun requestCurrentWeather(locationId:String):CurrentWeatherResponse{
        return currentWeatherProxy.requestCurrentWeatherById(cityid = locationId)
    }
    suspend fun requestCurrentWeatherWithoutDetail(locationId:String):CurrentWeatherResponse{
        return currentWeatherProxy.requestCurrentWeatherByIdWithoutDetail(cityid = locationId)
    }

    suspend fun requestForecastWeather(locationId:String,metric:Boolean):ForecastResponse{
        return forecastsProxy.requestFiveDayForecasts(cityid = locationId,metric = metric)
    }

    suspend fun requestWeatherInfo(locationInfo: LocationInfo,matrics:Boolean): WeatherInfoPackage {
        val currentWeatherResponse = requestCurrentWeather(locationInfo.Key)
        val hourlyWeatherResponse = requestHourlyWeather(locationInfo.Key,metric = matrics)
        val forecastWeatherResponse = requestForecastWeather(locationInfo.Key,metric = matrics)
        return WeatherInfoPackage(currentWeatherResponse,forecastWeatherResponse,hourlyWeatherResponse,locationInfo)
    }

}