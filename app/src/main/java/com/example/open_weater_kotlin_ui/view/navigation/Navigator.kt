package com.example.open_weater_kotlin_ui.view.navigation


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.open_weater_kotlin_ui.view.ChangeLocation.ChangeLocationScreen
import com.example.open_weater_kotlin_ui.view.HourlyForecast.HourlyForecastScreen
import com.example.open_weater_kotlin_ui.view.WeeklyForecast.WeeklyForecastScreen
import com.example.open_weater_kotlin_ui.viewModel.WeatherViewModel

@RequiresApi(Build.VERSION_CODES.O)
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
        composable("weekly_forecast") {
            WeeklyForecastScreen(navHostController, viewMode)
        }
    }
}