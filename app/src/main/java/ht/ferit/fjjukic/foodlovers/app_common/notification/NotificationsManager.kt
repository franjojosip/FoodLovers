package ht.ferit.fjjukic.foodlovers.app_common.notification

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
    private val appContext: Context
) {
    private lateinit var notificationChannel: NotificationChannel
    private val notificationManager: NotificationManager by lazy {
        appContext.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
    }

    fun sendNotification(imageUri: Uri, title: String, message: String) {
        val intent = Intent(Intent.ACTION_VIEW, imageUri)
        val pendingIntent =
            PendingIntent.getActivity(appContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val contentView = RemoteViews(appContext.packageName, R.layout.notification_layout)
        contentView.setTextViewText(R.id.tv_title, title)
        contentView.setImageViewUri(R.id.iv_picture, imageUri)

        notificationChannel = NotificationChannel(
            R.string.app_channel_id.toString(),
            message,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GREEN
        notificationChannel.enableVibration(false)
        notificationManager.createNotificationChannel(notificationChannel)

        val builder = NotificationCompat.Builder(appContext, R.string.app_channel_id.toString())
            .setContent(contentView)
            .setSmallIcon(R.drawable.ic_app)
            .setLargeIcon(BitmapFactory.decodeResource(appContext.resources, R.drawable.ic_app))
            .setContentIntent(pendingIntent)

        notificationManager.notify(1234, builder.build())
    }
}