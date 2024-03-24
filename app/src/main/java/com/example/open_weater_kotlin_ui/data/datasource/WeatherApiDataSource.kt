package com.example.open_weater_kotlin_uidata.datasource

import com.example.open_weater_kotlin_uidata.model.CurrentWeather
import com.example.open_weater_kotlin_uidata.model.LocationCoordinate
import com.example.open_weater_kotlin_uinetwork.WeatherApi
import com.example.open_weater_kotlin_uiutil.coroutine.CoroutineDispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class WeatherApiDataSource(
    private val weatherApi: WeatherApi,
    private val coroutineDispatchers: CoroutineDispatchers
) {

    suspend fun getCoordinatesByLocationName(locationName: String): Response<List<LocationCoordinate>> {
        return withContext(coroutineDispatchers.io) {
            weatherApi
                .getCoordinatesByLocationNameAsync(locationName = locationName)
                .await()
        }
    }

    suspend fun getCurrentWeatherData(lat: Double, lon: Double): Response<CurrentWeather> {
        return withContext(coroutineDispatchers.io) {
            weatherApi
                .getCurrentWeatherDataAsync(
                    lat = lat,
                    lon = lon
                )
                .await()
        }
    }

    suspend fun reverseGeocoding(lat: Double, lon: Double): Response<List<LocationCoordinate>> {
        return withContext(coroutineDispatchers.io) {
            weatherApi
                .reverseGeocodingAsync(
                    lat = lat,
                    lon = lon
                )
                .await()
        }
    }
}
