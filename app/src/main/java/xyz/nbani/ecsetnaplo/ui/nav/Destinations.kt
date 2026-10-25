package xyz.nbani.ecsetnaplo.ui.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Destination : NavKey {
    // Main routes
    @Serializable data object Main : Destination

    // Timetable route
    @Serializable data class Timetable(val isEditor: Boolean) : Destination

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