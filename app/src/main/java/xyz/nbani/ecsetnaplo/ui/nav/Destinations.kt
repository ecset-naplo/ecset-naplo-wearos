package xyz.nbani.ecsetnaplo.ui.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import xyz.nbani.ecsetnaplo.data.local.entity.RegularPeriod
import xyz.nbani.ecsetnaplo.data.serialization.LocalDateSerializer
import xyz.nbani.ecsetnaplo.data.serialization.LocalTimeSerializer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

sealed interface Destination : NavKey {
    // Main routes
    @Serializable data object Main : Destination

    // Timetable routes
    @Serializable data class Timetable(val isEditor: Boolean) : Destination
    @Serializable data class PeriodDetails(
        @Serializable(with = LocalDateSerializer::class) val date: LocalDate
    ) : Destination
    @Serializable data class AddPeriodPicker(
        val day: DayOfWeek,
        val period: Int,
        @Serializable(with = LocalTimeSerializer::class) val startTime: LocalTime,
        @Serializable(with = LocalDateSerializer::class) val validFrom: LocalDate
    ) : Destination {

    }
    @Serializable data class AddRegularPeriod(
        val day: DayOfWeek ,
        val period: Int,
        @Serializable(with = LocalTimeSerializer::class) val startTime: LocalTime,
        @Serializable(with = LocalDateSerializer::class) val validFrom: LocalDate
    ) : Destination
    @Serializable data class EditPeriod(
       @Serializable(with = LocalDateSerializer::class) val dateDefault: LocalDate
    ) : Destination

    // Rooms screen
    @Serializable data class Rooms(val isEditor: Boolean) : Destination
    @Serializable data object AddRoom : Destination
    @Serializable data class EditRoom(val id: Long) : Destination
    // Teachers screen
    @Serializable data class Teachers(val isEditor: Boolean) : Destination
    @Serializable data object AddTeacher : Destination
    @Serializable data class EditTeacher(val id: Long) : Destination
    @Serializable data object DeleteTeacher : Destination

    // Lessons screen
    @Serializable data class Lessons(val isEditor: Boolean) : Destination
    @Serializable data object AddLesson : Destination
    @Serializable data class EditLesson(val id: Long) : Destination
}