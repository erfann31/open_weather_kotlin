package com.example.open_weater_kotlin_ui.view.screen.hourly_forecast.widgets

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.open_weater_kotlin_ui.R
import com.example.open_weater_kotlin_ui.model.entities.HourlyForecast
import com.example.open_weater_kotlin_ui.model.utils.Convertor.getResourceId
import com.example.open_weater_kotlin_ui.view.screen.hourly_forecast.selectedItemId
import com.example.open_weater_kotlin_ui.view_model.WeatherViewModel

/**
 * Composable function to display an hourly forecast item in a row.
 *
 * @param item The hourly forecast item to display.
 * @param index The index of the item in the list.
 * @param viewModel The ViewModel instance to access the metric system setting.
 *
 * This function creates a card for each hourly forecast item, displaying the time, weather icon, and temperature.
 * It also handles click events to select the item.
 *
 * @author Motahare Vakili
 */
@Composable
fun RowItems(item: HourlyForecast, index: Int, viewModel: WeatherViewModel) {
    val metric = remember {
        mutableStateOf(
            if (viewModel.isMetric.value) {
                "℃"
            } else {
                "°F"
            }
        )
    }
    /**
     * Creates a MutableInteractionSource to handle click interactions without any visual feedback.
     * This is used to manage the clickable state of the card.
     */
    val interactionSource = remember { MutableInteractionSource() }


    /**
     * Determines if the current item is selected based on its index.
     * This is used to apply different styles to the selected item.
     *
     * @param selectedItemId The state that holds the ID of the selected item.
     * @param index The index of the current item in the list.
     * @return True if the current item's index matches the selectedItemId, otherwise false.
     */
    val isSelected = selectedItemId.intValue == index

    Card(
        modifier = Modifier
            .padding(6.dp)
            .height(195.dp)
            .width(72.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            )
            { if (!isSelected) selectedItemId.intValue = index },
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.White else colorResource(R.color.customCard)
        ),
        elevation = CardDefaults.cardElevation(10.dp),
    )
    {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                text = item.dateTimeText!!.substring(10, 16),
                style = TextStyle(
                    color = if (isSelected) colorResource(R.color.customCard) else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold))
                ),
            )
            Icon(
                modifier = Modifier.height(35.dp).width(35.dp),
                painter = getResourceId(
                    item.weather?.get(0)?.icon.toString(),
                    item.dateTimeText.substring(11, 13).toIntOrNull(radix = 10) ?: 0
                ),
                contentDescription = "icon",
                tint = if (isSelected) colorResource(R.color.customCard) else Color.White
            )
            Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically)
            {
                Text(
                    text = "${item.main!!.temp?.toInt().toString()}°",
                    style = TextStyle(
                        color = if (isSelected) colorResource(R.color.customCard) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold))
                    )
                )

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