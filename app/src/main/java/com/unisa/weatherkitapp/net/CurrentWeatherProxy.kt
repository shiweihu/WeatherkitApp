package com.unisa.weatherkitapp.net

import com.unisa.weatherkitapp.MyApplication
import com.unisa.weatherkitapp.data.currentweather.CurrentWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Locale

interface CurrentWeatherProxy {

    @GET("currentconditions/v1/{cityid}")
    suspend fun requestCurrentWeatherById(
        @Path("cityid") cityid:String,
        @Query("apikey") apikey:String = NetworkModule.API_KEY,
        @Query("language") language:String = MyApplication.LOCAL_LANGUAGE_CODE,
        @Query("details") details:Boolean = true
    ):CurrentWeatherResponse


    @Headers("Cache-Control: max-age=3600")
    @GET("currentconditions/v1/{cityid}")
    suspend fun requestCurrentWeatherByIdWithoutDetail(
        @Path("cityid") cityid:String,
        @Query("apikey") apikey:String = NetworkModule.API_KEY,
        @Query("language") language:String = MyApplication.LOCAL_LANGUAGE_CODE,
        @Query("details") details:Boolean = false
    ):CurrentWeatherResponse

}