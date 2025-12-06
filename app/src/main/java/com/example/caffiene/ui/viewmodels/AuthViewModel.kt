package com.example.caffiene.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caffiene.data.model.User
import com.example.caffiene.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import javax.inject.Inject

data class UserRegistration(
    val username: String,
    val email: String,
    val password: String,
    val dailyCaffeineLimit: Int,
    val weightLbs: Double
)

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentUser: User? = null,
    val isAuthenticated: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(AuthUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    fun register(
        username: String,
        email: String,
        password: String,
        dailyCaffeineLimit: Int,
        weightLbs: Double,
    ) {

        viewModelScope.launch {
            repository.createUser(
                User(
                    username = username,
                    email = email,
                    passwordHash = password,
                    dailyCaffeineLimit = dailyCaffeineLimit,
                    weightLbs = weightLbs
                ))
                .onSuccess { userId ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = true
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Registration failed"
                        )
                    }
                }
        }
    }

    /**
     * Login existing user
     * NOTE: This requires adding authentication endpoint to Flask API
     */
//    fun login(
//        username: String,
//        password: String,
//        onSuccess: (User) -> Unit
//    ) {
//        viewModelScope.launch {
//            uiState = uiState.copy(isLoading = true, errorMessage = null)
//
//            repository.authenticateUser(username, password)
//                .onSuccess { user ->
//                    uiState = uiState.copy(
//                        isLoading = false,
//                        currentUser = user,
//                        isAuthenticated = true
//                    )
//                    onSuccess(user)
//                }
//                .onFailure { error ->
//                    uiState = uiState.copy(
//                        isLoading = false,
//                        errorMessage = when {
//                            error is NotImplementedError ->
//                                "Login endpoint not implemented yet. Please register instead."
//                            error.message?.contains("401") == true ->
//                                "Invalid username or password"
//                            else ->
//                                error.message ?: "Login failed"
//                        }
//                    )
//                }
//        }
//    }
}

