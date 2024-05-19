package com.example.open_weater_kotlin_ui.model.utils

import com.example.open_weater_kotlin_ui.model.domain.ApiInterface
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    const val Base = "https://api.openweathermap.org/"
    private const val APP_ID = "271d1234d3f497eed5b1d80a07b3fcd1"

    val api: ApiInterface by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(Base)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        retrofit.create(ApiInterface::class.java)

        val loggingInterceptor = Interceptor { chain ->
            val request = chain.request()
//            Log.i("APIRequest", request.url.toString())
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        retrofit.newBuilder().client(client).build().create(ApiInterface::class.java)
    }


    fun getAppId(): String {
        return APP_ID
    }
}