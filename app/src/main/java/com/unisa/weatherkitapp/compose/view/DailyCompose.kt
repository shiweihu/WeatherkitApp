package com.unisa.weatherkitapp.compose.view

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.unisa.weatherkitapp.R
import com.unisa.weatherkitapp.data.forecast.DailyForecast
import com.unisa.weatherkitapp.data.forecast.ForecastResponse
import com.unisa.weatherkitapp.public.variable.LocalLocationInfo
import com.unisa.weatherkitapp.viewmodel.DailyViewModel
import java.util.Locale

@Composable
fun DailyCompose(
    model: DailyViewModel = hiltViewModel(),
    forecastResponse: ForecastResponse,
    showBottomSheet: (DailyForecast) -> Unit
) {
//    LazyColumn(
//        modifier = Modifier.padding(5.dp,0.dp),
//    ){
//        items(forecastResponse.DailyForecasts){
//            DailyItems(it,model.getImage(it.Day.Icon),model.getImage(it.Night.Icon),showBottomSheet = showBottomSheet)
//            Spacer(modifier = Modifier.height(10.dp))
//        }
//    }
    Column(
        modifier = Modifier.padding(5.dp, 0.dp),
    ) {
        forecastResponse.DailyForecasts.forEach {
            DailyItems(
                it,
                model.getImage(it.Day.Icon),
                model.getImage(it.Night.Icon),
                showBottomSheet = showBottomSheet
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }


}

@Composable
fun DailyItems(
    dailyForecast: DailyForecast,
    dayImageId: Int,
    nightImageId: Int,
    showBottomSheet: (DailyForecast) -> Unit = {}
) {

    val locationInfo = LocalLocationInfo.current
    val timeZone = TimeZone.getTimeZone(locationInfo.TimeZone.Name)
    val calendar: Calendar = Calendar.getInstance(timeZone)
    calendar.timeInMillis = dailyForecast.EpochDate * 1000L

    val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).also {
        it.timeZone = timeZone
    }.format(calendar.time)

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        modifier = Modifier
            .fillMaxWidth(1f)
            .clickable {
                showBottomSheet(dailyForecast)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(20.dp, 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text(
                text = dayOfWeek,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(0.3f)
            )
            Column(
                modifier = Modifier.fillMaxWidth(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(25.dp),
                    painter = painterResource(id = dayImageId),
                    contentDescription = dailyForecast.Day.IconPhrase
                )
                Spacer(modifier = Modifier.height(5.dp))
                Image(
                    modifier = Modifier.size(25.dp),
                    painter = painterResource(id = nightImageId),
                    contentDescription = dailyForecast.Night.IconPhrase
                )
            }
            Text(modifier = Modifier.fillMaxWidth(1f),
                textAlign = TextAlign.End,
                text="${dailyForecast.Temperature.Minimum.Value}/${dailyForecast.Temperature.Maximum.Value}${dailyForecast.Temperature.Maximum.Unit}",
                style = MaterialTheme.typography.bodyLarge)

        }
    }

}

@Preview
@Composable
fun PrevenDailyItems() {
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
            "          \"Value\": 13.7,\n" +
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
            "          \"Value\": 13.7,\n" +
            "          \"Unit\": \"C\",\n" +
            "          \"UnitType\": 17,\n" +
            "          \"Phrase\": \"Cool\"\n" +
            "        }\n" +
            "      },\n" +
            "      \"HoursOfSun\": 2.2,\n" +
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
            "        \"CloudCover\": 87,\n" +
            "        \"Evapotranspiration\": {\n" +
            "          \"Value\": 1,\n" +
            "          \"Unit\": \"mm\",\n" +
            "          \"UnitType\": 3\n" +
            "        },\n" +
            "        \"SolarIrradiance\": {\n" +
            "          \"Value\": 1366.3,\n" +
            "          \"Unit\": \"W/m²\",\n" +
            "          \"UnitType\": 33\n" +
            "        },\n" +
            "        \"RelativeHumidity\": {\n" +
            "          \"Minimum\": 53,\n" +
            "          \"Maximum\": 88,\n" +
            "          \"Average\": 69\n" +
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
            "            \"Value\": 7.7,\n" +
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
            "            \"Value\": 8.8,\n" +
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
    val gson = Gson()
    val dailyForecast = gson.fromJson<DailyForecast>(data, DailyForecast::class.java)
    DailyItems(
        dailyForecast = dailyForecast,
        R.drawable.mostly_sunny,
        R.drawable.mostly_clear_night
    )
}