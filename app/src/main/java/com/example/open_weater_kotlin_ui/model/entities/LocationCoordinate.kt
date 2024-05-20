package com.example.open_weater_kotlin_ui.model.entities

import com.google.gson.annotations.SerializedName

/**
 * Data class representing location coordinates.
 *
 * @property id The ID of the location coordinate.
 * @property name The name of the location.
 * @property localName The local names of the location.
 * @property lat The latitude coordinate.
 * @property lon The longitude coordinate.
 * @property country The country of the location.
 * @property state The state of the location.
 */
data class LocationCoordinate(
    val id: Int = 0,
    val name: String,
    @SerializedName("local_names") val localName: Map<String, String>?,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String? = null
)