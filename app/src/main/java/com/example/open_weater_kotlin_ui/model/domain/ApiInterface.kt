package com.example.open_weater_kotlin_ui.model.domain

import com.example.open_weater_kotlin_ui.model.entities.CurrentWeather
import com.example.open_weater_kotlin_ui.model.entities.ForecastDaily
import com.example.open_weater_kotlin_ui.model.entities.ForecastHourly
import com.example.open_weater_kotlin_ui.model.entities.LocationCoordinate
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for API endpoints.
 *
 *    * @author Erfan Nasri
 */

interface ApiInterface {

    /**
     * Fetches coordinates by location name asynchronously.
     * @param locationName The name of the location.
     * @param appid The API key.
     * @return A [Deferred] containing the response with a list of [LocationCoordinate].
     */
    @GET("geo/1.0/direct?limit=10")
    fun getCoordinatesByLocationNameAsync(
        @Query("q") locationName: String,
        @Query("appid") appid: String
    ): Deferred<Response<List<LocationCoordinate>>>

    /**
     * Reverse geocoding to fetch location coordinates asynchronously.
     * @param lat The latitude.
     * @param lon The longitude.
     * @param appid The API key.
     * @return A [Deferred] containing the response with a list of [LocationCoordinate].
     */
    @GET("geo/1.0/reverse?limit=5")
    fun reverseGeocodingAsync(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String
    ): Deferred<Response<List<LocationCoordinate>>>

    /**
     * Fetches current weather data asynchronously.
     * @param lat The latitude.
     * @param lon The longitude.
     * @param unit The unit of measurement.
     * @param appid The API key.
     * @return A [Deferred] containing the response with [CurrentWeather].
     */
    @GET("data/2.5/weather")
    fun getCurrentWeatherDataAsync(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") unit: String,
        @Query("appid") appid: String,
    ): Deferred<Response<CurrentWeather>>

    /**
     * Fetches daily forecast data asynchronously.
     * @param lat The latitude.
     * @param lon The longitude.
     * @param unit The unit of measurement.
     * @param appid The API key.
     * @return A [Deferred] containing the response with [ForecastDaily].
     */
    @GET("data/2.5/forecast/daily?cnt=8")
    fun getDailyForecastAsync(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") unit: String,
        @Query("appid") appid: String
    ): Deferred<Response<ForecastDaily>>

    /**
     * Fetches hourly forecast data asynchronously.
     * @param lat The latitude.
     * @param lon The longitude.
     * @param unit The unit of measurement.
     * @param appid The API key.
     * @return A [Deferred] containing the response with [ForecastHourly].
     */
    @GET("data/2.5/forecast?cnt=8")
    fun getHourlyForecastAsync(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") unit: String,
        @Query("appid") appid: String
    ): Deferred<Response<ForecastHourly>>
}