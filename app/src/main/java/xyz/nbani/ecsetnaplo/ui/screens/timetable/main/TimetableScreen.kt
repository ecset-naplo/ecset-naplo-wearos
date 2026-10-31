package xyz.nbani.ecsetnaplo.ui.screens.timetable.main

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import androidx.wear.compose.material3.AnimatedPage
import androidx.wear.compose.material3.PagerScaffoldDefaults
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import xyz.nbani.ecsetnaplo.ui.screens.timetable.components.DayPage
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@Composable
fun TimetableScreen(
    viewModel: TimetableViewModel,
    onOpenAddPeriodPickerScreen: (DayOfWeek, Int, LocalTime, LocalDate) -> Unit,
    onOpenPeriodDetailsScreen: (LocalDate) -> Unit,
    isEditor: Boolean,
) {
    val weekStart = remember { LocalDate.now().with(DayOfWeek.MONDAY) }
    val todayIndex = remember {
        ChronoUnit.DAYS.between(weekStart, LocalDate.now()).toInt()
    }

    val pagerState = rememberPagerState(initialPage = todayIndex, pageCount = { 7 })

    LaunchedEffect(pagerState, weekStart) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collectLatest { page ->
                val selectedDate = weekStart.plusDays(page.toLong())
                viewModel.selectDate(selectedDate)
            }
    }

    HorizontalPager(
        state = pagerState,
        flingBehavior = PagerScaffoldDefaults.snapWithSpringFlingBehavior(state = pagerState)
    ) { page ->
        val date = weekStart.plusDays(page.toLong())
        AnimatedPage(pageIndex = page, pagerState = pagerState) {
            DayPage(
                viewModel,
                date,
                onOpenAddPeriodPickerScreen,
                onOpenPeriodDetailsScreen,
                isEditor
            )
        }
    }
}