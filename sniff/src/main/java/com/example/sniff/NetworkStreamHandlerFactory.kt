package com.example.sniff

import android.util.Log
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.net.URLStreamHandler
import java.net.URLStreamHandlerFactory
import javax.net.ssl.HttpsURLConnection

class NetworkStreamHandlerFactory: URLStreamHandlerFactory {
    override fun createURLStreamHandler(protocol: String): URLStreamHandler? {
        return when (protocol) {
            "http" -> CustomHttpHandler()
            "https" -> CustomHttpsHandler()
            else -> null
        }
    }
}

class CustomHttpHandler : URLStreamHandler() {
    override fun openConnection(url: URL): URLConnection {
        val originalConn = url.openConnection() as HttpURLConnection
        return WrappedHttpURLConnection(originalConn) // Yeh wrapper request/response intercept karega
    }
}

class CustomHttpsHandler : URLStreamHandler() {
    override fun openConnection(url: URL): URLConnection {
        val originalConn = url.openConnection() as HttpsURLConnection
        return WrappedHttpURLConnection(originalConn)
    }
}

class WrappedHttpURLConnection(private val delegate: HttpURLConnection) : HttpURLConnection(delegate.url) {
    // Request intercept: Headers add/log karo
    override fun setRequestMethod(method: String) {
        // Log request method, URL, etc.
        println("Request: $method ${delegate.url}")
        Log.d("NetworkHandler", "Request: $method ${delegate.url}")
        delegate.requestMethod = method
    }

    override fun usingProxy(): Boolean {
        return delegate.usingProxy()
    }

    // Response intercept: Status, body log karo
    override fun getInputStream(): InputStream {
        // Log response code, headers
        println("Response Code: ${delegate.responseCode}")
        Log.d("NetworkHandler", "Response: $ ${delegate.inputStream}")
        return delegate.inputStream
    }


    override fun connect() = delegate.connect()
    override fun disconnect() = delegate.disconnect()

}