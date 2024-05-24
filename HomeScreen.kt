package com.example.open_weather_kotlin_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen(navController: NavController) {
    var isCelsius by rememberSaveable { mutableStateOf(true) }
    val currentTemp = 26 // دمای فعلی
    val currentCondition = "Sunny" // وضعیت فعلی هوا

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
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Isfahan",
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Normal,
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.poppins_semibold))
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$currentTemp° $currentCondition",
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.poppins_light))
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("search") },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text(
                text = "Change Location",
                style = TextStyle(
                    color = colorResource(R.color.customBlue),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_semibold))
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(
                onClick = { navController.navigate("hourlyForecast") },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = "Hourly Forecast",
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_semibold))
                    )
                )
            }

            TextButton(
                onClick = { navController.navigate("weeklyForecast") },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = "Weekly Forecast",
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_semibold))
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (isCelsius) "°C" else "°F",
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_semibold))
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Switch(
                checked = isCelsius,
                onCheckedChange = { isCelsius = !isCelsius },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorResource(R.color.customCard),
                    uncheckedThumbColor = Color.Gray
                )
            )
        }
    }
}
