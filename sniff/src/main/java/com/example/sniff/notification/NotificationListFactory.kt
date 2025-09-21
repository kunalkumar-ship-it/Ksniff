package com.example.sniff.notification

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.sniff.R
import com.example.sniff.data.SnifferLog
import com.example.sniff.manager.SnifferStore

class NotificationListFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {

    private var logs: List<SnifferLog> = emptyList()

    override fun onCreate() {}

    override fun onDataSetChanged() {
        logs = SnifferStore.getLogs()
    }

    override fun getCount() = logs.size

    override fun getViewAt(position: Int): RemoteViews {
        val log = logs[position]
        return RemoteViews(context.packageName, R.layout.notification_list_item).apply {
            setTextViewText(R.id.item_text, "${log.method} ${log.url}")
        }
    }

    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount() = 1
    override fun getItemId(position: Int) = position.toLong()
    override fun hasStableIds() = true
    override fun onDestroy() {}
}
