package com.example.open_weater_kotlin_uinetwork.interceptor

import com.example.open_weater_kotlin_uinetwork.NetworkConstants
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import timber.log.Timber
import java.lang.Exception
import java.util.concurrent.TimeUnit

class CacheTimeoutInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        return try {
            val response = chain.proceed(request)

            val cacheControl = CacheControl.Builder()
                .maxAge(1, TimeUnit.MINUTES)
                .build()

            response.newBuilder()
                .header(NetworkConstants.CACHE_CONTROL, cacheControl.toString()).build()

        } catch (e: Exception) {
            Timber.e(e.stackTraceToString())
            val msg = e.message ?: ""

            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(999)
                .message(msg)
                .body(msg.toResponseBody(null)).build()
        }
    }
}
