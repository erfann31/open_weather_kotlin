package com.example.open_weater_kotlin_ui.model.entities

import com.google.gson.annotations.SerializedName

/**
 * Data class representing hourly forecast.
 *
 * @property forecastHourlyId The ID of the hourly forecast.
 * @property cod Internal parameter.
 * @property message Internal parameter.
 * @property cnt Number of forecasts returned.
 * @property list The list of hourly forecasts.
 * @property city The city information.
 */
data class ForecastHourly(
    val forecastHourlyId: Int = 0,
    val cod: String?,
    val message: Int?,
    val cnt: Int?,
    val list: List<HourlyForecast>?,
    val city: City?
)

/**
 * Data class representing hourly forecast details.
 *
 * @property dt Time of data forecasted.
 * @property main Main weather information.
 * @property weather The list of weather conditions.
 * @property clouds Cloudiness information.
 * @property wind Wind information.
 * @property visibility Visibility distance.
 * @property pop Probability of precipitation.
 * @property sys Additional system information.
 * @property dateTimeText Date and time of the forecast.
 */
data class HourlyForecast(
    val dt: Long?,
    val main: Main?,
    val weather: List<Weather>?,
    val clouds: Clouds?,
    val wind: Wind?,
    val visibility: Int?,
    val pop: Double?,
    val sys: Sys?,
    @SerializedName("dt_txt") val dateTimeText: String?
)