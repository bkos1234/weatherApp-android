package com.example.weatherapp.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.weatherapp.util.Constants
import com.example.weatherapp.data.model.CurrentWeather

// nie pobiera danych, tylko pokazuje jak to zrobic

// NIE UŻYWAMY TU BASE_URL. UŻYWAMY BASE_URL PRZY INICJALIZACJI RETROFITA

interface WeatherService {
    @GET("onecall") //reszta jest w BASE_URL
    fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = Constants.API_KEY,
        @Query("units") units: String = Constants.UNITS,
        @Query("exclude") exclude: String = Constants.EXCLUDE
    ): Call<CurrentWeather>
}

// call to typ zwracany przez metodę getCurrentWeather, reprezentuje pojedyncze wywołanie API z retrofit