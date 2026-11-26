package xyz.nbani.ecsetnaplo.ui.nav

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import xyz.nbani.ecsetnaplo.ui.model.MainPage
import xyz.nbani.ecsetnaplo.ui.screens.currentlesson.CurrentLessonScreen
import xyz.nbani.ecsetnaplo.ui.screens.settings.SettingsScreen
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel

@Composable
fun MainPager(
    timetableViewModel: TimetableViewModel,
    pendingDestination: String?,
    onDestinationHandled: () -> Unit,
    onOpenTimetable: (Boolean) -> Unit,
    onOpenRoomsScreen: (Boolean) -> Unit,
    onOpenTeachersScreen: (Boolean) -> Unit,
    onOpenLessonsScreen: (Boolean) -> Unit,
    onOpenDeveloperOptionsScreen: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { MainPage.entries.size })

    LaunchedEffect(pendingDestination) {
        if (pendingDestination == "current_lesson") {
            pagerState.scrollToPage(0)
        }
        if (pendingDestination != null) onDestinationHandled()
    }

    HorizontalPager(state = pagerState) { index ->
        when (MainPage.entries[index]) {
            MainPage.CurrentLesson -> CurrentLessonScreen(timetableViewModel, onOpenTimetable)
            MainPage.Settings -> SettingsScreen(
                timetableViewModel,
                onOpenTimetable,
                onOpenRoomsScreen,
                onOpenTeachersScreen,
                onOpenLessonsScreen,
                onOpenDeveloperOptionsScreen
            )
        }
    }
}