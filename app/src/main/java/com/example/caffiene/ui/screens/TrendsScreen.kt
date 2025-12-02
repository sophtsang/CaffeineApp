package com.example.caffiene.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TrendsScreen(
    totalCaffeine: Float,
    caffeineLimit: Float = 500f,
    lastLogTime: LocalDateTime?,
    onNavigate: () -> Unit
) {
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("EEEE, MMM d")

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = today.format(formatter),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
fun PreviewTrendsScreen() {

}
