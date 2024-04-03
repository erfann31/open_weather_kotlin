package com.example.open_weater_kotlin_ui.models

import com.google.gson.annotations.SerializedName

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

data class Coord(
    val lon: Double?,
    val lat: Double?
)

data class Weather(
    val id: Long?,
    val main: String?,
    val description: String?,
    val icon: String?
)

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

data class Wind(
    val speed: Double?,
    val deg: Int?,
    val gust: Double?
)

data class Rain(
    @SerializedName("1h") val oneH: Double?,
    @SerializedName("3h") val threeH: Double?
)

data class Snow(
    @SerializedName("1h") val oneH: Double?,
    @SerializedName("3h") val threeH: Double?
)

data class Clouds(
    val all: Long?
)

data class Sys(
    val type: Int?,
    val id: Int?,
    val country: String?,
    val sunrise: Long?,
    val sunset: Long?
)