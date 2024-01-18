package com.unisa.weatherkitapp.data.forecast

data class Headline(
    val Category: String = "",
    val EffectiveDate: String = "",
    val EffectiveEpochDate: Int = 0,
    val EndDate: String = "",
    val EndEpochDate: Int = 0,
    val Link: String = "",
    val MobileLink: String = "",
    val Severity: Int = 0,
    val Text: String = ""
)