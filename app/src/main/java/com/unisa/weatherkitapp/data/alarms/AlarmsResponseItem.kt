package com.unisa.weatherkitapp.data.alarms

data class AlarmsResponseItem(
    val Alarms: List<Alarm> = listOf(),
    val Date: String = "",
    val EpochDate: Int = 0,
    val Link: String = "",
    val MobileLink: String = ""
)