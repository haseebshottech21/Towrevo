package com.Towrevo.fcm

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import com.google.firebase.messaging.RemoteMessage
import com.Towrevo.R
import com.Towrevo.ui.authentication.LoginActivity
import com.Towrevo.util.CHANNEL_ID
import com.Towrevo.util.CHANNEL_NAME


private lateinit var mNotification: Notification
private val mNotificationId: Int = 1000

@SuppressLint("NewApi")
fun createChannel(context:Context) {


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME, importance)
        notificationChannel.enableVibration(true)
        notificationChannel.setShowBadge(true)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.parseColor("#e8334a")
        notificationChannel.description = context.getString(R.string.app_name)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(notificationChannel)
    }

}

fun notification(context:Context,data: RemoteMessage) {
    createChannel(context)
    var notifyIntent = Intent(context, LoginActivity::class.java)

    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val res = context.resources
    val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)


    val pendingIntent =
        PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)



    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        mNotification = Notification.Builder(context,
            CHANNEL_ID
        )
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher_round))
            .setAutoCancel(true)
            .setContentTitle("Test")
            .setStyle(
                Notification.BigTextStyle()
                    .bigText("This is test message")
            )
            .setNumber(1)
            .setSound(uri)
            .setContentText("This is test message").build()
    } else {
        mNotification = Notification.Builder(context)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher_round))
            .setAutoCancel(true)
            .setContentTitle("Test")
            .setStyle(
                Notification.BigTextStyle()
                    .bigText("This is test message")
            )
            .setNumber(1)
            .setSound(uri)
            .setContentText("This is test message").build()
    }
    notificationManager.notify(
        mNotificationId,
        mNotification
    )
}