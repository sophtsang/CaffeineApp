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
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.SearchBar
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    beverageName: String,
    caffeineAmt: String,
    onBeverageNameChange: (String) -> Unit,
    onCaffeineAmtChange: (String) -> Unit,
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
                TextField(
                    value = beverageName,
                    placeholder = { Text("Drink Name", fontSize = 16.sp) },
                    onValueChange = { it -> onBeverageNameChange(it) },
                    modifier = Modifier.weight(1f),
                )
                TextField(
                    value = caffeineAmt,
                    placeholder = { Text("Caffeine Amount", fontSize = 16.sp) },
                    onValueChange = { it -> onCaffeineAmtChange(it) },
                    modifier = Modifier.weight(1f)
                )
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
@Preview
@Composable
private fun HeaderPreview() {
    Header (
        beverageName = "",
        caffeineAmt = "",
        onBeverageNameChange = {},
        onCaffeineAmtChange = {},
        onClickLog = {}
    )
}