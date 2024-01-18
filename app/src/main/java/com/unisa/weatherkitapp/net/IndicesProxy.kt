package com.unisa.weatherkitapp.net

import com.unisa.weatherkitapp.MyApplication
import com.unisa.weatherkitapp.data.indices.IndicesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Locale

interface IndicesProxy {
    @Headers("Cache-Control: max-age=86400")
    @GET("indices/v1/daily/5day/{locationID}/groups/{groupID}")
    suspend fun requestIndicesByGroup(
        @Path("locationID")locationID:String,
        @Path("groupID")groupID:Int,
        @Query("language")language:String = MyApplication.LOCAL_LANGUAGE_CODE,
        @Query("details")details:Boolean = true,
        @Query("apikey") apikey:String = NetworkModule.API_KEY
    ):IndicesResponse
}