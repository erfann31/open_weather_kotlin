package com.example.open_weater_kotlin_ui.view.screen.home_screen

import RoundedButton
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.open_weater_kotlin_ui.R
import com.example.open_weater_kotlin_ui.model.entities.DailyForecast
import com.example.open_weater_kotlin_ui.model.entities.HourlyForecast
import com.example.open_weater_kotlin_ui.model.utils.Convertor
import com.example.open_weater_kotlin_ui.model.utils.Convertor.getGeographicalDirection
import com.example.open_weater_kotlin_ui.view.screen.hourly_forecast.selectedItemId
import com.example.open_weater_kotlin_ui.view.screen.hourly_forecast.widgets.GridItems
import com.example.open_weater_kotlin_ui.view.screen.hourly_forecast.widgets.RowItems
import com.example.open_weater_kotlin_ui.view.theme.GradientBackground
import com.example.open_weater_kotlin_ui.view_model.WeatherViewModel
import java.util.Locale

@Composable
fun HomeScreen(navHostController: NavHostController
    ,viewModel: WeatherViewModel) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val isLoading by viewModel.isLoading.observeAsState(true)
    val isMetric = viewModel.isMetric.value
    val temp = remember {
        mutableStateOf(
            if (isMetric) {
                "℃"
            } else {
                "°F"
            }
        )
    }
    val context = LocalContext.current

    val current by viewModel.currentWeather.observeAsState()
    val dailyForecast by viewModel.dailyForecast.observeAsState()
    val error by viewModel.error.observeAsState()

    if (error != null) {
        Toast.makeText(context, "An error occurred: $error", Toast.LENGTH_SHORT).show()
    }
    if (isLoading) {
        // Display loading indicator
        GradientBackground {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    } else {
        val dailyForecasts: List<DailyForecast>? = dailyForecast?.list
        Box(
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
                .padding(top = 6.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.padding(10.dp)
                        ) {
                            current?.name?.let {
                                Text(
                                    modifier = Modifier.padding(top = 60.dp),
                                    text = it,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    style = TextStyle(
                                        textAlign = TextAlign.Center,
                                        color = Color.White,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 36.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_semibold))
                                    )
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${current?.main?.temp?.toInt().toString()}°",
                                    style = TextStyle(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 52.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_light))
                                    )
                                )
                                Text(
                                    text = if (temp.value == "℃") "c" else "F",
                                    style = TextStyle(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 40.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_light))
                                    )
                                )


                            }
                            Spacer(modifier = Modifier.width(8.dp))

                            val id =current?.weather?.get(0)?.id?.toInt()
                            if (id != null) {
                                Text(
                                    text = if (id == 800) "Clear" else Convertor.getStatus(id / 100),
                                    style = TextStyle(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_light))
                                    )
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    text = "H : ${String.format(Locale.ENGLISH, "%.1f", dailyForecasts?.get(0)?.temp?.max)}°",
                                    style = TextStyle(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 22.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_light))
                                    )
                                )
                                Spacer(modifier = Modifier.width(14.dp))
                                Text(
                                    text =  "L : ${String.format(Locale.ENGLISH, "%.1f", dailyForecasts?.get(0)?.temp?.min)}°",
                                    style = TextStyle(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 22.sp,
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
                                    text = "Change Location",
                                    style = TextStyle(
                                        color = colorResource(id = R.color.customBlue),
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_semibold))
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(150.dp))
                        RoundedButton(onClick = { navHostController.navigate("hourly_forecast") }, text = {
                            Text(
                                text = "Hourly Forecast", style = TextStyle(
                                    color = Color.White,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_semibold))
                                )
                            )
                        },
                            item = {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "go_to_hourly_forecast_screen",
                                    Modifier.size(35.dp)
                                )
                            }
                        )



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
                        },
                            item = {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "go_to_hourly_forecast_screen",
                                    Modifier.size(35.dp)
                                )
                            }
                        )
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
                                    text = "℃ / °F",
                                    style = TextStyle(
                                        color = colorResource(R.color.unitColor),
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_semibold))
                                    )
                                )
                            }
                        },
                            item = {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                    )
                                } else {
                                    Switch(
                                        checked = !viewModel.isMetric.value, onCheckedChange = {
                                            viewModel.toggleUnit()
                                        },
                                        colors = SwitchDefaults.colors(
                                            uncheckedBorderColor =Color.Gray ,
                                            checkedBorderColor =colorResource(R.color.customCard) ,
                                            checkedThumbColor = Color.White,
                                            uncheckedThumbColor = Color.LightGray,
                                            uncheckedTrackColor = Color.Gray,
                                            checkedTrackColor = colorResource(R.color.customCard)
                                        )
                                    )
                                }
                            }
                        )

                    }
                }

            }


        }


    }
}
