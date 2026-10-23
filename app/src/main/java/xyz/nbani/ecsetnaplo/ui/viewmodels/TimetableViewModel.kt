package xyz.nbani.ecsetnaplo.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import xyz.nbani.ecsetnaplo.EcsetApp
import xyz.nbani.ecsetnaplo.data.local.entity.RoomEntity
import xyz.nbani.ecsetnaplo.data.local.entity.Teacher
import xyz.nbani.ecsetnaplo.data.model.Period
import xyz.nbani.ecsetnaplo.data.model.PeriodItem
import xyz.nbani.ecsetnaplo.data.repository.TimetableRepository
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class TimetableViewModel(
    private val repository: TimetableRepository
) : ViewModel() {
    private val selectedDate = MutableStateFlow(LocalDate.now())

    val date: StateFlow<LocalDate> = selectedDate

    val periods: StateFlow<List<PeriodItem>> = selectedDate
        .flatMapLatest { date ->
            combine(
                repository.observeAllPeriodsOfDate(date),
                repository.observeCurrentPeriod()
            ) { periods, current ->
                periods.map { PeriodItem(it, isCurrent = it == current) }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5), emptyList())

    val currentPeriod: StateFlow<Period?> = repository.observeCurrentPeriod()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5), null)

    fun selectDate(date: LocalDate) {
        selectedDate.value = date
    }

    // Room
    val rooms: StateFlow<List<RoomEntity>?> = repository.observeAllRooms()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5), null)

    var room by mutableStateOf<RoomEntity?>(null)
        private set

    fun loadRoom(id: Long) {
        room = null
        viewModelScope.launch {
            room = repository.getRoom(id)
        }
    }

    fun insertRoom(room: RoomEntity) = viewModelScope.launch { repository.insertRoom(room) }

    fun updateRoom(room: RoomEntity) = viewModelScope.launch { repository.updateRoom(room) }

    fun deleteRoom(room: RoomEntity) = viewModelScope.launch { repository.deleteRoom(room) }


    // Teacher
    val teachers: StateFlow<List<Teacher>?> = repository.observeAllTeachers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5), null)

    var teacher by mutableStateOf<Teacher?>(null)
        private set

    fun loadTeacher(id: Long) {
        viewModelScope.launch {
            room = repository.getRoom(id)
        }
    }

    fun insertTeacher(teacher: Teacher) = viewModelScope.launch { repository.insertTeacher(teacher) }

    fun updateTeacher(teacher: Teacher) = viewModelScope.launch { repository.updateTeacher(teacher) }

    fun deleteTeacher(teacher: Teacher) = viewModelScope.launch { repository.deleteTeacher(teacher) }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as EcsetApp
                TimetableViewModel(app.timetableRepository)
            }
        }
    }
}