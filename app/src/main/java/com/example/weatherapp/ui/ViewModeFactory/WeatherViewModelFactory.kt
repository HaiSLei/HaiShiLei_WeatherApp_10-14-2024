package com.example.weatherapp.ui.ViewModeFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.repositories.WeatherRepo
import com.example.weatherapp.ui.WeatherViewModel

class WeatherViewModelFactory(private val weatherRepo: WeatherRepo,
                              private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(weatherRepo, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}