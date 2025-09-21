package com.example.sniff.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sniff.data.SnifferLog

@Composable
fun SnifferLogList(logs: List<SnifferLog>) {
    LazyColumn {
        items(logs.size){ index->
            val log= logs[index]
            Column (Modifier.padding(8.dp)) {
                Text("[$index] ${log.method} ${log.url}", style = MaterialTheme.typography.bodyLarge)
                Text("Code: ${log.code}", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                Text("Req Body: ${log.requestBody ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
                Text("Res Body: ${log.responseBody?.take(100) ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
    }

