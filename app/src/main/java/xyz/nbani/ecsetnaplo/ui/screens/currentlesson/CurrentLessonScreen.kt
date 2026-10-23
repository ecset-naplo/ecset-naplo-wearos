package xyz.nbani.ecsetnaplo.ui.screens.currentlesson

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.Icon
import xyz.nbani.ecsetnaplo.R
import xyz.nbani.ecsetnaplo.ui.screens.currentlesson.components.rememberProgress
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel

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
                Text(stringResource(R.string.no_lessons_get_rest))
            } else {
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

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        currentPeriod.lesson.lessonType.icon, null
                    )
                    Text(currentPeriod.lesson.longSubjectName)
                    if (currentPeriod.roomEntity != null) {
                        Text(stringResource(R.string.room, currentPeriod.roomEntity.name))
                    }
                }
            }
        }
    }
}