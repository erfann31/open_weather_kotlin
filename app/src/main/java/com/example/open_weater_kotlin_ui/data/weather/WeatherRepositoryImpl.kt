package com.example.open_weater_kotlin_uidata.weather

import com.example.open_weater_kotlin_uidata.datasource.WeatherApiDataSource
import com.example.open_weater_kotlin_uidata.datasource.WeatherLocalDataSource
import com.example.open_weater_kotlin_uidata.model.CurrentWeather
import com.example.open_weater_kotlin_uidata.model.LocationCoordinate
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImpl(
    private val weatherApiDataSource: WeatherApiDataSource,
    private val weatherLocalDataSource: WeatherLocalDataSource
) : WeatherRepository {

    override suspend fun getCoordinatesByLocationName(locationName: String) =
        weatherApiDataSource.getCoordinatesByLocationName(locationName = locationName)

    override suspend fun getLocationCoordinateFlow() =
        weatherLocalDataSource.getLocationCoordinateFlow()

    override suspend fun createLocationCoordinate(locationCoordinates: LocationCoordinate) =
        weatherLocalDataSource.createLocationCoordinate(locationCoordinates)

    override suspend fun getCurrentWeatherData(lat: Double, lon: Double) =
        weatherApiDataSource.getCurrentWeatherData(
            lat = lat,
            lon = lon
        )

    override suspend fun reverseGeocoding(
        lat: Double,
        lon: Double
    ) = weatherApiDataSource.reverseGeocoding(
        lat = lat,
        lon = lon
    )

    override suspend fun createCurrentWeather(currentWeather: CurrentWeather) =
        weatherLocalDataSource.createCurrentWeather(currentWeather = currentWeather)

    override suspend fun getCurrentWeatherFlow(): Flow<CurrentWeather?> = weatherLocalDataSource.getCurrentWeatherFlow()
}
