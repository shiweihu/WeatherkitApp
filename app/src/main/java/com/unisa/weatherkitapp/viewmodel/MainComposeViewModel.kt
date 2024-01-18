package com.unisa.weatherkitapp.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unisa.weatherkitapp.data.DevicePoint
import com.unisa.weatherkitapp.data.WeatherInfoPackage
import com.unisa.weatherkitapp.data.currentweather.BaseParemeters
import com.unisa.weatherkitapp.data.currentweather.BaseUnit
import com.unisa.weatherkitapp.data.currentweather.CurrentWeatherResponse
import com.unisa.weatherkitapp.data.forecast.ForecastResponse
import com.unisa.weatherkitapp.data.hourlyforecast.HourlyForecastResponse
import com.unisa.weatherkitapp.data.indices.IndicesResponse
import com.unisa.weatherkitapp.data.indices.IndicesResponseItem
import com.unisa.weatherkitapp.data.locationdata.LocationInfo
import com.unisa.weatherkitapp.dataStore
import com.unisa.weatherkitapp.repository.IndicesRepository
import com.unisa.weatherkitapp.repository.LocationRepository
import com.unisa.weatherkitapp.repository.Utils
import com.unisa.weatherkitapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainComposeViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository,
    private val utils: Utils,
    private val indicesRepository: IndicesRepository
):ViewModel() {



    fun getAirQualityLabel(enStr:String,context: Context):String{
        val strId = utils.airLabel[enStr]
        return if(strId != null){
            context.getString(strId)
        }else{
            enStr
        }
    }


    fun requestIndicesByGroupId(locationID:String,groupid:Int,errorNotice:()->Unit):State<List<IndicesResponseItem>> {
        val stateList = mutableStateOf<List<IndicesResponseItem>>(listOf())
        viewModelScope.launch {
            try {
                val list  = indicesRepository.requestIndices(locationID = locationID, groupid = groupid)
                stateList.value = list
            }catch (e:Exception){
                e.printStackTrace()
                errorNotice()
            }
        }
        return stateList
    }




    fun navigateToWeb(url:String,context: Context){
        utils.navigateToWeb(url, context)
    }

    fun getUnit(unitType:Boolean,paremeters: BaseParemeters): BaseUnit {
        return utils.getUnit(unitType,paremeters)
    }

    fun getImage(type:Int):Int{
        return utils.imageMap[type] ?: 1
    }


}