package com.example.open_weater_kotlin_ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.open_weater_kotlin_ui.models.CurrentWeather
import com.example.open_weater_kotlin_ui.models.ForecastDaily
import com.example.open_weater_kotlin_ui.models.ForecastHourly
import com.example.open_weater_kotlin_ui.models.LocationCoordinate
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _locationCoordinates = MutableLiveData<List<LocationCoordinate>>()
    val locationCoordinates: LiveData<List<LocationCoordinate>> = _locationCoordinates

    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: LiveData<CurrentWeather> = _currentWeather

    private val _dailyForecast = MutableLiveData<ForecastDaily>()
    val dailyForecast: LiveData<ForecastDaily> = _dailyForecast

    private val _hourlyForecast = MutableLiveData<ForecastHourly>()
    val hourlyForecast: LiveData<ForecastHourly> = _hourlyForecast

    fun getLocationCoordinates(locationName: String) {
        viewModelScope.launch {
            val response = repository.getCoordinatesByLocationName(locationName)
            if (response.isSuccessful) {
                _locationCoordinates.value = response.body()
                val coordinates = response.body()?.firstOrNull()
                if (coordinates != null) {
                    fetchWeatherData(coordinates.lat, coordinates.lon)
                }
            } else {
                //todo Handle error
            }
        }
    }

    private suspend fun fetchWeatherData(lat: Double, lon: Double) {
        val currentWeatherResponse = repository.getCurrentWeatherData(lat, lon)
        if (currentWeatherResponse.isSuccessful) {
            _currentWeather.value = currentWeatherResponse.body()
        }

        val dailyForecastResponse = repository.getDailyForecast(lat, lon)
        if (dailyForecastResponse.isSuccessful) {
            _dailyForecast.value = dailyForecastResponse.body()
        }

        val hourlyForecastResponse = repository.getHourlyForecast(lat, lon)
        if (hourlyForecastResponse.isSuccessful) {
            _hourlyForecast.value = hourlyForecastResponse.body()
        }
    }
}