package com.company.weather.presentation.weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.weather.domain.model.Weather
import com.company.weather.presentation.common.ErrorDialog
import com.company.weather.presentation.common.ScreenState
import com.company.weather.presentation.common.ShowProgressIndicator
import com.company.weather.presentation.common.UpdateButton
import com.company.weather.ui.theme.Typography

@Composable
fun WeatherScreen(
    latitude: String,
    longitude: String,
    cityName: String,
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getWeather(latitude = latitude, longitude = longitude)
    }
    val state by viewModel.weatherState.collectAsState()
    when (val currentState = state) {
        is ScreenState.Error -> {
            ErrorDialog(message = currentState.message, modifier = Modifier.fillMaxSize()) {
                viewModel.getWeather(latitude = latitude, longitude = longitude)
            }
        }

        is ScreenState.Loading -> {
            ShowProgressIndicator(modifier = Modifier.fillMaxSize())
        }

        is ScreenState.Success -> {
            WeatherContent(state = currentState.data, modifier = modifier.fillMaxSize(), cityName = cityName) {
                viewModel.getWeather(latitude = latitude, longitude = longitude)
            }
        }
    }
}

@Composable
private fun WeatherContent(
    state: Weather,
    cityName: String,
    modifier: Modifier = Modifier,
    onUpdateButtonClick: () -> Unit,
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(top = 40.dp),
            text = state.temperature,
            style = Typography.displayLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = cityName,
            style = Typography.displayMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.weight(1f))
        UpdateButton(modifier = Modifier.padding(bottom = 36.dp)) { onUpdateButtonClick() }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun WeatherContentPreview() {
    WeatherContent(
        state = Weather(1, "23°C", ""),
        cityName = "Москва",
        onUpdateButtonClick = {},
        modifier = Modifier.fillMaxSize()
    )
}
