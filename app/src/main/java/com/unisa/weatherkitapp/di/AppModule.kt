package com.unisa.weatherkitapp.di

import android.content.Context
import com.unisa.weatherkitapp.MyApplication
import com.unisa.weatherkitapp.room.AppDatabase
import com.unisa.weatherkitapp.room.LocationDao
import com.unisa.weatherkitapp.room.TopCitiesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideMyApplication(@ApplicationContext context: Context): MyApplication {
        return context as MyApplication
    }

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideLocationDao(appDatabase: AppDatabase): LocationDao {
        return appDatabase.locationDao()
    }

    @Singleton
    @Provides
    fun provideTopCitiesDao(appDatabase: AppDatabase): TopCitiesDao {
        return appDatabase.topCitiesDao()
    }






}