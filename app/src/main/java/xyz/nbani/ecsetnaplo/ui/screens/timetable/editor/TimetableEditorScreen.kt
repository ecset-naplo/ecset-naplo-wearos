package xyz.nbani.ecsetnaplo.ui.screens.timetable.editor

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import androidx.wear.compose.material3.AnimatedPage
import androidx.wear.compose.material3.PagerScaffoldDefaults
import xyz.nbani.ecsetnaplo.ui.screens.timetable.components.DayPage
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun TimetableEditorScreen(
    viewModel: TimetableViewModel,
    isEditor: Boolean,
) {
    val weekStart = remember { LocalDate.now().with(DayOfWeek.MONDAY) }
    val todayIndex = remember {
        ChronoUnit.DAYS.between(weekStart, LocalDate.now()).toInt()
    }

    val pagerState = rememberPagerState(initialPage = todayIndex, pageCount = { 7 })

    HorizontalPager(
        state = pagerState,
        flingBehavior = PagerScaffoldDefaults.snapWithSpringFlingBehavior(state = pagerState)
    ) { page ->
        val date = weekStart.plusDays(page.toLong())
        AnimatedPage(pageIndex = page, pagerState = pagerState) {
            DayPage(viewModel = viewModel, date = date)
        }
    }
}