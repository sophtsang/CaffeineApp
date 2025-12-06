package com.example.caffiene.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.caffiene.ui.viewmodels.AuthViewModel
import com.example.caffiene.ui.viewmodels.TrackerViewModel

@Composable
fun LogInScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    trackerModel: TrackerViewModel = hiltViewModel(),
    onNavigate: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var weightLbsText by remember { mutableStateOf("160") }     // Keep as Text for input
    var dailyCaffeineLimitText by remember { mutableStateOf("400") } // Keep as Text for input

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Use ViewModel state for loading/error indicators
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = uiState.isLoading

    // --- Validation Logic -
    val isPasswordValid = password.length >= 6
    val passwordsMatch = password == confirmPassword
    val isWeightValid = weightLbsText.toDoubleOrNull() != null
    val isLimitValid = dailyCaffeineLimitText.toIntOrNull() != null

    val isFormValid = username.isNotBlank() &&
            email.isNotBlank() &&
            isPasswordValid &&
            passwordsMatch &&
            isWeightValid &&
            isLimitValid

    fun handleSubmit() {
        val limit = dailyCaffeineLimitText.toIntOrNull() ?: 0
        val weight = weightLbsText.toDoubleOrNull() ?: 0.0

        viewModel.register(
            username = username,
            email = email,
            password = password,
            dailyCaffeineLimit = limit,
            weightLbs = weight,
        )

        trackerModel.onCaffeineLimitChange(limit.toFloat())

        onNavigate()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            supportingText = { Text("Must be unique") }
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible)
                            "Hide password"
                        else
                            "Show password"
                    )
                }
            },
            supportingText = { Text("At least 6 characters") },
            isError = password.isNotEmpty() && password.length < 6
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Weight Field
            OutlinedTextField(
                value = weightLbsText,
                onValueChange = { weightLbsText = it.filter { char -> char.isDigit() || char == '.' } },
                label = { Text("Weight") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = weightLbsText.isNotEmpty() && !isWeightValid,
                trailingIcon = { Text("lbs") }
            )

            OutlinedTextField(
                value = dailyCaffeineLimitText,
                onValueChange = { dailyCaffeineLimitText = it.filter { char -> char.isDigit() } },
                label = { Text("Caffeine Limit") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = dailyCaffeineLimitText.isNotEmpty() && !isLimitValid,
                trailingIcon = { Text("mg") }
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = ::handleSubmit,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            Text("Sign Up")
        }
    }
}

