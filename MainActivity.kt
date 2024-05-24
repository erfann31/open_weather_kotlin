package com.example.open_weather_kotlin_ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.open_weather_kotlin_ui.ui.theme.OpenWeatherKotlinUITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenWeatherKotlinUITheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") { HomeScreen(navController) }
                        composable("hourlyForecast") { HourlyForecast(navController) }
                        composable("weeklyForecast") { WeeklyForecast(navController) }
                        composable("search") { SearchScreen(navController) }
                    }
                }
            }
        }
    }
}
