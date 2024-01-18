package com.unisa.weatherkitapp.net

import com.unisa.weatherkitapp.MyApplication
import com.unisa.weatherkitapp.data.locationdata.LocationInfo
import com.unisa.weatherkitapp.data.locationdata.LocationResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Locale

interface LocationProxy {

    @Headers("Cache-Control: max-age=86400")
    @GET("locations/v1/search")
    suspend fun queryLocations(
        @Query("apikey") api:String = NetworkModule.API_KEY,
        @Query("q")text:String,
        @Query("language")language:String = MyApplication.LOCAL_LANGUAGE_CODE,
        @Query("details")details:Boolean = true,
        @Query("offset")offset:Int =0,
        @Query("alias")alias:String = "Never"
    ):LocationResponse

    @GET("locations/v1/cities/geoposition/search")
    suspend fun queryLocationsByGeoposition(
        @Query("apikey") api:String = NetworkModule.API_KEY,
        @Query("q")text:String,
        @Query("language")language:String = MyApplication.LOCAL_LANGUAGE_CODE,
        @Query("details")details:Boolean = true,
        @Query("toplevel")toplevel:Boolean = false,
    ):LocationInfo

    //这个协议不会返回完整的LocationInfo.
    @GET("locations/v1/cities/autocomplete")
    suspend fun autoComplete(
        @Query("apikey") api:String = NetworkModule.API_KEY,
        @Query("q")text:String,
        @Query("language")language:String = MyApplication.LOCAL_LANGUAGE_CODE
    ):LocationResponse


    @GET("locations/v1/{locationId}")
    suspend fun requestLocationInfoDetail(
        @Path("locationId") locationId:String,
        @Query("apikey") api:String = NetworkModule.API_KEY,
        @Query("language")language:String = MyApplication.LOCAL_LANGUAGE_CODE,
        @Query("details")details:Boolean = true,
    ):LocationInfo


}