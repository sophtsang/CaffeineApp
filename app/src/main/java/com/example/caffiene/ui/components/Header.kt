package com.example.caffiene.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    beverageName: String,
    caffeineAmt: String,
    timeStamp: String,
    onBeverageNameChange: (String) -> Unit,
    onCaffeineAmtChange: (String) -> Unit,
    onTimeStampChange: (String) -> Unit,
    onClickLog: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD4C5BA)
        ),
        modifier = Modifier
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "New Drink",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6F4E37)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    label = { Text("Drink Name") },
                    singleLine = true,
                    value = beverageName,
                    onValueChange = { it -> onBeverageNameChange(it) },
                    placeholder = { Text("Cold Brew") },
                    modifier = Modifier.weight(1f),
                )
                OutlinedTextField(
                    label = { Text("Caffeine (mg)") },
                    singleLine = true,
                    value = caffeineAmt,
                    onValueChange = { it -> onCaffeineAmtChange(it) },
                    placeholder = { Text("250.0") },
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val today = LocalDate.now()
                val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")

//                OutlinedTextField(
//                    label = { "Timestamp (MM/DD/YYYY HH:mm)" },
//                    singleLine = true,
//                    value = timeStamp,
//                    onValueChange = { it -> onTimeStampChange(it) },
//                    placeholder = { Text(today.format(formatter)) },
//                    modifier = Modifier.weight(1f)
//                )
            }
            IconButton(
                onClick = { onClickLog() },
                enabled = (beverageName.isNotBlank() && caffeineAmt.isNotBlank())
            ) {
                Icon(
                    imageVector = Icons.Rounded.Coffee,
                    contentDescription = "Coffee",
                    tint = Color(0xFF6F4E37)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun onDateTimeChange(
    timeStamp: String,
    onTimeStampChange: (LocalDateTime) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm", Locale.US)

    try {
        onTimeStampChange(LocalDateTime.parse(timeStamp, formatter))
    } catch (e: DateTimeParseException) {
        onTimeStampChange(LocalDateTime.now())
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun HeaderPreview() {
    Header (
        beverageName = "",
        caffeineAmt = "",
        timeStamp = "",
        onBeverageNameChange = {},
        onCaffeineAmtChange = {},
        onTimeStampChange = {},
        onClickLog = {}
    )
}