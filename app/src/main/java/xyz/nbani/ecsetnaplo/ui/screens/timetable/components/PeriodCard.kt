package xyz.nbani.ecsetnaplo.ui.screens.timetable.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import xyz.nbani.ecsetnaplo.R
import xyz.nbani.ecsetnaplo.data.local.entity.Lesson
import xyz.nbani.ecsetnaplo.data.local.entity.RegularPeriod
import xyz.nbani.ecsetnaplo.data.local.entity.RoomEntity
import xyz.nbani.ecsetnaplo.data.model.LessonType
import xyz.nbani.ecsetnaplo.data.model.Period
import xyz.nbani.ecsetnaplo.data.model.TimetableEntry
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun PeriodCard(
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation? = null,
    onClick: () -> Unit,
    periodItem: TimetableEntry,
) {
    if (periodItem is TimetableEntry.PeriodItem) {
        Button(
            icon = { Icon(periodItem.period.lesson.lessonType.icon, contentDescription = null) },
            label = { Text(periodItem.period.lesson.shortSubjectName) },
            secondaryLabel = periodItem.period.roomEntity?.let { { Text(it.name) } },
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            transformation = transformation,
            border = if (periodItem.isCurrent) BorderStroke(5.dp, Color.White) else null
        )
    } else if (periodItem is TimetableEntry.Break) {
        val startTime = periodItem.startTime.format(
            DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

        val endTime = periodItem.endTime.format(
            DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

        Button(
            label = { Text(stringResource(R.string.break_text)) },
            secondaryLabel = { Text("$startTime - $endTime") },
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            transformation = transformation,
            border = if (periodItem.isCurrent) BorderStroke(5.dp, Color.White) else null,
            colors = ButtonDefaults.buttonColors(
                MaterialTheme.colorScheme.secondaryDim
            )
        )
    }
}

class PeriodItemProvider : PreviewParameterProvider<TimetableEntry> {

    override val values = sequenceOf(
        TimetableEntry.PeriodItem(
            period = Period(
                regularPeriod = RegularPeriod(
                    lessonId = 1L,
                    roomId = 1L,
                    weekday = DayOfWeek.MONDAY,
                    period = 1,
                    startTime = LocalTime.of(8, 0),
                    endTime = LocalTime.of(8, 45),
                    validFrom = LocalDate.of(2026, 9, 1)
                ),
                lesson = Lesson(
                    longSubjectName = "Mathematics",
                    shortSubjectName = "Math",
                    lessonType = LessonType.MATH
                ),
                roomEntity = RoomEntity(name = "R1")
            ),
            isCurrent = false
        ),
        TimetableEntry.Break(
            previousPeriod = Period(
                regularPeriod = RegularPeriod(
                    lessonId = 1L,
                    roomId = 1L,
                    weekday = DayOfWeek.MONDAY,
                    period = 1,
                    startTime = LocalTime.of(8, 0),
                    endTime = LocalTime.of(8, 45),
                    validFrom = LocalDate.of(2026, 9, 1)
                ),
                lesson = Lesson(
                    longSubjectName = "Mathematics",
                    shortSubjectName = "Math",
                    lessonType = LessonType.MATH
                ),
                roomEntity = RoomEntity(name = "R1")
            ),
            nextPeriod = Period(
                regularPeriod = RegularPeriod(
                    lessonId = 2L,
                    roomId = 2L,
                    weekday = DayOfWeek.MONDAY,
                    period = 2,
                    startTime = LocalTime.of(8, 55),
                    endTime = LocalTime.of(9, 40),
                    validFrom = LocalDate.of(2026, 9, 1)
                ),
                lesson = Lesson(
                    longSubjectName = "English",
                    shortSubjectName = "English",
                    lessonType = LessonType.LANGUAGE
                ),
                roomEntity = RoomEntity(name = "R2")
            ),
            isCurrent = false
        ),
        TimetableEntry.Break(
            previousPeriod = Period(
                regularPeriod = RegularPeriod(
                    lessonId = 1L,
                    roomId = 1L,
                    weekday = DayOfWeek.MONDAY,
                    period = 1,
                    startTime = LocalTime.of(8, 0),
                    endTime = LocalTime.of(8, 45),
                    validFrom = LocalDate.of(2026, 9, 1)
                ),
                lesson = Lesson(
                    longSubjectName = "Mathematics",
                    shortSubjectName = "Math",
                    lessonType = LessonType.MATH
                ),
                roomEntity = RoomEntity(name = "R1")
            ),
            nextPeriod = Period(
                regularPeriod = RegularPeriod(
                    lessonId = 2L,
                    roomId = 2L,
                    weekday = DayOfWeek.MONDAY,
                    period = 2,
                    startTime = LocalTime.of(8, 55),
                    endTime = LocalTime.of(9, 40),
                    validFrom = LocalDate.of(2026, 9, 1)
                ),
                lesson = Lesson(
                    longSubjectName = "English",
                    shortSubjectName = "English",
                    lessonType = LessonType.LANGUAGE
                ),
                roomEntity = RoomEntity(name = "R2")
            ),
            isCurrent = true
        ),
        TimetableEntry.PeriodItem(
            period = Period(
                regularPeriod = RegularPeriod(
                    lessonId = 2L,
                    roomId = 2L,
                    weekday = DayOfWeek.MONDAY,
                    period = 2,
                    startTime = LocalTime.of(8, 55),
                    endTime = LocalTime.of(9, 40),
                    validFrom = LocalDate.of(2026, 9, 1)
                ),
                lesson = Lesson(
                    longSubjectName = "English",
                    shortSubjectName = "English",
                    lessonType = LessonType.LANGUAGE
                ),
                roomEntity = RoomEntity(name = "R2")
            ),
            isCurrent = true
        )
    )
}


@Preview(device = "id:wearos_small_round", showBackground = true)
@Composable
fun PeriodCardPreview(
    @PreviewParameter(PeriodItemProvider::class)
    periodItem: TimetableEntry
) {
    PeriodCard(
        periodItem = periodItem,
        onClick = { }
    )
}