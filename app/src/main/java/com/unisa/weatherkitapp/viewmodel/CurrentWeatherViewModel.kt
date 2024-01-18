package com.unisa.weatherkitapp.viewmodel

import androidx.lifecycle.ViewModel
import com.unisa.weatherkitapp.data.currentweather.BaseParemeters
import com.unisa.weatherkitapp.data.currentweather.BaseUnit
import com.unisa.weatherkitapp.repository.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val utils: Utils
) :ViewModel() {
    fun getImage(type:Int):Int{
        return utils.imageMap[type] ?: 1
    }
    fun getUnit(unitType:Boolean,paremeters:BaseParemeters):BaseUnit{
        return utils.getUnit(unitType,paremeters)
    }
}