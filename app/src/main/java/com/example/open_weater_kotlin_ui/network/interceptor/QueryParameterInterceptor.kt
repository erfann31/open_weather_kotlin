package com.example.open_weater_kotlin_uinetwork.interceptor

import com.example.open_weater_kotlin_ui.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import timber.log.Timber

class QueryParameterInterceptor() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        return try {

            val originalHttpUrl: HttpUrl = request.url

            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("appid", BuildConfig.APPID)
                .build()

            val requestBuilder: Request.Builder = request.newBuilder()
                .url(url)

            chain.proceed(requestBuilder.build())
        } catch (e: Exception) {
            Timber.e(e.stackTraceToString())
            val msg = e.message ?: ""

            Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(999)
                .message(msg)
                .body(msg.toResponseBody(null)).build()
        }
    }
}
