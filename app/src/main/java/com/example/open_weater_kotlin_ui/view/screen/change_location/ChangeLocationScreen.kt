package com.example.open_weater_kotlin_ui.view.screen.change_location

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.open_weater_kotlin_ui.R
import com.example.open_weater_kotlin_ui.model.Intent.goToMap
import com.example.open_weater_kotlin_ui.model.utils.LogFileObserver
import com.example.open_weater_kotlin_ui.model.utils.readLogsFromFile
import com.example.open_weater_kotlin_ui.view.screen.change_location.widgets.CityWidget
import com.example.open_weater_kotlin_ui.view.screen.change_location.widgets.RoundedTextField
import com.example.open_weater_kotlin_ui.view_model.WeatherViewModel
import com.example.open_weater_kotlin_ui.view_model.lisener.LocationInfoListener
import kotlinx.coroutines.delay

/**
 * A composable function that displays the screen for changing the location in the weather app.
 * The screen includes a search bar, a list of searched locations, and saved locations.
 *
 * @param navHostController The NavHostController for navigating between screens.
 * @param viewModel The WeatherViewModel providing the weather data and handling user interactions.
 *
 * @author Erfan Nasri
 */

@Composable
fun ChangeLocationScreen(
    navHostController: NavHostController,
    viewModel: WeatherViewModel = viewModel(),
) {
    val context = LocalContext.current
    val logs = remember { mutableStateOf(readLogsFromFile(context)) }
    val locationsName by viewModel.locationsName.observeAsState(emptyList())
    var locationName by remember { mutableStateOf("") }

    val observer = remember {
        LogFileObserver(context) {
            logs.value = readLogsFromFile(context)
        }
    }

    DisposableEffect(context) {
        observer.startWatching()
        onDispose {
            observer.stopWatching()
        }
    }
    var isLoading by remember { mutableStateOf(false) }

    /**
     * Sets a listener for location info fetching events. When the location info is fetched,
     * loading is stopped and the navigation to the "hourly_forecast" screen is triggered.
     */
    viewModel.listener = object : LocationInfoListener {
        override fun onLocationInfoFetched() {
            isLoading = false
            navHostController.navigate("hourly_forecast")
        }
    }


    /**
     * Launches an effect that checks if a loading operation is in progress. If it takes longer than 5 seconds,
     * a timeout message is displayed and loading is stopped.
     */
    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(5000)
            if (isLoading) {
                Toast.makeText(context, "Request timed out. Redirecting...", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        colorResource(R.color.customCyan),
                        colorResource(R.color.customBlue)
                    )
                )
            )
            .padding(top = 6.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.padding(10.dp)
        )
        {
            RoundedTextField(
                textColor = colorResource(R.color.customBlue),
                cursurColor = colorResource(R.color.customBlue),
                containerColor = Color.White,
                borderColor = Color.Transparent,
                isRounded = true,
                placeholder = "Search for a city...",
                text = locationName,
                onSearchClicked = {
                    isLoading = true
                    viewModel.getLocationCoordinates(locationName)
                },
                onTextChanged = { newText ->
                    locationName = newText
                    viewModel.updateLocationName(locationName)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(horizontal = 12.dp),
                showIcon = true,
                textSize = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(Modifier.fillMaxSize()) {
                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(top = 30.dp),
                            contentAlignment = Alignment.Center,

                        ) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.height(30.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                } else {
                    items(locationsName) { cityName ->
                        CityWidget(text = cityName,
                            onClick = {
                                isLoading = true
                                viewModel.getLocationCoordinates(cityName)
                            },
                            isAdd = true,
                            iconOnClick = {
                                viewModel.addCityToFile(context, cityName)
                            }
                        )
                    }
                    item {
                        if (locationsName.isNotEmpty()) {
                            Divider(color = Color.White)
                            Spacer(Modifier.height(10.dp))
                        }
                    }
                    items(logs.value) { cityName ->
                        CityWidget(
                            isEdit = true, text = cityName,
                            iconOnClick = {
                                viewModel.deleteCityFromFile(context, cityName)
                            },
                            onClick = {
                                isLoading = true
                                viewModel.getLocationCoordinates(cityName)
                            },
                        )
                    }
                    item {
                        if (logs.value.isNotEmpty()) {
                            Divider(color = Color.White)
                            Spacer(Modifier.height(10.dp))
                        }
                    }
                    item {
                        TextButton(modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .fillMaxWidth(), onClick = {
                            goToMap(
                                viewModel,
                                locationName,
                                context,
                            )
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.location),
                                contentDescription = "go_to_map",
                                tint = colorResource(id = R.color.white),
                                modifier = Modifier.scale(0.9f),
                            )
                            Text(
                                text = " Go to Map",
                                style = TextStyle(
                                    color = colorResource(id = R.color.white),
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_semibold))
                                )
                            )
                        }
                    }
                    item {
                        //todo navigate to home
                        TextButton(modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .fillMaxWidth(), onClick = {
                            navHostController.navigate("weekly_forecast")
                        })
                        {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "go_to_home",
                                tint = colorResource(id = R.color.white),
                                modifier = Modifier.scale(0.9f),
                            )
                            Text(
                                text = " Go to Home",
                                style = TextStyle(
                                    color = colorResource(id = R.color.white),
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_semibold))
                                )
                            )
                        }
                    }
                    //todo remove
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Metric", style = TextStyle(color = Color.White, fontSize = 18.sp)
                            )
                            Switch(checked = !viewModel.isMetric.value, onCheckedChange = {
                                isLoading = true
                                viewModel.toggleUnit()
                            })
                            Text(
                                text = "Imperial", style = TextStyle(color = Color.White, fontSize = 18.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}
