package com.unisa.weatherkitapp.data.hourlyforecast

import com.google.gson.annotations.SerializedName

data class HourlyForecastResponseItem(
    val Ceiling: Ceiling = Ceiling(),
    val CloudCover: Int = 0,
    val DateTime: String = "",
    val DewPoint: DewPoint = DewPoint(),
    val EpochDateTime: Int = 0,
    val Evapotranspiration: Evapotranspiration = Evapotranspiration(),
    val HasPrecipitation: Boolean = false,
    val Ice: Ice = Ice(),
    val IceProbability: Int = 0,
    val IconPhrase: String = "",
    val IndoorRelativeHumidity: Int? = 0,
    val IsDaylight: Boolean = false,
    val Link: String = "",
    val MobileLink: String = "",
    val PrecipitationProbability: Int = 0,
    val Rain: Rain = Rain(),
    val RainProbability: Int = 0,
    val RealFeelTemperature: RealFeelTemperature? ,
    val RealFeelTemperatureShade: RealFeelTemperatureShade?,
    val RelativeHumidity: Int? = 0,
    val Snow: Snow = Snow(),
    val SnowProbability: Int = 0,
    val SolarIrradiance: SolarIrradiance = SolarIrradiance(),
    val Temperature: Temperature = Temperature(),
    val ThunderstormProbability: Int = 0,
    val TotalLiquid: TotalLiquid = TotalLiquid(),
    val UVIndex: Int = 0,
    val UVIndexText: String = "",
    val Visibility: Visibility = Visibility(),
    val WeatherIcon: Int = 0,
    @JvmField
    @field:SerializedName("WetBulbGlobeTemperature")
    val WetBulbGlobeTemperature: WetBulbGlobeTemperature? = WetBulbGlobeTemperature(),
    val WetBulbTemperature: WetBulbTemperature? = WetBulbTemperature(),
    val Wind: Wind = Wind(),
    val WindGust: WindGust = WindGust()
)