package xyz.nbani.ecsetnaplo.ui.screens.timetable.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import androidx.wear.compose.material3.AnimatedPage
import androidx.wear.compose.material3.PagerScaffoldDefaults
import kotlinx.coroutines.flow.distinctUntilChanged
import xyz.nbani.ecsetnaplo.ui.screens.timetable.components.DayPage
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TimetableScreen(
    viewModel: TimetableViewModel,
    onOpenAddPeriodPickerScreen: (DayOfWeek, Int, LocalTime, LocalDate) -> Unit,
    onOpenPeriodDetailsScreen: (LocalDate) -> Unit,
    isEditor: Boolean,
) {
    var selectedDate by rememberSaveable { mutableStateOf(LocalDate.now()) }

    val weekStart = selectedDate.with(DayOfWeek.MONDAY)
    val currentWeekStart = rememberUpdatedState(weekStart)

    val pagerState = rememberPagerState(
        initialPage = selectedDate.dayOfWeek.value - 1,
        pageCount = { 7 }
    )

    LaunchedEffect(selectedDate) {
        val targetPage = selectedDate.dayOfWeek.value - 1
        if (pagerState.currentPage != targetPage) {
            pagerState.animateScrollToPage(targetPage)
        }
        viewModel.selectDate(selectedDate)
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .distinctUntilChanged()
            .collect { page ->
                val newDate = currentWeekStart.value.plusDays(page.toLong())
                if (newDate != selectedDate) selectedDate = newDate
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
                onChangeDate = { selectedDate = it },
                isEditor
            )
        }
    }
}