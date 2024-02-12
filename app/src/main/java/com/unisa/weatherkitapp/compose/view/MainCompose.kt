package com.unisa.weatherkitapp.compose.view

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.ads.AdSize
import com.google.gson.Gson
import com.unisa.weatherkitapp.R
import com.unisa.weatherkitapp.data.WeatherInfoPackage
import com.unisa.weatherkitapp.data.currentweather.BaseUnit
import com.unisa.weatherkitapp.data.currentweather.CurrentWeatherResponseItem
import com.unisa.weatherkitapp.data.forecast.DailyForecast
import com.unisa.weatherkitapp.data.hourlyforecast.HourlyForecastResponseItem
import com.unisa.weatherkitapp.data.indices.IndicesResponseItem
import com.unisa.weatherkitapp.public.variable.LocalLocationInfo
import com.unisa.weatherkitapp.public.variable.LocalUnitType
import com.unisa.weatherkitapp.public.variable.LocalsnackbarHostState
import com.unisa.weatherkitapp.public.variable.MAINCOMPOSE_ADID
import com.unisa.weatherkitapp.viewmodel.MainComposeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainCompose(
    weatherInfoPackage: WeatherInfoPackage?,
    modifier: Modifier = Modifier,
    model: MainComposeViewModel = hiltViewModel(),
    navigateFuc: (route: String) -> Unit
) {
    val locationInfo = LocalLocationInfo.current
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomSheetType by remember{ mutableStateOf(BottomSheetType.CURRENT) }
    var bottomSheetData: Any by remember { mutableStateOf(Any()) }
    val context = LocalContext.current
    val snackbarHostState = LocalsnackbarHostState.current


    if (weatherInfoPackage != null) {
        val currentWeatherResponseItem = weatherInfoPackage.currentWeatherResponse[0]
        Scaffold(
            modifier = modifier.fillMaxSize(1f),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                    navigationIcon = {
                        IconButton(onClick = { navigateFuc(Route.LOCATION.name) }) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = stringResource(
                                    id = R.string.search,
                                ),
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    },
                    actions = {
                        Row {
                            IconButton(onClick = { navigateFuc(Route.SETTING.name) }) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "setting"
                                )
                            }
                            IconButton(onClick = {
                                model.navigateToWeb(
                                    currentWeatherResponseItem.MobileLink,
                                    context
                                )
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.website_icon),
                                    contentDescription = "setting"
                                )
                            }
                        }
                    },
                    title = {
                        Text(
                            text = locationInfo.LocalizedName.ifEmpty { locationInfo.EnglishName },
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth(1f)
                    .verticalScroll(state = rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                CurrentWeatherView(
                    currentWeatherResponseItem = currentWeatherResponseItem,
                    modifier = Modifier.fillMaxWidth(1f),
                    showBottomSheet = {
                        showBottomSheet = true
                        bottomSheetType = BottomSheetType.CURRENT
                        bottomSheetData = it
                    })
                HourlyCompose(hourlyForecastResponse = weatherInfoPackage!!.hourlyForecastResponse) {
                    showBottomSheet = true
                    bottomSheetType = BottomSheetType.HOURSLY
                    bottomSheetData = it
                }
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(10.dp, 0.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = weatherInfoPackage.forecastResponse.Headline.Text,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                DailyCompose(forecastResponse = weatherInfoPackage.forecastResponse) {
                    showBottomSheet = true
                    bottomSheetType = BottomSheetType.DAILY
                    bottomSheetData = it
                }
                Spacer(modifier = Modifier.height(10.dp))
                AdvertiseViewCompose(adsize = AdSize.LARGE_BANNER, adid = MAINCOMPOSE_ADID, keyWords = listOf(locationInfo.EnglishName,locationInfo.Country.EnglishName))
                Spacer(modifier = Modifier.height(10.dp))
                IndexCompose() {
                    showBottomSheet = true
                    bottomSheetType = BottomSheetType.INDICES
                    bottomSheetData = it
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState
                ) {
                    when (bottomSheetType) {
                        BottomSheetType.CURRENT -> {
                            if(bottomSheetData is CurrentWeatherResponseItem){
                                CurrentBottomSheet(currentWeatherResponseItem = bottomSheetData as CurrentWeatherResponseItem)
                            }else{
                                showBottomSheet = false
                            }
                        }

                        BottomSheetType.HOURSLY -> {
                            val item = bottomSheetData as HourlyForecastResponseItem
                            HourslyBottomSheet(
                                weatherIcon = model.getImage(item.WeatherIcon),
                                item = item
                            )
                        }

                        BottomSheetType.DAILY -> {
                            val item = bottomSheetData as DailyForecast
                            DailyBottomSheet(
                                dailyForecast = item,
                                model.getImage(item.Day.Icon),
                                model.getImage(item.Night.Icon)
                            )
                        }
                        BottomSheetType.INDICES -> {
                            val errorMessage = stringResource(id = R.string.network_error)
                            IndicesBottomSheet(groupid = bottomSheetData as Int){
                                scope.launch {
                                    sheetState.hide()
                                }.invokeOnCompletion {
                                    if(!sheetState.isVisible){
                                        showBottomSheet = false
                                    }
                                }
                                scope.launch {
                                    snackbarHostState.showSnackbar(message = errorMessage, duration = SnackbarDuration.Short)
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        var count by remember { mutableIntStateOf(0) }
        LaunchedEffect(key1 = true) {
            while (true) {
                delay(500)
                count = count % 2 + 1
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(60.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                Text(
                    text = "${stringResource(id = R.string.loading)}${if (count == 1) ".." else if (count == 2) "..." else "."}",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun IndicesBottomSheet(
    groupid: Int,
    model: MainComposeViewModel = hiltViewModel(),
    closeBottomSheet:()->Unit
) {

    val locationInfo = LocalLocationInfo.current
    val state = rememberLazyListState()
    val indicesList by remember(groupid) {
        model.requestIndicesByGroupId(
            locationID = locationInfo.Key,
            groupid = groupid
        ){
            closeBottomSheet()
        }
    }
    if(indicesList.isEmpty()){
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(500.dp),
            contentAlignment = Alignment.TopCenter
        ){
            CircularProgressIndicator(modifier = Modifier.size(50.dp))
        }
    }else{
        val daysIndicesMap = indicesList.groupBy {
            it.EpochDateTime
        }
        LazyColumn(
            state =state,
            modifier = Modifier.fillMaxHeight(0.9f)
        ){
            daysIndicesMap.forEach { (key, value) ->
                stickyHeader {
                    IndicesListHeader(key)
                }

                items(value) { item ->
                    IndicesListitems(item)
                }
            }
        }
    }
}

@Composable
fun IndicesListHeader(
    epochDateTime:Int
){
    val locationInfo = LocalLocationInfo.current
    val timeZone = TimeZone.getTimeZone(locationInfo.TimeZone.Name)
    val calendar: Calendar = Calendar.getInstance(timeZone)
    calendar.timeInMillis = epochDateTime * 1000L

    val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).also {
        it.timeZone = timeZone
    }.format(calendar.time)
    Box(
        modifier = Modifier
            .fillMaxWidth(1f)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ){
        Text( modifier = Modifier.padding(10.dp,0.dp), text = dayOfWeek, style = MaterialTheme.typography.displaySmall)
    }
}

@Composable
fun IndicesListitems(
    item: IndicesResponseItem,
    model:MainComposeViewModel = hiltViewModel()
){
    val context = LocalContext.current
    ListItem(
        headlineContent = { Text(text = item.Name) },
        supportingContent = { Text(text = item.Text ?: "", overflow = TextOverflow.Ellipsis)},
        trailingContent = {
             Row(
                 verticalAlignment = Alignment.CenterVertically
             ) {
                 Text(text = item.Category ?: item.Value.toString())
                 Spacer(modifier = Modifier.width(5.dp))
                 IconButton(onClick = {
                     model.navigateToWeb(context=context, url = item.MobileLink)
                 }) {
                     Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "go to web")
                 }
             }
        },

    )
    Divider()
}




@Composable
fun CurrentBottomSheet(
    currentWeatherResponseItem: CurrentWeatherResponseItem,
    model: MainComposeViewModel = hiltViewModel()
) {

    val unitType = LocalUnitType.current
    val windGustSpeed = model.getUnit(unitType, currentWeatherResponseItem.WindGust.Speed)
    val cloudCeiling = model.getUnit(unitType, currentWeatherResponseItem.Ceiling)
    val temperatureDeparture =
        model.getUnit(unitType, currentWeatherResponseItem.Past24HourTemperatureDeparture)
    val apparentTemperature =
        model.getUnit(unitType, currentWeatherResponseItem.ApparentTemperature)
    val windChillTemperature =
        model.getUnit(unitType, currentWeatherResponseItem.WindChillTemperature)
    val wetBulbTemperature = model.getUnit(unitType, currentWeatherResponseItem.WetBulbTemperature)
    val wetBulbGlobeTemperature =
        model.getUnit(unitType, currentWeatherResponseItem.WetBulbGlobeTemperature)
    val precip1hr =
        model.getUnit(unitType, currentWeatherResponseItem.PrecipitationSummary.PastHour)
    val precip3hr =
        model.getUnit(unitType, currentWeatherResponseItem.PrecipitationSummary.Past3Hours)
    val precip6hr =
        model.getUnit(unitType, currentWeatherResponseItem.PrecipitationSummary.Past6Hours)
    val precip9hr =
        model.getUnit(unitType, currentWeatherResponseItem.PrecipitationSummary.Past9Hours)
    val precip12hr =
        model.getUnit(unitType, currentWeatherResponseItem.PrecipitationSummary.Past12Hours)
    val precip18hr =
        model.getUnit(unitType, currentWeatherResponseItem.PrecipitationSummary.Past18Hours)
    val precip24hr =
        model.getUnit(unitType, currentWeatherResponseItem.PrecipitationSummary.Past24Hours)

    Box(
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(0.9f),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .verticalScroll(state = rememberScrollState())
            ) {
                MyListItem(R.string.wind_gust, windGustSpeed)
                MyListItem(R.string.cloud_ceiling, cloudCeiling)
                if (currentWeatherResponseItem.ObstructionsToVisibility.isNotEmpty()) {
                    MyListItem(
                        R.string.Cause_limited_visibility,
                        currentWeatherResponseItem.ObstructionsToVisibility
                    )
                }
                if (currentWeatherResponseItem.CloudCover != null) {
                    MyListItem(
                        R.string.cloud_cover,
                        R.string.cloud_cover_description,
                        "${currentWeatherResponseItem.CloudCover}%"
                    )
                }
                MyListItem(
                    R.string.pressure_tendency,
                    currentWeatherResponseItem.PressureTendency.LocalizedText
                )
                MyListItem(
                    R.string.temperature_departure,
                    R.string.temperature_departure_description,
                    temperatureDeparture
                )
                MyListItem(R.string.apparent_temperature, apparentTemperature)
                MyListItem(R.string.wind_chill_temperature, windChillTemperature)
                MyListItem(
                    R.string.wet_bulb_temperature,
                    R.string.wet_bulb_temperature_description,
                    wetBulbTemperature
                )
                MyListItem(
                    R.string.wet_bulb_globe_temperature,
                    R.string.wet_bulb_globe_temperature_description,
                    wetBulbGlobeTemperature
                )
                MyListItem(R.string.precip1hr, R.string.precip1hr_description, precip1hr)
                MyListItem(R.string.precip3hr, R.string.precip3hr_description, precip3hr)
                MyListItem(R.string.precip6hr, R.string.precip6hr_description, precip6hr)
                MyListItem(R.string.precip9hr, R.string.precip9hr_description, precip9hr)
                MyListItem(R.string.precip12hr, R.string.precip12hr_description, precip12hr)
                MyListItem(R.string.precip18hr, R.string.precip18hr_description, precip18hr)
                MyListItem(R.string.precip24hr, R.string.precip24hr_description, precip24hr)
            }
        }
    }
}

@Composable
fun MyListItem(strId: Int, supportId: Int, trailingText: BaseUnit) {
    ListItem(
        headlineContent = { Text(stringResource(id = strId)) },
        supportingContent = { Text(stringResource(id = supportId)) },
        trailingContent = { Text("${trailingText.Value}${trailingText.Unit}") }
    )
    Divider()
}

@Composable
fun MyListItem(strId: Int, supportId: Int, trailingText: String) {
    ListItem(
        headlineContent = { Text(stringResource(id = strId)) },
        supportingContent = { Text(stringResource(id = supportId)) },
        trailingContent = { Text(trailingText) }
    )
    Divider()
}

@Composable
fun MyListItem(strId: Int, baseUnit: BaseUnit) {
    ListItem(
        headlineContent = { Text(stringResource(id = strId)) },
        //supportingContent = { Text("Secondary text") },
        trailingContent = { Text("${baseUnit.Value}${baseUnit.Unit}") }
    )
    Divider()
}

@Composable
fun MyListItem(strId: Int, str: String) {
    ListItem(
        headlineContent = { Text(stringResource(id = strId)) },
        //supportingContent = { Text("Secondary text") },
        trailingContent = { Text(str) }
    )
    Divider()
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HourslyBottomSheet(
    item: HourlyForecastResponseItem,
    weatherIcon: Int
) {
    Box(
        modifier = Modifier
            .padding(0.dp, 10.dp, 0.dp, 0.dp)
            .fillMaxHeight(0.9f)
            .fillMaxWidth(1f),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(state = rememberScrollState())
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = weatherIcon),
                    modifier = Modifier.size(40.dp),
                    contentDescription = item.IconPhrase
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = item.IconPhrase, style = MaterialTheme.typography.titleSmall)
            }
            Text(
                text = "${item.Temperature.Value.toInt()}${item.Temperature.Unit}",
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = if (!item.HasPrecipitation) stringResource(id = R.string.no_precipitation) else stringResource(
                    id = R.string.chance_precipitation
                ), style = MaterialTheme.typography.titleLarge
            )

            if (item.HasPrecipitation) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("${stringResource(id = R.string.rain_probability)}:${item.RainProbability}%", style = MaterialTheme.typography.bodySmall)
                    Text("${stringResource(id = R.string.snow_probability)}:${item.SnowProbability}%",style = MaterialTheme.typography.bodySmall)
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("${stringResource(id = R.string.ice_probability)}:${item.IceProbability}%",style = MaterialTheme.typography.bodySmall)
                    Text("${stringResource(id = R.string.thunderstorm_probability)}:${item.ThunderstormProbability}%",style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            if (item.RealFeelTemperature != null) {
                MyListItem(
                    strId = R.string.feel_like,
                    str = "${item.RealFeelTemperature.Value}${item.RealFeelTemperature.Unit}"
                )
            }
            if(item.WetBulbTemperature != null){
                MyListItem(
                    strId = R.string.wet_bulb_temperature,
                    supportId = R.string.wet_bulb_temperature_description,
                    trailingText = "${item.WetBulbTemperature.Value}${item.WetBulbTemperature.Unit}"
                )
            }
            if(item.WetBulbGlobeTemperature != null){
                MyListItem(
                    strId = R.string.wet_bulb_globe_temperature,
                    supportId = R.string.wet_bulb_globe_temperature_description,
                    trailingText = "${item.WetBulbGlobeTemperature.Value}${item.WetBulbGlobeTemperature.Unit}"
                )
            }

            if (item.RainProbability > 0) {
                MyListItem(strId = R.string.rain, "${item.Rain.Value}${item.Rain.Unit}")
            }
            if (item.SnowProbability > 0) {
                MyListItem(strId = R.string.snow, "${item.Snow.Value}${item.Snow.Unit}")
            }
            if (item.IceProbability > 0) {
                MyListItem(strId = R.string.ice, "${item.Ice.Value}${item.Ice.Unit}")
            }
            MyListItem(strId = R.string.cloud_cover, "${item.CloudCover}%")
            MyListItem(
                strId = R.string.evapotranspiration,
                "${item.Evapotranspiration.Value}${item.Evapotranspiration.Unit}"
            )
            MyListItem(
                strId = R.string.solar_irradiance,
                "${item.SolarIrradiance.Value}${item.SolarIrradiance.Unit}"
            )


            MyListItem(strId = R.string.dew_point, "${item.DewPoint.Value}${item.DewPoint.Unit}")
            MyListItem(
                strId = R.string.wind_speed,
                "${item.Wind.Speed.Value}${item.Wind.Speed.Unit} ${item.Wind.Direction.Localized}"
            )
            MyListItem(
                strId = R.string.wind_gust,
                "${item.WindGust.Speed.Value}${item.WindGust.Speed.Unit}"
            )
            if (item.RelativeHumidity != null) {
                MyListItem(strId = R.string.relative_humidity, "${item.RelativeHumidity}%")
            }
            if (item.IndoorRelativeHumidity != null) {
                MyListItem(strId = R.string.indoor_humidity, "${item.IndoorRelativeHumidity}%")
            }
            MyListItem(
                strId = R.string.visibility,
                "${item.Visibility.Value}${item.Visibility.Unit}"
            )
            MyListItem(
                strId = R.string.cloud_ceiling,
                "${item.Ceiling.Value.toInt()}${item.Ceiling.Unit}"
            )
            MyListItem(strId = R.string.uv_index, item.UVIndexText)

        }

    }

}

@Composable
fun DailyBottomSheet(
    dailyForecast: DailyForecast,
    dayIcon: Int,
    nightIcon: Int
) {
    val locationInfo = LocalLocationInfo.current
    var state by remember { mutableIntStateOf(0) }
    val titles = listOf(
        stringResource(id = R.string.day),
        stringResource(id = R.string.night),
        stringResource(id = R.string.overall)
    )
    Column {
        TabRow(selectedTabIndex = state) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = state == index,
                    onClick = { state = index },
                    text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) }
                )
            }
        }
        if (state == 0) {
            DayView(dailyForecast, dayIcon)
        } else if (state == 1) {
            NightView(dailyForecast, nightIcon)
        } else {
            OverallView(dailyForecast)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OverallView(
    dailyForecast: DailyForecast,
    model: MainComposeViewModel = hiltViewModel()
) {

    val locationInfo = LocalLocationInfo.current
    val context = LocalContext.current
    val timeZone = TimeZone.getTimeZone(locationInfo.TimeZone.Name)

    var date1 = Date(dailyForecast.Sun.EpochRise * 1000L)
    var date2 = Date(dailyForecast.Sun.EpochSet * 1000L)

    val sunrise = SimpleDateFormat("HH:mm", Locale.getDefault()).also {
        it.timeZone = timeZone
    }.format(date1.time)
    val sunset = SimpleDateFormat("HH:mm", Locale.getDefault()).also {
        it.timeZone = timeZone
    }.format(date2.time)

    // 计算时间差（单位：毫秒）
    var timeDifferenceMillis = abs(date1.time - date2.time)
    // 将时间差转换为小时和分钟
    val hoursSun = timeDifferenceMillis / (60 * 60 * 1000)
    val minutesSun = (timeDifferenceMillis % (60 * 60 * 1000)) / (60 * 1000)

    date1 = Date(dailyForecast.Moon.EpochRise * 1000L)
    date2 = Date(dailyForecast.Moon.EpochSet * 1000L)
    val moonrise = SimpleDateFormat("HH:mm", Locale.getDefault()).also {
        it.timeZone = timeZone
    }.format(date1.time)
    val moonset = SimpleDateFormat("HH:mm", Locale.getDefault()).also {
        it.timeZone = timeZone
    }.format(date2.time)
    // 计算时间差（单位：毫秒）
    timeDifferenceMillis = abs(date1.time - date2.time)
    // 将时间差转换为小时和分钟
    val hoursMoon = timeDifferenceMillis / (60 * 60 * 1000)
    val minutesMoon = (timeDifferenceMillis % (60 * 60 * 1000)) / (60 * 1000)


    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(1f)
            .padding(0.dp, 5.dp)
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(5.dp, 0.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 指定列数
                modifier = Modifier.padding(8.dp)
            ) {
                items(dailyForecast.AirAndPollen) {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "${model.getAirQualityLabel(it.Name, context = context)}:${it.Category}")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(5.dp, 0.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(10.dp, 0.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(1f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.sunny),
                            contentDescription = stringResource(
                                id = R.string.day
                            )
                        )
                        Column {
                            Text(
                                text = "${hoursSun} ${stringResource(id = R.string.hours)}",
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = "${minutesSun} ${stringResource(id = R.string.minute)}",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                    Divider()
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(1f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.sunrise),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(text = sunrise, style = MaterialTheme.typography.bodyLarge)
                    }
                    Divider()
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(1f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.sunset),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(text = sunset, style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(10.dp, 0.dp)
                ) {

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(1f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.clear_night),
                            contentDescription = stringResource(
                                id = R.string.day
                            )
                        )
                        Column {
                            Text(
                                text = "${hoursSun} ${stringResource(id = R.string.hours)}",
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = "${minutesSun} ${stringResource(id = R.string.minute)}",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                    Divider()
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(1f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.moonrise),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(text = moonrise, style = MaterialTheme.typography.bodyLarge)
                    }
                    Divider()
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(1f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.moonset),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(text = moonset, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }


}


@Composable
fun DayView(
    dailyForecast: DailyForecast,
    dayIcon: Int,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(1f)
            .padding(5.dp, 20.dp, 5.dp, 0.dp)
            .verticalScroll(state = rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(60.dp),
                painter = painterResource(id = dayIcon),
                contentDescription = dailyForecast.Day.IconPhrase
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "${dailyForecast.Temperature.Maximum.Value}${dailyForecast.Temperature.Minimum.Unit}",
                    style = MaterialTheme.typography.displayMedium
                )
                Text(
                    text = dailyForecast.Day.IconPhrase,
                    style = MaterialTheme.typography.bodySmall
                )
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = dailyForecast.Day.ShortPhrase, style = MaterialTheme.typography.titleLarge)
        if (dailyForecast.Day.HasPrecipitation) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(0.95f)
            ) {
                Text(
                    "${stringResource(id = R.string.rain_probability)}:${dailyForecast.Day.RainProbability}%",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    "${stringResource(id = R.string.snow_probability)}:${dailyForecast.Day.SnowProbability}%",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(0.95f)
            ) {
                Text(
                    "${stringResource(id = R.string.ice_probability)}:${dailyForecast.Day.IceProbability}%",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    "${stringResource(id = R.string.thunderstorm_probability)}:${dailyForecast.Day.ThunderstormProbability}%",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        MyListItem(
            strId = R.string.wind_speed,
            str = "${dailyForecast.Day.Wind.Speed.Value}${dailyForecast.Day.Wind.Speed.Unit} ${dailyForecast.Day.Wind.Direction.Localized}"
        )
        MyListItem(
            strId = R.string.wind_gust,
            str = "${dailyForecast.Day.WindGust.Speed.Value}${dailyForecast.Day.WindGust.Speed.Unit} ${dailyForecast.Day.WindGust.Direction.Localized}"
        )

        MyListItem(strId = R.string.hours_rain, str = "${dailyForecast.Day.HoursOfRain}")
        MyListItem(
            strId = R.string.rain,
            str = "${dailyForecast.Day.Rain.Value}${dailyForecast.Day.Rain.Unit}"
        )


        MyListItem(strId = R.string.hours_snow, str = "${dailyForecast.Day.HoursOfSnow}")
        MyListItem(
            strId = R.string.snow,
            str = "${dailyForecast.Day.Snow.Value}${dailyForecast.Day.Snow.Unit}"
        )


        MyListItem(strId = R.string.hours_ice, str = "${dailyForecast.Day.HoursOfIce}")
        MyListItem(
            strId = R.string.ice,
            str = "${dailyForecast.Day.Ice.Value}${dailyForecast.Day.Ice.Unit}"
        )

        MyListItem(
            strId = R.string.evapotranspiration,
            str = "${dailyForecast.Day.Evapotranspiration.Value}${dailyForecast.Day.Evapotranspiration.Unit}"
        )
        MyListItem(
            strId = R.string.solar_irradiance,
            str = "${dailyForecast.Day.SolarIrradiance.Value}${dailyForecast.Day.SolarIrradiance.Unit}"
        )
        if(dailyForecast.Day.RelativeHumidity != null){
            MyListItem(
                strId = R.string.relative_humidity,
                str = "${dailyForecast.Day.RelativeHumidity.Minimum}% - ${dailyForecast.Day.RelativeHumidity.Maximum}%"
            )
        }
        MyListItem(
            strId = R.string.wet_bulb_temperature,
            supportId = R.string.wet_bulb_temperature_description,
            trailingText = "${dailyForecast.Day.WetBulbTemperature.Minimum.Value}/${dailyForecast.Day.WetBulbTemperature.Maximum.Value}${dailyForecast.Day.WetBulbTemperature.Maximum.Unit}"
        )
        MyListItem(
            strId = R.string.wet_bulb_globe_temperature,
            supportId = R.string.wet_bulb_globe_temperature_description,
            trailingText = "${dailyForecast.Day.WetBulbGlobeTemperature.Minimum.Value}/${dailyForecast.Day.WetBulbGlobeTemperature.Maximum.Value}${dailyForecast.Day.WetBulbGlobeTemperature.Maximum.Unit}"
        )
    }

}

@Composable
fun NightView(
    dailyForecast: DailyForecast,
    nightIcon: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(1f)
            .padding(5.dp, 20.dp, 5.dp, 0.dp)
            .verticalScroll(state = rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(60.dp),
                painter = painterResource(id = nightIcon),
                contentDescription = dailyForecast.Night.IconPhrase
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "${dailyForecast.Temperature.Minimum.Value}${dailyForecast.Temperature.Minimum.Unit}",
                    style = MaterialTheme.typography.displayMedium
                )
                Text(
                    text = dailyForecast.Night.IconPhrase,
                    style = MaterialTheme.typography.bodySmall
                )
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = dailyForecast.Night.ShortPhrase, style = MaterialTheme.typography.titleLarge)
        if (dailyForecast.Night.HasPrecipitation) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(0.95f)
            ) {
                Text(
                    "${stringResource(id = R.string.rain_probability)}:${dailyForecast.Night.RainProbability}%",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    "${stringResource(id = R.string.snow_probability)}:${dailyForecast.Night.SnowProbability}%",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(0.95f)
            ) {
                Text(
                    "${stringResource(id = R.string.ice_probability)}:${dailyForecast.Night.IceProbability}%",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    "${stringResource(id = R.string.thunderstorm_probability)}:${dailyForecast.Night.ThunderstormProbability}%",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        MyListItem(
            strId = R.string.wind_speed,
            str = "${dailyForecast.Night.Wind.Speed.Value}${dailyForecast.Night.Wind.Speed.Unit} ${dailyForecast.Night.Wind.Direction.Localized}"
        )
        MyListItem(
            strId = R.string.wind_gust,
            str = "${dailyForecast.Night.WindGust.Speed.Value}${dailyForecast.Night.WindGust.Speed.Unit} ${dailyForecast.Night.WindGust.Direction.Localized}"
        )

        MyListItem(strId = R.string.hours_rain, str = "${dailyForecast.Night.HoursOfRain}")
        MyListItem(
            strId = R.string.rain,
            str = "${dailyForecast.Night.Rain.Value}${dailyForecast.Night.Rain.Unit}"
        )


        MyListItem(strId = R.string.hours_snow, str = "${dailyForecast.Night.HoursOfSnow}")
        MyListItem(
            strId = R.string.snow,
            str = "${dailyForecast.Night.Snow.Value}${dailyForecast.Night.Snow.Unit}"
        )


        MyListItem(strId = R.string.hours_ice, str = "${dailyForecast.Night.HoursOfIce}")
        MyListItem(
            strId = R.string.ice,
            str = "${dailyForecast.Night.Ice.Value}${dailyForecast.Night.Ice.Unit}"
        )

        MyListItem(
            strId = R.string.evapotranspiration,
            str = "${dailyForecast.Night.Evapotranspiration.Value}${dailyForecast.Night.Evapotranspiration.Unit}"
        )
        MyListItem(
            strId = R.string.solar_irradiance,
            str = "${dailyForecast.Night.SolarIrradiance.Value}${dailyForecast.Night.SolarIrradiance.Unit}"
        )
        if(dailyForecast.Night.RelativeHumidity != null){
            MyListItem(
                strId = R.string.relative_humidity,
                str = "${dailyForecast.Night.RelativeHumidity.Minimum}% - ${dailyForecast.Night.RelativeHumidity.Maximum}%"
            )
        }
        MyListItem(
            strId = R.string.wet_bulb_temperature,
            supportId = R.string.wet_bulb_temperature_description,
            trailingText = "${dailyForecast.Night.WetBulbTemperature.Minimum.Value}${dailyForecast.Night.WetBulbTemperature.Minimum.Unit} - ${dailyForecast.Night.WetBulbTemperature.Maximum.Value}${dailyForecast.Night.WetBulbTemperature.Maximum.Unit}"
        )
        MyListItem(
            strId = R.string.wet_bulb_globe_temperature,
            supportId = R.string.wet_bulb_globe_temperature_description,
            trailingText = "${dailyForecast.Night.WetBulbGlobeTemperature.Minimum.Value}${dailyForecast.Night.WetBulbGlobeTemperature.Minimum.Unit} - ${dailyForecast.Night.WetBulbGlobeTemperature.Maximum.Value}${dailyForecast.Night.WetBulbGlobeTemperature.Maximum.Unit}"
        )
    }
}

enum class BottomSheetType {
    CURRENT,
    HOURSLY,
    DAILY,
    INDICES
}


@Preview
@Composable
fun PreviewDailyBottomSheet() {
    val gson = Gson()
    val data = "{\n" +
            "      \"Date\": \"2024-01-12T07:00:00-08:00\",\n" +
            "      \"EpochDate\": 1705071600,\n" +
            "      \"Sun\": {\n" +
            "        \"Rise\": \"2024-01-12T07:23:00-08:00\",\n" +
            "        \"EpochRise\": 1705072980,\n" +
            "        \"Set\": \"2024-01-12T17:11:00-08:00\",\n" +
            "        \"EpochSet\": 1705108260\n" +
            "      },\n" +
            "      \"Moon\": {\n" +
            "        \"Rise\": \"2024-01-12T08:46:00-08:00\",\n" +
            "        \"EpochRise\": 1705077960,\n" +
            "        \"Set\": \"2024-01-12T18:46:00-08:00\",\n" +
            "        \"EpochSet\": 1705113960,\n" +
            "        \"Phase\": \"WaxingCrescent\",\n" +
            "        \"Age\": 1\n" +
            "      },\n" +
            "      \"Temperature\": {\n" +
            "        \"Minimum\": {\n" +
            "          \"Value\": 8.2,\n" +
            "          \"Unit\": \"C\",\n" +
            "          \"UnitType\": 17\n" +
            "        },\n" +
            "        \"Maximum\": {\n" +
            "          \"Value\": 14.5,\n" +
            "          \"Unit\": \"C\",\n" +
            "          \"UnitType\": 17\n" +
            "        }\n" +
            "      },\n" +
            "      \"RealFeelTemperature\": {\n" +
            "        \"Minimum\": {\n" +
            "          \"Value\": 7,\n" +
            "          \"Unit\": \"C\",\n" +
            "          \"UnitType\": 17,\n" +
            "          \"Phrase\": \"Chilly\"\n" +
            "        },\n" +
            "        \"Maximum\": {\n" +
            "          \"Value\": 13.6,\n" +
            "          \"Unit\": \"C\",\n" +
            "          \"UnitType\": 17,\n" +
            "          \"Phrase\": \"Cool\"\n" +
            "        }\n" +
            "      },\n" +
            "      \"RealFeelTemperatureShade\": {\n" +
            "        \"Minimum\": {\n" +
            "          \"Value\": 7,\n" +
            "          \"Unit\": \"C\",\n" +
            "          \"UnitType\": 17,\n" +
            "          \"Phrase\": \"Chilly\"\n" +
            "        },\n" +
            "        \"Maximum\": {\n" +
            "          \"Value\": 13.6,\n" +
            "          \"Unit\": \"C\",\n" +
            "          \"UnitType\": 17,\n" +
            "          \"Phrase\": \"Cool\"\n" +
            "        }\n" +
            "      },\n" +
            "      \"HoursOfSun\": 2.1,\n" +
            "      \"DegreeDaySummary\": {\n" +
            "        \"Heating\": {\n" +
            "          \"Value\": 7,\n" +
            "          \"Unit\": \"C\",\n" +
            "          \"UnitType\": 17\n" +
            "        },\n" +
            "        \"Cooling\": {\n" +
            "          \"Value\": 0,\n" +
            "          \"Unit\": \"C\",\n" +
            "          \"UnitType\": 17\n" +
            "        }\n" +
            "      },\n" +
            "      \"AirAndPollen\": [\n" +
            "        {\n" +
            "          \"Name\": \"AirQuality\",\n" +
            "          \"Value\": 56,\n" +
            "          \"Category\": \"Moderate\",\n" +
            "          \"CategoryValue\": 2,\n" +
            "          \"Type\": \"Ozone\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"Name\": \"Grass\",\n" +
            "          \"Value\": 0,\n" +
            "          \"Category\": \"Low\",\n" +
            "          \"CategoryValue\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"Name\": \"Mold\",\n" +
            "          \"Value\": 32767,\n" +
            "          \"Category\": \"High\",\n" +
            "          \"CategoryValue\": 3\n" +
            "        },\n" +
            "        {\n" +
            "          \"Name\": \"Ragweed\",\n" +
            "          \"Value\": 0,\n" +
            "          \"Category\": \"Low\",\n" +
            "          \"CategoryValue\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"Name\": \"Tree\",\n" +
            "          \"Value\": 2,\n" +
            "          \"Category\": \"Low\",\n" +
            "          \"CategoryValue\": 1\n" +
            "        },\n" +
            "        {\n" +
            "          \"Name\": \"UVIndex\",\n" +
            "          \"Value\": 2,\n" +
            "          \"Category\": \"Low\",\n" +
            "          \"CategoryValue\": 1\n" +
            "        }\n" +
            "      ],\n" +
            "      \"Day\": {\n" +
            "        \"Icon\": 6,\n" +
            "        \"IconPhrase\": \"Mostly cloudy\",\n" +
            "        \"HasPrecipitation\": true,\n" +
            "        \"PrecipitationType\": \"Rain\",\n" +
            "        \"PrecipitationIntensity\": \"Light\",\n" +
            "        \"ShortPhrase\": \"A morning shower in spots\",\n" +
            "        \"LongPhrase\": \"A shower in spots late this morning; otherwise, some sun, then turning cloudy\",\n" +
            "        \"PrecipitationProbability\": 49,\n" +
            "        \"ThunderstormProbability\": 10,\n" +
            "        \"RainProbability\": 49,\n" +
            "        \"SnowProbability\": 0,\n" +
            "        \"IceProbability\": 0,\n" +
            "        \"Wind\": {\n" +
            "          \"Speed\": {\n" +
            "            \"Value\": 9.3,\n" +
            "            \"Unit\": \"km/h\",\n" +
            "            \"UnitType\": 7\n" +
            "          },\n" +
            "          \"Direction\": {\n" +
            "            \"Degrees\": 297,\n" +
            "            \"Localized\": \"WNW\",\n" +
            "            \"English\": \"WNW\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"WindGust\": {\n" +
            "          \"Speed\": {\n" +
            "            \"Value\": 16.7,\n" +
            "            \"Unit\": \"km/h\",\n" +
            "            \"UnitType\": 7\n" +
            "          },\n" +
            "          \"Direction\": {\n" +
            "            \"Degrees\": 326,\n" +
            "            \"Localized\": \"NW\",\n" +
            "            \"English\": \"NW\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"TotalLiquid\": {\n" +
            "          \"Value\": 0,\n" +
            "          \"Unit\": \"mm\",\n" +
            "          \"UnitType\": 3\n" +
            "        },\n" +
            "        \"Rain\": {\n" +
            "          \"Value\": 0,\n" +
            "          \"Unit\": \"mm\",\n" +
            "          \"UnitType\": 3\n" +
            "        },\n" +
            "        \"Snow\": {\n" +
            "          \"Value\": 0,\n" +
            "          \"Unit\": \"cm\",\n" +
            "          \"UnitType\": 4\n" +
            "        },\n" +
            "        \"Ice\": {\n" +
            "          \"Value\": 0,\n" +
            "          \"Unit\": \"mm\",\n" +
            "          \"UnitType\": 3\n" +
            "        },\n" +
            "        \"HoursOfPrecipitation\": 0.5,\n" +
            "        \"HoursOfRain\": 0.5,\n" +
            "        \"HoursOfSnow\": 0,\n" +
            "        \"HoursOfIce\": 0,\n" +
            "        \"CloudCover\": 88,\n" +
            "        \"Evapotranspiration\": {\n" +
            "          \"Value\": 1,\n" +
            "          \"Unit\": \"mm\",\n" +
            "          \"UnitType\": 3\n" +
            "        },\n" +
            "        \"SolarIrradiance\": {\n" +
            "          \"Value\": 1297,\n" +
            "          \"Unit\": \"W/m²\",\n" +
            "          \"UnitType\": 33\n" +
            "        },\n" +
            "        \"RelativeHumidity\": {\n" +
            "          \"Minimum\": 53,\n" +
            "          \"Maximum\": 88,\n" +
            "          \"Average\": 68\n" +
            "        },\n" +
            "        \"WetBulbTemperature\": {\n" +
            "          \"Minimum\": {\n" +
            "            \"Value\": 2.9,\n" +
            "            \"Unit\": \"C\",\n" +
            "            \"UnitType\": 17\n" +
            "          },\n" +
            "          \"Maximum\": {\n" +
            "            \"Value\": 9.7,\n" +
            "            \"Unit\": \"C\",\n" +
            "            \"UnitType\": 17\n" +
            "          },\n" +
            "          \"Average\": {\n" +
            "            \"Value\": 7.9,\n" +
            "            \"Unit\": \"C\",\n" +
            "            \"UnitType\": 17\n" +
            "          }\n" +
            "        },\n" +
            "        \"WetBulbGlobeTemperature\": {\n" +
            "          \"Minimum\": {\n" +
            "            \"Value\": 3.5,\n" +
            "            \"Unit\": \"C\",\n" +
            "            \"UnitType\": 17\n" +
            "          },\n" +
            "          \"Maximum\": {\n" +
            "            \"Value\": 11.4,\n" +
            "            \"Unit\": \"C\",\n" +
            "            \"UnitType\": 17\n" +
            "          },\n" +
            "          \"Average\": {\n" +
            "            \"Value\": 9.1,\n" +
            "            \"Unit\": \"C\",\n" +
            "            \"UnitType\": 17\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      \"Night\": {\n" +
            "        \"Icon\": 38,\n" +
            "        \"IconPhrase\": \"Mostly cloudy\",\n" +
            "        \"HasPrecipitation\": false,\n" +
            "        \"ShortPhrase\": \"Mostly cloudy\",\n" +
            "        \"LongPhrase\": \"Mostly cloudy\",\n" +
            "        \"PrecipitationProbability\": 25,\n" +
            "        \"ThunderstormProbability\": 0,\n" +
            "        \"RainProbability\": 25,\n" +
            "        \"SnowProbability\": 0,\n" +
            "        \"IceProbability\": 0,\n" +
            "        \"Wind\": {\n" +
            "          \"Speed\": {\n" +
            "            \"Value\": 9.3,\n" +
            "            \"Unit\": \"km/h\",\n" +
            "            \"UnitType\": 7\n" +
            "          },\n" +
            "          \"Direction\": {\n" +
            "            \"Degrees\": 196,\n" +
            "            \"Localized\": \"SSW\",\n" +
            "            \"English\": \"SSW\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"WindGust\": {\n" +
            "          \"Speed\": {\n" +
            "            \"Value\": 11.1,\n" +
            "            \"Unit\": \"km/h\",\n" +
            "            \"UnitType\": 7\n" +
            "          },\n" +
            "          \"Direction\": {\n" +
            "            \"Degrees\": 160,\n" +
            "            \"Localized\": \"SSE\",\n" +
            "            \"English\": \"SSE\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"TotalLiquid\": {\n" +
            "          \"Value\": 0,\n" +
            "          \"Unit\": \"mm\",\n" +
            "          \"UnitType\": 3\n" +
            "        },\n" +
            "        \"Rain\": {\n" +
            "          \"Value\": 0,\n" +
            "          \"Unit\": \"mm\",\n" +
            "          \"UnitType\": 3\n" +
            "        },\n" +
            "        \"Snow\": {\n" +
            "          \"Value\": 0,\n" +
            "          \"Unit\": \"cm\",\n" +
            "          \"UnitType\": 4\n" +
            "        },\n" +
            "        \"Ice\": {\n" +
            "          \"Value\": 0,\n" +
            "          \"Unit\": \"mm\",\n" +
            "          \"UnitType\": 3\n" +
            "        },\n" +
            "        \"HoursOfPrecipitation\": 0,\n" +
            "        \"HoursOfRain\": 0,\n" +
            "        \"HoursOfSnow\": 0,\n" +
            "        \"HoursOfIce\": 0,\n" +
            "        \"CloudCover\": 94,\n" +
            "        \"Evapotranspiration\": {\n" +
            "          \"Value\": 0,\n" +
            "          \"Unit\": \"mm\",\n" +
            "          \"UnitType\": 3\n" +
            "        },\n" +
            "        \"SolarIrradiance\": {\n" +
            "          \"Value\": 0,\n" +
            "          \"Unit\": \"W/m²\",\n" +
            "          \"UnitType\": 33\n" +
            "        },\n" +
            "        \"RelativeHumidity\": {\n" +
            "          \"Minimum\": 72,\n" +
            "          \"Maximum\": 80,\n" +
            "          \"Average\": 76\n" +
            "        },\n" +
            "        \"WetBulbTemperature\": {\n" +
            "          \"Minimum\": {\n" +
            "            \"Value\": 6.8,\n" +
            "            \"Unit\": \"C\",\n" +
            "            \"UnitType\": 17\n" +
            "          },\n" +
            "          \"Maximum\": {\n" +
            "            \"Value\": 8.3,\n" +
            "            \"Unit\": \"C\",\n" +
            "            \"UnitType\": 17\n" +
            "          },\n" +
            "          \"Average\": {\n" +
            "            \"Value\": 7.4,\n" +
            "            \"Unit\": \"C\",\n" +
            "            \"UnitType\": 17\n" +
            "          }\n" +
            "        },\n" +
            "        \"WetBulbGlobeTemperature\": {\n" +
            "          \"Minimum\": {\n" +
            "            \"Value\": 8.2,\n" +
            "            \"Unit\": \"C\",\n" +
            "            \"UnitType\": 17\n" +
            "          },\n" +
            "          \"Maximum\": {\n" +
            "            \"Value\": 9.6,\n" +
            "            \"Unit\": \"C\",\n" +
            "            \"UnitType\": 17\n" +
            "          },\n" +
            "          \"Average\": {\n" +
            "            \"Value\": 8.9,\n" +
            "            \"Unit\": \"C\",\n" +
            "            \"UnitType\": 17\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      \"Sources\": [\n" +
            "        \"AccuWeather\"\n" +
            "      ],\n" +
            "      \"MobileLink\": \"http://www.accuweather.com/en/us/mountain-view-ca/94041/daily-weather-forecast/337169?day=1&unit=c&lang=en-us\",\n" +
            "      \"Link\": \"http://www.accuweather.com/en/us/mountain-view-ca/94041/daily-weather-forecast/337169?day=1&unit=c&lang=en-us\"\n" +
            "    }"
    val dailyForecast = gson.fromJson(data, DailyForecast::class.java)
    DailyBottomSheet(
        dailyForecast = dailyForecast,
        dayIcon = R.drawable.mostly_sunny,
        nightIcon = R.drawable.mostly_cloudy
    )
}

@Preview
@Composable
fun PreviewHourslyBottomSheet() {
    val gson = Gson()
    val hourlyForecastResponseItem = gson.fromJson<HourlyForecastResponseItem>(
        "{\n" +
                "    \"DateTime\": \"2024-01-12T07:00:00-08:00\",\n" +
                "    \"EpochDateTime\": 1705071600,\n" +
                "    \"WeatherIcon\": 35,\n" +
                "    \"IconPhrase\": \"Partly cloudy\",\n" +
                "    \"HasPrecipitation\": false,\n" +
                "    \"IsDaylight\": false,\n" +
                "    \"Temperature\": {\n" +
                "      \"Value\": 38,\n" +
                "      \"Unit\": \"F\",\n" +
                "      \"UnitType\": 18\n" +
                "    },\n" +
                "    \"RealFeelTemperature\": {\n" +
                "      \"Value\": 40,\n" +
                "      \"Unit\": \"F\",\n" +
                "      \"UnitType\": 18,\n" +
                "      \"Phrase\": \"Chilly\"\n" +
                "    },\n" +
                "    \"RealFeelTemperatureShade\": {\n" +
                "      \"Value\": 40,\n" +
                "      \"Unit\": \"F\",\n" +
                "      \"UnitType\": 18,\n" +
                "      \"Phrase\": \"Chilly\"\n" +
                "    },\n" +
                "    \"WetBulbTemperature\": {\n" +
                "      \"Value\": 38,\n" +
                "      \"Unit\": \"F\",\n" +
                "      \"UnitType\": 18\n" +
                "    },\n" +
                "    \"WetBulbGlobeTemperature\": {\n" +
                "      \"Value\": 38,\n" +
                "      \"Unit\": \"F\",\n" +
                "      \"UnitType\": 18\n" +
                "    },\n" +
                "    \"DewPoint\": {\n" +
                "      \"Value\": 36,\n" +
                "      \"Unit\": \"F\",\n" +
                "      \"UnitType\": 18\n" +
                "    },\n" +
                "    \"Wind\": {\n" +
                "      \"Speed\": {\n" +
                "        \"Value\": 2.3,\n" +
                "        \"Unit\": \"mi/h\",\n" +
                "        \"UnitType\": 9\n" +
                "      },\n" +
                "      \"Direction\": {\n" +
                "        \"Degrees\": 324,\n" +
                "        \"Localized\": \"NW\",\n" +
                "        \"English\": \"NW\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"WindGust\": {\n" +
                "      \"Speed\": {\n" +
                "        \"Value\": 3.5,\n" +
                "        \"Unit\": \"mi/h\",\n" +
                "        \"UnitType\": 9\n" +
                "      }\n" +
                "    },\n" +
                "    \"RelativeHumidity\": 94,\n" +
                "    \"IndoorRelativeHumidity\": 31,\n" +
                "    \"Visibility\": {\n" +
                "      \"Value\": 10,\n" +
                "      \"Unit\": \"mi\",\n" +
                "      \"UnitType\": 2\n" +
                "    },\n" +
                "    \"Ceiling\": {\n" +
                "      \"Value\": 20000,\n" +
                "      \"Unit\": \"ft\",\n" +
                "      \"UnitType\": 0\n" +
                "    },\n" +
                "    \"UVIndex\": 0,\n" +
                "    \"UVIndexText\": \"Low\",\n" +
                "    \"PrecipitationProbability\": 0,\n" +
                "    \"ThunderstormProbability\": 0,\n" +
                "    \"RainProbability\": 0,\n" +
                "    \"SnowProbability\": 0,\n" +
                "    \"IceProbability\": 0,\n" +
                "    \"TotalLiquid\": {\n" +
                "      \"Value\": 0,\n" +
                "      \"Unit\": \"in\",\n" +
                "      \"UnitType\": 1\n" +
                "    },\n" +
                "    \"Rain\": {\n" +
                "      \"Value\": 0,\n" +
                "      \"Unit\": \"in\",\n" +
                "      \"UnitType\": 1\n" +
                "    },\n" +
                "    \"Snow\": {\n" +
                "      \"Value\": 0,\n" +
                "      \"Unit\": \"in\",\n" +
                "      \"UnitType\": 1\n" +
                "    },\n" +
                "    \"Ice\": {\n" +
                "      \"Value\": 0,\n" +
                "      \"Unit\": \"in\",\n" +
                "      \"UnitType\": 1\n" +
                "    },\n" +
                "    \"CloudCover\": 45,\n" +
                "    \"Evapotranspiration\": {\n" +
                "      \"Value\": 0,\n" +
                "      \"Unit\": \"in\",\n" +
                "      \"UnitType\": 1\n" +
                "    },\n" +
                "    \"SolarIrradiance\": {\n" +
                "      \"Value\": 0,\n" +
                "      \"Unit\": \"W/m²\",\n" +
                "      \"UnitType\": 33\n" +
                "    },\n" +
                "    \"MobileLink\": \"http://www.accuweather.com/en/us/mountain-view-ca/94041/hourly-weather-forecast/337169?day=1&hbhhour=7&lang=en-us\",\n" +
                "    \"Link\": \"http://www.accuweather.com/en/us/mountain-view-ca/94041/hourly-weather-forecast/337169?day=1&hbhhour=7&lang=en-us\"\n" +
                "  }", HourlyForecastResponseItem::class.java
    )
    HourslyBottomSheet(hourlyForecastResponseItem, R.drawable.fog)
}

