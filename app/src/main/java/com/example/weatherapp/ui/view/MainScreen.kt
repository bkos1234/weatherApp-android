import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.data.api.RetrofitClient
import com.example.weatherapp.data.model.CurrentWeather
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.ui.viewmodel.WeatherUiState
import com.example.weatherapp.ui.viewmodel.WeatherViewModel
import com.example.weatherapp.ui.viewmodel.WeatherViewModelFactory

@Composable
fun MainScreen() {

    val weatherService = RetrofitClient.weatherService

    // inicjalizacja WeatherRepository (pobiera dane z API)
    val repository = WeatherRepository(weatherService)

    // tworzenie viewmodelu do przechowywania danych z użyciem fabryki
    val viewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModelFactory(repository)
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchWeatherData(51.0, 17.0)
    }

    // |||||| KONIEC LOGIKI, TERAZ UI ||||||||||||||

    Column {
        when (uiState) {
            is WeatherUiState.Loading -> { Text("Loading...") }
            is WeatherUiState.Success -> {
                val weatherData = (uiState as WeatherUiState.Success).weatherData // info
                WeatherDetails(weatherData)
            }
            is WeatherUiState.Error -> {
                val errorMessage = (uiState as WeatherUiState.Error).message
                Text(text = "Error: $errorMessage")
            }
        }
    }
}

@Composable
fun WeatherDetails(weatherData: CurrentWeather) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Blue)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Ikona pogody - musisz jakoś powiązać iconId z drawable
                // Image(painter = painterResource(id = getWeatherIcon(weatherData.current.weather[0].icon)), contentDescription = "Weather Icon")

                Text(
                    text = "${weatherData.currentWeatherData.temperature} °C",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(text = weatherData.currentWeatherData.weather[0].description)
                Text(text = "Feels like: ${weatherData.currentWeatherData.feelsLike} °C")

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // Wilgotność
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        //Icon(Icons.Default.WaterDrop, contentDescription = "Humidity") // Ikona kropli
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Humidity: ${weatherData.currentWeatherData.humidity}%")
                    }

                    // Ciśnienie
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        //Icon(Icons.Default.ArrowDownward, contentDescription = "Pressure") // Ikona strzałki w dół
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Pressure: ${weatherData.currentWeatherData.pressure} hPa")
                    }
                }
                // Inne dane, np. wiatr
                Text(text = "Wind: ${weatherData.currentWeatherData.windSpeed} m/s, ${weatherData.currentWeatherData.windDeg}°")

                // Miasto - na razie na sztywno, później będzie dynamicznie
                //Text(text = "Wroclaw", modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainScreen()
}