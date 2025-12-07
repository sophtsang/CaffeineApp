package com.example.caffiene.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.EmojiFoodBeverage
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Spa
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caffiene.data.model.Beverage
import com.example.caffiene.data.model.User
import com.example.caffiene.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import okhttp3.OkHttpClient
import java.time.LocalDate
import javax.inject.Inject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
data class CaffeineLog (
    val id: Long = System.currentTimeMillis(),
    val beverageName: String,
    val caffeineAmt: String,
    val timeStamp: LocalDateTime = LocalDateTime.now()
)

data class BeverageUiState(
    val isLoading: Boolean = false,
    val beverages: List<Beverage> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class TrackerViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val _bevUiState = MutableStateFlow(BeverageUiState(isLoading = true))
    val bevUiState = _bevUiState.asStateFlow()
    private val _beverageName = MutableStateFlow("")
    val beverageName = _beverageName.asStateFlow()

    private val _caffeineAmt = MutableStateFlow("")
    val caffeineAmt = _caffeineAmt.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    private val _timeStamp = MutableStateFlow("")
    @RequiresApi(Build.VERSION_CODES.O)
    val timeStamp = _timeStamp.asStateFlow()

    private val _logs = MutableStateFlow<List<CaffeineLog>>(emptyList())
    val logs : StateFlow<List<CaffeineLog>> = _logs.asStateFlow()

    private val _totalCaffeine = MutableStateFlow(0f)
    val totalCaffeine = _totalCaffeine.asStateFlow()

    private val _caffeineLimit = MutableStateFlow(0f)
    val caffeineLimit = _caffeineLimit.asStateFlow()

    private val _lastLogTime = MutableStateFlow<LocalDateTime?>(null)
    val lastLogTime: StateFlow<LocalDateTime?> = _lastLogTime.asStateFlow()

    private val _dailyTotals = MutableStateFlow<Map<LocalDate, Float>>(emptyMap())
    val dailyTotals: StateFlow<Map<LocalDate, Float>> = _dailyTotals.asStateFlow()

    private val _beverageList = MutableStateFlow<Map<Int, Beverage>>(emptyMap())
    val beverageList: StateFlow<Map<Int, Beverage>> = _beverageList.asStateFlow()

    private val _nextBeverageId = MutableStateFlow<Int>(0)
    val nextBeverageId: StateFlow<Int> = _nextBeverageId.asStateFlow()

    fun onBeverageNameChange(name : String) {
        _beverageName.value = name
    }

    fun onCaffeineAmtChange(amt : String) {
        _caffeineAmt.value = if (amt.toFloatOrNull() != null) amt else ""
    }

    fun onCaffeineLimitChange(amt : Float) {
        _caffeineLimit.value = amt
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onTimeStampChange(time : String) {
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm", Locale.US)

        try {
            LocalDateTime.parse(time, formatter)
            _timeStamp.value = time
        } catch (e: DateTimeParseException) {
            _timeStamp.value = LocalDateTime.now().toString()
        }
    }

    fun listBeverages() {
        viewModelScope.launch {
            repository.getBeverages()
                .onSuccess { bevs ->
                    _beverageList.value = bevs.associateBy { it.id }
                }
        }
    }

    fun createBeverage(
        id: Int = _nextBeverageId.value,
        name: String,
        caffeineContent: Int,
        category: String
    ) {
        val newBev = Beverage (
            id = id,
            name = name,
            caffeineContentMg = caffeineContent,
            category = category
        )

        viewModelScope.launch {
            repository.createBeverage(newBev)
                .onSuccess{ _ ->
                    _nextBeverageId.value++
                    _beverageList.value = _beverageList.value + (id to newBev)
                }
            listBeverages()
        }
    }

    fun deleteBeverage(
        id: Int
    ) {
        viewModelScope.launch {
            repository.deleteBeverage(id)
                .onSuccess {
                    _beverageList.value = _beverageList.value - id
                    delay(100)
                    listBeverages()
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun logCaffeine() {
        val name = _beverageName.value
        val amt = _caffeineAmt.value
        val time = _timeStamp.value

        if (name.isNotBlank() && amt.isNotBlank()) {
            val log = CaffeineLog(
                beverageName = name,
                caffeineAmt = amt
            )

            _logs.value = _logs.value + log
            _lastLogTime.value = log.timeStamp
            _totalCaffeine.value += (amt.toFloatOrNull() ?: 0f)
            _beverageName.value = ""
            _caffeineAmt.value = ""

            recalculateDailyTotals()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteLog(id: Long) {
        val logToDelete = _logs.value.find { it.id == id }
        logToDelete?.caffeineAmt?.toFloatOrNull()?.let { amt ->
            _totalCaffeine.value -= amt
        }
        _logs.value = _logs.value.filterNot { it.id == id }
        _lastLogTime.value = _logs.value.maxByOrNull { it.timeStamp }?.timeStamp

        recalculateDailyTotals()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun recalculateDailyTotals() {
        _dailyTotals.value = _logs.value
            .groupBy { it.timeStamp.toLocalDate() }
            .mapValues { entry ->
                entry.value.fold(0f) { acc, log -> acc + (log.caffeineAmt.toFloatOrNull() ?: 0f) }
            }
    }
}
