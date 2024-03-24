package com.example.open_weater_kotlin_uiui.search

import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.open_weater_kotlin_uiui.location.getLocationPermissions
import com.example.open_weater_kotlin_uiui.search.viewmodel.SearchViewModel
import com.example.open_weater_kotlin_uiui.search.viewmodel.SearchViewModelImpl
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SearchUi(
    navController: NavController,
    searchViewModel: SearchViewModel
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    when (searchViewModel.getState()) {
        is SearchViewModelImpl.SearchState.SearchLoadedState -> {

            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(""),
                    value = searchViewModel.getSearchEditText(),
                    onValueChange = { searchViewModel.updateSearchEditText(it) },
                    label = { Text("Search City") }
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        getLocationPermissions(
                            onLocationGranted = { location ->
                                searchViewModel.currentLocationClicked(location)
                            },
                            context = context,
                            coroutineScope = coroutineScope,
                            locationPermissionsState = locationPermissionsState
                        )
                    }
                ) {
                    Text(text = "Current Location")
                }

                val locationCoordinates = searchViewModel.getLocationCoordinates()

                if (locationCoordinates.isNotEmpty()) {
                    locationCoordinates
                        .forEach { locationCoordinate ->
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 8.dp)
                                .testTag("locationCoordinatesText")
                                .clickable {
                                    searchViewModel.locationClicked(locationCoordinate = locationCoordinate)
                                }) {
                                Text(
                                    modifier = Modifier.padding(bottom = 8.dp),
                                    text = locationCoordinate.name,
                                    style = MaterialTheme.typography.h6
                                )
                                Divider()
                            }
                        }
                }
            }

            when (searchViewModel.getSearchNavRouteUiNavRouteUiState()) {
                is SearchViewModelImpl.SearchNavRouteUi.Idle -> {}
                is SearchViewModelImpl.SearchNavRouteUi.GoToWeatherDetailScreenUi -> {
                    navController.popBackStack()
                    searchViewModel.resetNavRouteUiToIdle()
                }

                else -> {}
            }
        }

        else -> {}
    }
}
