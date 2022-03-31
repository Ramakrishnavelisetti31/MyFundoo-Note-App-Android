package com.example.fundooapp.view

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters

class WorkAlarm(val context: Context, private val params: WorkerParameters): Worker(context, params) {
    private val notification = Notification()
    private val intent = Intent()
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun doWork(): Result {
        val i = Intent(context, ViewReminderNote::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, Notification.NOTIFICATION_ID, i,0)
        notification.createNotificationChannel(
            context, inputData.getString(TITLE).toString(),
            inputData.getString(MESSAGE).toString(), pendingIntent)
        return Result.success()
    }

    companion object {
        const val TITLE = "Fundoo"
        const val MESSAGE = "Note reminder"
    }
}