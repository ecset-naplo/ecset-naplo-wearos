package xyz.nbani.ecsetnaplo.ui.screens.timetable.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import xyz.nbani.ecsetnaplo.R
import xyz.nbani.ecsetnaplo.ui.components.sectionHeader
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun DayPage(
    viewModel: TimetableViewModel,
    date: LocalDate,
    isEditor: Boolean = false
) {
    val state = rememberTransformingLazyColumnState()
    val spec = rememberTransformationSpec()

    val periodItems by viewModel.periods.collectAsStateWithLifecycle()

    val locale = LocalConfiguration.current.locales[0]
    val dateText = date.format(
        DateTimeFormatter
            .ofLocalizedDate(FormatStyle.FULL)
            .withLocale(locale)
    )

    ScreenScaffold(
        scrollState = state
    ) { contentPadding ->
        TransformingLazyColumn(
            state = state,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            sectionHeader(dateText)

            if (periodItems.isEmpty()) {
                item { Text(stringResource(R.string.no_lessons_today_get_rest)) }
            } else {
                items(periodItems.size) { index ->
                    val periodItem = periodItems[index]
                    PeriodCard(
                        periodItem = periodItem,
                        onClick = {  },
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec)
                    )
                }
            }
        }
    }

}