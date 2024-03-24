package com.example.open_weater_kotlin_uiui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.open_weater_kotlin_uiui.theme.WeatherAndroid2023Theme

@Composable
fun WeatherUi() {
    val navController = rememberNavController()
    WeatherAndroid2023Theme {
        Scaffold { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {

                NavigationGraph(navController = navController)
            }
        }
    }
}
