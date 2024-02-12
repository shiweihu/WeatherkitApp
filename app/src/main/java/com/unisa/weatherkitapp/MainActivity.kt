package com.unisa.weatherkitapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.unisa.weatherkitapp.compose.view.MyAPPCompose
import com.unisa.weatherkitapp.data.DevicePoint
import com.unisa.weatherkitapp.public.variable.LocalDevice
import com.unisa.weatherkitapp.public.variable.LocalUnitType
import com.unisa.weatherkitapp.public.variable.LocalsnackbarHostState
import com.unisa.weatherkitapp.repository.Utils
import com.unisa.weatherkitapp.ui.theme.WeatherkitAppTheme
import com.unisa.weatherkitapp.viewmodel.MainActivityUiState
import com.unisa.weatherkitapp.viewmodel.MainActivityViewModel
import com.unisa.weatherkitapp.worker.AlarmsWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)
    private val defultPoint = DevicePoint(-74.1448313, 40.6976307)
    private val model: MainActivityViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // 权限被授予
                getLocation()
            } else {
                uiState = MainActivityUiState.Success(defultPoint)
            }
        }


    @SuppressLint("MissingPermission")
    fun getLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                uiState = MainActivityUiState.Success(
                    DevicePoint(
                        latitude = it.result.latitude,
                        longitude = it.result.longitude
                    )
                )
            } else {
                val locationRequest =
                    LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000).build()
                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        if (locationResult.locations.isNotEmpty()) {
                            val location = locationResult.locations[0]
                            uiState = MainActivityUiState.Success(
                                DevicePoint(
                                    latitude = location.latitude,
                                    longitude = location.longitude
                                )
                            )
                        } else {
                            uiState = MainActivityUiState.Success(defultPoint)
                        }
                    }
                }
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setLanguageCode()

        model.topCitiesLanguage.observe(this){
            if(it == null || it != MyApplication.LOCAL_LANGUAGE_CODE){
                model.queryTopCities()
            }
        }
    }

    private fun setLanguageCode() {
        val languageCodeToLocalization = Utils.languageCodeToLocalization
        val languageCode = Locale.getDefault().language
        val countryCode = Locale.getDefault().country.lowercase(Locale.getDefault())
        val list = languageCodeToLocalization[languageCode]
        if (!list.isNullOrEmpty()) {
            MyApplication.LOCAL_LANGUAGE_CODE = list.find {
                it.contains(countryCode)
            } ?: languageCode
        }
        MyApplication.COUNTRY_CODE = Locale.getDefault().country
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)


        MobileAds.initialize(this) {}

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            getLocation()
        }

        model.checkOpenCount(this)

        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                MainActivityUiState.Loading -> true
                is MainActivityUiState.Success -> false
            }
        }
        setContent {
            WeatherkitAppTheme {
                // A surface container using the 'background' color from the theme
                if (uiState is MainActivityUiState.Success) {
                    val unitType by model.getUnitType(LocalContext.current)
                        .collectAsStateWithLifecycle(
                            initialValue = true
                        )
                    CompositionLocalProvider(
                        LocalDevice provides (uiState as MainActivityUiState.Success).devicePoint,
                        LocalUnitType provides unitType
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            val isOnline by model.isOnlineFlow(this)
                                .collectAsStateWithLifecycle(true)
                            if (isOnline) {
                                MyAPPCompose()
                            } else {
                                NetworkDisconnectionCompose()
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun NetworkDisconnectionCompose() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize(1f)
            .background(MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
            Text(
                text = stringResource(id = R.string.network_state),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}


