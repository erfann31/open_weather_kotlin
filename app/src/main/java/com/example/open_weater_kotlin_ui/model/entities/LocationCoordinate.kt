package com.example.open_weater_kotlin_ui.model.entities


import com.google.gson.annotations.SerializedName

data class LocationCoordinate(
    val id: Int = 0,
    val name: String,
    @SerializedName("local_names") val localName: Map<String, String>?,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String? = null
)
