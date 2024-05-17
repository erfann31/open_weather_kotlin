package com.example.open_weater_kotlin_ui.view.HourlyForecast.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.open_weater_kotlin_ui.R


@Composable
fun GridItems(item: MutableMap<String, Any?>) {

    Card(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 5.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.customBox)),
        elevation = CardDefaults.cardElevation(10.dp)
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .scale(1.3f), painter = painterResource(id = item["icon"] as Int), contentDescription = null,
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = item["title"] as String,
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_semibold))
                )
            )

        }

        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){
                (item["txt1"] as String?)?.let {
                    Text(
                        text = it,
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold))
                        ),
                    )
                }

                (item["txt3"] as String?)?.let {
                    Text(
                        text = it,
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_semibold))
                        ),
                    )
                }
            }


            Spacer(modifier = Modifier.height(6.dp))

            (item["txt2"] as String?)?.let {
                Text(
                    text = it,
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_light))
                    ),
                )
            }

        }


    }
}
