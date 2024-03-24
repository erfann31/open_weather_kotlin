package com.example.open_weater_kotlin_uiui.detail.viewmodel

import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.open_weater_kotlin_ui.data.weather.WeatherIconHelper
import com.example.open_weater_kotlin_uidata.model.CurrentWeather
import com.example.open_weater_kotlin_uidata.weather.WeatherUseCase
import com.example.open_weater_kotlin_uiutil.coroutine.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WeatherDetailViewModelImpl @Inject constructor(
    private val weatherUseCase: WeatherUseCase,
    coroutineDispatchers: CoroutineDispatchers,
) : ViewModel(), WeatherDetailViewModel {

    private val compositeJob = Job()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.tag(tag)
            .e("coroutineExceptionHandler() error occurred: $throwable \n ${throwable.message}")
        //todo show error snack bar
    }

    private val coroutineContext =
        compositeJob + coroutineDispatchers.main + exceptionHandler + SupervisorJob()

    private val flowCompositeJob = Job()

    private val flowContext =
        flowCompositeJob + coroutineDispatchers.main + exceptionHandler + SupervisorJob()

    private val tag = WeatherDetailViewModelImpl::class.java.simpleName

    private val weatherDetailState =
        mutableStateOf<WeatherDetailState>(WeatherDetailState.LoadingState)

    override fun getState(): WeatherDetailState = weatherDetailState.value

    override fun getWeatherDetailNavRouteUiState(): WeatherDetailNavRouteUi? {
        val state = getState()
        if (state !is WeatherDetailState.WeatherDetailLoadedState) return null

        return state.weatherDetailNavRouteUi.value
    }

    override fun goToSearchScreenUi() {
        val state = getState()
        if (state !is WeatherDetailState.WeatherDetailLoadedState) return
        state.weatherDetailNavRouteUi.value = WeatherDetailNavRouteUi.GoToSearchScreenUi
    }

    init {
        viewModelScope.launch(flowContext) {
            weatherUseCase.getLocationCoordinateFlow().collectLatest { locationCoordinate ->
                if (locationCoordinate != null) {
                    Timber.tag(tag).d("getLocationCoordinateFlow() location: $locationCoordinate")
                    weatherUseCase.fetchCurrentWeatherData(
                        lat = locationCoordinate.lat,
                        long = locationCoordinate.lon
                    )
                }
            }
        }

        viewModelScope.launch(flowContext) {
            weatherUseCase.getCurrentWeatherFlow().collectLatest { currentWeather ->
                val state = getState()

                if ((currentWeather == null && state is WeatherDetailState.LoadingState) || currentWeather != null) {
                    Timber.tag(tag).d("getCurrentWeatherFlow() currentWeather: $currentWeather")
                    weatherDetailState.value = WeatherDetailState.WeatherDetailLoadedState(
                        currentWeather = currentWeather
                    )
                }
            }
        }
    }

    override fun resetNavRouteUiToIdle() {
        val state = getState()
        if (state !is WeatherDetailState.WeatherDetailLoadedState) return
        state.weatherDetailNavRouteUi.value = WeatherDetailNavRouteUi.Idle
    }

    override fun onLocationGranted(location: Location) {
        viewModelScope.launch(coroutineContext) {
            if (weatherUseCase.getLocationCoordinateFlow().firstOrNull() == null) {
                Timber.tag(tag).d("onLocationGranted(location: Location) location: $location")
                weatherUseCase.fetchReverseGeocoding(
                    lat = location.latitude,
                    lon = location.longitude
                )
            }
        }
    }

    override fun onLocationDenied() {
        viewModelScope.launch(coroutineContext) {
            if (weatherUseCase.getLocationCoordinateFlow().firstOrNull() == null) {
                Timber.tag(tag).d("onLocationDenied()")
                weatherDetailState.value = WeatherDetailState.WeatherDetailLoadedState()
                goToSearchScreenUi()
            }
        }
    }

    override fun getCurrentWeatherName(): String {
        val state = getState()
        if (state !is WeatherDetailState.WeatherDetailLoadedState) return ""
        val currentWeather = state.currentWeather
        return currentWeather?.name ?: ""
    }

    override fun getCurrentWeatherIconUrl(): String {
        val state = getState()
        if (state !is WeatherDetailState.WeatherDetailLoadedState) return ""
        val currentWeather = state.currentWeather
        val description = currentWeather?.weather?.first()?.description
        val icon = currentWeather?.weather?.first()?.icon
        return WeatherIconHelper.getIconUrl(icon, description) ?: ""
    }

    override fun getCurrentTemp(): Double? {
        val state = getState()
        if (state !is WeatherDetailState.WeatherDetailLoadedState) return null
        val currentWeather = state.currentWeather
        return currentWeather?.main?.temp
    }

    override fun getCurrentWeatherMainDescription(): String {
        val state = getState()
        if (state !is WeatherDetailState.WeatherDetailLoadedState) return ""
        val currentWeather = state.currentWeather
        return currentWeather?.weather?.first()?.main ?: ""
    }

    override fun getCurrentWeatherFeelsLike(): Double? {
        val state = getState()
        if (state !is WeatherDetailState.WeatherDetailLoadedState) return null
        val currentWeather = state.currentWeather
        return currentWeather?.main?.feelsLike
    }

    override fun getCurrentWeatherTempMin(): Int? {
        val state = getState()
        if (state !is WeatherDetailState.WeatherDetailLoadedState) return null
        val currentWeather = state.currentWeather
        return currentWeather?.main?.tempMin?.toInt()
    }

    override fun getCurrentWeatherTempMax(): Int? {
        val state = getState()
        if (state !is WeatherDetailState.WeatherDetailLoadedState) return null
        val currentWeather = state.currentWeather
        return currentWeather?.main?.tempMax?.toInt()
    }

    sealed class WeatherDetailState {
        object LoadingState : WeatherDetailState()
        data class WeatherDetailLoadedState(
            val currentWeather: CurrentWeather? = null,
            val weatherDetailNavRouteUi: MutableState<WeatherDetailNavRouteUi> = mutableStateOf(
                WeatherDetailNavRouteUi.Idle
            )
        ) : WeatherDetailState()
    }

    sealed class WeatherDetailNavRouteUi {
        object Idle : WeatherDetailNavRouteUi()
        object GoToSearchScreenUi : WeatherDetailNavRouteUi()
    }
}
