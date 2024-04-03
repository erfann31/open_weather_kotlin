package com.example.open_weater_kotlin_ui.viewModel

import com.example.open_weater_kotlin_ui.domain.ApiInterface

import com.example.open_weater_kotlin_ui.models.CurrentWeather
import com.example.open_weater_kotlin_ui.models.ForecastDaily
import com.example.open_weater_kotlin_ui.models.ForecastHourly
import com.example.open_weater_kotlin_ui.models.LocationCoordinate
import com.example.open_weater_kotlin_ui.utils.RetrofitInstance
import retrofit2.Response

class WeatherRepository(private val apiInterface: ApiInterface) {
    private val appId = RetrofitInstance.getAppId()
    suspend fun getCoordinatesByLocationName(locationName: String): Response<List<LocationCoordinate>> {
        return apiInterface.getCoordinatesByLocationNameAsync(locationName, appId).await()
    }

    suspend fun getCurrentWeatherData(lat: Double, lon: Double): Response<CurrentWeather> {
        return apiInterface.getCurrentWeatherDataAsync(lat, lon, appId).await()
    }

    suspend fun reverseGeocoding(lat: Double, lon: Double): Response<List<LocationCoordinate>> {
        return apiInterface.reverseGeocodingAsync(lat, lon, appId).await()
    }

    suspend fun getDailyForecast(lat: Double, lon: Double): Response<ForecastDaily> {
        return apiInterface.getDailyForecastAsync(lat, lon, appId).await()
    }

    suspend fun getHourlyForecast(lat: Double, lon: Double): Response<ForecastHourly> {
        return apiInterface.getHourlyForecastAsync(lat, lon, appId).await()
    }
}