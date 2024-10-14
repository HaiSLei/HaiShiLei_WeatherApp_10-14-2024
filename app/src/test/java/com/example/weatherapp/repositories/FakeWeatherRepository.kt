package com.example.weatherapp.repositories

import com.example.weatherapp.models.CityData
import com.example.weatherapp.models.TemperatureData
import com.example.weatherapp.models.WeatherData
import retrofit2.Response

class FakeWeatherRepository: WeatherRepo {
    override suspend fun getWeather(city: String): Response<CityData> {
            return Response.success(CityData(
                listOf(WeatherData(
                    "1000",
                    "Cloudy",
                    "Wet Cloudy",
                    "04d"
                )),
                TemperatureData("65","66", "60"),
                "Seattle"
            ))
    }

    override suspend fun getWeather(lat: Double, long: Double): Response<CityData> {
        return Response.success(CityData(
            listOf(WeatherData(
                "1001",
                "Sunny",
                "Bright sun",
                "05d"
            )),
            TemperatureData("8","85", "90"),
            "New York"
        ))
    }
}