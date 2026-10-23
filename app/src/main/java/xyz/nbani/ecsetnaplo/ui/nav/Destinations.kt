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
}