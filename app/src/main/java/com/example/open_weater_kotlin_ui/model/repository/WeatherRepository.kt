package com.example.open_weater_kotlin_ui.model.repository

import com.example.open_weater_kotlin_ui.model.entities.CurrentWeather
import com.example.open_weater_kotlin_ui.model.entities.ForecastDaily
import com.example.open_weater_kotlin_ui.model.entities.ForecastHourly
import com.example.open_weater_kotlin_ui.model.entities.LocationCoordinate
import retrofit2.Response

interface WeatherRepository {
    suspend fun getCoordinatesByLocationName(locationName: String): Response<List<LocationCoordinate>>
    suspend fun getCurrentWeatherData(lat: Double, lon: Double): Response<CurrentWeather>
    suspend fun reverseGeocoding(lat: Double, lon: Double): Response<List<LocationCoordinate>>
    suspend fun getDailyForecast(lat: Double, lon: Double): Response<ForecastDaily>
    suspend fun getHourlyForecast(lat: Double, lon: Double): Response<ForecastHourly>
}
