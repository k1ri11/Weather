package com.company.weather.presentation.cities_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.weather.domain.model.City
import com.company.weather.presentation.common.ErrorDialog
import com.company.weather.presentation.common.ScreenState
import com.company.weather.presentation.common.ShowProgressIndicator
import com.company.weather.ui.theme.Typography

@Composable
fun CitiesListScreen(
    modifier: Modifier = Modifier,
    viewModel: CitiesViewModel = hiltViewModel(),
    onItemClick: (City) -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getCities()
    }
    val state by viewModel.citiesState.collectAsState()
    when (val currentState = state) {
        is ScreenState.Error -> {
            ErrorDialog(message = currentState.message, modifier = Modifier.fillMaxSize()) { viewModel.getCities() }
        }

        is ScreenState.Loading -> {
            ShowProgressIndicator(modifier = Modifier.fillMaxSize())
        }

        is ScreenState.Success -> {
            CitiesListContent(
                state = currentState.data,
                modifier = modifier.fillMaxSize(),
                onItemClick = { onItemClick(it) }
            )
        }
    }
}

@Composable
fun CitiesListContent(
    state: CitiesState,
    onItemClick: (City) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val moveStickyHeader by remember {
        derivedStateOf {
            state.endIndexes.contains(listState.firstVisibleItemIndex + 1)
        }
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
    ) {
        itemsIndexed(state.sortedCities) { index, city ->
            CityItem(
                city,
                showCharHeader = state.startIndexes.contains(index) && listState.firstVisibleItemIndex != index,
            ) {
                onItemClick(state.sortedCities[index])
            }
        }
    }
    LetterHeader(
        char = state.sortedCities[listState.firstVisibleItemIndex].cityName.first().toString(),
        modifier = Modifier
            .size(40.dp)
            .then(
                if (moveStickyHeader) {
                    Modifier.offset { IntOffset(0, -listState.firstVisibleItemScrollOffset) }
                } else Modifier
            )
    )
}

@Composable
fun CityItem(
    city: City,
    showCharHeader: Boolean,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        if (showCharHeader) {
            LetterHeader(
                char = city.cityName.first().toString(),
                modifier = Modifier.width(40.dp)
            )
        } else {
            Spacer(modifier = Modifier.width(40.dp))
        }
        Box(contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                .clickable { onItemClick(city.cityName) }
                .padding(horizontal = 16.dp)) {
            Text(
                text = city.cityName,
                style = Typography.bodyLarge,
            )
        }
    }
}

@Composable
fun LetterHeader(char: String, modifier: Modifier = Modifier) {
    Text(
        text = char,
        style = Typography.titleLarge,
        maxLines = 1,
        modifier = modifier
            .padding(start = 16.dp)
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun CitiesListContentPreview() {
    val citiesState = CitiesState(
        citiesMap = mapOf(
            "А" to listOf(
                City("Адыгейск", "385200", "44.8783715", "39.190172"),
                City("Алейск", "658125", "52.4920914", "82.7794145")
            ),
            "Б" to listOf(
                City("Барнаул", "656000", "53.3481145", "83.7798362"),
                City("Белогорск", "676805", "50.9212235", "128.4738742"),
                City("Белокуриха", "659900", "51.9960534", "84.9840343"),
                City("Бийск", "659300", "52.5393683", "85.2138852"),
                City("Благовещенск", "675000", "50.2905935", "127.5272186")
            ),
            "Г" to listOf(
                City("Горно-Алтайск", "649000", "51.9582681", "85.9602957"),
                City("Горняк", "658420", "50.9979032", "81.4643059")
            ),
            "З" to listOf(
                City("Завитинск", "676870", "50.1064678", "129.4392897"),
                City("Заринск", "659100", "53.7063495", "84.9314703"),
                City("Зея", "676151", "53.7339151", "127.2658079"),
                City("Змеиногорск", "658480", "51.1580235", "82.1872476")
            ),
            "К" to listOf(
                City("Камень-на-Оби", "658700", "53.7915454", "81.3545173")
            ),
            "М" to listOf(
                City("Майкоп", "385000", "44.6098268", "40.1006527")
            ),
            "Н" to listOf(
                City("Новоалтайск", "658041", "53.4120811", "83.9311249")
            ),
            "Р" to listOf(
                City("Райчихинск", "676770", "49.7941882", "129.4112149"),
                City("Рубцовск", "658200", "51.5013075", "81.2077729")
            ),
            "С" to listOf(
                City("Славгород", "658820", "52.9993053", "78.6459674")
            ),
            "Я" to listOf(
                City("Яровое", "658837", "52.9251728", "78.5729898")
            )
        ),
        startIndexes = listOf(0, 2, 7, 9, 13, 14, 15, 16, 18, 19),
        endIndexes = listOf(2, 7, 9, 13, 14, 15, 16, 18, 19, 20),
        sortedCities = listOf(
            City("Адыгейск", "385200", "44.8783715", "39.190172"),
            City("Алейск", "658125", "52.4920914", "82.7794145"),
            City("Барнаул", "656000", "53.3481145", "83.7798362"),
            City("Белогорск", "676805", "50.9212235", "128.4738742"),
            City("Белокуриха", "659900", "51.9960534", "84.9840343"),
            City("Бийск", "659300", "52.5393683", "85.2138852"),
            City("Благовещенск", "675000", "50.2905935", "127.5272186"),
            City("Горно-Алтайск", "649000", "51.9582681", "85.9602957"),
            City("Горняк", "658420", "50.9979032", "81.4643059"),
            City("Завитинск", "676870", "50.1064678", "129.4392897"),
            City("Заринск", "659100", "53.7063495", "84.9314703"),
            City("Зея", "676151", "53.7339151", "127.2658079"),
            City("Змеиногорск", "658480", "51.1580235", "82.1872476"),
            City("Камень-на-Оби", "658700", "53.7915454", "81.3545173"),
            City("Майкоп", "385000", "44.6098268", "40.1006527"),
            City("Новоалтайск", "658041", "53.4120811", "83.9311249"),
            City("Райчихинск", "676770", "49.7941882", "129.4112149"),
            City("Рубцовск", "658200", "51.5013075", "81.2077729"),
            City("Славгород", "658820", "52.9993053", "78.6459674"),
            City("Яровое", "658837", "52.9251728", "78.5729898")
        )
    )
    CitiesListContent(state = citiesState, onItemClick = {})
}



