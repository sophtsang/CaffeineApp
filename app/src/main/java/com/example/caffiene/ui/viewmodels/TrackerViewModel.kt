package com.example.caffiene.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.time.LocalDateTime

data class CaffeineLog(
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

    private val _logs = MutableStateFlow<List<CaffeineLog>>(emptyList())
    val logs : StateFlow<List<CaffeineLog>> = _logs.asStateFlow()

    private val _totalCaffeine = MutableStateFlow(0f)
    val totalCaffeine = _totalCaffeine.asStateFlow()

    private val _lastLogTime = MutableStateFlow<LocalDateTime?>(null)
    val lastLogTime: StateFlow<LocalDateTime?> = _lastLogTime.asStateFlow()


    fun onBeverageNameChange(name : String) {
        _beverageName.value = name
    }

    fun onCaffeineAmtChange(amt : String) {
        _caffeineAmt.value = if (amt.toFloatOrNull() != null) amt else ""
    }

    fun logCaffeine() {
        val name = _beverageName.value
        val amt = _caffeineAmt.value

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
        }
    }

    fun updateLog(id: Long, newName: String, newAmt: String) {
        _logs.value = _logs.value.map { log ->
            if (log.id == id) log.copy(
                beverageName = newName,
                caffeineAmt = newAmt
            ) else log
        }
    }

    fun deleteLog(id: Long) {
        val logToDelete = _logs.value.find { it.id == id }
        logToDelete?.caffeineAmt?.toFloatOrNull()?.let { amt ->
            _totalCaffeine.value -= amt
        }
        _logs.value = _logs.value.filterNot { it.id == id }
        _lastLogTime.value = _logs.value.maxByOrNull { it.timeStamp }?.timeStamp
    }



}
