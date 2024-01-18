package com.unisa.weatherkitapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unisa.weatherkitapp.repository.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val utils: Utils
):ViewModel() {

    fun getUnitType(context: Context): Flow<Boolean> {
        return utils.getUnitType(context).stateIn(viewModelScope, started = SharingStarted.Eagerly,true)
    }
    fun setUnitType(context: Context,unitType:Boolean){
        viewModelScope.launch {
            utils.setUnitType(context,unitType)
        }
    }

    fun getLanguageList(code:String):List<String>{
        return Utils.languageCodeToLocalization[code] ?: listOf()
    }

    fun getIndicesMap() = utils.indexMap





}