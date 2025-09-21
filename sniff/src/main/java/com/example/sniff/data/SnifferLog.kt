package com.example.sniff.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class SnifferLog(
    val method: String? = String(),
    val url: String? = String(),
    val requestHeaders: Map<String, String>? = emptyMap<String, String>(),
    val requestBody: String?? = String(),
    val code: Int? = 0,
    val responseHeaders: Map<String, String>? = emptyMap<String, String>(),
    val responseBody: String? = String(),
    val apiTiming: Double? = 0.0
) : Parcelable

fun okhttp3.Headers.toMap(): Map<String, String> {
    return this.names().associateWith { this[it].orEmpty() }
}