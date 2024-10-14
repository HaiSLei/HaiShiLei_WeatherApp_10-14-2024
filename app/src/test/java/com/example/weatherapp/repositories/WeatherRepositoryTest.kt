package com.example.weatherapp.repositories

import kotlinx.coroutines.runBlocking
import org.junit.Test

class WeatherRepositoryTest {

    @Test
    fun verifyInvalidLatAndLongReturnFailure() {
        runBlocking{
            val weather = WeatherRepoImp.getWeather(-100.0,100.0)
            assert(!weather.isSuccessful)
        }
    }

    @Test
    fun verifyInvalidLatAndLongReturnSuccess() {
        runBlocking{
            val weather = WeatherRepoImp.getWeather(80.0,50.0)
            assert(weather.isSuccessful)
        }
    }


    @Test
    fun verifyBlankCityReturnFailure() {
        runBlocking{
            val weather = WeatherRepoImp.getWeather("    ")
            assert(!weather.isSuccessful)
        }
    }

    @Test
    fun verifyEmptyCityReturnFailure() {
        runBlocking{
            val weather = WeatherRepoImp.getWeather("")
            assert(!weather.isSuccessful)
        }
    }

    @Test
    fun verifyMisspellCityReturnFailure() {
        runBlocking{
            val weather = WeatherRepoImp.getWeather("NewYerk")
            assert(!weather.isSuccessful)
        }
    }

    @Test
    fun verifyCityReturnSuccessful() {
        runBlocking{
            val weather = WeatherRepoImp.getWeather("Boston")
            assert(weather.isSuccessful)
        }
    }




}