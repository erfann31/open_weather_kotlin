package com.example.open_weater_kotlin_uidata.weather

import com.example.open_weater_kotlin_uidata.model.CurrentWeather
import com.example.open_weater_kotlin_uidata.model.LocationCoordinate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.Path

interface WeatherRepository {

    suspend fun getCoordinatesByLocationName(locationName: String): Response<List<LocationCoordinate>>

    suspend fun getLocationCoordinateFlow(): Flow<LocationCoordinate?>

    suspend fun createLocationCoordinate(locationCoordinates: LocationCoordinate)

    suspend fun getCurrentWeatherData(lat: Double, lon: Double): Response<CurrentWeather>

    suspend fun reverseGeocoding(lat: Double, lon: Double): Response<List<LocationCoordinate>>

    suspend fun createCurrentWeather(currentWeather: CurrentWeather)

    suspend fun getCurrentWeatherFlow(): Flow<CurrentWeather?>
}
