package com.example.open_weater_kotlin_ui.view_model

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.open_weater_kotlin_ui.model.entities.CityItem
import com.example.open_weater_kotlin_ui.model.entities.CurrentWeather
import com.example.open_weater_kotlin_ui.model.entities.ForecastDaily
import com.example.open_weater_kotlin_ui.model.entities.ForecastHourly
import com.example.open_weater_kotlin_ui.model.entities.LocationCoordinate
import com.example.open_weater_kotlin_ui.model.repository.WeatherRepositoryImpl
import com.example.open_weater_kotlin_ui.model.utils.readCitiesFromFile
import com.example.open_weater_kotlin_ui.view_model.lisener.LocationInfoListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * ViewModel class for managing weather data and related operations.
 *
 * @property repository The repository used for fetching weather data.
 * @property listener Listener for location info updates.
 * @property isMetric Mutable state to track the unit of measurement (metric/imperial).
 * @property isLoading LiveData to track the loading state.
 * @property locationsName LiveData to hold the names of locations.
 * @property lat LiveData to hold the latitude.
 * @property lon LiveData to hold the longitude.
 * @property dailyForecast LiveData to hold daily forecast data.
 * @property hourlyForecast LiveData to hold hourly forecast data.
 * @property error LiveData to hold error messages.
 * @property locationNameFlow Flow to handle location name updates.
 *
 * @author Erfan Nasri
 */
class WeatherViewModel(private val repository: WeatherRepositoryImpl) : ViewModel() {
    var listener: LocationInfoListener? = null
    var isMetric = mutableStateOf(true)
        private set

    var tempUnit = mutableStateOf("℃")
        private set

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private val _locationsName = MutableLiveData<List<String>>()
    val locationsName: LiveData<List<String>> = _locationsName

    private val _lat = MutableLiveData<Double>()
    val lat: LiveData<Double> = _lat

    private val _lon = MutableLiveData<Double>()
    val lon: LiveData<Double> = _lon

    private val _locationCoordinates = MutableLiveData<List<LocationCoordinate>>()

    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: LiveData<CurrentWeather> = _currentWeather

    private val _dailyForecast = MutableLiveData<ForecastDaily>()
    val dailyForecast: LiveData<ForecastDaily> = _dailyForecast

    private val _hourlyForecast = MutableLiveData<ForecastHourly>()
    val hourlyForecast: LiveData<ForecastHourly> = _hourlyForecast

    private val locationNameFlow = MutableStateFlow("")

    private val _citiesNameFromFile = MutableLiveData<List<String>>()

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        tempUnit.value = if (isMetric.value) " ℃" else " °F"
        locationNameFlow
            .filter { it.isNotEmpty() }
            .onEach { fillLocationsName(it) }
            .launchIn(viewModelScope)
    }

    fun toggleUnit() {
        isMetric.value = !isMetric.value
        updateWeatherData(lat.value!!, lon.value!!)
        tempUnit.value = if (isMetric.value) " ℃" else " °F"
    }


    /**
     * Adds a city name to a file.
     *
     * @param context The context of the application.
     * @param cityName The name of the city to be added.
     */
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

    /**
     * Deletes a city name from a file.
     *
     * @param context The context of the application.
     * @param cityName The name of the city to be deleted.
     */
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


    /**
     * Sets the latitude and longitude for the current location.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     */
    fun setLatLon(latitude: Double, longitude: Double) {
        _lat.value = latitude
        _lon.value = longitude
        updateWeatherData(latitude, longitude)
    }

    /**
     * Updates the weather data based on the given latitude and longitude.
     *
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     */
    private fun updateWeatherData(lat: Double, lon: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                fetchWeatherData(lat, lon)
            } catch (e: Exception) {
                _error.postValue(e.message ?: "Unknown error occurred")
            }
            _isLoading.value = false

        }
    }

    /**
     * Fetches the coordinates for a given location name.
     *
     * @param locationName The name of the location.
     */
    fun getLocationCoordinates(locationName: String) {
        viewModelScope.launch {

            _isLoading.value = true

            val response = repository.getCoordinatesByLocationName(locationName)
            if (response.isSuccessful) {
                _locationCoordinates.value = response.body()
                val coordinates = response.body()?.firstOrNull()
                if (coordinates != null) {
                    _lat.value = coordinates.lat
                    _lon.value = coordinates.lon
                    fetchWeatherData(coordinates.lat, coordinates.lon)
                }

                _isLoading.value = false

            } else {
                val errorBody = response.errorBody()?.string()
                _error.postValue(errorBody ?: "Unknown error occurred")
            }
        }
    }

    fun updateLocationName(locationName: String) {
        locationNameFlow.value = locationName
    }

    fun clearLocationsName() {
        _locationsName.value = emptyList()
    }

    /**
     * Fills the list of location names based on the given location name.
     *
     * @param locationName The name of the location.
     */
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

    /**
     * Fetches the weather data for the given latitude and longitude.
     *
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     */
    private suspend fun fetchWeatherData(lat: Double, lon: Double) {
        try {
            val unit = if (isMetric.value) "metric" else "imperial"

            val currentWeatherResponse = repository.getCurrentWeatherData(lat, lon, unit)
            if (currentWeatherResponse.isSuccessful) {
                _currentWeather.value = currentWeatherResponse.body()
            }

            val dailyForecastResponse = repository.getDailyForecast(lat, lon, unit)
            if (dailyForecastResponse.isSuccessful) {
                _dailyForecast.value = dailyForecastResponse.body()
            }

            val hourlyForecastResponse = repository.getHourlyForecast(lat, lon, unit)
            if (hourlyForecastResponse.isSuccessful) {
                _hourlyForecast.value = hourlyForecastResponse.body()
            }

            listener?.onLocationInfoFetched()
        } catch (e: Exception) {
            _error.postValue(e.message ?: "Unknown error occurred")
        }
    }

    companion object {
        @Volatile
        private var instance: WeatherViewModel? = null

        /**
         * Gets the singleton instance of WeatherViewModel.
         *
         * @param repository The repository used for fetching weather data.
         * @return The singleton instance of WeatherViewModel.
         */

        fun getInstance(repository: WeatherRepositoryImpl): WeatherViewModel {
            return instance ?: synchronized(this) {
                instance ?: WeatherViewModel(repository).also { instance = it }
            }
        }
    }
}