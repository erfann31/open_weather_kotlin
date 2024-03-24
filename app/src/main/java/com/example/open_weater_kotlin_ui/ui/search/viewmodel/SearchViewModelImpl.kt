package com.example.open_weater_kotlin_uiui.search.viewmodel

import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.open_weater_kotlin_uidata.model.LocationCoordinate
import com.example.open_weater_kotlin_uidata.weather.WeatherUseCase
import com.example.open_weater_kotlin_uiutil.coroutine.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModelImpl @Inject constructor(
    private val weatherUseCase: WeatherUseCase,
    coroutineDispatchers: CoroutineDispatchers
) : ViewModel(), SearchViewModel {

    private val compositeJob = Job()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.tag(tag)
            .e("coroutineExceptionHandler() error occurred: $throwable \n ${throwable.message}")
        //todo fix this showErrorDialog()
    }

    private val coroutineContext =
        compositeJob + coroutineDispatchers.main + exceptionHandler + SupervisorJob()

    private val flowCompositeJob = Job()

    private val tag = SearchViewModelImpl::class.java.simpleName

    private val searchState = mutableStateOf<SearchState>(SearchState.SearchLoadedState())

    override fun getState(): SearchState = searchState.value

    override fun currentLocationClicked(location: Location) {
        viewModelScope.launch(coroutineContext) {
            weatherUseCase.fetchReverseGeocoding(lat = location.latitude, lon = location.longitude)
            goToWeatherDetailScreenUi()
        }
    }

    override fun locationClicked(locationCoordinate: LocationCoordinate) {
        viewModelScope.launch(coroutineContext) {
            weatherUseCase.createLocationCoordinate(locationCoordinate = locationCoordinate)
            goToWeatherDetailScreenUi()
        }
    }

    override fun getSearchNavRouteUiNavRouteUiState(): SearchNavRouteUi? {
        val state = getState()
        if (state !is SearchState.SearchLoadedState) return null
        return state.searchNavRouteUi.value
    }

    override fun goToWeatherDetailScreenUi() {
        val state = getState()
        if (state !is SearchState.SearchLoadedState) return
        val searchNavRouteUiState = state.searchNavRouteUi
        searchNavRouteUiState.value = SearchNavRouteUi.GoToWeatherDetailScreenUi
    }

    override fun updateSearchEditText(text: String) {
        val state = getState()
        if (state !is SearchState.SearchLoadedState) return
        state.searchEditText.value = text

        viewModelScope.launch(coroutineContext) {
            val locationCoordinates =
                weatherUseCase.fetchCoordinatesByLocationName(locationName = text)
            state.locationCoordinates.value = locationCoordinates
        }
    }

    override fun getSearchEditText(): String {
        val state = getState()
        if (state !is SearchState.SearchLoadedState) return ""
        return state.searchEditText.value
    }

    override fun getLocationCoordinates(): List<LocationCoordinate> {
        val state = getState()
        if (state !is SearchState.SearchLoadedState) return emptyList()
        val locationCoordinates = state.locationCoordinates.value

        return if(getSearchEditText().isNotEmpty() && locationCoordinates.isNotEmpty()) {
            locationCoordinates
        } else {
            emptyList()
        }
    }

    override fun resetNavRouteUiToIdle() {
        val state = getState()
        if (state !is SearchState.SearchLoadedState) return
        val searchNavRouteUiState = state.searchNavRouteUi
        searchNavRouteUiState.value = SearchNavRouteUi.Idle
    }

    sealed class SearchState {
        data class SearchLoadedState(
            val locationCoordinates: MutableState<List<LocationCoordinate>> = mutableStateOf(
                emptyList()
            ),
            val searchEditText: MutableState<String> = mutableStateOf(""),
            val searchNavRouteUi: MutableState<SearchNavRouteUi> = mutableStateOf(
                SearchNavRouteUi.Idle
            )
        ) : SearchState()
    }

    sealed class SearchNavRouteUi {
        object Idle : SearchNavRouteUi()
        object GoToWeatherDetailScreenUi : SearchNavRouteUi()
    }
}
