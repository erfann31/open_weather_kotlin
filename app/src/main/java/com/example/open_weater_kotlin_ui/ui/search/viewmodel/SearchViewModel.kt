package com.example.open_weater_kotlin_uiui.search.viewmodel

import android.location.Location
import com.example.open_weater_kotlin_uidata.model.LocationCoordinate
import com.example.open_weater_kotlin_uiui.search.viewmodel.SearchViewModelImpl.SearchState

interface SearchViewModel {
    fun getState(): SearchState

    fun currentLocationClicked(location: Location)

    fun locationClicked(locationCoordinate: LocationCoordinate)

    fun getSearchNavRouteUiNavRouteUiState(): SearchViewModelImpl.SearchNavRouteUi?

    fun goToWeatherDetailScreenUi()

    fun resetNavRouteUiToIdle()

    fun updateSearchEditText(text: String)

    fun getSearchEditText(): String

    fun getLocationCoordinates(): List<LocationCoordinate>
}
