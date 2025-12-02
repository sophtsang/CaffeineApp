package com.example.caffiene.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.caffiene.ui.components.Header
import com.example.caffiene.ui.viewmodels.CaffeineLog
import com.example.caffiene.ui.viewmodels.TrackerViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
Main Home Screen: Tracker Screen Format:

#######################
LOG CARD:
[log caffeine intake]:
[beverage name]
[caffeine amt (mg)]
[...]

[LOG Button]
#######################

DAILY SUMMARY CARD:
Mini-Trends Card (MiniTrendsScreen.kt): Click for trends -> navigate to trends page (TrendsScreen.kt)
Mini-Calculator Card (MiniCalcScreen.kt): Click for calculator -> navigate to calculator page (CalculatorScreen.kt)

#######################

LOG LIST...

#######################
*/

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TrackerScreen(
    viewModel: TrackerViewModel = hiltViewModel(),
    onNavToTrends: () -> Unit,
    onNavToCalc: () -> Unit
) {
    var addBeverage by remember { mutableStateOf(false) }
    val beverageName by viewModel.beverageName.collectAsStateWithLifecycle()
    val caffeineAmt by viewModel.caffeineAmt.collectAsStateWithLifecycle()
    val logs by viewModel.logs.collectAsStateWithLifecycle()
    val totalCaffeine by viewModel.totalCaffeine.collectAsStateWithLifecycle()
    val lastLogTime by viewModel.lastLogTime.collectAsStateWithLifecycle()
//    var editingLog by remember { mutableStateOf<CaffeineLog?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE, MMM d")

        Text(
            text = today.format(formatter),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )

        if (addBeverage) {
            Dialog(
                onDismissRequest = { addBeverage = false }
            ) {
                Header(
                    beverageName = beverageName,
                    caffeineAmt = caffeineAmt,
                    onBeverageNameChange = viewModel::onBeverageNameChange,
                    onCaffeineAmtChange = viewModel::onCaffeineAmtChange,
                    onClickLog = {
                        viewModel.logCaffeine()
                        addBeverage = false
                    }
                )
            }
        }
        // Display logged beverages of the day:
        Spacer(Modifier.height(20.dp))
        LogsRow(
            logs = logs,
            onDelete = { id -> viewModel.deleteLog(id) },
            onAddBeverage = { addBeverage = true }
        )
        Spacer(Modifier.height(20.dp))

        MiniTrendsScreen(
            totalCaffeine = totalCaffeine,
            lastLogTime = lastLogTime,
            onNavigate = onNavToTrends
        )
        Spacer(Modifier.height(20.dp))
        MiniCalcScreen(onNavToCalc)
    }
}

@Composable
fun LogsRow(
    logs: List<CaffeineLog>,
    onDelete: (Long) -> Unit,
    onAddBeverage: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Logged Beverages",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3A332C)
                )

                IconButton(onClick = onAddBeverage) {
                    Icon(
                        imageVector = Icons.Rounded.Coffee,
                        contentDescription = "Coffee",
                        tint = Color(0xFF6F4E37)
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            if (logs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nothing logged yet...",
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }
            } else {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(logs.reversed(), key = { it.id }) { log ->
                        LogCard(
                            log = log,
                            onClick = { onDelete(log.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LogCard(
    log : CaffeineLog,
    onClick: () -> Unit
) {
    Card() {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = log.beverageName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(10.dp))
                IconButton(onClick = onClick) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Remove"
                    )
                }
            }
            Spacer(Modifier.width(10.dp))
            Text("${log.caffeineAmt} mg", fontSize = 14.sp)
        }
    }
}

@Preview
@Composable
private fun LogCardPreview() {
    LogCard(
        CaffeineLog(
            beverageName = "Cold Brew",
            caffeineAmt = "250.0"
        ),
        {}
    )
}