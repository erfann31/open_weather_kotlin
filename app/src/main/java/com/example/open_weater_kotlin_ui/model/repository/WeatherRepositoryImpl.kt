package com.example.open_weater_kotlin_ui.model.repository

import com.example.open_weater_kotlin_ui.model.domain.ApiInterface

import com.example.open_weater_kotlin_ui.model.entities.CurrentWeather
import com.example.open_weater_kotlin_ui.model.entities.ForecastDaily
import com.example.open_weater_kotlin_ui.model.entities.ForecastHourly
import com.example.open_weater_kotlin_ui.model.entities.LocationCoordinate
import com.example.open_weater_kotlin_ui.model.utils.RetrofitInstance
import retrofit2.Response

class WeatherRepositoryImpl(private val apiInterface: ApiInterface): WeatherRepository {
    private val appId = RetrofitInstance.getAppId()
    override suspend fun getCoordinatesByLocationName(locationName: String): Response<List<LocationCoordinate>> {
        return apiInterface.getCoordinatesByLocationNameAsync(locationName, appId).await()
    }

    override suspend fun getCurrentWeatherData(lat: Double, lon: Double): Response<CurrentWeather> {
        return apiInterface.getCurrentWeatherDataAsync(lat, lon, appId).await()
    }

    override suspend fun reverseGeocoding(lat: Double, lon: Double): Response<List<LocationCoordinate>> {
        return apiInterface.reverseGeocodingAsync(lat, lon, appId).await()
    }

    override suspend fun getDailyForecast(lat: Double, lon: Double): Response<ForecastDaily> {
        return apiInterface.getDailyForecastAsync(lat, lon, appId).await()
    }

    override suspend fun getHourlyForecast(lat: Double, lon: Double): Response<ForecastHourly> {
        return apiInterface.getHourlyForecastAsync(lat, lon, appId).await()
    }
}