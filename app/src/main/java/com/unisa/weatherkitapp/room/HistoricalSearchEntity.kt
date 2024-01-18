package com.unisa.weatherkitapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoricalSearchEntity(
    @PrimaryKey
    val text:String,
    val timestampField:Long = System.currentTimeMillis()
)
