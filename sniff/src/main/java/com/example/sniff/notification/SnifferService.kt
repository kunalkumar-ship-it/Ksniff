package com.example.sniff.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.sniff.R
import com.example.sniff.SnifferActivity
import com.example.sniff.data.SnifferLog

class SnifferService : Service() {
    private val channelId = "sniffer_channel"
    private val notificationId = 101

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "UPDATE_NOTIFICATION") {
            val logs = intent.getParcelableArrayListExtra<SnifferLog>("logs")
            logs?.let {
                updateNotification(it)
            }
        }
        return START_STICKY
    }

    @SuppressLint("RemoteViewLayout")
    private fun updateNotification(logs: List<SnifferLog>) {
        val notifIntent = Intent(this, SnifferActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notifIntent, PendingIntent.FLAG_IMMUTABLE
        )

        // Build big text using StringBuilder
        val sb = StringBuilder()
        logs.forEach { log ->
            val endpoint = extractEndpoint(log.url)
            sb.append("status ${log.code}   End Point $endpoint   time ${log.apiTiming}\n\n")
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setContentTitle("APIs......")
            .setContentText("")
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(sb.toString().trim())             )
            .build()

        startForeground(notificationId, notification)
    }





    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Sniffer Logs",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
        // For lower than Android O, no need to create a channel
    }

    private fun extractEndpoint(url: String?): String {
        if (url.isNullOrBlank()) return ""
        return try {
            val uri = Uri.parse(url)
            uri.lastPathSegment ?: url // fallback if parsing fails
        } catch (e: Exception) {
            url // fallback to raw url
        }
    }

    override fun onBind(intent: Intent?) = null
}
