package com.example.open_weater_kotlin_uinetwork

import com.example.open_weater_kotlin_uidata.model.CurrentWeather
import com.example.open_weater_kotlin_uidata.model.LocationCoordinate
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("geo/1.0/direct?limit=10")
    fun getCoordinatesByLocationNameAsync(
        @Query("q") locationName: String,
    ): Deferred<Response<List<LocationCoordinate>>>

    @GET("data/2.5/weather?units=metric")
    fun getCurrentWeatherDataAsync(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Deferred<Response<CurrentWeather>>

    @GET("geo/1.0/reverse?limit=5")
    fun reverseGeocodingAsync(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Deferred<Response<List<LocationCoordinate>>>
}
