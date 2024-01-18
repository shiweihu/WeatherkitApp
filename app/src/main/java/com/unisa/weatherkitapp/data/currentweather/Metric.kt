package com.unisa.weatherkitapp.data.currentweather

data class Metric(
    override val Phrase: String = "",
    override val Unit: String = "",
    override val UnitType: Int = 0,
    override val Value: Double = 0.0
): BaseUnit