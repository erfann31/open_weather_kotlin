package com.example.open_weater_kotlin_ui.view.ChangeLocation.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.open_weater_kotlin_ui.R

@Composable
fun CityWidget(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {
    },
    isAdd: Boolean = false,
) {
    val text = if (isAdd) {
        stringResource(id = R.string.add_city)
    } else {
        "city"
    }
    Surface(
        onClick = onClick,
        modifier = modifier.width(
            width = LocalConfiguration.current.screenWidthDp.dp,
        ),
        color = Color(0x00000000),
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 30.dp, vertical = 4.dp),
                    text = text,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight(700),
                        fontFamily = FontFamily(
                            Font(
                                R.font.poppins_semibold,
                            ),
                        ),
                        color =
                            Color(0xFFFFFFFF)
                    )
                )
                    Icon(
                        imageVector =
                        if (isAdd){
                            Icons.Filled.Add
                        }
                                else{
                            Icons.Filled.Close

                        },
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                            .size(30.0.dp)
                            .clickable {
                            },

                        contentDescription = "bottom_bar_item_active",
                        tint =
                            Color(0xFFE3E3E3)

                    )
            }

        }
    }
}
