package com.example.open_weater_kotlin_uihilt.module

import android.content.Context
import com.example.open_weater_kotlin_ui.BuildConfig
import com.example.open_weater_kotlin_uinetwork.NetworkConstants
import com.example.open_weater_kotlin_uinetwork.WeatherApi
import com.example.open_weater_kotlin_uinetwork.interceptor.CacheTimeoutInterceptor
import com.example.open_weater_kotlin_uinetwork.interceptor.QueryParameterInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun httpCacheDirectory(@ApplicationContext context: Context): File {
        return File(context.cacheDir, NetworkConstants.HTTP_CACHE_DIR)
    }

    @Singleton
    @Provides
    fun cache(httpCacheDirectory: File): Cache {
        return Cache(httpCacheDirectory, NetworkConstants.HTTP_CACHE_SIZE)
    }

    @Singleton
    @Provides
    fun loggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        if(BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }

        return loggingInterceptor
    }

    @Singleton
    @Provides
    fun queryParameterInterceptor(): QueryParameterInterceptor {
        return QueryParameterInterceptor()
    }

    @Singleton
    @Provides
    fun cacheTimeoutInterceptor(): CacheTimeoutInterceptor {
        return CacheTimeoutInterceptor()
    }

    @Singleton
    @Provides
    fun okHttpClient(
        cache: Cache,
        loggingInterceptor: HttpLoggingInterceptor,
        cacheTimeoutInterceptor: CacheTimeoutInterceptor,
        queryParameterInterceptor: QueryParameterInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(cacheTimeoutInterceptor)
            .addInterceptor(queryParameterInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun retrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun weatherApi(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }
}
