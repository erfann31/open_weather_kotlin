package com.example.open_weater_kotlin_ui.view.screen.change_location.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.open_weater_kotlin_ui.R

/**
 * A custom text field with a rounded shape, customizable colors, and an optional search icon.
 *
 * @param placeholderColor The color of the placeholder text.
 * @param cursurColor The color of the cursor.
 * @param textColor The color of the input text.
 * @param containerColor The background color of the text field.
 * @param borderColor The color of the border when the text field is focused or unfocused.
 * @param isRounded Boolean flag to determine if the text field should have rounded corners.
 * @param text The current text to display inside the text field.
 * @param showIcon Boolean flag to determine if a search icon should be shown.
 * @param textSize The size of the input text.
 * @param placeholder The placeholder text to display when the text field is empty.
 * @param onSearchClicked Callback to be invoked when the search icon is clicked or the search action is triggered.
 * @param onTextChanged Callback to be invoked when the text changes.
 *
 * @author Erfan Nasri
 */


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RoundedTextField(
    modifier: Modifier = Modifier,
    placeholderColor: Color = Color(0xFF929292),
    cursurColor: Color,
    textColor: Color,
    containerColor: Color,
    borderColor: Color,
    isRounded: Boolean,
    text: String,
    showIcon: Boolean,
    textSize: TextUnit,
    placeholder: String,
    onSearchClicked: () -> Unit = {},
    onTextChanged: (String) -> Unit,
) {
    var shape = RoundedCornerShape(5)
    if (isRounded) {
        shape = RoundedCornerShape(percent = 50)
    }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    OutlinedTextField(
        value = text,
        textStyle = TextStyle(
            fontSize = textSize,
            fontFamily = FontFamily(
                Font(
                    R.font.poppins,
                ),
            ),
            color = textColor,
        ),
        onValueChange = {
            onTextChanged(it)
        },
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    keyboardController?.show()
                }
            },
        placeholder = {
            Text(
                placeholder,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = textSize,
                    fontFamily = FontFamily(
                        Font(
                            R.font.poppins,
                        ),
                    ),
                    color = placeholderColor,
                )
            )
        },

        shape = shape,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = cursurColor,
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            focusedBorderColor = borderColor,
            unfocusedBorderColor = borderColor,
        ),
        trailingIcon = {
            if (showIcon) {
                IconButton(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    onClick = onSearchClicked
                ) {
                    Icon(
                        Icons.Default.Search,
                        tint = colorResource(R.color.customBlue),
                        contentDescription = "Search",
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                onSearchClicked()
                focusManager.clearFocus()
            }
        )
    )
}
