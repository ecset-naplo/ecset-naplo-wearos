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
import xyz.nbani.ecsetnaplo.data.local.entity.IrregularPeriod
import xyz.nbani.ecsetnaplo.data.local.entity.Lesson
import xyz.nbani.ecsetnaplo.data.local.entity.RegularPeriod
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

    // Regular period
    var regularPeriod by mutableStateOf<RegularPeriod?>(null)
        private set

    fun loadRegularPeriod(id: Long)  {
        regularPeriod = null
        viewModelScope.launch {
            regularPeriod = repository.getRegularPeriod(id)
        }
    }

    fun clearRegularPeriod() { regularPeriod = null }

    fun insertRegularPeriod(period: RegularPeriod) = viewModelScope.launch {
        repository.insertRegularPeriod(period)
    }

    fun updateRegularPeriod(period: RegularPeriod) = viewModelScope.launch {
        repository.updateRegularPeriod(period)
    }

    fun deleteRegularPeriod(period: RegularPeriod) = viewModelScope.launch {
        repository.deleteRegularPeriod(period)
    }


    // Irregular period
    fun insertIrregularPeriod(period: IrregularPeriod) = viewModelScope.launch {
        repository.insertIrregularPeriod(period)
    }

    fun updateIrregularPeriod(period: IrregularPeriod) = viewModelScope.launch {
        repository.insertIrregularPeriod(period)
    }

    fun deleteIrregularPeriod(period: IrregularPeriod) = viewModelScope.launch {
        repository.insertIrregularPeriod(period)
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

    fun clearRoom() {
        room = null
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
        teacher = null
        viewModelScope.launch {
            teacher = repository.getTeacher(id)
        }
    }

    fun clearTeacher() {
        teacher = null
    }

    fun insertTeacher(teacher: Teacher) = viewModelScope.launch { repository.insertTeacher(teacher) }

    fun updateTeacher(teacher: Teacher) = viewModelScope.launch { repository.updateTeacher(teacher) }

    // in delete functions, teacherEntry is used because the teacher variable is used as well in fun
    fun deleteTeacherWithKeepLessons(teacherEntry: Teacher) = viewModelScope.launch {
        if (teacher?.id == teacherEntry.id) { teacher = null }
        repository.deleteTeacherWithKeepLessons(teacherEntry)
    }

    fun deleteTeacherWithDeleteLessons(teacherEntry: Teacher) = viewModelScope.launch {
        if (teacher?.id == teacherEntry.id) { teacher = null }
        repository.deleteTeacherWithDeleteLessons(teacherEntry)
    }

    // Lesson
    // Room
    val lessons: StateFlow<List<Lesson>?> = repository.observeAllLessons()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5), null)

    var lesson by mutableStateOf<Lesson?>(null)
        private set

    fun loadLesson(id: Long) {
        lesson = null
        teacher = null
        viewModelScope.launch {
            lesson = repository.getLesson(id)
            teacher = lesson?.teacherId?.let { repository.getTeacher(it) }
        }
    }

    fun clearLesson() {
        lesson = null
    }

    fun insertLesson(lesson: Lesson) = viewModelScope.launch { repository.insertLesson(lesson) }

    fun updateLesson(lesson: Lesson) = viewModelScope.launch { repository.updateLesson(lesson) }

    fun deleteLesson(lesson: Lesson) = viewModelScope.launch { repository.deleteLesson(lesson) }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as EcsetApp
                TimetableViewModel(app.timetableRepository)
            }
        }
    }
}