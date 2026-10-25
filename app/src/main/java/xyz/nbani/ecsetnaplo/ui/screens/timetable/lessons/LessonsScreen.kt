package xyz.nbani.ecsetnaplo.ui.screens.timetable.lessons

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import xyz.nbani.ecsetnaplo.R
import xyz.nbani.ecsetnaplo.ui.components.ClickRowElement
import xyz.nbani.ecsetnaplo.ui.components.sectionHeader
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel

@Composable
fun LessonsScreen(
    viewModel: TimetableViewModel,
    onOpenAddLessonScreen: () -> Unit,
    onOpenEditLessonScreen: (Long) -> Unit,
    isEditor: Boolean,
    onSelectLesson: (() -> Unit)? = null
) {
    LaunchedEffect(isEditor, onSelectLesson == null) {
        if (isEditor && onSelectLesson == null) {
            Log.w("LessonsScreen", "onSelectLesson was not passed even though isEditor is true")
        }
    }

    val state = rememberTransformingLazyColumnState()
    val spec = rememberTransformationSpec()

    val lessons by viewModel.lessons.collectAsStateWithLifecycle()

    ScreenScaffold(
        scrollState = state
    ) { contentPadding ->
        TransformingLazyColumn(
            state = state,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            sectionHeader(R.string.lessons)

            lessons?.let {
                items(it.size) { index ->
                    val lesson = it[index]
                    ClickRowElement(
                        icon = lesson.lessonType.icon,
                        title = lesson.shortSubjectName,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = {
                            if (isEditor) {
                                onOpenEditLessonScreen(lesson.id)
                            } else {
                                onSelectLesson?.invoke()
                            }
                        },
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec),
                        onLongClick = {
                            onOpenEditLessonScreen(lesson.id)
                        }
                    )
                }
            }
            item {
                ClickRowElement(
                    icon = Icons.Default.Add,
                    title = stringResource(R.string.add_lesson),
                    border = BorderStroke(5.dp, Color.White),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    onClick = { onOpenAddLessonScreen() },
                    modifier = Modifier.transformedHeight(this, spec),
                    transformation = SurfaceTransformation(spec),
                )
            }
        }
    }
}