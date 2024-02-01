package com.unisa.weatherkitapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase

@TypeConverters(Converters::class)
@Database(entities = [LocationEntity::class,HistoricalSearchEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun locationDao(): LocationDao
    abstract fun historicalSearchDao():HistoricalSearchDao
    companion object {
        const val DATABASE_NAME = "weather_database"
        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                        }

                    }
                )
                .build()
        }
    }
}