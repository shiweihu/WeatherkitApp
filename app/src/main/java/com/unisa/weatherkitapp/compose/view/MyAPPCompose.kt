package com.unisa.weatherkitapp.compose.view

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.unisa.weatherkitapp.R
import com.unisa.weatherkitapp.public.variable.LocalDevice
import com.unisa.weatherkitapp.public.variable.LocalLocationInfo
import com.unisa.weatherkitapp.public.variable.LocalUnitType
import com.unisa.weatherkitapp.public.variable.LocalsnackbarHostState
import com.unisa.weatherkitapp.viewmodel.MyAPPViewModel
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun MyAPPCompose(
    model: MyAPPViewModel = hiltViewModel()
) {

    val devicePoint = LocalDevice.current
    val isLocationEmpty by model.isLocationEmpty.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = isLocationEmpty) {
        if (isLocationEmpty) {
            model.queryLocationInfo(devicePoint)
        }
    }
    val navController = rememberNavController()
    val locationInfo by model.selectedLocation.collectAsStateWithLifecycle()
    val unitTyoe = LocalUnitType.current
    val context = LocalContext.current
    if (locationInfo != null) {
        LaunchedEffect(key1 = locationInfo!!.Key, key2 = unitTyoe) {
            model.requestWeatherInfo(locationInfo!!, matrics = unitTyoe)
        }
        val snackbarHostState = remember { SnackbarHostState() }
        CompositionLocalProvider(
            LocalLocationInfo provides locationInfo!!,
            LocalsnackbarHostState provides snackbarHostState
        ) {
            val configuration = LocalConfiguration.current
            val density = LocalDensity.current.density
            val screenWidthDP = configuration.screenWidthDp * density


            Scaffold(
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                }
            ) { paddingValue ->
                NavHost(
                    navController = navController,
                    startDestination = Route.MAIN.name,
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .padding(paddingValue),
                    enterTransition = {
                        slideIn(
                            animationSpec = tween(300, easing = FastOutSlowInEasing),
                            initialOffset = {
                                return@slideIn IntOffset(-screenWidthDP.toInt(), 0)
                            })
                    },
                    popEnterTransition = {
                        fadeIn(animationSpec = tween(300,easing = FastOutSlowInEasing))
                    },
                    popExitTransition = {

                        slideOut(animationSpec = tween(300,easing = FastOutLinearInEasing), targetOffset = {
                            return@slideOut IntOffset(-screenWidthDP.toInt(), 0)
                        })
                    },
                    exitTransition = {
                        fadeOut(animationSpec = tween(300,easing = FastOutLinearInEasing))
                    }
                ) {
                    composable(Route.MAIN.name) {
                        val weatherPack by remember { model.weatherInfoPackage }

                        LaunchedEffect(key1 = weatherPack) {
                            if (weatherPack != null) {
                                val zoomTimeZone: TimeZone =
                                    TimeZone.getTimeZone(locationInfo!!.TimeZone.Name)
                                model.createNoticeWorker(context, locationInfo!!)
                                while (true) {
                                    val lastUpdateTime = Calendar.getInstance(zoomTimeZone).also {
                                        it.timeInMillis =
                                            weatherPack!!.hourlyForecastResponse.map { item ->
                                                return@map item.EpochDateTime
                                            }.minOrNull()?.toLong()?.times(1000) ?: 0
                                    }
                                    delay(60 * 1000 * 10) // 10分钟检查一次
                                    val currentTime = Calendar.getInstance(zoomTimeZone)
                                    val timeDifferenceInMillis =
                                        currentTime.timeInMillis - lastUpdateTime.timeInMillis
                                    val timeDifferenceInHours =
                                        timeDifferenceInMillis.toDouble() / (1000.0 * 60.0 * 60.0) // 将毫秒转换为小时
                                    if (timeDifferenceInHours >= 0) {
                                        model.rerequestWeatherInfo(
                                            locationInfo!!,
                                            matrics = unitTyoe
                                        )
                                        return@LaunchedEffect
                                    }
                                }
                            }
                        }

                        MainCompose(
                            weatherInfoPackage = weatherPack,
                            navigateFuc = {
                                if (navController.currentDestination?.route == Route.MAIN.name) {
                                    navController.navigate(it) {
                                        // Pop up to the start destination of the graph to
                                        // avoid building up a large stack of destinations
                                        // on the back stack as users select items
                                        //popUpTo(navController.graph.findStartDestination().id) {
                                        //    saveState = true
                                        //   inclusive = true
                                        //}
                                        // Avoid multiple copies of the same destination when
                                        // reselecting the same item
                                        //launchSingleTop = false
                                        // Restore state when reselecting a previously selected item
                                        //restoreState = true
                                    }
                                }
                            })

                    }
                    composable(Route.LOCATION.name) {
                        LocationSearchCompose(navigateFuc = {
                            if (it == Route.MAIN.name && navController.currentDestination?.route == Route.LOCATION.name) {
                                navController.popBackStack()
                                navController.navigateUp()
                            }
                        })
                    }
                    composable(Route.SETTING.name) {
                        SettingCompose() {
                            if (it == Route.MAIN.name && navController.currentDestination?.route == Route.SETTING.name) {
                                navController.popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class Route() {
    MAIN,
    LOCATION,
    SETTING,
}