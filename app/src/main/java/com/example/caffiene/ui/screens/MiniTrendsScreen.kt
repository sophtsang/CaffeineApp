package com.example.caffiene.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun MiniTrendsScreen(
    totalCaffeine: Float,
    caffeineLimit: Float = 500f,
    onNavigate: () -> Unit
) {
    val progress = (totalCaffeine / caffeineLimit).coerceIn(0f, 1f)
    val remaining = (caffeineLimit - totalCaffeine).coerceAtLeast(0f)

    Card(
        modifier = Modifier
            .clickable { onNavigate() }
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),  // take full width of the Card
            horizontalAlignment = Alignment.CenterHorizontally // center contents
        ) {
            Text(
                text = "Trends",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A332C)
            )
            Spacer(Modifier.height(10.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(150.dp)
            ) {
                CircularProgressIndicator(
                    progress = { progress },
                    color = Color(0xFF6E665D),
                    strokeWidth = 12.dp,
                    trackColor = Color.LightGray,
                    strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
                    modifier = Modifier.size(150.dp)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${remaining} mg",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text("Remaining")
                }
            }
            Spacer(Modifier.height(10.dp))
            Text("Daily Caffeine Intake")
        }
    }
}

@Composable
fun MiniTrendsCard(
    onCardClicked: () -> Unit
) {
    // when clicked should navigate to TrendsScreen.kt
    Card(
        modifier = Modifier
            .clickable { onCardClicked () }
    ) {
        Text("TRENDS")

    }
}

@Preview
@Composable
private fun MiniTrendsPreview() {
    MaterialTheme {
        MiniTrendsCard({})
    }
}