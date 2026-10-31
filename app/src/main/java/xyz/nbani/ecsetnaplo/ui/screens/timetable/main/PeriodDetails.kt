package xyz.nbani.ecsetnaplo.ui.screens.timetable.main

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import xyz.nbani.ecsetnaplo.R
import xyz.nbani.ecsetnaplo.data.local.entity.Teacher
import xyz.nbani.ecsetnaplo.ui.components.ClickRowElement
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun PeriodDetails(
    viewModel: TimetableViewModel,
    onOpenPeriodEditor: () -> Unit
) {
    val state = rememberTransformingLazyColumnState()
    val spec = rememberTransformationSpec()

    val period = viewModel.period
    val teacher = viewModel.teacher

    var teacherName by remember { mutableStateOf("") }

    var navigating by rememberSaveable { mutableStateOf(false) }
    val currentNavigating by rememberUpdatedState(navigating)

    LaunchedEffect(period) {
        period?.lesson?.teacherId?.let {
            viewModel.loadTeacher(it)
        }
        teacher?.let {
            teacherName = it.name
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (!currentNavigating) {
                viewModel.clearPeriod()
                viewModel.clearTeacher()
            }
        }
    }

    ScreenScaffold(
        scrollState = state,
    ) { contentPadding ->
        if (period != null) {
            TransformingLazyColumn(
                state = state,
                contentPadding = contentPadding.let {
                    PaddingValues(
                        start = it.calculateStartPadding(LayoutDirection.Ltr),
                        top = it.calculateTopPadding() + 10.dp,
                        end = it.calculateEndPadding(LayoutDirection.Ltr),
                        bottom = it.calculateBottomPadding() + 26.dp
                    )
                },
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Icon(period.lesson.lessonType.icon, contentDescription = null)
                }
                item {
                    Text(period.lesson.longSubjectName)
                }
                item {
                    Text(period.lesson.shortSubjectName, fontStyle = FontStyle.Italic)
                }
                item {
                    val startTime = period.startTime.format(
                        DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                    val endTime = period.endTime.format(
                        DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                    Text("$startTime - $endTime")
                }
                item {
                    Text(
                        text = period.roomEntity?.name ?: stringResource(R.string.no_room),
                        fontStyle = if (period.roomEntity != null) FontStyle.Italic else null
                    )
                }
                item {
                    Text(
                        text = teacherName,
                    )
                }
                item {
                    ClickRowElement(
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec),
                        icon = Icons.Default.Edit,
                        title = stringResource(R.string.edit_period),
                        onClick = { navigating = true; onOpenPeriodEditor() }
                    )
                }
            }
        }
    }
}