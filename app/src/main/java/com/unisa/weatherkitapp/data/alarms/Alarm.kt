package com.unisa.weatherkitapp.data.alarms

data class Alarm(
    val AlarmType: String = "",
    val Day: Day = Day(),
    val Night: Night = Night(),
    val Value: Value = Value()
)