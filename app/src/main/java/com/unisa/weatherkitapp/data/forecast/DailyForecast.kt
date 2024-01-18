package com.unisa.weatherkitapp.data.forecast

data class DailyForecast(
    val AirAndPollen: List<AirAndPollen> = listOf(),
    val Date: String = "",
    val Day: Day = Day(),
    val DegreeDaySummary: DegreeDaySummary = DegreeDaySummary(),
    val EpochDate: Int = 0,
    val HoursOfSun: Double = 0.0,
    val Link: String = "",
    val MobileLink: String = "",
    val Moon: Moon = Moon(),
    val Night: Night = Night(),
    val RealFeelTemperature: RealFeelTemperature = RealFeelTemperature(),
    val RealFeelTemperatureShade: RealFeelTemperatureShade = RealFeelTemperatureShade(),
    val Sources: List<String> = listOf(),
    val Sun: Sun = Sun(),
    val Temperature: Temperature = Temperature()
)