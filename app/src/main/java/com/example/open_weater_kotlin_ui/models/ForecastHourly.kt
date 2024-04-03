package com.example.open_weater_kotlin_ui.models


import com.google.gson.annotations.SerializedName

data class ForecastHourly(
    val forecastHourlyId: Int = 0,
    val cod: String?,
    val message: Int?,
    val cnt: Int?,
    val list: List<HourlyForecast>?,
    val city: City?
)


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




