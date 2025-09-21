package com.example.sniff

import android.os.Bundle
import android.widget.ExpandableListView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.sniff.data.SnifferLog
import com.example.sniff.manager.SnifferStore
import com.example.sniff.notification.SnifferLogList
import com.example.sniff.ui.SnifferExpandableListAdapter


class SnifferActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sniffer)

        val expandableListView = findViewById<ExpandableListView>(R.id.expandableListView)

        // Example data: replace with your SnifferLog list
        val logs: List<SnifferLog> = SnifferStore.getLogs()

        val requestList = logs.map { "${it.method} ${it.url}" }
        val responseMap = HashMap<String, String>().apply {
            logs.forEach { put("${it.method} ${it.url}", it.responseBody ?: "No response") }
        }

        val adapter = SnifferExpandableListAdapter(this, requestList, responseMap)
        expandableListView.setAdapter(adapter)

    }
}