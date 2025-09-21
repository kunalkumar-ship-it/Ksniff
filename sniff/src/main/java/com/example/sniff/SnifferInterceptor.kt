package com.example.sniff

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.sniff.data.SnifferLog
import com.example.sniff.manager.SnifferStore
import com.example.sniff.notification.SnifferService
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import org.json.JSONObject
import java.nio.charset.Charset

class SnifferInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val requestBodyString = request.body?.let { body ->
            val buffer = Buffer()
            body.writeTo(buffer)
            buffer.readString(Charset.forName("UTF-8"))
        }
        val response = chain.proceed(request)
        val startNs = System.nanoTime()
        val tookMs = (System.nanoTime() - startNs) / 1_000_000.0
        val tookMsFormatted = try {
            String.format("%.2f", tookMs).toDouble()
        } catch (e: Exception) {
            0.0
        }
        val responseBody = response.peekBody(Long.MAX_VALUE).string()

        val log = SnifferLog(
            method = request.method,
            url = request.url.toString(),
            requestHeaders = request.headers.toMap(),
            requestBody = requestBodyString ?: String(),
            code = response.code ?: 200,
            responseHeaders = response.headers.toMap() ?: emptyMap(),
            responseBody = responseBody,
            apiTiming = tookMsFormatted
        )

        // Save log in store
        SnifferStore.addLog(log)
        Log.d("Trace", "log::${log}")
        val intent = Intent(Sniffer.getContext(), SnifferService::class.java).apply {
            action = "UPDATE_NOTIFICATION"
            putParcelableArrayListExtra("logs", ArrayList(SnifferStore.getLogs()))
        }

// For Android O and above
        ContextCompat.startForegroundService(Sniffer.getContext(), intent)
        return response
    }
}
