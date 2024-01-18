package com.unisa.weatherkitapp.repository

import com.unisa.weatherkitapp.net.IndicesProxy
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IndicesRepository @Inject constructor(
    private val indicesProxy: IndicesProxy
) {
    suspend fun requestIndices(locationID:String, groupid:Int) = indicesProxy.requestIndicesByGroup(locationID = locationID, groupID = groupid)
}