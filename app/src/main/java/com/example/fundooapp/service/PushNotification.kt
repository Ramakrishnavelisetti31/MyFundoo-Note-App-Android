package com.example.fundooapp.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import com.example.fundooapp.view.HomeActivity
import com.example.fundooapp.view.Notification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class PushNotification: FirebaseMessagingService() {
    private var notification = Notification()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.notification != null) {
            generateNotification(
                remoteMessage.notification!!.title!!,
                remoteMessage.notification!!.body!!
            )
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun generateNotification(title: String, body: String) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, Notification.NOTIFICATION_ID, intent, PendingIntent.FLAG_ONE_SHOT)
        notification.createNotificationChannel(this, title, body, pendingIntent)
    }
}