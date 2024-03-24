package com.example.open_weater_kotlin_uiui.detail.viewmodel

import android.location.Location

interface WeatherDetailViewModel {

    fun getState(): WeatherDetailViewModelImpl.WeatherDetailState

    fun resetNavRouteUiToIdle()

    fun onLocationGranted(location: Location)

    fun onLocationDenied()

    fun getWeatherDetailNavRouteUiState(): WeatherDetailViewModelImpl.WeatherDetailNavRouteUi?

    fun goToSearchScreenUi()

    fun getCurrentWeatherName(): String

    fun getCurrentWeatherIconUrl(): String?

    fun getCurrentTemp(): Double?

    fun getCurrentWeatherMainDescription(): String

    fun getCurrentWeatherFeelsLike(): Double?

    fun getCurrentWeatherTempMin(): Int?

    fun getCurrentWeatherTempMax(): Int?
}
