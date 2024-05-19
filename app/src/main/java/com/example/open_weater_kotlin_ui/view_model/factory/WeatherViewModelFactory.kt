package com.example.open_weater_kotlin_ui.view_model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.open_weater_kotlin_ui.model.repository.WeatherRepositoryImpl
import com.example.open_weater_kotlin_ui.view_model.WeatherViewModel

class WeatherViewModelFactory(private val repository: WeatherRepositoryImpl) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
