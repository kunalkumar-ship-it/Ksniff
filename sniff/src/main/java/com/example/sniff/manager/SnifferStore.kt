package com.example.sniff.manager

import com.example.sniff.data.SnifferLog

object SnifferStore {
    private val logs = mutableListOf<SnifferLog>()

    fun addLog(log: SnifferLog) {
        logs.add(log)
    }

    fun getLogs(): List<SnifferLog> = logs.toList()

    fun clear() {
        logs.clear()
    }
}
