package com.unisa.weatherkitapp.repository

import com.unisa.weatherkitapp.net.AlarmsProxy
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmsRepository @Inject constructor(
    private val alarmsProxy: AlarmsProxy
) {
    suspend fun getOnedayAlarms(locationid:String) = alarmsProxy.getTodayAlarms(locationid = locationid)
}