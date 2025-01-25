package com.example.weatherapp.data.api

import com.example.weatherapp.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // inicjalizacja Retrofit do pobierania z API
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // inicjalizacja WeatherService (sposób komunikacji z API)
    val weatherService: WeatherService by lazy {
        retrofit.create(WeatherService::class.java)
    }
}