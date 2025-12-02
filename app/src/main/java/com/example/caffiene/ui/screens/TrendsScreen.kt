package com.example.caffiene.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.caffiene.ui.viewmodels.CaffeineLog
import com.example.caffiene.ui.viewmodels.TrackerViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TrendsScreen(
    viewModel: TrackerViewModel = hiltViewModel()
) {
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("EEEE, MMM d")
    val weeklyLogs by viewModel.dailyTotals.collectAsStateWithLifecycle()
    val totalCaffeine by viewModel.totalCaffeine.collectAsStateWithLifecycle()
    val lastLogTime by viewModel.lastLogTime.collectAsStateWithLifecycle()

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

        CollapsableCard(
            title = "Daily Intake Trends",
            content = {
                DailyIntakeCard(
                    totalCaffeine = totalCaffeine,
                    lastLogTime = lastLogTime
                )
            }
        )

        CollapsableCard(
            title = "Weekly Intake Trends",
            content = {
                WeeklyIntakeCard(
                    dailyTotals = weeklyLogs
                )
            }
        )
    }
}

@Composable
fun CollapsableCard(
    title: String,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded }  // Toggle expand/collapse
            .animateContentSize(),                // Smooth size animation
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A332C)
            )
            Spacer(Modifier.height(10.dp))
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                content()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyIntakeCard(
    totalCaffeine: Float,
    caffeineLimit: Float = 500f,
    lastLogTime: LocalDateTime?
) {
    val progress = (totalCaffeine / caffeineLimit).coerceIn(0f, 1f)
    val remaining = (caffeineLimit - totalCaffeine).coerceAtLeast(0f)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),  // take full width of the Card
        horizontalAlignment = Alignment.CenterHorizontally // center contents
    ) {
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
        LastDrinkText(lastLogTime)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyIntakeCard(
    dailyTotals: Map<LocalDate, Float>
) {
    val today = LocalDate.now()
    val weeklyLogs = (0..6).map { offset ->
        val date = today.minusDays(6L - offset)  // Oldest first
        date to (dailyTotals[date] ?: 0f)
    }
    val maxIntake = (weeklyLogs.maxOfOrNull { it.second } ?: 1f)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),  // take full width of the Card
        horizontalAlignment = Alignment.CenterHorizontally // center contents
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(150.dp)
        ) {
            weeklyLogs.forEach { (date, total) ->
                val heightFraction = if (maxIntake == 0f) 0f else total / maxIntake
                val barWeight = if (heightFraction > 0f) heightFraction else 0.01f
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(barWeight)
                            .background(if (heightFraction > 0f) Color(0xFF6F4E37) else Color.Transparent)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(date.dayOfWeek.name.take(3)) // Mon, Tue, ...
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewTrendsScreen() {
    TrendsScreen()
}
