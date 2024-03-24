package com.example.open_weater_kotlin_uidata.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class LocationCoordinate(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    @SerializedName("local_names") val localName: Map<String, String>?,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String? = null
)
