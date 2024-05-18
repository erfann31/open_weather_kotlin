package com.example.open_weater_kotlin_ui.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.open_weater_kotlin_ui.models.WeatherRepository
import com.example.open_weater_kotlin_ui.models.entities.CityItem
import com.example.open_weater_kotlin_ui.models.entities.CurrentWeather
import com.example.open_weater_kotlin_ui.models.entities.ForecastDaily
import com.example.open_weater_kotlin_ui.models.entities.ForecastHourly
import com.example.open_weater_kotlin_ui.models.entities.LocationCoordinate
import com.example.open_weater_kotlin_ui.models.utils.readCitiesFromFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

@OptIn(FlowPreview::class)
class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    var listener: LocationInfoListener? = null

    private val _locationDetails = MutableLiveData<List<LocationCoordinate>>()
    val locationDetails: LiveData<List<LocationCoordinate>> = _locationDetails

    private val _locationsName = MutableLiveData<List<String>>()
    val locationsName: LiveData<List<String>> = _locationsName

    private val _lat = MutableLiveData<Double>()
    val lat: LiveData<Double> = _lat

    private val _lon = MutableLiveData<Double>()
    val lon: LiveData<Double> = _lon

    private val _locationCoordinates = MutableLiveData<List<LocationCoordinate>>()
    val locationCoordinates: LiveData<List<LocationCoordinate>> = _locationCoordinates

    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: LiveData<CurrentWeather> = _currentWeather

    private val _dailyForecast = MutableLiveData<ForecastDaily>()
    val dailyForecast: LiveData<ForecastDaily> = _dailyForecast

    private val _hourlyForecast = MutableLiveData<ForecastHourly>()
    val hourlyForecast: LiveData<ForecastHourly> = _hourlyForecast

    private val locationNameFlow = MutableStateFlow("")

    private val _citiesNameFromFile = MutableLiveData<List<String>>()

    fun addCityToFile(context: Context, cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val logFile = File(context.filesDir, "city_logs.json")
            val logEntries = mutableListOf<CityItem>()

            if (logFile.exists()) {
                val jsonText = logFile.readText()
                val jsonArray = JSONArray(jsonText)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val city = jsonObject.getString("cityName")
                    logEntries.add(CityItem(city))
                }
            }

            val newLogEntry = CityItem(cityName)
            logEntries.add(newLogEntry)

            if (logEntries.size > 5) {
                logEntries.removeAt(0)
            }

            val jsonArray = JSONArray()
            for (entry in logEntries) {
                val jsonObject = JSONObject()
                jsonObject.put("cityName", entry.name)
                jsonArray.put(jsonObject)
            }

            logFile.writeText(jsonArray.toString())

            _citiesNameFromFile.postValue(readCitiesFromFile(context))
        }
    }

    fun deleteCityFromFile(context: Context, cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val logFile = File(context.filesDir, "city_logs.json")
            val logEntries = mutableListOf<CityItem>()

            if (logFile.exists()) {
                val jsonText = logFile.readText()
                val jsonArray = JSONArray(jsonText)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val city = jsonObject.getString("cityName")
                    logEntries.add(CityItem(city))
                }
            }

            val iterator = logEntries.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next()
                if (entry.name == cityName) {
                    iterator.remove()
                }
            }

            val jsonArray = JSONArray()
            for (entry in logEntries) {
                val jsonObject = JSONObject()
                jsonObject.put("cityName", entry.name)
                jsonArray.put(jsonObject)
            }

            logFile.writeText(jsonArray.toString())

            _citiesNameFromFile.postValue(readCitiesFromFile(context))
        }
    }

    init {
        locationNameFlow
            .filter { it.isNotEmpty() }
            .onEach { fillLocationsName(it) }
            .launchIn(viewModelScope)
    }

    fun setLatLon(latitude: Double, longitude: Double) {
        _lat.value = latitude
        _lon.value = longitude
        updateWeatherData(latitude, longitude)
    }

    fun updateWeatherData(lat: Double, lon: Double) {
        viewModelScope.launch {
            fetchWeatherData(lat, lon)
        }
    }

    fun getLocationCoordinates(locationName: String) {
        viewModelScope.launch {
            val response = repository.getCoordinatesByLocationName(locationName)
            if (response.isSuccessful) {
                _locationCoordinates.value = response.body()
                val coordinates = response.body()?.firstOrNull()
                if (coordinates != null) {
                    _lat.value = coordinates.lat
                    _lon.value = coordinates.lon
                    fetchWeatherData(coordinates.lat, coordinates.lon)
                }
            } else {
                //todo Handle error
            }
        }
    }

    fun updateLocationName(locationName: String) {
        locationNameFlow.value = locationName
    }

    private fun fillLocationsName(locationName: String) {
        viewModelScope.launch {
            val response = repository.getCoordinatesByLocationName(locationName)
            if (response.isSuccessful) {
                _locationCoordinates.value = response.body()
                val locationCoordinatesList = _locationCoordinates.value ?: emptyList()
                val namesList = mutableListOf<String>()

                locationCoordinatesList.forEach { coordinate ->
                    val reverseResponse = repository.reverseGeocoding(coordinate.lat, coordinate.lon)
                    if (reverseResponse.isSuccessful) {
                        val reverseLocationDetails = reverseResponse.body() ?: emptyList()
                        namesList.addAll(reverseLocationDetails.map { it.name })
                    }
                }

                _locationsName.postValue(namesList)
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
        listener?.onLocationInfoFetched()
    }
}