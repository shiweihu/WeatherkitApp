package com.unisa.weatherkitapp.data.indices

data class IndicesResponseItem(
    val Ascending: Boolean = false,
    val Category: String? = "",
    val CategoryValue: Int = 0,
    val EpochDateTime: Int = 0,
    val ID: Int = 0,
    val Link: String = "",
    val LocalDateTime: String = "",
    val MobileLink: String = "",
    val Name: String = "",
    val Text: String? = "",
    val Value: Double = 0.0
)