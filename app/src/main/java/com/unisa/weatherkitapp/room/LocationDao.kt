package com.unisa.weatherkitapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("select * from LocationEntity where selected == 1")
    fun getSelectedLocation():Flow<LocationEntity?>

    @Query("select * from LocationEntity where selected == 1")
    suspend fun syncGetSelectedLocation():LocationEntity

    @Query("select count(locationId) from locationentity")
    fun countLocations(): Flow<Int>

    @Query("select * from locationentity order by insertTime desc")
    fun getLocations(): Flow<List<LocationEntity>>

    @Upsert
    suspend fun insertLocation(locationEntity: LocationEntity)

    @Query("delete from LocationEntity where locationId = :id")
    suspend fun deleteLocation(id:String)

    @Query("Update LocationEntity set selected = 0")
    suspend fun resetSelected()

    @Transaction
    suspend fun updateSelectLocation(locationEntity: LocationEntity){
        resetSelected()
        insertLocation(locationEntity)
    }


}