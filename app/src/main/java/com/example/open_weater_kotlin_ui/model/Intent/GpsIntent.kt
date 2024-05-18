package com.example.open_weater_kotlin_ui.model.Intent

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.open_weater_kotlin_ui.viewModel.WeatherViewModel

fun goToMap(viewModel: WeatherViewModel, locationName: String, context: Context) {
    val lat = viewModel.lat.value
    val lon = viewModel.lon.value
    if (lat != null && lon != null) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=$lat,$lon($locationName)")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)
    }
}