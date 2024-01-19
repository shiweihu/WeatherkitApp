package com.unisa.weatherkitapp.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.impl.utils.getActiveNetworkCompat
import com.unisa.weatherkitapp.data.DevicePoint
import com.unisa.weatherkitapp.dataStore
import com.unisa.weatherkitapp.repository.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
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

    fun isOnlineFlow(context: Context) = callbackFlow {
        val connectivityManager = context.getSystemService<ConnectivityManager>()
        if (connectivityManager == null) {
            channel.trySend(false)
            channel.close()
            return@callbackFlow
        }

        /**
         * The callback's methods are invoked on changes to *any* network matching the [NetworkRequest],
         * not just the active network. So we can simply track the presence (or absence) of such [Network].
         */
        val callback = object : ConnectivityManager.NetworkCallback() {

            private val networks = mutableSetOf<Network>()

            override fun onAvailable(network: Network) {
                networks += network
                channel.trySend(true)
            }

            override fun onLost(network: Network) {
                networks -= network
                channel.trySend(networks.isNotEmpty())
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, callback)

        /**
         * Sends the latest connectivity status to the underlying channel.
         */

        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        channel.trySend(networkCapabilities != null &&
                (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)))

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.conflate()

    fun getUnitType(context: Context): Flow<Boolean> {
        return utils.getUnitType(context).stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(),true)
    }



}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val devicePoint: DevicePoint) : MainActivityUiState
}