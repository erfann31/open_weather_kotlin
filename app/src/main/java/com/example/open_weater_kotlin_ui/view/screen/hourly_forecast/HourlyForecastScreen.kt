package com.example.open_weater_kotlin_ui.view.screen.hourly_forecast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.open_weater_kotlin_ui.R
import com.example.open_weater_kotlin_ui.model.entities.HourlyForecast
import com.example.open_weater_kotlin_ui.model.utils.Convertor.getGeographicalDirection
import com.example.open_weater_kotlin_ui.model.utils.Convertor.getHumidityType
import com.example.open_weater_kotlin_ui.model.utils.Convertor.getStatus
import com.example.open_weater_kotlin_ui.view.screen.hourly_forecast.widgets.GridItems
import com.example.open_weater_kotlin_ui.view.screen.hourly_forecast.widgets.RowItems
import com.example.open_weater_kotlin_ui.view.theme.GradientBackground
import com.example.open_weater_kotlin_ui.view_model.WeatherViewModel

/**
 * A mutable state integer representing the ID of the selected item in the hourly forecast list.
 * This is used to track and update the currently selected forecast item.
 */
val selectedItemId = mutableIntStateOf(0)


/**
 * This composable function displays the hourly forecast screen with the weather data.
 * It shows the current weather, hourly forecast, and detailed information for a selected item.
 *
 * @param navHostController The NavHostController to navigate between different screens.
 * @param viewModel The WeatherViewModel to observe the weather data.
 *
 * @author Motahare Vakili
 */

