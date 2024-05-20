package com.example.open_weater_kotlin_ui.model.utils

import android.content.Context
import android.os.FileObserver
import org.json.JSONArray
import java.io.File

/**
 * Reads the list of cities from a JSON file stored in the application's private files directory.
 *
 * @param context The application context.
 * @return A list of city names.
 *
 * @author Erfan Nasri
 */
fun readCitiesFromFile(context: Context): List<String> {
    val logFile = File(context.filesDir, "city_logs.json")
    val cities = mutableListOf<String>()

    if (logFile.exists()) {
        val jsonText = logFile.readText()
        val jsonArray = JSONArray(jsonText)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val cityName = jsonObject.getString("cityName")
            cities.add(cityName)
        }
    }


    return cities
}

/**
 * Observes changes to the "city_logs.json" file in the application's private files directory.
 * Calls the provided callback function when the file is created or modified.
 *
 * @param context The application context.
 * @param callback The callback function to be invoked when the file is created or modified.
 *
 *  * @author Erfan Nasri
 */
class LogFileObserver(
    private val context: Context,
    private val callback: () -> Unit
) : FileObserver(context.filesDir.path + File.separator + "city_logs.json", CREATE or MODIFY) {

    /**
     * Called when an event occurs in the monitored file or directory.
     *
     * @param event The type of event which happened.
     * @param path The path of the file which triggered the event.
     */
    override fun onEvent(event: Int, path: String?) {
        if (event == CREATE || event == MODIFY) {
            callback()
        }
    }
}

/**
 * Reads the logs from the "city_logs.json" file stored in the application's private files directory.
 * The logs are returned in reverse order.
 *
 * @param context The application context.
 * @return A list of city names from the log file in reverse order.
 *
 *  * @author Erfan Nasri
 */
fun readLogsFromFile(context: Context): List<String> {
    val logFile = File(context.filesDir, "city_logs.json")
    val logs = mutableListOf<String>()

    if (logFile.exists()) {
        val jsonText = logFile.readText()
        val jsonArray = JSONArray(jsonText)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val cityName = jsonObject.getString("cityName")
            logs.add(cityName)
        }
    }

    return logs.reversed()

}