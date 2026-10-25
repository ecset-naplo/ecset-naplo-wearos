package xyz.nbani.ecsetnaplo.ui.nav

sealed class Destination(val route: String) {
    // Main routes
    object Main : Destination("main")

    // Timetable route
    object Timetable : Destination("timetable/{isEditor}")

    // Rooms screen
    object Rooms : Destination("rooms/{isEditor}")
    object AddRoom : Destination("addRoom")
    object EditRoom : Destination("editRoom/{id}")
    // Teachers screen
    object Teachers : Destination("teachers/{isEditor}")
    object AddTeacher : Destination("addTeacher")
    object EditTeacher : Destination("editTeacher/{id}")
    object DeleteTeacher : Destination("deleteTeacher")

    // Lessons screen
    object Lessons : Destination("lessons/{isEditor}")
    object AddLesson : Destination("addLesson")
    object EditLesson : Destination("editLesson/{id}")
}