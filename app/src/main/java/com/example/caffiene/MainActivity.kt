package com.example.caffiene

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.caffiene.ui.screens.CalculatorScreen
import com.example.caffiene.ui.screens.TrackerScreen
import com.example.caffiene.ui.screens.TrendsScreen
import com.example.caffiene.ui.theme.CaffieneTheme
import com.example.caffiene.ui.viewmodels.TrackerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaffieneTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        NavHost(
                            navController = navController,
                            startDestination = "main"
                        ) {
                            navigation(startDestination = "trackerscreen", route = "main") {
                                composable("trackerscreen") { backStackEntry ->
                                    val parentEntry = remember(backStackEntry) {
                                        navController.getBackStackEntry("main")
                                    }
                                    val viewModel: TrackerViewModel = hiltViewModel(parentEntry)

                                    TrackerScreen(
                                        viewModel = viewModel,
                                        onNavToTrends = { navController.navigate("trendsscreen") },
                                        onNavToCalc = { navController.navigate("calculatorscreen") }
                                    )
                                }

                                composable("trendsscreen") { backStackEntry ->
                                    val parentEntry = remember(backStackEntry) {
                                        navController.getBackStackEntry("main")
                                    }
                                    TrendsScreen()
                                }

                                composable("calculatorscreen") { backStackEntry ->
                                    val parentEntry = remember(backStackEntry) {
                                        navController.getBackStackEntry("main")
                                    }
                                    CalculatorScreen()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}