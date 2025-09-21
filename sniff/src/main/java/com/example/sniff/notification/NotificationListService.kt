package com.example.sniff.notification

import android.content.Intent
import android.widget.RemoteViewsService

class NotificationListService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return NotificationListFactory(this.applicationContext)
    }
}
