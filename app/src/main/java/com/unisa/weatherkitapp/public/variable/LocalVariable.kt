package com.unisa.weatherkitapp.public.variable

import android.os.Build
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.unisa.weatherkitapp.BuildConfig
import com.unisa.weatherkitapp.data.DevicePoint
import com.unisa.weatherkitapp.data.locationdata.LocationInfo


val LocalDevice = staticCompositionLocalOf<DevicePoint> {
   DevicePoint(0.0,0.0)
}

val LocalLocationInfo = staticCompositionLocalOf<LocationInfo> {
   LocationInfo()
}

val LocalUnitType = compositionLocalOf {
   true
}

val LocalsnackbarHostState = staticCompositionLocalOf<SnackbarHostState?>{
   null
}

val MAINCOMPOSE_ADID = BuildConfig.maincompose_banner
val SEARCH_ADID = BuildConfig.search_banner
