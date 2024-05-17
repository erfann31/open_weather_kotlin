package com.example.open_weater_kotlin_ui.view.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.open_weater_kotlin_ui.view.ChangeLocation.ChangeLocationScreen
import com.example.open_weater_kotlin_ui.view.HourlyForecast.HourlyForecastScreen
import com.example.open_weater_kotlin_ui.viewModel.WeatherViewModel

@Composable
fun Navigator(viewMode: WeatherViewModel) {
    val navHostController = rememberNavController()

    NavHost(navController = navHostController, startDestination = "hourly_forecast") {
        composable("hourly_forecast") {
            HourlyForecastScreen(navHostController, viewMode)
        }
        composable("change_location") {
            ChangeLocationScreen(navHostController, viewMode)
        }
    }
}