package com.unisa.weatherkitapp.viewmodel

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unisa.weatherkitapp.data.DevicePoint
import com.unisa.weatherkitapp.dataStore
import com.unisa.weatherkitapp.repository.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val utils: Utils
) : ViewModel() {
//    val uiState: StateFlow<MainActivityUiState> = userDataRepository.userData.map {
//        Success(it)
//    }.stateIn(
//        scope = viewModelScope,
//        initialValue = Loading,
//        started = SharingStarted.WhileSubscribed(5_000),
//    )

    fun getUnitType(context: Context): Flow<Boolean> {
        return utils.getUnitType(context).stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(),true)
    }



}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val devicePoint: DevicePoint) : MainActivityUiState
}