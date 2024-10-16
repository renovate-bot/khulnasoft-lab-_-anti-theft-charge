package com.aliciasykes.anti_theft_charge

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.app.PendingIntent
import android.content.Intent

class NotificationUtil(_mainActivity: MainActivity, channelId: String) {

    private var mainActivity: MainActivity = _mainActivity
    private var notificationManager: NotificationManager

    init {
        notificationManager = createNotificationChannel(channelId)
    }

    /**
     * Receives a title and description, then initialises the chanel and displays the notification
     * NotificationId: 0 = armed, 1 = under attack
     */
    fun showNotification(title: String, description: String, notificationID: Int){
        val channelId = "atc"
        createNotificationChannel(channelId)

        val contentIntent = PendingIntent.getActivity(
                mainActivity, 0,
                Intent(mainActivity,
                MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(mainActivity, channelId)
            .setSmallIcon(R.drawable.icon)
            .setContentTitle(title)
            .setContentText(description)
            .setStyle(NotificationCompat.BigTextStyle())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .setContentIntent(contentIntent)
        with(NotificationManagerCompat.from(mainActivity)) {
            notify(notificationID, builder.build())
        }
    }

    fun dismissAllNotifications() {
        notificationManager.cancelAll()
    }

    fun dismissSpecificNotification(notificationID: Int) {
        notificationManager.cancel(notificationID)
    }

    /**
     * Create the notification chanel, and set the importance
     */
    private fun createNotificationChannel(channelId: String): NotificationManager {
        val localNotificationManager =
                mainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = mainActivity.getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = mainActivity.getString(R.string.what_is_notification)
            localNotificationManager.createNotificationChannel(channel)
        }
        return localNotificationManager
    }
}
