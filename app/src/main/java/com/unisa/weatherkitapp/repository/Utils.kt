package com.unisa.weatherkitapp.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.startActivity
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.unisa.weatherkitapp.R
import com.unisa.weatherkitapp.data.currentweather.BaseParemeters
import com.unisa.weatherkitapp.data.currentweather.BaseUnit
import com.unisa.weatherkitapp.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Utils @Inject constructor() {

    fun navigateToWeb(url:String,context: Context){
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }



    fun getOpenCount(context: Context):Flow<Int>{
        val countKey  = intPreferencesKey("count")
        return context.dataStore.data.map { preferences ->
            preferences[countKey] ?: 0
        }
    }

    suspend fun setOpenCount(context: Context,count:Int){
        val countKey  = intPreferencesKey("count")
        context.dataStore.edit {preferences->
            preferences[countKey] = count
        }
    }

    fun getUnitType(context: Context):Flow<Boolean>{
        val unitTypeKey  = booleanPreferencesKey("type")
        return context.dataStore.data.map { preferences ->
            preferences[unitTypeKey] ?: true
        }
    }

    suspend fun setUnitType(context: Context, unitType: Boolean){
        val unitTypeKey  = booleanPreferencesKey("type")
        context.dataStore.edit {preferences->
            preferences[unitTypeKey] = unitType
        }
    }


//    fun getSelectedIndices(context: Context):Flow<String>{
//        val IndicesList  = stringPreferencesKey("selectedindices")
//        return context.dataStore.data.map { preferences ->
//            preferences[IndicesList] ?: "8,10,11,12,30,58"
//        }
//    }
//    suspend fun setSelectedIndices(context: Context,str:String){
//        val IndicesList  = stringPreferencesKey("selectedindices")
//        context.dataStore.edit {preferences->
//            preferences[IndicesList] = str
//        }
//    }



    fun getUnit(unitType:Boolean,paremeters: BaseParemeters): BaseUnit {
        return if(unitType) paremeters.Metric else paremeters.Imperial
    }

    val airLabel = mapOf(
        "AirQuality" to R.string.air_quality,
        "Mold" to R.string.mold,
        "Grass" to R.string.grass,
        "Ragweed" to R.string.ragweed,
        "Tree" to R.string.tree,
        "UVIndex" to R.string.uv_index
    )


    val indexIcons = mapOf(
        2 to R.drawable.aches_and_pains_icon,
        3 to R.drawable.respiratory_icon,
        4 to R.drawable.gardening_icon,
        5 to R.drawable.environmental_icon,
        6 to R.drawable.outdoor_living_icon,
        7 to R.drawable.beach_marine_icon,
        8 to R.drawable.sportman_icon,
        10 to R.drawable.health_icon,
        11 to R.drawable.outdoor_icon,
        12 to R.drawable.sporting_icon,
        30 to R.drawable.pollen_icon,
        32 to R.drawable.allergy_icon,
        33 to R.drawable.cold_flu_icon,
        34 to R.drawable.drive_icon,
        35 to R.drawable.lawn_icon,
        36 to R.drawable.entertaining,
        38 to R.drawable.air_travel_icon,
        39 to R.drawable.arthritis_icon,
        40 to R.drawable.respiratory_icon,
        41 to R.drawable.astronomy_icon,
        45 to R.drawable.fishing_icon,
        46 to R.drawable.golf_icon,
        48 to R.drawable.hiking_icon,
        50 to R.drawable.hunter_icon,
        51 to R.drawable.migraine_icon,
        53 to R.drawable.sailing_icon,
        55 to R.drawable.sinus_icon,
        56 to R.drawable.ski_icon,
        59 to R.drawable.mosquito_icon,
        60 to R.drawable.winter_icon,
        61 to R.drawable.pest_icon,
        58 to R.drawable.common_icon
    )

    val indexMap = mapOf(
        R.string.lifestyle_aches_and_pains_api to 2,
        R.string.lifestyle_respiratory_api to 3,
        R.string.lifestyle_gardening_api to 4,
        R.string.lifestyle_environmental_api to 5,
        R.string.lifestyle_outdoor_living_api to 6,
        R.string.lifestyle_beach_and_marine_api to 7,
        R.string.lifestyle_sportsman_api to 8,
        R.string.lifestyle_health_api to 10,
        R.string.lifestyle_outdoor_api to 11,
        R.string.lifestyle_sporting_api to 12,
        R.string.pollen to 30,
        R.string.lifestyle_allergies to 32,
        R.string.lifestyle_cold_and_flu to 33,
        R.string.lifestyle_driving to 34,
        R.string.lifestyle_lawn_and_garden to 35,
        R.string.lifestyle_entertaining to 36,
        R.string.lifestyle_air_travel to 38,
        R.string.lifestyle_arthritis to 39,
        R.string.lifestyle_respiratory to 40,
        R.string.lifestyle_astronomy to 41,
        R.string.lifestyle_fishing to 45,
        R.string.lifestyle_golf to 46,
        R.string.lifestyle_hiking to 48,
        R.string.lifestyle_hunting to 50,
        R.string.lifestyle_migraine to 51,
        R.string.lifestyle_sailing to 53,
        R.string.lifestyle_sinus to 55,
        R.string.lifestyle_ski to 56,
        R.string.web_all to 58,
        R.string.mosquito to 59,
        R.string.winter_cast to 60,
        R.string.pest to 61,
    )
//    R.string.china_indices to 100,
//    R.string.korean_indices to 102
    
    val imageMap = mapOf<Int,Int>(
        1 to R.drawable.sunny,
        2 to R.drawable.mostly_sunny,
        3 to R.drawable.partly_sunny,
        4 to R.drawable.intermittent_clouds,
        5 to R.drawable.hazy_sunshine,
        6 to R.drawable.mostly_cloudy,
        7 to R.drawable.mostly_cloudy,
        8 to R.drawable.mostly_cloudy,
        11 to R.drawable.fog,
        12 to R.drawable.showers_icon,
        13 to R.drawable.mostly_cloudy_showers,
        14 to R.drawable.mostly_cloudy_showers,
        15 to R.drawable.t_storms,
        16 to R.drawable.t_storms,
        17 to R.drawable.t_storms,
        18 to R.drawable.rain,
        19 to R.drawable.flurries,
        20 to R.drawable.flurries,
        21 to R.drawable.flurries,
        22 to R.drawable.flurries,
        23 to R.drawable.flurries,
        24 to R.drawable.ice,
        25 to R.drawable.sleet,
        26 to R.drawable.freezing_rain,
        29 to R.drawable.rain_snow,
        30 to R.drawable.hot,
        31 to R.drawable.cold,
        32 to R.drawable.windy,
        33 to R.drawable.clear_night,
        34 to R.drawable.clear_night,
        35 to R.drawable.clear_night,
        36 to R.drawable.clear_night,
        37 to R.drawable.hazy_sunshine,
        38 to R.drawable.mostly_cloudy_night,
        39 to R.drawable.partly_cloudy_showers,
        40 to R.drawable.partly_cloudy_showers,
        41 to R.drawable.t_storms,
        42 to R.drawable.t_storms,
        43 to R.drawable.rain_snow_night,
        44 to R.drawable.snow_night
    )

    companion object{
        val languageCodeToLocalization = mapOf(
            "ar" to listOf("ar-dz", "ar-bh", "ar-eg", "ar-iq", "ar-jo", "ar-kw", "ar-lb", "ar-ly", "ar-ma", "ar-om", "ar-qa", "ar-sa", "ar-sd", "ar-sy", "ar-tn", "ar-ae", "ar-ye"),
            "az" to listOf("az-latn", "az-latn-az"),
            "bn" to listOf("bn-bd", "bn-in"),
            "bs" to listOf("bs-ba"),
            "bg" to listOf("bg-bg"),
            "ca" to listOf("ca-es"),
            "zh" to listOf("zh-hk", "zh-mo", "zh-cn", "zh-hans", "zh-hans-cn", "zh-hans-hk", "zh-hans-mo", "zh-hans-sg", "zh-sg", "zh-tw", "zh-hant", "zh-hant-hk", "zh-hant-mo", "zh-hant-tw"),
            "hr" to listOf("hr-hr"),
            "cs" to listOf("cs-cz"),
            "da" to listOf("da-dk"),
            "nl" to listOf("nl-aw", "nl-be", "nl-cw", "nl-nl", "nl-sx"),
            "en" to listOf("en-as", "en-us", "en-au", "en-bb", "en-be", "en-bz", "en-bm", "en-bw", "en-cm", "en-ca", "en-gh", "en-gu", "en-gy", "en-hk", "en-in", "en-ie", "en-jm", "en-ke", "en-mw", "en-my", "en-mt", "en-mh", "en-mu", "en-na", "en-nz", "en-ng", "en-mp", "en-pk", "en-ph", "en-rw", "en-sg", "en-za", "en-tz", "en-th", "en-tt", "en-um", "en-vi", "en-ug", "en-gb", "en-zm", "en-zw"),
            "et" to listOf("et-ee"),
            "fa" to listOf("fa-af", "fa-ir"),
            "fil" to listOf("fil-ph"),
            "fi" to listOf("fi-fi"),
            "fr" to listOf("fr-dz", "fr-be", "fr-bj", "fr-bf", "fr-bi", "fr-cm", "fr-ca", "fr-cf", "fr-td", "fr-km", "fr-cg", "fr-cd", "fr-ci", "fr-dj", "fr-gq", "fr-fr", "fr-gf", "fr-ga", "fr-gp", "fr-gn", "fr-lu", "fr-mg", "fr-ml", "fr-mq", "fr-mu", "fr-yt", "fr-mc", "fr-ma", "fr-ne", "fr-re", "fr-rw", "fr-bl", "fr-mf", "fr-sn", "fr-sc", "fr-ch", "fr-tg", "fr-tn"),
            "de" to listOf("de-at", "de-be", "de-de", "de-li", "de-lu", "de-ch"),
            "el" to listOf("el-cy", "el-gr"),
            "gu" to listOf(),
            "he" to listOf("he-il"),
            "hi" to listOf("hi-in"),
            "hu" to listOf("hu-hu"),
            "is" to listOf("is-is"),
            "id" to listOf("id-id"),
            "it" to listOf("it-it", "it-ch"),
            "ja" to listOf("ja-jp"),
            "kn" to listOf(),
            "kk" to listOf("kk-kz"),
            "ko" to listOf("ko-kr"),
            "lv" to listOf("lv-lv"),
            "lt" to listOf("lt-lt"),
            "mk" to listOf("mk-mk"),
            "ms" to listOf("ms-bn", "ms-my"),
            "mr" to listOf(),
            "nb" to listOf(),
            "pl" to listOf("pl-pl"),
            "pt" to listOf("pt-ao", "pt-br", "pt-cv", "pt-gw", "pt-mz", "pt-pt", "pt-st"),
            "pa" to listOf("pa-in"),
            "ro" to listOf("ro-md", "ro-mo", "ro-ro"),
            "ru" to listOf("ru-md", "ru-mo", "ru-ru", "ru-ua"),
            "sr" to listOf("sr-latn", "sr-latn-ba", "sr-me", "sr-rs"),
            "sk" to listOf("sk-sk"),
            "sl" to listOf("sl-sl"),
            "es" to listOf("es-ar", "es-bo", "es-cl", "es-co", "es-cr", "es-do", "es-ec", "es-sv", "es-gq", "es-gt", "es-hn", "es-419", "es-mx", "es-ni", "es-pa", "es-py", "es-pe", "es-pr", "es-es", "es-us", "es-uy", "es-ve"),
            "sw" to listOf("sw-cd", "sw-ke", "sw-tz", "sw-ug"),
            "sv" to listOf("sv-fi", "sv-se"),
            "tl" to listOf(),
            "ta" to listOf("ta-in", "ta-lk"),
            "te" to listOf("te-in"),
            "th" to listOf("th-th"),
            "tr" to listOf("tr-tr"),
            "uk" to listOf("uk-ua"),
            "ur" to listOf("ur-bd", "ur-in", "ur-np", "ur-pk"),
            "uz" to listOf("uz-latn", "uz-latn-uz"),
            "vi" to listOf("vi-vn")
        )
    }
}