@Composable
fun HourlyForecastScreen(
    navHostController: NavHostController,
    viewModel: WeatherViewModel = viewModel(),
) {
    val isMetric = viewModel.isMetric.value
    val temp = remember {
        mutableStateOf(
            if (isMetric) {
                "℃"
            } else {
                "°F"
            }
        )
    }
    val windSpeed = remember {
        mutableStateOf(
            if (isMetric) {
                "km/h"
            } else {
                "mph"
            }
        )
    }
    val hourlyForecast by viewModel.hourlyForecast.observeAsState()
    if (hourlyForecast == null) {
        // Display loading message
        GradientBackground {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    } else {
        val hourlyForecasts: List<HourlyForecast>? = hourlyForecast?.list

        /**
         * A mutable state representing the currently selected hourly forecast item.
         * It is remembered and updated whenever the `selectedItemId` changes.
         */
        var selectedItem by remember(selectedItemId) { mutableStateOf<HourlyForecast?>(null) }

        LaunchedEffect(selectedItemId.intValue) {
            hourlyForecasts?.let { forecasts ->
                selectedItem = forecasts.getOrElse(selectedItemId.intValue) { null }
            }
        }
        LaunchedEffect(hourlyForecasts, selectedItemId.intValue) {
            hourlyForecasts?.let { forecasts ->
                if (selectedItemId.intValue >= 0 && selectedItemId.intValue < forecasts.size) {
                    selectedItem = forecasts[selectedItemId.intValue]
                }
            }
        }

        /**
         * A map containing information about the real feel temperature.
         * Keys and values:
         * - "title": The title of the box, which is "Real Feel".
         * - "icon": The resource ID for the temperature icon.
         * - "txt1": The formatted real feel temperature.
         * - "txt2": The formatted maximum and minimum temperatures.
         * - "txt3": The temperature unit (°C or °F).
         */
        val box1 = mutableMapOf<String, Any?>(
            "title" to "Real Feel",
            "icon" to R.drawable.temperature, // Use resource ID directly
            "txt1" to "%.1f".format(selectedItem?.main?.feelsLike),
            "txt2" to "${"%.1f".format(selectedItem?.main?.tempMax)}° / ${"%.1f".format(selectedItem?.main?.tempMin)}°",
            "txt3" to " ${temp.value}"
        )

        /**
         * A map containing information about the humidity.
         * Keys and values:
         * - "title": The title of the box, which is "Humidity".
         * - "icon": The resource ID for the humidity icon.
         * - "txt1": The humidity value as a string.
         * - "txt2": The humidity type description.
         */
        val box2 = mutableMapOf<String, Any?>(
            "title" to "Humidity",
            "icon" to R.drawable.humidity, // Use resource ID directly
            "txt1" to selectedItem?.main?.humidity.toString(),
            "txt2" to selectedItem?.main?.humidity?.let { getHumidityType(it.toInt()) },
            "txt3" to " %"
        )

        /**
         * A map containing information about the wind.
         * Keys and values:
         * - "title": The title of the box, which is "Wind".
         * - "icon": The resource ID for the wind icon.
         * - "txt1": The formatted wind speed.
         * - "txt2": The geographical direction of the wind.
         * - "txt3": The wind speed unit (km/h or mph).
         */
        val box3 = mutableMapOf<String, Any?>(
            "title" to "Wind",
            "icon" to R.drawable.wind, // Use resource ID directly
            "txt1" to "%.1f".format(selectedItem?.wind?.speed),
            "txt2" to "To ${selectedItem?.wind?.deg?.let { getGeographicalDirection(it) }}",
            "txt3" to " ${windSpeed.value}"
        )

        /**
         * A map containing information about the cloud cover.
         * - "title": The title of the box, which is "Cloud Cover".
         * - "icon": The resource ID for the cloud icon.
         * - "txt1": The cloud cover percentage as a string.
         * - "txt2": The description of the weather.
         */
        val box4 = mutableMapOf<String, Any?>(
            "title" to "Cloud Cover",
            "icon" to R.drawable.cloud, // Use resource ID directly
            "txt1" to selectedItem?.clouds?.all.toString(),
            "txt2" to selectedItem?.weather?.getOrNull(0)?.description,
            "txt3" to " %"
        )

        val boxList = mutableListOf(box1, box2, box3, box4)

        Box(
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
                .padding(top = 6.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.padding(10.dp)
                )
                {
                    hourlyForecast?.city?.name?.let {
                        Text(
                            modifier = Modifier.padding(horizontal = 60.dp),
                            text = it,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                fontWeight = FontWeight.Normal,
                                fontSize = 30.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_semibold))
                            )
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom)
                    {
                        Text(
                            text = "${hourlyForecasts?.get(0)?.main?.temp?.toInt().toString()}°",
                            style = TextStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_light))
                            )
                        )
                        Text(
                            text = if (temp.value == "℃") "c" else "F",
                            style = TextStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_light))
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        val id = hourlyForecasts?.get(0)?.weather?.get(0)?.id?.toInt()
                        if (id != null) {
                            Text(
                                text = if (id == 800) "Clear" else getStatus(id / 100),
                                style = TextStyle(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_light))
                                )
                            )
                        }
                    }

                    TextButton(onClick = { navHostController.navigate("change_location") }) {
                        Icon(
                            modifier = Modifier.scale(0.8f),
                            painter = painterResource(id = R.drawable.location),
                            contentDescription = null,
                            tint = colorResource(id = R.color.customBlue)
                        )

                        Text(
                            text = "Change Location",
                            style = TextStyle(
                                color = colorResource(id = R.color.customBlue),
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_semibold))
                            )
                        )
                    }
                }


                    LazyRow(
                        modifier = Modifier.padding(vertical = 10.dp)
                    ) {
                        item { Spacer(modifier = Modifier.width(10.dp)) }
                        items(items = hourlyForecasts ?: emptyList()) { items ->
                            if (hourlyForecasts != null) {
                                RowItems(items, hourlyForecasts.indexOf(items), viewModel)
                            }
                        }
                        item { Spacer(modifier = Modifier.width(10.dp)) }
                    }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.padding(10.dp),
                ) {
                    items(boxList) { item ->
                        GridItems(item = item)
                    }
                }
            }

            IconButton(modifier = Modifier.padding(start = 12.dp, top = 12.dp), onClick = { navHostController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )

            }
        }
    }
}


