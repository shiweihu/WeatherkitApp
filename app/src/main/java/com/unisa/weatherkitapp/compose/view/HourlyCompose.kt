package com.unisa.weatherkitapp.compose.view

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.unisa.weatherkitapp.R
import com.unisa.weatherkitapp.data.hourlyforecast.HourlyForecastResponse
import com.unisa.weatherkitapp.data.hourlyforecast.HourlyForecastResponseItem
import com.unisa.weatherkitapp.public.variable.LocalLocationInfo
import com.unisa.weatherkitapp.viewmodel.HourlyViewModel
import java.util.Locale


@Composable
fun HourlyCompose(
    modifier: Modifier = Modifier.fillMaxWidth(1f),
    hourlyForecastResponse: HourlyForecastResponse,
    model:HourlyViewModel = hiltViewModel(),
    hourlyItemClick:(HourlyForecastResponseItem)->Unit
) {
    val state = rememberLazyListState()
    LazyRow(
        state = state,
        modifier = modifier.padding(5.dp,5.dp)
    ){
        items(hourlyForecastResponse){
            HourlyItem(it,model.getImage(it.WeatherIcon),hourlyItemClick = hourlyItemClick)
        }
    }
}

@Composable
fun HourlyItem(
    item: HourlyForecastResponseItem,
    imageid:Int,
    hourlyItemClick:(HourlyForecastResponseItem)->Unit = {}
){
    val locationInfo = LocalLocationInfo.current
    val timeZone = TimeZone.getTimeZone(locationInfo.TimeZone.Name)
    val calendar: Calendar = Calendar.getInstance(timeZone)
    calendar.timeInMillis = item.EpochDateTime * 1000L

    val time = SimpleDateFormat("HH:mm", Locale.getDefault()).also {
        it.timeZone = timeZone
    }.format(calendar.time)
    Card(
        modifier = Modifier.padding(5.dp,0.dp).clickable{
            hourlyItemClick(item)
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier.width(80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = time, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(5.dp))
            Image(painter = painterResource(id = imageid), modifier = Modifier.size(30.dp),contentDescription = "")
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "${item.Temperature.Value.toInt()}${item.Temperature.Unit}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview
@Composable
fun previewHourlyItem(){
    val gson = Gson()
    val hourlyForecastResponseItem = gson.fromJson<HourlyForecastResponseItem>("{\n" +
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
            "  }",HourlyForecastResponseItem::class.java)
    HourlyItem(item = hourlyForecastResponseItem, imageid = R.drawable.ice)
}