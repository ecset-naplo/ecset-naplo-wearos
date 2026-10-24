package xyz.nbani.ecsetnaplo.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import xyz.nbani.ecsetnaplo.ui.screens.timetable.editor.TimetableEditorScreen
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
    navController: NavHostController = rememberSwipeDismissableNavController(),
    timetableViewModel: TimetableViewModel,
) {
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = Destination.Main.route,
        modifier = modifier
    ) {
        // Main routes
        composable(Destination.Main.route) {
            MainPager(
                timetableViewModel = timetableViewModel,
                onOpenTimetable = { isEditor ->
                    navController.navigate("timetable/$isEditor")
                },
                onOpenRoomsScreen = {
                    isEditor -> navController.navigate("rooms/$isEditor")
                },
                onOpenTeachersScreen = {
                    isEditor -> navController.navigate("teachers/$isEditor")
                }
            )
        }

        // Timetable editor
        composable (
            route = Destination.Timetable.route,
            arguments = listOf(
                navArgument("isEditor") {
                    type = NavType.BoolType
                }
            )
        ) { backStackEntry ->
            val isEditor = backStackEntry.arguments
                ?.getBoolean("isEditor") ?: false

            TimetableEditorScreen(
                viewModel = timetableViewModel,
                isEditor = isEditor
            )
        }

        // Rooms screen
        composable(
            route = Destination.Rooms.route,
            arguments = listOf(
                navArgument("isEditor") {
                    type = NavType.BoolType
                }
            )
        ) { backStackEntry ->
            val isEditor = backStackEntry.arguments
                ?.getBoolean("isEditor") ?: false

            RoomsScreen(
                viewModel = timetableViewModel,
                onOpenAddRoomScreen = { navController.navigate(Destination.AddRoom.route) },
                onOpenEditRoomScreen = { id -> navController.navigate("editRoom/$id") },
                isEditor = isEditor
            )
        }
        composable(route = Destination.AddRoom.route) {
            AddRoom(
                viewModel = timetableViewModel,
                popNavStackBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Destination.EditRoom.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments
                ?.getLong("id") ?: return@composable

            EditRoom(
                viewModel = timetableViewModel,
                popNavStackBack = { navController.popBackStack() },
                roomId = id
            )
        }

        // Teachers screen
        composable(
            route = Destination.Teachers.route,
            arguments = listOf(
                navArgument("isEditor") {
                    type = NavType.BoolType
                }
            )
        ) { backStackEntry ->
            val isEditor = backStackEntry.arguments
                ?.getBoolean("isEditor") ?: false

            TeachersScreen(
                viewModel = timetableViewModel,
                onOpenAddTeacherScreen = { navController.navigate(Destination.AddTeacher.route) },
                onOpenEditTeacherScreen = { id -> navController.navigate("editTeacher/$id") },
                isEditor = isEditor
            )
        }
        composable(route = Destination.AddTeacher.route) {
            AddTeacher(
                viewModel = timetableViewModel,
                popNavStackBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Destination.EditTeacher.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments
                ?.getLong("id") ?: return@composable

            EditTeacher(
                viewModel = timetableViewModel,
                onOpenDeleteTeacherScreen = { navController.navigate(Destination.DeleteTeacher.route) },
                popNavStackBack = { navController.popBackStack() },
                teacherId = id
            )
        }
        composable(
            route = Destination.DeleteTeacher.route
        ) {
            DeleteTeacher(
                viewModel = timetableViewModel,
                popNavStackBack = { navController.popBackStack() }
            )
        }
    }
}