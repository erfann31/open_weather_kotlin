package com.example.open_weater_kotlin_ui.model.entities

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the current weather.
 *
 * @property currentWeatherId The ID of the current weather.
 * @property id The ID of the weather data.
 * @property coord The coordinates of the location.
 * @property weather The list of weather conditions.
 * @property base The base weather data.
 * @property main The main weather parameters.
 * @property visibility The visibility in meters.
 * @property wind The wind parameters.
 * @property rain The rain parameters.
 * @property snow The snow parameters.
 * @property clouds The cloudiness parameters.
 * @property dt The time of data calculation.
 * @property sys The system information.
 * @property timezone The timezone offset.
 * @property name The name of the location.
 * @property cod The internal parameter.
 */
data class CurrentWeather(
    val currentWeatherId: Int = 0,
    val id: Long,
    val coord: Coord?,
    val weather: List<Weather>?,
    val base: String?,
    val main: Main?,
    val visibility: Long?,
    val wind: Wind?,
    val rain: Rain?,
    val snow: Snow?,
    val clouds: Clouds?,
    val dt: Int?,
    val sys: Sys?,
    val timezone: Long?,
    val name: String?,
    val cod: Long?
)

/**
 * Data class representing coordinates.
 *
 * @property lon The longitude.
 * @property lat The latitude.
 */
data class Coord(
    val lon: Double?,
    val lat: Double?
)

/**
 * Data class representing weather conditions.
 *
 * @property id The weather condition ID.
 * @property main The weather condition group.
 * @property description The weather condition description.
 * @property icon The weather icon ID.
 */
data class Weather(
    val id: Long?,
    val main: String?,
    val description: String?,
    val icon: String?
)

/**
 * Data class representing main weather parameters.
 *
 * @property temp The temperature.
 * @property feelsLike The apparent temperature.
 * @property tempMin The minimum temperature.
 * @property tempMax The maximum temperature.
 * @property pressure The atmospheric pressure.
 * @property humidity The humidity.
 * @property seaLevel The sea level pressure.
 * @property grndLevel The ground level pressure.
 */
data class Main(
    val temp: Double?,
    @SerializedName("feels_like") val feelsLike: Double?,
    @SerializedName("temp_min") val tempMin: Double?,
    @SerializedName("temp_max") val tempMax: Double?,
    val pressure: Long?,
    val humidity: Long?,
    @SerializedName("sea_level") val seaLevel: Long?,
    @SerializedName("grnd_level") val grndLevel: Long?
)

/**
 * Data class representing wind parameters.
 *
 * @property speed The wind speed.
 * @property deg The wind direction in degrees.
 * @property gust The wind gust.
 */
data class Wind(
    val speed: Double?,
    val deg: Int?,
    val gust: Double?
)

/**
 * Data class representing rain parameters.
 *
 * @property oneH Rain volume for the last 1 hour.
 * @property threeH Rain volume for the last 3 hours.
 */
data class Rain(
    @SerializedName("1h") val oneH: Double?,
    @SerializedName("3h") val threeH: Double?
)

/**
 * Data class representing snow parameters.
 *
 * @property oneH Snow volume for the last 1 hour.
 * @property threeH Snow volume for the last 3 hours.
 */
data class Snow(
    @SerializedName("1h") val oneH: Double?,
    @SerializedName("3h") val threeH: Double?
)

/**
 * Data class representing cloudiness parameters.
 *
 * @property all Cloudiness percentage.
 */
data class Clouds(
    val all: Long?
)

/**
 * Data class representing system information.
 *
 * @property type The type of data.
 * @property id The ID of the data.
 * @property country The country code.
 * @property sunrise The time of sunrise.
 * @property sunset The time of sunset.
 */
data class Sys(
    val type: Int?,
    val id: Int?,
    val country: String?,
    val sunrise: Long?,
    val sunset: Long?
)