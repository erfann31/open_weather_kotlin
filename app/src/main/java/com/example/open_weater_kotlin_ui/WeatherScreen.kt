package com.example.open_weater_kotlin_ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.open_weater_kotlin_ui.viewModel.WeatherViewModel

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = viewModel()
) {
    val locationName = rememberSaveable { mutableStateOf("") }
    val locationCoordinates by viewModel.locationCoordinates.observeAsState(emptyList())
    val currentWeather by viewModel.currentWeather.observeAsState()
    val dailyForecast by viewModel.dailyForecast.observeAsState()
    val hourlyForecast by viewModel.hourlyForecast.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp,top=16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TextField(
            value = locationName.value,
            onValueChange = { locationName.value = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter location name") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.getLocationCoordinates(locationName.value) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Get Weather")
        }

        if (locationCoordinates.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Divider(modifier = Modifier.height(2.dp))
            Text("Location Coordinates:", modifier = Modifier.padding(top = 10.dp))
            locationCoordinates.forEach { coordinate ->
                Text("${coordinate.name}, ${coordinate.country} (${coordinate.lat}, ${coordinate.lon})")
            }
        }

        if (currentWeather != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Divider(modifier = Modifier.height(2.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Current Weather:")
            Text(text = "Temperature: ${currentWeather!!.main?.temp}°C")
            Text(text = "Description: ${currentWeather!!.weather?.firstOrNull()?.description ?: "N/A"}")
        }

        if (dailyForecast != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Divider(modifier = Modifier.height(2.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Daily Forecast:")
            dailyForecast!!.list?.take(16)?.forEach { forecast ->
                Text("${forecast.temp!!.day} - ${forecast.weather?.firstOrNull()?.description}")
            }
        }

        if (hourlyForecast != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Divider(modifier = Modifier.height(2.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Hourly Forecast:")
            hourlyForecast!!.list?.take(10)?.forEach { forecast ->
                Text("${forecast.dateTimeText} - ${forecast.weather?.firstOrNull()?.description}")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

    }
}