package com.example.open_weater_kotlin_ui.view.ChangeLocation.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.open_weater_kotlin_ui.R

@Composable
fun CityWidget(
    modifier: Modifier = Modifier,
    text: String = stringResource(id = R.string.add_city),
    onClick: () -> Unit = {},
    iconOnClick: () -> Unit = {},
    onTextChanged: (String) -> Unit = {},
    textFieldValue: String = "",
    isEdit: Boolean = false,
    isAdd: Boolean = false,
) {

    var showTextField by remember { mutableStateOf(false) }

    Surface(
        onClick = onClick,
        modifier = modifier
            .width(width = LocalConfiguration.current.screenWidthDp.dp)
            .height(50.dp)
            .padding(horizontal = 12.dp),
        color = Color(0x00000000),
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showTextField) {
                    RoundedTextField(
                        text = textFieldValue,
                        onTextChanged = { onTextChanged(it) },
                        placeholder = "Type name of city here...",
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp).padding(end =  40.dp),
                        textSize = 12.sp,
                        showIcon = false
                    )
                } else {
                    Text(
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        text = text,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight(700),
                            fontFamily = FontFamily(
                                Font(
                                    R.font.poppins_semibold,
                                ),
                            ),
                            color = Color(0xFFFFFFFF)
                        )
                    )
                }
                if (isEdit || isAdd) {
                    IconButton(
                        onClick = {
                            if (isAdd) {
                                if (showTextField) {
                                    iconOnClick()
                                    showTextField = false
                                } else {
                                    showTextField = true
                                }
                            } else {
                                iconOnClick()
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                            .size(30.0.dp)
                    ) {
                        Icon(
                            imageVector =
                            if (showTextField) {
                                Icons.Default.Check
                            } else {
                                if (isEdit) {
                                    Icons.Default.Close
                                } else {
                                    Icons.Default.Add
                                }
                            },
                            contentDescription = "bottom_bar_item_active",
                            tint = Color(0xFFE3E3E3)
                        )
                    }
                }
            }
        }
    }
}

