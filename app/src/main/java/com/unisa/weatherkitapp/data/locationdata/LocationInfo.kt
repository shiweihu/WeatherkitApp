package com.unisa.weatherkitapp.data.locationdata

import androidx.room.Entity


data class LocationInfo(
    val AdministrativeArea: AdministrativeArea = AdministrativeArea(),
    val Country: Country = Country(),
    val DataSets: List<String> = listOf(),
    val Details: Details = Details(),
    val EnglishName: String = "",
    val GeoPosition: GeoPosition = GeoPosition(),
    val IsAlias: Boolean = false,
    val Key: String = "",
    val LocalizedName: String = "",
    val PrimaryPostalCode: String = "",
    val Rank: Int = 0,
    val Region: Region = Region(),
    val SupplementalAdminAreas: List<SupplementalAdminArea> = listOf(),
    val TimeZone: TimeZone = TimeZone(),
    val Type: String = "",
    val Version: Int = 0
)