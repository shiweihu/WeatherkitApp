package com.unisa.weatherkitapp.viewmodel

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.unisa.weatherkitapp.data.DevicePoint
import com.unisa.weatherkitapp.data.WeatherInfoPackage
import com.unisa.weatherkitapp.data.currentweather.CurrentWeatherResponse
import com.unisa.weatherkitapp.data.forecast.ForecastResponse
import com.unisa.weatherkitapp.data.hourlyforecast.HourlyForecastResponse
import com.unisa.weatherkitapp.data.locationdata.LocationInfo
import com.unisa.weatherkitapp.repository.LocationRepository
import com.unisa.weatherkitapp.repository.WeatherRepository
import com.unisa.weatherkitapp.worker.AlarmsWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MyAPPViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
):ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val isLocationEmpty: StateFlow<Boolean> = locationRepository.countLocations().flatMapLatest {
        return@flatMapLatest flowOf(it<=0)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedLocation: StateFlow<LocationInfo?> = locationRepository.getSelectedLocation().flatMapLatest {
        return@flatMapLatest flowOf(it?.locationInfo)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )


    private val _weatherInfoPackage: MutableState<WeatherInfoPackage?> = mutableStateOf(null)
    val weatherInfoPackage: State<WeatherInfoPackage?> get() = _weatherInfoPackage
    fun requestWeatherInfo(locationInfo: LocationInfo,matrics:Boolean,onError:(Exception)->Unit){
        _weatherInfoPackage.value = null
        rerequestWeatherInfo(locationInfo,matrics,onError)
    }
    fun rerequestWeatherInfo(locationInfo: LocationInfo,matrics:Boolean,onError:(Exception)->Unit){
        viewModelScope.launch {
            try {
                val currentWeatherResponseJob = viewModelScope.async {
                    weatherRepository.requestCurrentWeather(locationInfo.Key)
                }
                val hourlyWeatherResponseJob = viewModelScope.async {
                    weatherRepository.requestHourlyWeather(locationInfo.Key,metric = matrics)
                }
                val forecastWeatherResponseJob = viewModelScope.async {
                    weatherRepository.requestForecastWeather(locationInfo.Key,metric = matrics)
                }
                val list = awaitAll(currentWeatherResponseJob,forecastWeatherResponseJob,hourlyWeatherResponseJob)
                _weatherInfoPackage.value = WeatherInfoPackage(list[0] as CurrentWeatherResponse,list[1] as ForecastResponse,list[2] as HourlyForecastResponse,locationInfo)

            }catch (e:Exception){
                onError(e)
            }
        }
    }

    fun createNoticeWorker(context: Context,locationInfo: LocationInfo){
        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // 创建每天早上8点触发的周期性任务
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone(locationInfo.TimeZone.Name)
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        var initialDelay: Long = calendar.timeInMillis - System.currentTimeMillis()
        if (initialDelay < 0) {
            // 如果当前时间已过8点，则将初始延迟设为明天的8点
            initialDelay += TimeUnit.DAYS.toMillis(1)
        }
        val workTag = "excuse_8_am"
        val workRequest: PeriodicWorkRequest = PeriodicWorkRequest.Builder(
            AlarmsWorker::class.java, 1, TimeUnit.DAYS
        ).setInitialDelay(1000, TimeUnit.MILLISECONDS).setConstraints(constraints).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(workTag,
            ExistingPeriodicWorkPolicy.UPDATE,workRequest)
    }



    fun queryLocationInfo(point: DevicePoint,onError: (Exception) -> Unit){
        viewModelScope.launch {
            try {
                val response = locationRepository.queryLocationByPoint(point)
                locationRepository.insertLocation(response,1)
            }catch (e:Exception){
                onError(e)
            }
        }
    }

}