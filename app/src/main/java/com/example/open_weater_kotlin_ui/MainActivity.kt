package com.example.open_weater_kotlin_ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import com.example.open_weater_kotlin_ui.utils.RetrofitInstance
import com.example.open_weater_kotlin_ui.viewModel.WeatherRepository
import com.example.open_weater_kotlin_ui.viewModel.WeatherViewModel
import com.example.open_weater_kotlin_ui.viewModel.WeatherViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp {
                val apiInterface = RetrofitInstance.api
                val weatherRepository = WeatherRepository(apiInterface)
                val factory = WeatherViewModelFactory(weatherRepository)
                val viewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]
                WeatherScreen(viewModel)
            }
        }
    }
}

@Composable
fun WeatherApp(content: @Composable () -> Unit) {
    MaterialTheme {
        Surface {
            content()
        }
    }
}
