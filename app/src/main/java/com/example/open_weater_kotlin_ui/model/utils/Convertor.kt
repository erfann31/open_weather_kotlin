package com.example.open_weater_kotlin_ui.model.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.example.open_weater_kotlin_ui.R
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

object Convertor {

    /**
     * Retrieves the appropriate drawable resource based on the icon name and the current time.
     *
     * @param iconName The name of the weather icon.
     * @param currentTime The current time in hours (0-23).
     * @return The painter resource for the icon.
     *
     * @author Motahare Vakili
     */
    @Composable
    fun getResourceId(iconName: String, currentTime: Int): Painter {
        val suffix = if (currentTime in 6..18) "d" else "n"
        val resourceName = "ic${iconName.substring(0, 2)}${suffix}"
        return painterResource(id = R.drawable::class.java.getField(resourceName).getInt(null))
    }

    /**
     * Determines the humidity type based on the given percentage.
     *
     * @param percentage The humidity percentage.
     * @return A string representing the humidity type.
     *
     * @author Motahare Vakili
     */
    fun getHumidityType(percentage: Int): String {
        return when {
            percentage in 30..50 -> "Normal "
            percentage < 30 -> "Low "
            percentage in 51..70 -> "High"
            else -> "Very High"
        }
    }

    /**
     * Determines the geographical direction based on the given degree.
     *
     * @param degree The degree (0-360).
     * @return A string representing the geographical direction.
     *
     * @author Motahare Vakili
     */
    fun getGeographicalDirection(degree: Int): String {
        return when (degree) {
            in 0..22 -> "North"
            in 23..67 -> "Northeast"
            in 68..112 -> "East"
            in 113..157 -> "Southeast"
            in 158..202 -> "South"
            in 203..247 -> "Southwest"
            in 248..292 -> "West"
            in 293..337 -> "Northwest"
            in 338..360 -> "North"
            else -> "Invalid Degree"
        }
    }

    /**
     * Determines the weather status based on the given number.
     *
     * @param number The weather status code.
     * @return A string representing the weather status.
     *
     * @author Motahare Vakili
     */
    fun getStatus(number: Int): String {
        return when (number) {
            2 -> "Thunderstorm"
            3 -> "Rainy"
            5 -> "Rainy"
            6 -> "Snowy"
            7 -> "Misty"
            8 -> "Cloudy"
            else -> ""
        }
    }

    /**
     * Retrieves the appropriate drawable resource for weekly forecast based on the icon name.
     *
     * @param iconName The name of the weather icon.
     * @return The painter resource for the icon.
     *
     *  @author Motahare Vakili
     */
    @Composable
    fun getResourceId_weekly(iconName: String): Painter {
        val resourceName = "ic${iconName.substring(0, 2)}d"
        return painterResource(id = R.drawable::class.java.getField(resourceName).getInt(null))
    }


    /**
     * Converts a timestamp in milliseconds to a formatted date string.
     *
     * @param timestamp The timestamp in milliseconds.
     * @return A formatted date string.
     *
     * @author Motahare Vakili
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertMillisToDate(timestamp: Long): String {
        val dt = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return dt.format(formatter)
    }

    /**
     * Determines the day of the week from a formatted date string.
     *
     * @param timestamp The formatted date string.
     * @return A string representing the day of the week.
     *
     * @author Motahare Vakili
     */
    fun getDayOfWeek(timestamp: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = sdf.parse(timestamp)
        val calendar = Calendar.getInstance()
        calendar.time = date!!
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        return when (dayOfWeek) {
            Calendar.SUNDAY -> "Sun"
            Calendar.MONDAY -> "Mon"
            Calendar.TUESDAY -> "Tue"
            Calendar.WEDNESDAY -> "Wed"
            Calendar.THURSDAY -> "Thu"
            Calendar.FRIDAY -> "Fri"
            Calendar.SATURDAY -> "Sat"
            else -> ""
        }
    }

}