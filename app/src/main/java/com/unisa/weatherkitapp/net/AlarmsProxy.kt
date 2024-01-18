package com.unisa.weatherkitapp.net

import com.unisa.weatherkitapp.MyApplication
import com.unisa.weatherkitapp.data.alarms.AlarmsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Locale

interface AlarmsProxy {
    @GET("alarms/v1/1day/{locationid}")
    suspend fun getTodayAlarms(
        @Path("locationid")locationid:String,
        @Query("language") language:String = MyApplication.LOCAL_LANGUAGE_CODE,
        @Query("apikey") apikey:String = NetworkModule.API_KEY
    ):AlarmsResponse
}