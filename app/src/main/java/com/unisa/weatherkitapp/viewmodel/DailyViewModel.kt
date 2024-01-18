package com.unisa.weatherkitapp.viewmodel

import androidx.lifecycle.ViewModel
import com.unisa.weatherkitapp.repository.Utils
import com.unisa.weatherkitapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DailyViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val utils: Utils
) :ViewModel(){

    fun getImage(type:Int):Int{
        return utils.imageMap[type] ?: 1
    }

}