package com.example.open_weater_kotlin_ui.model.repository

import com.example.open_weater_kotlin_ui.model.domain.ApiInterface

import com.example.open_weater_kotlin_ui.model.entities.CurrentWeather
import com.example.open_weater_kotlin_ui.model.entities.ForecastDaily
import com.example.open_weater_kotlin_ui.model.entities.ForecastHourly
import com.example.open_weater_kotlin_ui.model.entities.LocationCoordinate
import com.example.open_weater_kotlin_ui.model.utils.RetrofitInstance
import retrofit2.Response

/**
 * Implementation of the WeatherRepository interface to fetch weather data from the API.
 *
 * @property apiInterface The interface for API communication.
 *
 * @author Erfan Nasri
 */
class WeatherRepositoryImpl(private val apiInterface: ApiInterface) : WeatherRepository {
    private val appId = RetrofitInstance.getAppId()

    /**
     * Retrieves location coordinates by the given location name from the API.
     *
     * @param locationName The name of the location.
     * @return A Response object containing a list of LocationCoordinate objects.
     */
    override suspend fun getCoordinatesByLocationName(locationName: String): Response<List<LocationCoordinate>> {
        return apiInterface.getCoordinatesByLocationNameAsync(locationName, appId).await()
    }


    /**
     * Performs reverse geocoding to retrieve location coordinates by latitude and longitude.
     *
     * @param lat The latitude coordinate.
     * @param lon The longitude coordinate.
     * @return A Response object containing a list of LocationCoordinate objects.
     */
    override suspend fun reverseGeocoding(lat: Double, lon: Double): Response<List<LocationCoordinate>> {
        return apiInterface.reverseGeocodingAsync(lat, lon, appId).await()
    }

    /**
     * Retrieves the current weather data based on the given coordinates.
     *
     * @param lat The latitude coordinate.
     * @param lon The longitude coordinate.
     * @param unit The unit of measurement for temperature.
     * @return A Response object containing the CurrentWeather data.
     */
    override suspend fun getCurrentWeatherData(lat: Double, lon: Double, unit: String): Response<CurrentWeather> {
        return apiInterface.getCurrentWeatherDataAsync(lat, lon, unit, appId).await()
    }


    /**
     * Retrieves the daily forecast based on the given coordinates.
     *
     * @param lat The latitude coordinate.
     * @param lon The longitude coordinate.
     * @param unit The unit of measurement for temperature.
     * @return A Response object containing the ForecastDaily data.
     */
    override suspend fun getDailyForecast(lat: Double, lon: Double, unit: String): Response<ForecastDaily> {
        return apiInterface.getDailyForecastAsync(lat, lon, unit, appId).await()
    }

    /**
     * Retrieves the hourly forecast based on the given coordinates.
     *
     * @param lat The latitude coordinate.
     * @param lon The longitude coordinate.
     * @param unit The unit of measurement for temperature.
     * @return A Response object containing the ForecastHourly data.
     */
    override suspend fun getHourlyForecast(lat: Double, lon: Double, unit: String): Response<ForecastHourly> {
        return apiInterface.getHourlyForecastAsync(lat, lon, unit, appId).await()
    }
}