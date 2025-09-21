package com.example.sniff

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object Sniffer {
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
        createNotificationChannel()
    }

    internal fun getContext(): Context {
        return appContext
            ?: throw IllegalStateException("Sniffer not initialized. Call Sniffer.init(context) in Application.onCreate().")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "sniffer_channel",
                "API Sniffer",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getContext().getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
