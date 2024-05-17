package com.example.open_weater_kotlin_ui.view.WeeklyForecast


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.example.open_weater_kotlin_ui.models.entities.DailyForecast
import com.example.open_weater_kotlin_ui.models.utils.Util
import com.example.open_weater_kotlin_ui.models.utils.Util.convertMillisToDate
import com.example.open_weater_kotlin_ui.models.utils.Util.getDayOfWeek


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RowItems(item: DailyForecast, index: Int) {
    val interactionSource = remember { MutableInteractionSource() }
    val isSelected = w_selectedItemId.intValue == index
    Card(modifier = Modifier
        .padding(6.dp)
        .height(190.dp)
        .width(70.dp)
        .clickable(
            interactionSource = interactionSource,
            indication = null
        ) { if (!isSelected) w_selectedItemId.intValue = index },
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.White else colorResource(R.color.customCard)
        ),
        elevation = CardDefaults.cardElevation(10.dp))
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
                text = (getDayOfWeek(convertMillisToDate(item.dt!!))),
                style = TextStyle(
                    color = if (isSelected) colorResource(R.color.customCard) else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold))
                ),
            )
            Icon(
                modifier = Modifier
                    .scale(1.8f), painter = Util.getResourceId_weekly(
                    item.weather?.get(0)?.icon.toString()),
                contentDescription = null,
                tint = if (isSelected) colorResource(R.color.customCard) else Color.White
            )

            Text(
                text = "${item.temp?.day?.toInt().toString()}°",
                style = TextStyle(
                    color = if (isSelected) colorResource(R.color.customCard) else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold))
                ),
            )

        }


    }
}