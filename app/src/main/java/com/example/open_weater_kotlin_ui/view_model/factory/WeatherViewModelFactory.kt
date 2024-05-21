package com.example.open_weater_kotlin_ui.view_model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.open_weater_kotlin_ui.model.repository.WeatherRepositoryImpl
import com.example.open_weater_kotlin_ui.view_model.WeatherViewModel

/**
 * Factory class for creating instances of [WeatherViewModel].
 *
 * @property repository The repository used by the ViewModel to fetch data.
 *
 * @author Erfan Nasri
 */
class WeatherViewModelFactory(private val repository: WeatherRepositoryImpl) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of the given `Class`.
     *
     * @param modelClass A `Class` whose instance is requested.
     * @return A newly created `ViewModel`.
     * @throws IllegalArgumentException if the `modelClass` is not assignable from `WeatherViewModel`.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
