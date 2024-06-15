package com.company.weather.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.company.weather.ui.theme.Typography

@Composable
fun ShowProgressIndicator(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.size(48.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ShowProgressIndicatorPreview() {
    ShowProgressIndicator()
}

@Composable
fun ErrorDialog(
    message: String,
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = Typography.titleSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        UpdateButton(modifier = Modifier.padding(top = 42.dp)) { onButtonClick() }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorDialogPreview() {
    ErrorDialog(message = "Произошла ошибка", onButtonClick = {})
}

@Composable
fun UpdateButton(modifier: Modifier = Modifier, onButtonClick: () -> Unit) {
    Button(
        modifier = modifier,
        onClick = onButtonClick,
    ) {
        Text(
            text = "Обновить",
            style = Typography.titleSmall,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateButtonPreview() {
    UpdateButton(onButtonClick = {})
}