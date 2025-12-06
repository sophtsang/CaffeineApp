package com.example.caffiene.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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
@HiltViewModel
class TrackerViewModel @Inject constructor() : ViewModel() {
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

    private val _lastLogTime = MutableStateFlow<LocalDateTime?>(null)
    val lastLogTime: StateFlow<LocalDateTime?> = _lastLogTime.asStateFlow()

    private val _dailyTotals = MutableStateFlow<Map<LocalDate, Float>>(emptyMap())
    val dailyTotals: StateFlow<Map<LocalDate, Float>> = _dailyTotals.asStateFlow()

    fun onBeverageNameChange(name : String) {
        _beverageName.value = name
    }

    fun onCaffeineAmtChange(amt : String) {
        _caffeineAmt.value = if (amt.toFloatOrNull() != null) amt else ""
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
