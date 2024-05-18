package com.example.open_weater_kotlin_ui.model.utils

import android.content.Context
import android.os.FileObserver
import org.json.JSONArray
import java.io.File


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

class LogFileObserver(
    private val context: Context,
    private val callback: () -> Unit
) : FileObserver(context.filesDir.path + File.separator + "city_logs.json", CREATE or MODIFY) {

    override fun onEvent(event: Int, path: String?) {
        if (event == CREATE || event == MODIFY) {
            callback()
        }
    }
}

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