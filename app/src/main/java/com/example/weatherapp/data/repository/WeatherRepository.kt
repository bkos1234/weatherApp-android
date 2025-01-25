package com.example.weatherapp.data.repository

import com.example.weatherapp.data.api.WeatherService
import com.example.weatherapp.data.model.CurrentWeather
import kotlinx.coroutines.Dispatchers // !!!
import kotlinx.coroutines.withContext // !!!

// pobierane dane z API za pomocą Retrofit, potem udostępniamy do viewModel

class WeatherRepository(private val weatherService: WeatherService) // przekazujemy weatherService
{
    suspend fun getCurrentWeather(latitude: Double, longitude: Double): Result<CurrentWeather> = withContext(
        Dispatchers.IO)
    {
        try
        {
            val response = weatherService.getCurrentWeather(latitude,longitude).execute()
            if (response.isSuccessful)
            {
                Result.success(response.body()!!)
            }
            else
            {
                Result.failure(Exception("API error: ${response.code()}"))
            }
        }
        catch (e: Exception)
        {
            Result.failure(e)
        }
    }
}