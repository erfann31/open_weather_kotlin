package com.example.open_weater_kotlin_ui.view.screen.home_screen

import RoundedButton
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.open_weater_kotlin_ui.R
import com.example.open_weater_kotlin_ui.model.entities.DailyForecast
import com.example.open_weater_kotlin_ui.model.utils.Convertor
import com.example.open_weater_kotlin_ui.view_model.WeatherViewModel
import java.util.Locale

/**
 * Composable function for the Home Screen.
 *
 * This function displays the main screen of the weather application.
 * It includes the current weather information and buttons for navigating to hourly and weekly forecasts.
 * Additionally, it features a switch for toggling between metric and imperial temperature units.
 *
 * @param navHostController The navigation controller used for navigating between screens.
 * @param viewModel The view model containing the data and logic for the Home Screen.
 *
 * @author Motahare Vakili & Erfan Nasri
 */
@Composable
fun HomeScreen(
    navHostController: NavHostController, viewModel: WeatherViewModel
) {
    val isLoading by viewModel.isLoading.observeAsState(true)
    val tempUnit by viewModel.tempUnit
    val context = LocalContext.current

    val current by viewModel.currentWeather.observeAsState()
    val dailyForecast by viewModel.dailyForecast.observeAsState()
    val error by viewModel.error.observeAsState()

    if (error != null) {
        Toast.makeText(context, "An error occurred: $error", Toast.LENGTH_SHORT).show()
    }

    val dailyForecasts: List<DailyForecast>? = dailyForecast?.list
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        colorResource(R.color.customCyan), colorResource(R.color.customBlue)
                    )
                )
            )
            .padding(top = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                item {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceEvenly,
                            ) {
                                current?.name?.let {
                                    Text(
                                        text = it,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        style = TextStyle(
                                            textAlign = TextAlign.Center,
                                            color = Color.White,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 36.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins))
                                        )
                                    )
                                }
                                Row(
                                    horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top)
                                {
                                    Text(
                                        modifier = Modifier.padding(start = 24.dp),
                                        text = current?.main?.temp?.toInt().toString(),
                                        style = TextStyle(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 76.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins_light))
                                        )
                                    )
                                    Text(
                                        modifier = Modifier.padding(top = 14.dp),
                                        text = tempUnit,
                                        style = TextStyle(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 28.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins_light)),

                                        )
                                    )
                                }
                                val id = current?.weather?.get(0)?.id?.toInt()
                                if (id != null) {
                                    Text(
                                        text = if (id == 800) "Clear" else Convertor.getStatus(id / 100), style = TextStyle(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 24.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins_light)),
                                        )
                                    )
                                }
                                Row(
                                    horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom
                                ) {
                                    Text(
                                        text = "High : ${String.format(Locale.ENGLISH, "%.1f", dailyForecasts?.get(0)?.temp?.max)}°",
                                        style = TextStyle(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins_light))
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Text(
                                        text = "Low : ${String.format(Locale.ENGLISH, "%.1f", dailyForecasts?.get(0)?.temp?.min)}°",
                                        style = TextStyle(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins_light))
                                        )
                                    )
                                }
                                TextButton(onClick = { navHostController.navigate("change_location") }) {
                                    Icon(
                                        modifier = Modifier.scale(0.8f),
                                        painter = painterResource(id = R.drawable.location),
                                        contentDescription = null,
                                        tint = colorResource(id = R.color.customBlue)
                                    )

                                    Text(
                                        text = "Change Location", style = TextStyle(
                                            color = colorResource(id = R.color.customBlue),
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins_semibold))
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                RoundedButton(onClick = { navHostController.navigate("hourly_forecast") }, text = {
                    Text(
                        text = "Hourly Forecast", style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_semibold))
                        )
                    )
                }, item = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "go_to_hourly_forecast_screen",
                        Modifier.size(35.dp),
                        tint = Color.White
                    )
                })
                Spacer(modifier = Modifier.height(20.dp))
                RoundedButton(onClick = { navHostController.navigate("weekly_forecast") }, text = {
                    Text(
                        text = "Weekly Forecast", style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_semibold))
                        )
                    )
                }, item = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "go_to_hourly_forecast_screen",
                        Modifier.size(35.dp),
                        tint = Color.White
                    )
                })
                Spacer(modifier = Modifier.height(20.dp))
                RoundedButton(onClick = {
                    viewModel.toggleUnit()
                }, text = {
                    Row {
                        Text(
                            text = "Metric Switch", style = TextStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_semibold))
                            )
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "℃ / °F", style = TextStyle(
                                color = colorResource(R.color.unitColor),
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_semibold))
                            )
                        )
                    }
                }, item = {

                        Switch(
                            modifier = Modifier.scale(scaleY = 0.9f, scaleX = 1f),
                            checked = !viewModel.isMetric.value, onCheckedChange = {
                                viewModel.toggleUnit()
                            }, colors = SwitchDefaults.colors(
                                uncheckedBorderColor = Color.Gray,
                                checkedThumbColor = Color.White,
                                uncheckedThumbColor = Color.LightGray,
                                uncheckedTrackColor = Color.Gray,
                                checkedTrackColor = colorResource(R.color.customBlue)
                            ),

                        )

                }
                )
            }
        }

    }
}