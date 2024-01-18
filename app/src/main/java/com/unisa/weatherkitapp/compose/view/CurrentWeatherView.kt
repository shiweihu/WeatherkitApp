package com.unisa.weatherkitapp.compose.view

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unisa.weatherkitapp.R
import com.unisa.weatherkitapp.data.currentweather.CurrentWeatherResponseItem
import com.unisa.weatherkitapp.public.variable.LocalLocationInfo
import com.unisa.weatherkitapp.public.variable.LocalUnitType
import com.unisa.weatherkitapp.viewmodel.CurrentWeatherViewModel
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun CurrentWeatherView(
    modifier: Modifier,
    currentWeatherResponseItem: CurrentWeatherResponseItem,
    model:CurrentWeatherViewModel = hiltViewModel(),
    showBottomSheet:(CurrentWeatherResponseItem)->Unit
){
    val unitType = LocalUnitType.current
    val temperature = model.getUnit(unitType,currentWeatherResponseItem.Temperature)
    val realFeelTemperature = model.getUnit(unitType,currentWeatherResponseItem.RealFeelTemperature)
    val windSpeed = model.getUnit(unitType,currentWeatherResponseItem.Wind.Speed)
    val pressure = model.getUnit(unitType,currentWeatherResponseItem.Pressure)
    val visibility = model.getUnit(unitType,currentWeatherResponseItem.Visibility)
    val dewpoint = model.getUnit(unitType,currentWeatherResponseItem.DewPoint)

    val locationInfo = LocalLocationInfo.current
    val zoomTimeZone: TimeZone = TimeZone.getTimeZone(locationInfo.TimeZone.Name)
    val sdf: SimpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply {
        timeZone = zoomTimeZone
    }
    var currentTime by remember { mutableStateOf(Calendar.getInstance(zoomTimeZone)) }
    val formattedTime by remember(currentTime) {
        mutableStateOf(sdf.format(currentTime.time))
    }

    LaunchedEffect(true) {
        while (true) {
            delay(1000) // 1秒更新一次
            currentTime = Calendar.getInstance(zoomTimeZone)
        }
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = formattedTime, style = MaterialTheme.typography.titleLarge)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(painter = painterResource(id = model.getImage(currentWeatherResponseItem.WeatherIcon)), contentDescription = stringResource(
                    id = R.string.weather_icon_describe
                ),modifier = Modifier.size(50.dp))
                Spacer(modifier = Modifier.width(5.dp))
                Column {
                    Text(text = currentWeatherResponseItem.WeatherText, style = MaterialTheme.typography.titleLarge)
                    Text(text = realFeelTemperature.Phrase, style = MaterialTheme.typography.bodySmall)
                }
            }
            Text(text = "${temperature.Value.toInt()}${temperature.Unit}", style = MaterialTheme.typography.displayMedium)
            Text(text = "${stringResource(id = R.string.feel_like)} ${realFeelTemperature.Value.toInt()}${realFeelTemperature.Unit}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(10.dp))
            if(currentWeatherResponseItem.HasPrecipitation){
                Text(text = stringResource(id = R.string.precipitatopm), style = MaterialTheme.typography.bodyLarge)
            }else{
                Text(text = stringResource(id = R.string.no_precipitatopm), style = MaterialTheme.typography.bodyLarge)
            }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                modifier = Modifier
                    .fillMaxWidth(0.98f)
                    .clickable {
                        showBottomSheet(currentWeatherResponseItem)
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(10.dp, 10.dp)
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(1f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text("${stringResource(id = R.string.wind_speed)}:${windSpeed.Value}${windSpeed.Unit} ${currentWeatherResponseItem.Wind.Direction.Localized}", style = MaterialTheme.typography.labelSmall)
                        Text("${stringResource(id = R.string.humidity)}:${currentWeatherResponseItem.IndoorRelativeHumidity}%", style = MaterialTheme.typography.labelSmall)
                        Text("${stringResource(id = R.string.uv_index)}:${currentWeatherResponseItem.UVIndexText}", style = MaterialTheme.typography.labelSmall)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(1f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text("${stringResource(id = R.string.pressure)}:${pressure.Value}${pressure.Unit}", style = MaterialTheme.typography.labelSmall)
                        Text("${stringResource(id = R.string.visibility)}:${visibility.Value}${visibility.Unit}", style = MaterialTheme.typography.labelSmall)
                        Text("${stringResource(id = R.string.dew_point)}:${dewpoint.Value}${dewpoint.Unit}", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

