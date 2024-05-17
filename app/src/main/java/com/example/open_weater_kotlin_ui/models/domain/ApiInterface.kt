package com.example.open_weater_kotlin_ui.models.domain


import com.example.open_weater_kotlin_ui.models.entities.CurrentWeather
import com.example.open_weater_kotlin_ui.models.entities.ForecastDaily
import com.example.open_weater_kotlin_ui.models.entities.ForecastHourly
import com.example.open_weater_kotlin_ui.models.entities.LocationCoordinate
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("geo/1.0/direct?limit=10")
    fun getCoordinatesByLocationNameAsync(
        @Query("q") locationName: String,
        @Query("appid") appid: String
    ): Deferred<Response<List<LocationCoordinate>>>

    @GET("data/2.5/weather?units=metric")
    fun getCurrentWeatherDataAsync(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
    ): Deferred<Response<CurrentWeather>>

    @GET("geo/1.0/reverse?limit=5")
    fun reverseGeocodingAsync(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String
    ): Deferred<Response<List<LocationCoordinate>>>

    @GET("data/2.5/forecast/daily?units=metric&cnt=16")
    fun getDailyForecastAsync(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String
    ): Deferred<Response<ForecastDaily>>

    @GET("data/2.5/forecast?units=metric&cnt=8")
    fun getHourlyForecastAsync(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String
    ): Deferred<Response<ForecastHourly>>
}