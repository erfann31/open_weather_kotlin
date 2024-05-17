package com.example.open_weater_kotlin_ui.models.entities

import com.google.gson.annotations.SerializedName


data class ForecastDaily(
    val forecastDailyId: Int = 0,
    val city: City?,
    val cod: String?,
    val message: Double?,
    val cnt: Int?,
    val list: List<DailyForecast>?
)

data class City(
    val id: Int?,
    val name: String?,
    val coord: Coord?,
    val country: String?,
    val population: Long?,
    val timezone: Long?
)


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

data class Temperature(
    val day: Double?,
    val min: Double?,
    val max: Double?,
    val night: Double?,
    val eve: Double?,
    val morn: Double?
)

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