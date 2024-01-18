package com.unisa.weatherkitapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.unisa.weatherkitapp.R
import com.unisa.weatherkitapp.data.alarms.AlarmsResponse
import com.unisa.weatherkitapp.repository.AlarmsRepository
import com.unisa.weatherkitapp.repository.LocationRepository
import com.unisa.weatherkitapp.repository.Utils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class AlarmsWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val alarmsRepository: AlarmsRepository,
    private val locationRepository: LocationRepository
): CoroutineWorker(context,params){

    override suspend fun doWork(): Result {

        val selectedLocation = locationRepository.syncGetSelectedLocation()
        val alarmsList = alarmsRepository.getOnedayAlarms(selectedLocation.locationId)
        createNotification(alarmsList)
        return Result.success()
    }

    private fun createNotification(alarmsList:AlarmsResponse) {
        val channelId = "my_channel_id"
        val channelName = "My Channel"

        // 创建通知渠道（仅适用于 Android 8.0 及更高版本）
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        // 可以设置其他通知渠道的属性
        // ...

        val notificationManager =
            applicationContext.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
        // }

        val intent = applicationContext.packageManager.getLaunchIntentForPackage(applicationContext.packageName)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val types = alarmsList.flatMap {
            it.Alarms.map {
                it.AlarmType
            }
        }
        if(types.isNotEmpty()){
            // 创建通知
            val notification = NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(applicationContext.getString(R.string.daily_express))
                .setContentText(types.joinToString(" "))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            // 显示通知
            val notificationId = System.currentTimeMillis().toInt() // 每个通知需要一个唯一的 ID
            notificationManager.notify(notificationId, notification)
        }
    }

}