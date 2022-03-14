package com.example.fundooapp.view

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.fundooapp.R

class Notification {

    @SuppressLint("ObsoleteSdkInt")
    fun createNotificationChannel(context: Context, title: String, message:String, pendingIntent: PendingIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            channel.description = DESCRIPTION
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(NOTIFICATION_ID, createSampleNotification(title, message, pendingIntent, context))
        }
    }

    private fun createSampleNotification(title: String, message:String, pendingIntent: PendingIntent, context: Context): Notification? {
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_baseline_edit_24)
            setContentTitle(title)
            setContentText(message)
            setDefaults(NotificationCompat.DEFAULT_ALL)
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(true)
            setContentIntent(pendingIntent)
        }
        return notificationBuilder.build()
    }


    companion object {
        const val  NOTIFICATION_ID = 1
        const val CHANNEL_ID = "Fundoo"
        const val CHANNEL_NAME = "com.example.fundooapp.view"
        const val DESCRIPTION = "channel for notification"
    }
}