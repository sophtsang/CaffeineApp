package com.example.caffiene.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MiniCalcScreen(
    onNavigate: () -> Unit
) {
    MiniCalcCard(
        onCardClicked = { onNavigate() }
    )
}
@Composable
fun MiniCalcCard(
    onCardClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable { onCardClicked() }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),  // take full width of the Card
            horizontalAlignment = Alignment.CenterHorizontally // center contents
        ) {
            Text(
                text = "Caffeine Calculator",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A332C)
            )
        }
    }
}

@Preview
@Composable
private fun MiniCalcPreview() {
    MaterialTheme {
        MiniCalcCard( {} )
    }
}