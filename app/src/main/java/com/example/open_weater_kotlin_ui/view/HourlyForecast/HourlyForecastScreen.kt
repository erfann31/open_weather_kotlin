package com.example.open_weater_kotlin_ui.viewModel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.open_weater_kotlin_ui.models.entities.HourlyForecast
import com.example.open_weater_kotlin_ui.models.utils.Util.getGeographicalDirection
import com.example.open_weater_kotlin_ui.models.utils.Util.getHumidityType
import com.example.open_weater_kotlin_ui.models.utils.Util.getStatus
import com.example.open_weater_kotlin_ui.view.HourlyForecast.widgets.GridItems
import com.example.open_weater_kotlin_ui.view.HourlyForecast.widgets.RowItems
import com.example.open_weater_kotlin_ui.viewModel.WeatherViewModel

val selectedItemId = mutableIntStateOf(0)

@Composable
fun HourlyForecastScreen(
    viewModel: WeatherViewModel = viewModel(),
) {
    val hourlyForecast by viewModel.hourlyForecast.observeAsState()
    if (hourlyForecast == null) {
        // نمایش پیام Loading
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = colorResource(R.color.customCard))
        }
    } else {
        val hourlyForecasts: List<HourlyForecast>? = hourlyForecast?.list
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
        val box1 = mutableMapOf<String, Any?>(
            "title" to " Real Feel",
            "icon" to R.drawable.temperature, // Use resource ID directly
            "txt1" to "${"%.1f".format(selectedItem?.main?.feelsLike)}°",
            "txt2" to "${"%.1f".format(selectedItem?.main?.tempMax)}° / ${"%.1f".format(selectedItem?.main?.tempMin)}°"
        )

        val box2 = mutableMapOf<String, Any?>(
            "title" to "Humidity",
            "icon" to R.drawable.humidity, // Use resource ID directly
            "txt1" to "${selectedItem?.main?.humidity}%",
            "txt2" to selectedItem?.main?.humidity?.let { getHumidityType(it.toInt()) }
        )

        val box3 = mutableMapOf<String, Any?>(
            "title" to "Wind",
            "icon" to R.drawable.wind, // Use resource ID directly
            "txt1" to "${"%.1f".format(selectedItem?.wind?.speed)} km/h",
            "txt2" to "to ${selectedItem?.wind?.deg?.let { getGeographicalDirection(it) }}"
        )

        val box4 = mutableMapOf<String, Any?>(
            "title" to " Cloud Cover",
            "icon" to R.drawable.cloud, // Use resource ID directly
            "txt1" to "${selectedItem?.clouds?.all}%",
            "txt2" to selectedItem?.weather?.getOrNull(0)?.description
        )

        val boxList = mutableListOf(box1, box2, box3, box4)

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
                hourlyForecast?.city?.name?.let {
                    Text(
                        text = it,
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Normal,
                            fontSize = 30.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_semibold))
                        )
                    )
                }
                Row(horizontalArrangement = Arrangement.SpaceBetween)
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

                TextButton(onClick = { /*TODO*/ }) {
                    Icon(
                        modifier = Modifier
                            .scale(0.8f),
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
                modifier = Modifier
                    .padding(10.dp)
            ) {
                items(items = hourlyForecasts ?: emptyList()) { items ->
                    if (hourlyForecasts != null) {
                        RowItems(items, hourlyForecasts.indexOf(items))
                    }
                }

            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), modifier = Modifier
                    .padding(10.dp)
            ) {
                items(boxList) { item ->
                    GridItems(item = item)
                }
            }

        }
    }

}

