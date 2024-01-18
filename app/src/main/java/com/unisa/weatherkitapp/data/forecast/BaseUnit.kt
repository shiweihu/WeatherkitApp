package com.unisa.weatherkitapp.data.forecast

abstract class BaseUnit(
    open val Phrase: String = "",
    open val Unit: String = "",
    open val UnitType: Int = 0,
    open val Value: Double = 0.0
)
