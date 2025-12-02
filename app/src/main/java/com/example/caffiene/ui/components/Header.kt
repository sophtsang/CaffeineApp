package com.example.caffiene.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.SearchBar
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    beverageName: String,
    caffeineAmt: String,
    onBeverageNameChange: (String) -> Unit,
    onCaffeineAmtChange: (String) -> Unit,
    onClickLog: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text ("Log")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextField(
                value = beverageName,
                placeholder = { Text("Beverage Name", fontSize = 16.sp)},
                onValueChange = { it -> onBeverageNameChange(it) },
                modifier = Modifier.weight(1f)
            )
            TextField(
                value = caffeineAmt,
                placeholder = { Text("Caffeine Amount", fontSize = 16.sp) },
                onValueChange = { it -> onCaffeineAmtChange(it) },
                modifier = Modifier.weight(1f)
            )
        }
        Button(
            onClick = { onClickLog() },
            enabled = (beverageName.isNotBlank() && caffeineAmt.isNotBlank())
        ) {
            Text("+")
        }
    }
}

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