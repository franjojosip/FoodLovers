package ht.ferit.fjjukic.foodlovers.data

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import ht.ferit.fjjukic.foodlovers.R

class NotificationsManager(
    private val notificationManager: NotificationManager
) {
    private lateinit var notificationChannel: NotificationChannel

    fun sendNotification(context: Context, activity: Activity, imageUri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, imageUri)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val contentView = RemoteViews(activity.packageName, R.layout.notification_layout)
        contentView.setTextViewText(R.id.tv_title, "Spremljena je nova slika")
        contentView.setImageViewUri(R.id.iv_picture, imageUri)

        notificationChannel = NotificationChannel(
            R.string.channelId.toString(),
            R.string.notificationTitle.toString(),
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GREEN
        notificationChannel.enableVibration(false)
        notificationManager.createNotificationChannel(notificationChannel)

        val builder = NotificationCompat.Builder(context, R.string.channelId.toString())
            .setContent(contentView)
            .setSmallIcon(R.drawable.app_icon)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.app_icon))
            .setContentIntent(pendingIntent)

        notificationManager.notify(1234, builder.build())
    }
}