package com.example.open_weater_kotlin_ui.model.Intent

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.open_weater_kotlin_ui.view_model.WeatherViewModel

/**
 * Opens Google Maps to display a specific location.
 *
 * This method retrieves the latitude and longitude coordinates from the provided ViewModel,
 * constructs a URI using these coordinates and the location name, creates an Intent with
 * the ACTION_VIEW action and this URI, sets Google Maps as the default app to handle the Intent,
 * and finally executes the Intent to open Google Maps.
 *
 * @param viewModel The ViewModel containing the latitude and longitude coordinates.
 * @param locationName The name of the location to be displayed on Google Maps.
 * @param context The application context used to start the Google Maps activity.
 */
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