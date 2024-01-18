package com.unisa.weatherkitapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unisa.weatherkitapp.R
import com.unisa.weatherkitapp.repository.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class IndicesViewModel @Inject constructor(
    private val utils: Utils
):ViewModel() {

    fun getIndicesMap() = utils.indexMap

    fun getIndicesIcons(id:Int):Int{
        return utils.indexIcons[id] ?: R.drawable.common_icon
    }




}