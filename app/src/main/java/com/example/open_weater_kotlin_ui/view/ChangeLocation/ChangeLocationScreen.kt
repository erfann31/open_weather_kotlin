package com.example.open_weater_kotlin_ui.view.ChangeLocation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.open_weater_kotlin_ui.R
import com.example.open_weater_kotlin_ui.view.ChangeLocation.widgets.CityWidget
import com.example.open_weater_kotlin_ui.view.ChangeLocation.widgets.RoundedSearchTextField
import com.example.open_weater_kotlin_ui.viewModel.LocationInfoListener
import com.example.open_weater_kotlin_ui.viewModel.WeatherViewModel

@Composable
fun ChangeLocationScreen(
    navHostController: NavHostController,
    viewModel: WeatherViewModel = viewModel(),
) {
    var isLoading by remember { mutableStateOf(false) }
    viewModel.listener = object : LocationInfoListener {
        override fun onLocationInfoFetched() {
            isLoading = false
            navHostController.navigate("hourly_forecast")
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
        var locationName by remember { mutableStateOf("") }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.padding( 10.dp)
        )
        {
            RoundedSearchTextField(
                placeholder = "Search for a city...",
                text = locationName,
                onSearchClicked = {
                    isLoading = true
                    viewModel.getLocationCoordinates(locationName)
                },
                onTextChanged = { newText ->
                    locationName = newText
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (isLoading) {
                LazyColumn(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
            } else {
                LazyColumn(Modifier.fillMaxSize()) {
                    item {
                        CityWidget(navHostController)
                        CityWidget(navHostController)
                        CityWidget(navHostController)
                        CityWidget(navHostController)

                    }
                    item {
                        CityWidget(navHostController, isAdd = true)

                    }
                }
            }
}
}
}