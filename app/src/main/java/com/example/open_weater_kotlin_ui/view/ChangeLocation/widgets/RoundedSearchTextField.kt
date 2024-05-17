package com.example.open_weater_kotlin_ui.view.ChangeLocation.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.open_weater_kotlin_ui.R

@Composable
fun RoundedSearchTextField(
    text: String,
    placeholder: String,
    onSearchClicked: () -> Unit,
    onTextChanged: (String) -> Unit,
) {
    val shape: Shape = RoundedCornerShape(percent = 50)
    OutlinedTextField(
        value = text,
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontFamily = FontFamily(
                Font(
                    R.font.poppins,
                ),
            ),
            color = Color(0xFF000000),


            ),
        onValueChange = {
            onTextChanged(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(horizontal = 12.dp),
        placeholder = {
            Text(
                placeholder, style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.poppins,
                        ),
                    ),
                    color =
                    Color(0xFF929292)
                )
            )
        },
        shape = shape,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color
                .Transparent,
        ),
        trailingIcon = {
            IconButton(
                modifier = Modifier.padding(horizontal = 12.dp),
                onClick = onSearchClicked
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }
    )
}
