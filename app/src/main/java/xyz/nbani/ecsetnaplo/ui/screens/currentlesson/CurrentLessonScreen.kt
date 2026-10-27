package xyz.nbani.ecsetnaplo.ui.screens.currentlesson

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButton
import androidx.wear.compose.material3.IconButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import xyz.nbani.ecsetnaplo.R
import xyz.nbani.ecsetnaplo.ui.components.ClickRowElement
import xyz.nbani.ecsetnaplo.ui.screens.currentlesson.components.rememberProgress
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun CurrentLessonScreen(
    viewModel: TimetableViewModel,
    onOpenTimetable: (Boolean) -> Unit
) {
    val period by viewModel.currentPeriod.collectAsStateWithLifecycle()
    val currentPeriod = period

    ScreenScaffold {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (currentPeriod == null) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(
                        start = it.calculateStartPadding(LayoutDirection.Ltr),
                        top = it.calculateTopPadding(),
                        end = it.calculateEndPadding(LayoutDirection.Ltr),
                        bottom = it.calculateBottomPadding()
                    ),
                ) {
                    Text(
                        stringResource(R.string.no_lessons_get_rest),
                        textAlign = TextAlign.Center
                    )

                    ClickRowElement(
                        icon = Icons.Default.TableChart,
                        title = stringResource(R.string.timetable),
                        onClick = { onOpenTimetable(false) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                val (minutesTillEnd, secondsTillEnd) = timeLeftTillEnd(currentPeriod.endTime)

                val progress = rememberProgress(currentPeriod)
                val animated by animateFloatAsState(
                    targetValue = progress,
                    animationSpec = tween(1000, easing = LinearEasing),
                    label = "progress",
                )

                CircularProgressIndicator(
                    progress = { animated },
                    modifier = Modifier.fillMaxSize(),
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        currentPeriod.lesson.lessonType.icon, null
                    )
                    Text(currentPeriod.lesson.longSubjectName)
                    if (currentPeriod.roomEntity != null) {
                        Text(stringResource(R.string.room, currentPeriod.roomEntity.name))
                    }
                    Text(
                        text = when {
                            minutesTillEnd != 0L && secondsTillEnd != 0L -> {
                                stringResource(
                                    R.string.minutes_seconds_left,
                                    minutesTillEnd,
                                    secondsTillEnd
                                )
                            }
                            minutesTillEnd != 0L -> {
                                stringResource(R.string.minutes_left, minutesTillEnd)
                            }
                            else -> {
                                stringResource(R.string.seconds_left, secondsTillEnd)
                            }
                        },
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        stringResource(R.string.till_time, currentPeriod.endTime.format(
                            DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                        ),
                        style = MaterialTheme.typography.labelSmall
                    )

                    IconButton(
                        onClick = { onOpenTimetable(false) },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.TableChart,
                            contentDescription = stringResource(R.string.timetable),
                        )
                    }
                }
            }
        }
    }
}

private fun timeLeftTillEnd(endTime: LocalTime): Pair<Long, Long> {
    val now = LocalTime.now()
    val secondsLeft = Duration.between(now, endTime).seconds
    return (secondsLeft / 60) to (secondsLeft % 60)
}