package com.example.open_weater_kotlin_uiui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.open_weater_kotlin_uiui.detail.viewmodel.WeatherDetailViewModel
import com.example.open_weater_kotlin_uiui.theme.WeatherGreen

@Composable
fun WeatherTopAppBar(
    weatherDetailViewModel: WeatherDetailViewModel
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = WeatherGreen,
        contentColor = Color.White,
        title = {
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { weatherDetailViewModel.goToSearchScreenUi() }
                    .testTag("weatherDetailTopBarIcon"),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "weatherDetailTopBarIcon"
                )

                var topBarTitle = weatherDetailViewModel.getCurrentWeatherName()
                if (topBarTitle.isEmpty()) {
                    topBarTitle = "Search locations"
                }
                Text(
                    modifier = Modifier.padding(start = 20.dp),
                    text = topBarTitle,
                    style = MaterialTheme.typography.h6
                )
            }
        }
    )
}
