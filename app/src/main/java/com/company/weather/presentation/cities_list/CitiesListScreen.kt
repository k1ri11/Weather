package com.company.weather.presentation.cities_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
                citiesList = currentState.data,
                modifier = modifier.fillMaxSize(),
                onItemClick = { onItemClick(it) }
            )
        }
    }
}

@Composable
fun CitiesListContent(
    citiesList: List<City>,
    modifier: Modifier = Modifier,
    onItemClick: (City) -> Unit,
) {
    StickyLetterList(
        items = citiesList,
        modifier = modifier.fillMaxSize(),
        charHeaderWidth = 40.dp,
    ) { item ->
        CityItem(city = item, onItemClick = onItemClick)
    }
}

@Composable
private fun StickyLetterList(
    items: List<City>,
    modifier: Modifier = Modifier,
    charHeaderWidth: Dp = 40.dp,
    initialTextStyle: TextStyle = Typography.titleLarge,
    itemFactory: @Composable (LazyItemScope.(City) -> Unit),
) {
    val state: LazyListState = rememberLazyListState()
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val gutterPx = with(density) {
        charHeaderWidth.toPx()
    }
    var itemHeight by remember { mutableIntStateOf(0) }
    Box(
        modifier = modifier
            .padding(start = 16.dp)
            .drawWithCache {
                onDrawBehind {
                    var initial: Char? = null
                    if (itemHeight == 0) {
                        itemHeight = state.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 0
                    }
                    state.layoutInfo.visibleItemsInfo.forEachIndexed { index, itemInfo ->
                        val itemInitial = items.getOrNull(itemInfo.index)?.firstChar
                        if (itemInitial != null && itemInitial != initial) {
                            initial = itemInitial
                            val nextInitial = items.getOrNull(itemInfo.index + 1)?.firstChar
                            val textLayout = textMeasurer.measure(
                                text = AnnotatedString(itemInitial.toString()),
                                style = initialTextStyle,
                            )
                            val horizontalOffset = (gutterPx - textLayout.size.width) / 2
                            val verticalOffset = (itemHeight - textLayout.size.height) / 2
                            drawText(
                                textLayoutResult = textLayout,
                                topLeft = Offset(
                                    x = horizontalOffset,
                                    y = if (index != 0 || itemInitial != nextInitial) {
                                        itemInfo.offset.toFloat()
                                    } else {
                                        0f
                                    } + verticalOffset,
                                ),
                            )
                        }
                    }
                }
            }
    ) {
        LazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = charHeaderWidth)
        ) {
            items(items) { item ->
                itemFactory(item)
            }
        }
    }
}

@Composable
private fun CityItem(
    city: City,
    modifier: Modifier = Modifier,
    onItemClick: (City) -> Unit,
) {
    Box(contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
            .clickable { onItemClick(city) }
            .padding(horizontal = 16.dp)) {
        Text(
            text = city.cityName,
            style = Typography.bodyLarge,
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun CitiesListContentPreview() {
    CitiesListContent(
        onItemClick = {}, citiesList = listOf(
            City("Москва", 'М', "1", "55.7558", "37.6176"),
            City("Мурманск", 'М', "2", "68.9730", "33.0930"),
            City("Магадан", 'М', "3", "59.5681", "150.8085"),
            City("Санкт-Петербург", 'С', "4", "59.9343", "30.3351"),
            City("Самара", 'С', "5", "53.1959", "50.1002"),
            City("Саратов", 'С', "6", "51.5336", "46.0343"),
            City("Новосибирск", 'Н', "7", "55.0084", "82.9357"),
            City("Нижний Новгород", 'Н', "8", "56.3269", "44.0059"),
            City("Омск", 'О', "19", "54.9885", "73.3242"),
            City("Оренбург", 'О', "20", "51.7682", "55.0968"),
            City("Орёл", 'О', "21", "52.9685", "36.0692"),
            City("Ростов-на-Дону", 'Р', "22", "47.2357", "39.7015"),
            City("Рязань", 'Р', "23", "54.6254", "39.7359"),
            City("Реутов", 'Р', "24", "55.7580", "37.8576"),
            City("Уфа", 'У', "25", "54.7388", "55.9721"),
            City("Ульяновск", 'У', "26", "54.3188", "48.4024"),
            City("Уссурийск", 'У', "27", "43.8029", "131.9458"),
            City("Тюмень", 'Т', "28", "57.1613", "65.5250"),
            City("Тольятти", 'Т', "29", "53.5200", "49.4186"),
            City("Тверь", 'Т', "30", "56.8587", "35.9176")
        )
    )
}