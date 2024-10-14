package com.example.weatherapp.api


import com.example.weatherapp.models.CityData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface ApiService {

    //NOTE: Putting api key in code is not secure, but will remedy it in a real prod app
    @GET("data/2.5/weather")
    suspend fun getWeather(@Query("q") city: String,
                           @Query("units") tempUnit : String = "imperial",
                           @Query("appid") api: String = "0c689a80de03ac31d652a6b4fbe696e3") : Response<CityData>

    @GET("data/2.5/weather")
    suspend fun getWeather( @Query("lat") lat: Double,
                            @Query("lon") long: Double,
                            @Query("units") tempUnit : String = "imperial",
                            @Query("appid") api: String = "0c689a80de03ac31d652a6b4fbe696e3") : Response<CityData>


    companion object {
        val sharedInstance: ApiService by lazy {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor {
                    val builder = it.request().newBuilder()
                    it.proceed(builder.build())
                }.build()

             Retrofit.Builder()
                 .baseUrl("https://api.openweathermap.org/")
                 .addConverterFactory(GsonConverterFactory.create())
                 .client(client)
                 .build()
                 .create(ApiService::class.java)
        }
    }
}


