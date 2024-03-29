package com.unisa.weatherkitapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.unisa.weatherkitapp.repository.Utils
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Cache
import java.util.Locale
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@HiltAndroidApp
class MyApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory


    override fun onCreate() {
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        cache = Cache(this.cacheDir, cacheSize)
        super.onCreate()
    }

    companion object{
        var cache:Cache? = null
        var LOCAL_LANGUAGE_CODE = "en"
        var COUNTRY_CODE = "au"
    }



    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}