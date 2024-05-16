package com.example.open_weater_kotlin_ui.view.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.open_weater_kotlin_ui.HourlyForecastScreen
import com.example.open_weater_kotlin_ui.viewModel.WeatherViewModel

@Composable
fun Navigator(viewMode: WeatherViewModel) {
    val navHostController = rememberNavController()

    NavHost(navController = navHostController, startDestination = "hourly_forecast") {
        composable("hourly_forecast") {
            HourlyForecastScreen(navHostController,viewMode)
        }
    }
}