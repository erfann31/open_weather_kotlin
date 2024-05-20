package com.example.open_weater_kotlin_ui.view.screen.weekly_forecast

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.open_weater_kotlin_ui.model.entities.DailyForecast
import com.example.open_weater_kotlin_ui.model.utils.Convertor.getGeographicalDirection
import com.example.open_weater_kotlin_ui.model.utils.Convertor.getHumidityType
import com.example.open_weater_kotlin_ui.model.utils.Convertor.getStatus
import com.example.open_weater_kotlin_ui.view.screen.hourly_forecast.widgets.GridItems
import com.example.open_weater_kotlin_ui.view.screen.weekly_forecast.widgets.RowItems
import com.example.open_weater_kotlin_ui.view.theme.GradientBackground
import com.example.open_weater_kotlin_ui.view_model.WeatherViewModel

val w_selectedItemId = mutableIntStateOf(0)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyForecastScreen(
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

    val dailyForecast by viewModel.dailyForecast.observeAsState()

    if (dailyForecast == null) {
        // نمایش پیام Loading
        GradientBackground {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    } else {
        val dailyForecasts: List<DailyForecast>? = dailyForecast?.list
        var w_selectedItem by remember(w_selectedItemId) { mutableStateOf<DailyForecast?>(null) }

        LaunchedEffect(w_selectedItemId.intValue) {
            dailyForecasts?.let { forecasts ->
                w_selectedItem = forecasts.getOrElse(w_selectedItemId.intValue) { null }
            }
        }
        LaunchedEffect(dailyForecasts, w_selectedItemId.intValue) {
            dailyForecasts?.let { forecasts ->
                if (w_selectedItemId.intValue >= 0 && w_selectedItemId.intValue < forecasts.size) {
                    w_selectedItem = forecasts[w_selectedItemId.intValue]
                }
            }
        }
        val w_box1 = mutableMapOf<String, Any?>(
            "title" to " Real Feel",
            "icon" to R.drawable.temperature, // Use resource ID directly
            "txt1" to "%.1f".format(w_selectedItem?.feelsLike?.day),
            "txt2" to "${"%.1f".format(w_selectedItem?.temp?.max)}° / ${"%.1f".format(w_selectedItem?.temp?.min)}°",
            "txt3" to " ${temp.value}"
        )

        val w_box2 = mutableMapOf<String, Any?>(
            "title" to "Humidity",
            "icon" to R.drawable.humidity, // Use resource ID directly
            "txt1" to w_selectedItem?.humidity.toString(),
            "txt2" to w_selectedItem?.humidity?.let { getHumidityType(it.toInt()) },
            "txt3" to " %"
        )

        val w_box3 = mutableMapOf<String, Any?>(
            "title" to "Wind",
            "icon" to R.drawable.wind, // Use resource ID directly
            "txt1" to "%.1f".format(w_selectedItem?.speed),
            "txt2" to "To ${w_selectedItem?.deg?.let { getGeographicalDirection(it) }}",
            "txt3" to " ${windSpeed.value}"
        )

        val w_box4 = mutableMapOf<String, Any?>(
            "title" to " Cloud Cover",
            "icon" to R.drawable.cloud, // Use resource ID directly
            "txt1" to w_selectedItem?.clouds.toString(),
            "txt2" to w_selectedItem?.weather?.getOrNull(0)?.description,
            "txt3" to " %"
        )

        val w_boxList = mutableListOf(w_box1, w_box2, w_box3, w_box4)

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
                .padding(top = 6.dp),
        )
        {
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
                    dailyForecast?.city?.name?.let {
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
                        verticalAlignment = Alignment.Bottom,
                    )
                    {
                        Text(
                            text = "${dailyForecasts?.get(0)?.temp?.day?.toInt().toString()}°",
                            style = TextStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_light))
                            )
                        )

                        Text(
                            text = if (temp.value == "℃") "c" else "F", style = TextStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_light))
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        val id = dailyForecasts?.get(0)?.weather?.get(0)?.id?.toInt()
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
                    modifier = Modifier.padding(10.dp)
                ) {
                    items(items = dailyForecasts ?: emptyList()) { items ->
                        if (dailyForecasts != null) {
                            RowItems(
                                item = items,
                                index = dailyForecasts.indexOf(items),
                                viewModel,
                            )
                        }
                    }

                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.padding(10.dp),
                ) {
                    items(w_boxList) { item ->
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

