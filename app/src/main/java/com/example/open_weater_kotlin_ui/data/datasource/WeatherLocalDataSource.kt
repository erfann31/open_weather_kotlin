package com.example.open_weater_kotlin_uidata.datasource

import com.example.open_weater_kotlin_uidata.model.CurrentWeather
import com.example.open_weater_kotlin_uidata.model.LocationCoordinate
import com.example.open_weater_kotlin_uiroom.WeatherDao
import com.example.open_weater_kotlin_uiutil.coroutine.CoroutineDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class WeatherLocalDataSource(
    private val weatherDao: WeatherDao,
    private val coroutineDispatchers: CoroutineDispatchers
) {

    suspend fun getLocationCoordinateFlow(): Flow<LocationCoordinate?> {
        return withContext(coroutineDispatchers.io) {
            weatherDao.getLocationCoordinate()
        }
    }

    suspend fun createLocationCoordinate(locationCoordinates: LocationCoordinate) {
        withContext(coroutineDispatchers.io) {
            weatherDao.deleteAllLocationCoordinate()
            weatherDao.insertLocationCoordinate(locationCoordinates)
        }
    }

    suspend fun createCurrentWeather(currentWeather: CurrentWeather) {
        withContext(coroutineDispatchers.io) {
            weatherDao.deleteCurrentWeather()
            weatherDao.insertCurrentWeather(currentWeather)
        }
    }

    suspend fun getCurrentWeatherFlow(): Flow<CurrentWeather?> {
        return withContext(coroutineDispatchers.io) {
            weatherDao.getCurrentWeather()
        }
    }
}
