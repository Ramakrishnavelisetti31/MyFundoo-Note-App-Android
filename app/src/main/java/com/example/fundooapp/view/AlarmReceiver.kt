package com.example.fundooapp.view

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver: BroadcastReceiver() {
    private var notification = Notification()
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context?, intent: Intent?) {
        val i = Intent(context, ViewReminderNote::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, Notification.NOTIFICATION_ID, i, PendingIntent.FLAG_UPDATE_CURRENT)
      notification.createNotificationChannel(context!!, intent.getStringExtra(TITLE).toString(),
          intent.getStringExtra(MESSAGE).toString(), pendingIntent)
    }

    companion object {
        const val TITLE = "Fundoo"
        const val MESSAGE = "Note reminder"
    }
}