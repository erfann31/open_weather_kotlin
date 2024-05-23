package com.example.open_weater_kotlin_ui.view.screen.hourly_forecast.widgets

import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.open_weater_kotlin_ui.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * A Composable function that displays a grid item with specified attributes.
 *
 * @param item A mutable map containing the item's properties:
 * - "title": The title of the item (String).
 * - "icon": The resource ID for the item's icon (Int).
 * - "txt1": The primary text to display (String?).
 * - "txt2": The secondary text to display (String?).
 * - "txt3": The suffix text to display (String?).
 *
 *@author Motahare Vakili
 */

@Composable
fun GridItems(item: MutableMap<String, Any?>) {
    val scrollState = rememberScrollState(0)
    val coroutineScope = rememberCoroutineScope()
    val animationDuration = 3000
    val text = item["txt2"] as String?
    val density = LocalDensity.current
    val fontSize = 14.sp

    /**
     * LaunchedEffect to animate scrolling of text if its length exceeds a specified limit.
     *
     * This effect continuously scrolls the text horizontally if its length is greater than or equal to 12 characters.
     * It calculates the required scroll distance based on the text length and container width.
     * The text scrolls to the end, then back to the start, with delays between each scroll.
     *
     *  text: The text to be scrolled.
     *  fontSize: The size of the font used for the text.
     *  density: The density of the current display.
     *  scrollState: The scroll state object used to control the scrolling.
     *  animationDuration: The duration of the scroll animation in milliseconds.
     */

    LaunchedEffect(Unit) {
        if (text != null && text.length >= 12) {
            val textWidth = with(density) { (text.length * fontSize.toPx()).toInt() }
            val containerWidth = with(density) { 180.dp.toPx().toInt() }
            val scrollDistance = (textWidth - containerWidth).coerceAtLeast(0)

            if (scrollDistance > 0) {
                while (true) {
                    delay(2000)

                    coroutineScope.launch {
                        scrollState.animateScrollTo(scrollDistance, animationSpec = tween(durationMillis = animationDuration))
                    }.join()

                    coroutineScope.launch {
                        scrollState.animateScrollTo(0, animationSpec = tween(durationMillis = animationDuration))
                    }.join()

                    delay(1000)
                }
            }
        }
    }
    Card(
        modifier = Modifier
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .aspectRatio(1f),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.customBox)),
        elevation = CardDefaults.cardElevation(5.dp)
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
                    .scale(1.3f),
                painter = painterResource(id = item["icon"] as Int),
                contentDescription = "item",
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = item["title"] as String,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
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
                .padding(vertical = 10.dp, horizontal = 8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                (item["txt1"] as String?)?.let {
                    Text(
                        text = it,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
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
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
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
                    modifier = Modifier.horizontalScroll(scrollState),
                    text = text!!.uppercase(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = fontSize,
                        fontFamily = FontFamily(Font(R.font.poppins_light))
                    ),
                )
            }

        }


    }
}
