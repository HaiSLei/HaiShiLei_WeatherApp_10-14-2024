package com.example.weatherapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.models.CityData
import com.example.weatherapp.models.TemperatureData
import com.example.weatherapp.models.WeatherData
import com.example.weatherapp.repositories.FakeWeatherRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class WeatherViewModelTest {

    @get:Rule
    var instanceTaskExecutor = InstantTaskExecutorRule()

    private lateinit var viewModel : WeatherViewModel

    @Before
    fun setup() {
        val repo = FakeWeatherRepository()
        viewModel = WeatherViewModel(repo, ApplicationProvider.getApplicationContext())
    }

    @Test
    fun verifyCorrectCity_returnSuccessful() {
        viewModel.getWeather("Seattle")
        val result = viewModel.weatherResult.getOrAwaitValue().body()
        val expectedResult = CityData(
            listOf(
                WeatherData(
                "1000",
                "Cloudy",
                "Wet Cloudy",
                "04d"
            )),
            TemperatureData("65","66", "60"),
                "Seattle"
        )
        assertEquals(result,expectedResult)
    }

    @Test
    fun verifyBlankCity_returnFailure() {
        viewModel.getWeather("")
        val result = viewModel.weatherResult.getOrAwaitValue()
        assert(!result.isSuccessful)
    }

    @Test
    fun verifyEmptyCity_returnFailure() {
        viewModel.getWeather("    ")
        val result = viewModel.weatherResult.getOrAwaitValue()
        assert(!result.isSuccessful)
    }

    @Test
    fun verifyCorrectLatAndLong_returnSuccessful() {
        viewModel.getWeather(75.7,60.0)
        val result = viewModel.weatherResult.getOrAwaitValue().body()
        val expectedResult = CityData(
        listOf(WeatherData(
            "1001",
            "Sunny",
            "Bright sun",
            "05d"
        )),
        TemperatureData("8","85", "90"),
        "New York"
        )
        assertEquals(result,expectedResult)
    }
}