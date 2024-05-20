package com.example.open_weater_kotlin_ui.view.screen.weekly_forecast.widgets

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.open_weater_kotlin_ui.R
import com.example.open_weater_kotlin_ui.model.entities.DailyForecast
import com.example.open_weater_kotlin_ui.model.utils.Convertor
import com.example.open_weater_kotlin_ui.model.utils.Convertor.convertMillisToDate
import com.example.open_weater_kotlin_ui.model.utils.Convertor.getDayOfWeek
import com.example.open_weater_kotlin_ui.view.screen.weekly_forecast.w_selectedItemId
import com.example.open_weater_kotlin_ui.view_model.WeatherViewModel

/**
 * A composable function that displays a card for each day's weather forecast.
 * Each card shows the day of the week, date, weather icon, and temperature.
 * The card's background color changes based on whether it is selected.
 *
 * @param item The DailyForecast object containing the weather data for a specific day.
 * @param index The index of the current item in the list.
 * @param viewModel The WeatherViewModel providing the weather data and metric settings.
 *
 * @author Motahare Vakili
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RowItems(item: DailyForecast, index: Int, viewModel: WeatherViewModel) {
    // Metric unit setting based on the viewModel
    val metric = remember {
        mutableStateOf(
            if (viewModel.isMetric.value) {
                "℃"
            } else {
                "°F"
            }
        )
    }

    // Interaction source for the clickable card
    val interactionSource = remember { MutableInteractionSource() }

    // Check if the current item is selected
    val isSelected = w_selectedItemId.intValue == index

    // Convert the timestamp to a date string
    val date = item.dt?.let { convertMillisToDate(it) }

    // Card to display the weather forecast
    Card(
        modifier = Modifier
            .padding(6.dp)
            .height(200.dp)
            .width(70.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { if (!isSelected) w_selectedItemId.intValue = index },
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.White else colorResource(R.color.customCard)
        ),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Display the day of the week
                Text(
                    text = getDayOfWeek(date!!),
                    style = TextStyle(
                        color = if (isSelected) colorResource(R.color.customCard) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold))
                    )
                )

                // Display the date
                Text(
                    text = "${
                        if (date[5] == '0') date.substring(6, 7) else date.substring(6, 7)
                    }  \u0337${
                        if (date[8] == '0') date.substring(9, 10) else date.substring(8, 10)
                    }",
                    style = TextStyle(
                        color = if (isSelected) colorResource(R.color.customCard) else Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_light))
                    )
                )
            }

            // Display the weather icon
            Icon(
                modifier = Modifier.scale(1.8f),
                painter = Convertor.getResourceId_weekly(
                    item.weather?.get(0)?.icon.toString()
                ),
                contentDescription = "icon",
                tint = if (isSelected) colorResource(R.color.customCard) else Color.White
            )

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Display the temperature
                Text(
                    text = "${item.temp?.day?.toInt()}°",
                    style = TextStyle(
                        color = if (isSelected) colorResource(R.color.customCard) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold))
                    )
                )

                // Display the unit of temperature
                Text(
                    text = if (metric.value == "℃") "c" else "F",
                    style = TextStyle(
                        color = if (isSelected) colorResource(R.color.customCard) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_light))
                    )
                )
            }
        }
    }
}