package com.unisa.weatherkitapp.repository

import com.unisa.weatherkitapp.data.DevicePoint
import com.unisa.weatherkitapp.data.locationdata.LocationInfo
import com.unisa.weatherkitapp.net.LocationProxy
import com.unisa.weatherkitapp.room.LocationDao
import com.unisa.weatherkitapp.room.LocationEntity
import com.unisa.weatherkitapp.room.TopCitiesDao
import com.unisa.weatherkitapp.room.TopCitiesEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    private val locationDao: LocationDao,
    private val topCitiesDao: TopCitiesDao,
    private val locationProxy: LocationProxy
) {

    suspend fun requestTopCities()=locationProxy.requestTopCities()

    suspend fun requestLocationDetail(key:String) = locationProxy.requestLocationInfoDetail(locationId = key)
    suspend fun autoComplete(text:String) = locationProxy.autoComplete(text = text)
    suspend fun queryLocation(text:String) = locationProxy.queryLocations(text = text)
    suspend fun queryLocationByPoint(point: DevicePoint) = locationProxy.queryLocationsByGeoposition(text="${point.latitude},${point.longitude}")
    fun countLocations() = locationDao.countLocations()
    fun getSelectedLocation() = locationDao.getSelectedLocation()

    suspend fun syncGetSelectedLocation() = locationDao.syncGetSelectedLocation()

    fun getLocations() = locationDao.getLocations()

    fun getTopCitiesByCountry(code:String)=topCitiesDao.selectTopCitiesByCountry(code)
    fun getTopCitiesLanguage() = topCitiesDao.getTopCitiesLanguage()
    suspend fun cleanAndInsertNew(list:List<TopCitiesEntity>) = topCitiesDao.cleanAndInsertNew(list)

    suspend fun insertLocation(LocationInfo: LocationInfo, selected:Int){
        val entity = LocationEntity(locationId = LocationInfo.Key, locationInfo = LocationInfo,selected = selected)
        if(selected == 0){
            locationDao.insertLocation(entity)
        }else{
            locationDao.updateSelectLocation(entity)
        }
    }

    suspend fun deleteLocation(id:String) = locationDao.deleteLocation(id)


}