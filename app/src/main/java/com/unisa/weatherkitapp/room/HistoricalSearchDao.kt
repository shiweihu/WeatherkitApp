package com.unisa.weatherkitapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoricalSearchDao {

    @Query("select * from HistoricalSearchEntity order by timestampField DESC limit 10")
    fun queryList(): Flow<List<HistoricalSearchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoricalSearch(historicalSearchEntity:HistoricalSearchEntity)

}