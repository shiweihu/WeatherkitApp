package com.unisa.weatherkitapp.net

import android.util.Log
import com.unisa.weatherkitapp.MyApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    private val myHttpProxy: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder().also {
            //it.cache(file_cache?.let { it1 -> Cache(it1, 86400L) })
            it.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            it.cache(MyApplication.cache)
            it.addInterceptor { chain ->
                val origin  = chain.request()
                val response = chain.proceed(origin)
                return@addInterceptor response
            }
            it.connectTimeout(30L, TimeUnit.SECONDS)
            it.readTimeout(20L, TimeUnit.SECONDS)
        }.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideLocationProxy():LocationProxy{
        return myHttpProxy.create(LocationProxy::class.java)
    }

    @Singleton
    @Provides
    fun provideCurrentWeatherProxy():CurrentWeatherProxy{
        return myHttpProxy.create(CurrentWeatherProxy::class.java)
    }

    @Singleton
    @Provides
    fun provideForecastsProxy():ForecastsProxy{
        return myHttpProxy.create(ForecastsProxy::class.java)
    }

    @Singleton
    @Provides
    fun provideIndicesProxy():IndicesProxy{
        return myHttpProxy.create(IndicesProxy::class.java)
    }

    @Singleton
    @Provides
    fun provideAlarmsProxy():AlarmsProxy{
        return myHttpProxy.create(AlarmsProxy::class.java)
    }




    companion object{
        const val BASE_URL = "https://dataservice.accuweather.com/"
        const val API_KEY = "AaDATGzCsKX5d5oGsGajq2Ymm28q3kyU"
    }
}