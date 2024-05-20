package com.example.open_weater_kotlin_ui.model.entities

import com.google.gson.annotations.SerializedName

/**
 * Data class representing daily forecast.
 *
 * @property forecastDailyId The ID of the daily forecast.
 * @property city The city information.
 * @property cod Internal parameter.
 * @property message Internal parameter.
 * @property cnt Number of forecasts returned.
 * @property list The list of daily forecasts.
 */
data class ForecastDaily(
    val forecastDailyId: Int = 0,
    val city: City?,
    val cod: String?,
    val message: Double?,
    val cnt: Int?,
    val list: List<DailyForecast>?
)

/**
 * Data class representing city information.
 *
 * @property id The city ID.
 * @property name The city name.
 * @property coord The city coordinates.
 * @property country The country code.
 * @property population The city population.
 * @property timezone The timezone offset.
 */
data class City(
    val id: Int?,
    val name: String?,
    val coord: Coord?,
    val country: String?,
    val population: Long?,
    val timezone: Long?
)

/**
 * Data class representing daily forecast details.
 *
 * @property dt Time of data forecasted.
 * @property sunrise Time of sunrise.
 * @property sunset Time of sunset.
 * @property temp Temperature information.
 * @property feelsLike Apparent temperature information.
 * @property pressure Atmospheric pressure.
 * @property humidity Humidity.
 * @property weather The list of weather conditions.
 * @property speed Wind speed.
 * @property deg Wind direction in degrees.
 * @property gust Wind gust.
 * @property clouds Cloudiness percentage.
 * @property pop Probability of precipitation.
 * @property rain Rain volume for the last 1 hour.
 */
data class DailyForecast(
    val dt: Long?,
    val sunrise: Long?,
    val sunset: Long?,
    val temp: Temperature?,
    @SerializedName("feels_like")
    val feelsLike: FeelsLike?,
    val pressure: Int?,
    val humidity: Int?,
    val weather: List<Weather>?,
    val speed: Double?,
    val deg: Int?,
    val gust: Double?,
    val clouds: Int?,
    val pop: Double?,
    val rain: Double?
)

/**
 * Data class representing temperature information.
 *
 * @property day Day temperature.
 * @property min Minimum temperature.
 * @property max Maximum temperature.
 * @property night Night temperature.
 * @property eve Evening temperature.
 * @property morn Morning temperature.
 */
data class Temperature(
    val day: Double?,
    val min: Double?,
    val max: Double?,
    val night: Double?,
    val eve: Double?,
    val morn: Double?
)

/**
 * Data class representing feels-like temperature information.
 *
 * @property day Feels-like temperature during the day.
 * @property night Feels-like temperature during the night.
 * @property eve Feels-like temperature during the evening.
 * @property morn Feels-like temperature during the morning.
 */
data class FeelsLike(
    @SerializedName("day")
    val day: Double?,
    @SerializedName("night")
    val night: Double?,
    @SerializedName("eve")
    val eve: Double?,
    @SerializedName("morn")
    val morn: Double?
)