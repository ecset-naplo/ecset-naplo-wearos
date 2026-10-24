package xyz.nbani.ecsetnaplo.ui.nav

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import xyz.nbani.ecsetnaplo.ui.model.MainPage
import xyz.nbani.ecsetnaplo.ui.screens.currentlesson.CurrentLessonScreen
import xyz.nbani.ecsetnaplo.ui.screens.settings.SettingsScreen
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel

@Composable
fun MainPager(
    timetableViewModel: TimetableViewModel,
    onOpenTimetable: (Boolean) -> Unit,
    onOpenRoomsScreen: (Boolean) -> Unit,
    onOpenTeachersScreen: (Boolean) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { MainPage.entries.size })

    HorizontalPager(state = pagerState) { index ->
        when (MainPage.entries[index]) {
            MainPage.CurrentLesson -> CurrentLessonScreen(timetableViewModel, onOpenTimetable)
            MainPage.Settings -> SettingsScreen(
                onOpenTimetable, onOpenRoomsScreen, onOpenTeachersScreen
            )
        }
    }
}