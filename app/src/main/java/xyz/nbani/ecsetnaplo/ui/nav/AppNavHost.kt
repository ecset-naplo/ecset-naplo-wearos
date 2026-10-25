package xyz.nbani.ecsetnaplo.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.wear.compose.navigation3.rememberSwipeDismissableSceneStrategy
import xyz.nbani.ecsetnaplo.ui.screens.timetable.main.TimetableScreen
import xyz.nbani.ecsetnaplo.ui.screens.timetable.lessons.AddLesson
import xyz.nbani.ecsetnaplo.ui.screens.timetable.lessons.EditLesson
import xyz.nbani.ecsetnaplo.ui.screens.timetable.lessons.LessonsScreen
import xyz.nbani.ecsetnaplo.ui.screens.timetable.rooms.AddRoom
import xyz.nbani.ecsetnaplo.ui.screens.timetable.rooms.EditRoom
import xyz.nbani.ecsetnaplo.ui.screens.timetable.rooms.RoomsScreen
import xyz.nbani.ecsetnaplo.ui.screens.timetable.teachers.AddTeacher
import xyz.nbani.ecsetnaplo.ui.screens.timetable.teachers.DeleteTeacher
import xyz.nbani.ecsetnaplo.ui.screens.timetable.teachers.EditTeacher
import xyz.nbani.ecsetnaplo.ui.screens.timetable.teachers.TeachersScreen
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    timetableViewModel: TimetableViewModel,
) {
    val backStack = rememberNavBackStack(Destination.Main as NavKey)
    val strategy = rememberSwipeDismissableSceneStrategy<NavKey>()

    NavDisplay(
        backStack = backStack,
        sceneStrategies = listOf(strategy),
        modifier = modifier,
        entryProvider = entryProvider {
            // Main routes

            entry<Destination.Main> {
                MainPager(
                    timetableViewModel = timetableViewModel,
                    onOpenTimetable = { isEditor ->
                        backStack.add(Destination.Timetable(isEditor))
                    },
                    onOpenRoomsScreen = { isEditor ->
                        backStack.add(Destination.Rooms(isEditor))
                    },
                    onOpenTeachersScreen = { isEditor ->
                        backStack.add(Destination.Teachers(isEditor))
                    },
                    onOpenLessonsScreen = { isEditor ->
                        backStack.add(Destination.Lessons(isEditor))
                    }
                )
            }

            // Timetable
            entry<Destination.Timetable> { key ->
                TimetableScreen(
                    viewModel = timetableViewModel,
                    onOpenAddPeriodScreen = { /* TODO: backStack.add(Destination.AddPeriod()) */ },
                    isEditor = key.isEditor
                )
            }

            // Rooms screen
            entry<Destination.Rooms> { key ->
                RoomsScreen(
                    viewModel = timetableViewModel,
                    onOpenAddRoomScreen = { backStack.add(Destination.AddRoom) },
                    onOpenEditRoomScreen = { id -> backStack.add(Destination.EditRoom(id)) },
                    isEditor = key.isEditor
                )
            }
            entry<Destination.AddRoom> {
                AddRoom(
                    viewModel = timetableViewModel,
                    popNavStackBack = { backStack.removeLastOrNull() }
                )
            }
            entry<Destination.EditRoom> { key ->
                EditRoom(
                    viewModel = timetableViewModel,
                    popNavStackBack = { backStack.removeLastOrNull() },
                    roomId = key.id
                )
            }

            // Teachers screen
            entry<Destination.Teachers> { key ->
                TeachersScreen(
                    viewModel = timetableViewModel,
                    onOpenAddTeacherScreen = { backStack.add(Destination.AddTeacher) },
                    onOpenEditTeacherScreen = { id -> backStack.add(Destination.EditTeacher(id)) },
                    popNavStackBack = { backStack.removeLastOrNull() },
                    isEditor = key.isEditor
                )
            }
            entry<Destination.AddTeacher> {
                AddTeacher(
                    viewModel = timetableViewModel,
                    popNavStackBack = { backStack.removeLastOrNull() }
                )
            }
            entry<Destination.EditTeacher> { key ->
                EditTeacher(
                    viewModel = timetableViewModel,
                    onOpenDeleteTeacherScreen = { backStack.add(Destination.DeleteTeacher) },
                    popNavStackBack = { backStack.removeLastOrNull() },
                    teacherId = key.id
                )
            }
            entry<Destination.DeleteTeacher> {
                DeleteTeacher(
                    viewModel = timetableViewModel,
                    popNavStackBack = { backStack.removeLastOrNull() }
                )
            }

            // Lessons screen
            entry<Destination.Lessons> { key ->
                LessonsScreen(
                    viewModel = timetableViewModel,
                    onOpenAddLessonScreen = { backStack.add(Destination.AddRoom) },
                    onOpenEditLessonScreen = { id -> backStack.add(Destination.EditRoom(id)) },
                    onOpenAddLessonScreen = { backStack.add(Destination.AddLesson) },
                    onOpenEditLessonScreen = { id -> backStack.add(Destination.EditLesson(id)) },
                    isEditor = key.isEditor
                )
            }
            entry<Destination.AddLesson> {
                AddLesson(
                    viewModel = timetableViewModel,
                    onOpenTeachersScreen = { isEditor ->
                        backStack.add(Destination.Teachers(isEditor))
                    },
                    popNavStackBack = { backStack.removeLastOrNull() }
                )
            }
            entry<Destination.EditLesson> { key ->
                EditLesson(
                    viewModel = timetableViewModel,
                    onOpenTeachersScreen = { isEditor ->
                        backStack.add(Destination.Teachers(isEditor))
                    },
                    popNavStackBack = { backStack.removeLastOrNull() },
                    lessonId = key.id
                )
            }
        },
    )
}