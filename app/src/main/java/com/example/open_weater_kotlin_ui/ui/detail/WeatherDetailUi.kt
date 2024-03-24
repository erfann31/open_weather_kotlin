package com.example.open_weater_kotlin_uiui.detail

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.open_weater_kotlin_uiui.NavItem
import com.example.open_weater_kotlin_uiui.WeatherTopAppBar
import com.example.open_weater_kotlin_uiui.detail.viewmodel.WeatherDetailViewModel
import com.example.open_weater_kotlin_uiui.detail.viewmodel.WeatherDetailViewModelImpl
import com.example.open_weater_kotlin_uiui.detail.viewmodel.WeatherDetailViewModelImpl.WeatherDetailState.WeatherDetailLoadedState
import com.example.open_weater_kotlin_uiui.location.getLocationPermissions
import com.example.open_weater_kotlin_uiui.theme.WeatherGreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherDetailUi(
    navController: NavController,
    weatherDetailViewModel: WeatherDetailViewModel
) {

    val coroutineScope = rememberCoroutineScope()
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            ACCESS_COARSE_LOCATION,
            ACCESS_FINE_LOCATION,
        )
    )
    val context = LocalContext.current

    Scaffold(
        topBar = { WeatherTopAppBar(weatherDetailViewModel = weatherDetailViewModel) }
    ) { innerPadding ->
        when (val state = weatherDetailViewModel.getState()) {
            is WeatherDetailViewModelImpl.WeatherDetailState.LoadingState -> { }
            is WeatherDetailLoadedState -> {
                WeatherDetailLoadedUi(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    weatherDetailLoadedState = state,
                    viewModel = weatherDetailViewModel
                )
            }

            else -> {}
        }

        LaunchedEffect(key1 = locationPermissionsState.allPermissionsGranted , key2 = locationPermissionsState.shouldShowRationale) {
            getLocationPermissions(
                context = context,
                locationPermissionsState = locationPermissionsState,
                coroutineScope = coroutineScope,
                onLocationGranted = { location ->
                    weatherDetailViewModel.onLocationGranted(location)
                },
                onLocationDenied = {
                    weatherDetailViewModel.onLocationDenied()
                }
            )
        }
    }
}

@Composable
fun WeatherDetailLoadedUi(
    modifier: Modifier,
    navController: NavController,
    weatherDetailLoadedState: WeatherDetailLoadedState,
    viewModel: WeatherDetailViewModel
) {
    Card(
        backgroundColor = WeatherGreen,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 180.dp)
            .padding(4.dp)

    ) {
        if (weatherDetailLoadedState.currentWeather == null) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No Location Found!",
                    modifier = Modifier
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .wrapContentSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {

                    val currentTemp = viewModel.getCurrentTemp()
                    if (currentTemp != null) {
                        Text(
                            text = "$currentTemp℃",
                            modifier = Modifier
                                .padding(0.dp)
                                .testTag("currentTempText"),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h4
                        )
                    }

                    val mainDescription = viewModel.getCurrentWeatherMainDescription()
                    if (mainDescription.isNotEmpty()) {
                        Text(
                            text = mainDescription,
                            modifier = Modifier
                                .padding(0.dp)
                                .testTag("mainDescriptionText"),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h6
                        )
                    }

                    val feelsLike = viewModel.getCurrentWeatherFeelsLike()
                    if (feelsLike != null) {
                        Text(
                            text = "Feels like $feelsLike ℃",
                            modifier = Modifier
                                .padding(0.dp)
                                .testTag("feelsLikeText"),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h6
                        )
                    }

                    val low = viewModel.getCurrentWeatherTempMin()
                    val high = viewModel.getCurrentWeatherTempMax()

                    if (low != null && high != null) {
                        Text(
                            text = "High $high ℃ - Low $low ℃",
                            modifier = Modifier
                                .padding(0.dp)
                                .testTag("lowHighText"),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.body1
                        )
                    }

//                    Divider(Modifier.padding(top = 10.dp, bottom = 10.dp),
//                        color = Color.Red,
//                        thickness = 5.dp
//                        )
//                    val desc = viewModel.getState()
//
//                    if (low != null && high != null) {
//                        Text(
//                            fontSize = 18.sp,
//                            text = "$desc",
//                            modifier = Modifier
//                                .padding(0.dp)
//                                .testTag("lowHighText"),
//                            textAlign = TextAlign.Start,
//                            style = MaterialTheme.typography.body1
//                        )
//                    }

                }
                val url = viewModel.getCurrentWeatherIconUrl()
                if(!url.isNullOrEmpty()) {
                    AsyncImage(
                        modifier = Modifier
                            .size(160.dp)
                            .padding(16.dp),
                        model = url,
                        contentDescription = "currentWeatherIcon",
                    )
                }
            }
        }
    }

    when (viewModel.getWeatherDetailNavRouteUiState()) {
        is WeatherDetailViewModelImpl.WeatherDetailNavRouteUi.GoToSearchScreenUi -> {
            navController.navigate(NavItem.Search.nav_route)
            viewModel.resetNavRouteUiToIdle()
        }

        is WeatherDetailViewModelImpl.WeatherDetailNavRouteUi.Idle -> { }
        else -> {}
    }
}
