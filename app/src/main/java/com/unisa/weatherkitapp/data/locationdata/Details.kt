package com.unisa.weatherkitapp.data.locationdata

data class Details(
    val BandMap: String = "",
    val CanonicalLocationKey: String = "",
    val CanonicalPostalCode: String = "",
    val Climo: String = "",
    val DMA: DMA = DMA(),
    val Key: String = "",
    val LocalRadar: String = "",
    val LocationStem: String = "",
    val MarineStation: String = "",
    val MarineStationGMTOffset: Double = 0.0,
    val MediaRegion: String = "",
    val Metar: String = "",
    val NXMetro: String = "",
    val NXState: String = "",
    val PartnerID: Any = Any(),
    val Population: Int = 0,
    val PrimaryWarningCountyCode: String = "",
    val PrimaryWarningZoneCode: String = "",
    val Satellite: String = "",
    val Sources: List<Source> = listOf(),
    val StationCode: String = "",
    val StationGmtOffset: Double = 0.0,
    val Synoptic: String = "",
    val VideoCode: String = ""
)