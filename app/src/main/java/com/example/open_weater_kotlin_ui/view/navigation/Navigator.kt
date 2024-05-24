package com.example.open_weater_kotlin_ui.view.navigation


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.open_weater_kotlin_ui.view.screen.change_location.ChangeLocationScreen
import com.example.open_weater_kotlin_ui.view.screen.home_screen.HomeScreen
import com.example.open_weater_kotlin_ui.view.screen.hourly_forecast.HourlyForecastScreen
import com.example.open_weater_kotlin_ui.view.screen.weekly_forecast.WeeklyForecastScreen
import com.example.open_weater_kotlin_ui.view_model.WeatherViewModel

/**
 * A composable function that sets up the navigation for the weather app.
 *
 * @param viewMode The ViewModel used to manage weather-related data and state.
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigator(viewMode: WeatherViewModel) {
    val navHostController = rememberNavController()

    NavHost(navController = navHostController, startDestination = "home_screen") {
        composable("hourly_forecast") {
            HourlyForecastScreen(navHostController, viewMode)
        }
        composable("home_screen") {
            HomeScreen(navHostController, viewMode)
        }
        composable("change_location") {
            ChangeLocationScreen(navHostController, viewMode)
        }
        composable("weekly_forecast") {
            WeeklyForecastScreen(navHostController, viewMode)
        }
    }
}