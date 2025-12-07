package com.example.caffiene.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.caffiene.R
import com.example.caffiene.data.model.Beverage
import com.example.caffiene.ui.components.Header
import com.example.caffiene.ui.viewmodels.TrackerViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Bev(
    val id: Int,
    val name: String,
    val caffeine: String,
    val icon: ImageVector,
    val color: Color
)

fun nameToIcon(
    name: String
) : ImageVector {
    val icon = when (name) {
        "Espresso" -> Icons.Default.LocalCafe
        "Latte" -> Icons.Default.EmojiFoodBeverage
        "Cappuccino" -> Icons.Default.LocalBar
        "Celsius" -> Icons.Default.BatteryChargingFull
        "Matcha" -> Icons.Default.Spa
        "Cold Brew" -> Icons.Default.AcUnit
        else -> Icons.Default.LocalCafe
    }
    return icon
}

fun categoryToColor(
    category: String
) : Color {
    val color = when (category) {
        "Coffee" -> Color(0xFF6F4E37)
        "Tea" -> Color(0xFF7CB342)
        "Energy Drink" -> Color(0xFFFF6B6B)
        "Water" -> Color(0xFF9FDBE0)
        else -> Color(0xFF5D4E37)
    }
    return color
}

@Composable
fun BeverageScreen(
    onNavigate: () -> Unit,
    viewModel: TrackerViewModel = hiltViewModel()
) {
    var addBeverage by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.listBeverages()
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            viewModel.listBeverages()
        }
    }

    val beverageList by viewModel.beverageList.collectAsState()

    val beverages = remember(beverageList) {
        beverageList.values.map { bev ->
            Bev(
                id = bev.id,
                name = bev.name,
                caffeine = bev.caffeineContentMg.toString(),
                icon = nameToIcon(bev.name),
                color = categoryToColor(bev.category)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF8E1),
                        Color(0xFFFFE0B2)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_caf),
                    contentDescription = "Caf++ Logo",
                    modifier = Modifier
                        .size(200.dp),
                    contentScale = ContentScale.Fit
                )
            }

            if (addBeverage) {
                Dialog(
                    onDismissRequest = { addBeverage = false }
                ) {
                    DrinkButton(
                        onDismiss = { addBeverage = false },
                        viewModel = viewModel
                    )
                }
            }

            IconButton(
                onClick = { addBeverage = true },
            ) {
                Icon(
                    imageVector = Icons.Rounded.Coffee,
                    contentDescription = "Coffee",
                    tint = Color(0xFF6F4E37)
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(beverages) { beverage ->
                    BeverageCard(
                        beverage = beverage,
                        onNavigate = onNavigate
                    )
                }
            }


        }
    }
}

@Composable
fun BeverageCard(
    beverage: Bev,
    viewModel: TrackerViewModel = hiltViewModel(),
    onNavigate: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable {
                viewModel.deleteBeverage(beverage.id)
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(beverage.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = beverage.icon,
                    contentDescription = beverage.name,
                    modifier = Modifier.size(36.dp),
                    tint = beverage.color
                )
            }


            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = beverage.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF3E2723)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = beverage.caffeine,
                fontSize = 13.sp,
                color = Color(0xFF8D6E63),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun DrinkButton(
    onDismiss: () -> Unit,
    viewModel: TrackerViewModel = hiltViewModel()
) {
    var beverageName by remember { mutableStateOf("") }
    var caffeineContent by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

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
                    onValueChange = { it -> beverageName = it },
                    placeholder = { Text("Red Bull") },
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    label = { Text("Caffeine (mg)") },
                    singleLine = true,
                    value = caffeineContent,
                    onValueChange = { it -> caffeineContent = it },
                    placeholder = { Text("80") },
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    label = { Text("Beverage Category") },
                    singleLine = true,
                    value = category,
                    onValueChange = { it -> category = it },
                    placeholder = { Text("Energy Drink") },
                    modifier = Modifier.weight(1f)
                )
            }
            IconButton(
                onClick = {
                    viewModel.createBeverage(
                        name = beverageName,
                        caffeineContent = caffeineContent.toInt(),
                        category = category
                    )
                    onDismiss()
                },
                enabled = (beverageName.isNotBlank() && caffeineContent.isNotBlank()
                        && caffeineContent.toIntOrNull() != null && category.isNotBlank())
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

@Preview(showBackground = true)
@Composable
fun BeveragePreview() {
    BeverageScreen(onNavigate = {})
}