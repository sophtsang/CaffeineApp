package com.example.caffiene.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.caffiene.R

data class Beverage(
    val name: String,
    val caffeine: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun BeverageScreen() {
    val beverages = listOf(
        Beverage("Espresso", "63mg", Icons.Default.LocalCafe, Color(0xFF6F4E37)),
        Beverage("Latte", "75mg", Icons.Default.EmojiFoodBeverage, Color(0xFFA0826D)),
        Beverage("Cappuccino", "80mg", Icons.Default.LocalBar, Color(0xFF9C7A5E)),
        Beverage("Celsius", "80mg", Icons.Default.BatteryChargingFull, Color(0xFFFF6B6B)),
        Beverage("Matcha", "70mg", Icons.Default.Spa, Color(0xFF7CB342)),
        Beverage("Cold Brew", "200mg", Icons.Default.AcUnit, Color(0xFF5D4E37)),
    )

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
                .padding(16.dp)
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

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(beverages) { beverage ->
                    BeverageCard(beverage)
                }
            }
        }
    }
}

@Composable
fun BeverageCard(beverage: Beverage) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
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

@Preview(showBackground = true)
@Composable
fun BeveragePreview() {
    BeverageScreen()
}