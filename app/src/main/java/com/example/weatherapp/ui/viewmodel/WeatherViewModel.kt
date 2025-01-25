package com.example.weatherapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.CurrentWeather
import com.example.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    // prywatna zmienna do przechowywania danych w viewmodelu
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)

    // publiczna zmienna do obserwowania stanu UI
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    // pobranie danych pogodowych
    fun fetchWeatherData(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            val result = repository.getCurrentWeather(latitude, longitude)
            _uiState.value = if (result.isSuccess) {
                WeatherUiState.Success(result.getOrNull()!!)
            } else {
                WeatherUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}

// stany UI, sukces lub błąd
sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val weatherData: CurrentWeather) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}