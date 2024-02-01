package com.unisa.weatherkitapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unisa.weatherkitapp.data.currentweather.CurrentWeatherResponseItem
import com.unisa.weatherkitapp.data.locationdata.LocationInfo
import com.unisa.weatherkitapp.repository.LocationRepository
import com.unisa.weatherkitapp.repository.Utils
import com.unisa.weatherkitapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationSearchViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository,
    private val utils: Utils
) : ViewModel() {

    //以前搜索过的文字
    val historicalSearchList = locationRepository.querySearchHistory()
        .stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = listOf())

    //用于保存搜索出来的location
    private val _locationList = mutableStateOf<List<LocationInfo>>(listOf())
    val locationList get() = _locationList

    val usedLocations = locationRepository.getLocations().stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = listOf()
    )

    fun requestCurrentWeatherWithoutDetail(locationInfo: LocationInfo,onError:(Exception)->Unit): MutableState<CurrentWeatherResponseItem?> {
        val weatherForLocation = mutableStateOf<CurrentWeatherResponseItem?>(null)
        viewModelScope.launch {
            val response =
                weatherRepository.requestCurrentWeatherWithoutDetail(locationId = locationInfo.Key)
            if (response.isNotEmpty()) {
                weatherForLocation.value = response[0]
            }
        }
        return weatherForLocation
    }

    fun getImage(type: Int): Int {
        return utils.imageMap[type] ?: 1
    }

    private var autoCompleteJob: Job? = null
    fun autoComplete(query: String): MutableState<List<LocationInfo>> {
        autoCompleteJob?.cancel()
        return mutableStateOf<List<LocationInfo>>(listOf()).also {
            autoCompleteJob = viewModelScope.launch {
                delay(1000) // Debounce time
                try {
                    val list = locationRepository.autoComplete(query)
                    it.value = list
                } catch (e: Exception) {
                    // Handle exceptions
                    e.printStackTrace()
                }
            }
        }
    }

    fun selectLocation(locationInfo: LocationInfo, callBack: () -> Unit) {
        viewModelScope.launch {
            locationRepository.insertLocation(locationInfo, 1)
            callBack()
        }
    }

    fun requestLocationDetails(locationKey: String,onError:(Exception)->Unit ,callBack: () -> Unit) {
        viewModelScope.launch {
            try {
                val locationInfo = locationRepository.requestLocationDetail(locationKey)
                selectLocation(locationInfo, callBack)
            }catch (e:Exception){
                onError(e)
            }
        }
    }

    fun removeLocation(key: String) {
        viewModelScope.launch {
            locationRepository.deleteLocation(key)
        }
    }

    fun requestLocations(text: String,onError:(Exception)->Unit) {
        viewModelScope.launch {
            try {
                locationRepository.insertSearchHistory(text)
                val locations = locationRepository.queryLocation(text)
                _locationList.value = locations
            }catch (e:Exception){
                onError(e)
            }
        }
    }

}