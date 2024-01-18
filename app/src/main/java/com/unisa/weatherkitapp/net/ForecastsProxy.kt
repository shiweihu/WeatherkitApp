package com.unisa.weatherkitapp.net

import com.unisa.weatherkitapp.MyApplication
import com.unisa.weatherkitapp.data.forecast.ForecastResponse
import com.unisa.weatherkitapp.data.hourlyforecast.HourlyForecastResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Locale

interface ForecastsProxy {

    @Headers("Cache-Control: max-age=3600")
    @GET("forecasts/v1/daily/5day/{cityid}")
    suspend fun requestFiveDayForecasts(
        @Path("cityid")cityid:String,
        @Query("language")language:String = MyApplication.LOCAL_LANGUAGE_CODE,
        @Query("details")details:Boolean = true,
        @Query("metric")metric:Boolean = true,
        @Query("apikey") apikey:String = NetworkModule.API_KEY
    ):ForecastResponse


    @GET("forecasts/v1/hourly/12hour/{cityid}")
    suspend fun requestHourlyForecasts(
        @Path("cityid")cityid:String,
        @Query("language")language:String = MyApplication.LOCAL_LANGUAGE_CODE,
        @Query("details")details:Boolean = true,
        @Query("metric")metric:Boolean = true,
        @Query("apikey") apikey:String = NetworkModule.API_KEY
    ):HourlyForecastResponse




}