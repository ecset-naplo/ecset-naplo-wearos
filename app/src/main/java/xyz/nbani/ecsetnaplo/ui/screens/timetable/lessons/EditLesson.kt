package xyz.nbani.ecsetnaplo.ui.screens.timetable.lessons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import xyz.nbani.ecsetnaplo.R
import xyz.nbani.ecsetnaplo.data.model.LessonType
import xyz.nbani.ecsetnaplo.ui.components.ClickRowElement
import xyz.nbani.ecsetnaplo.ui.components.TextBox
import xyz.nbani.ecsetnaplo.ui.components.sectionHeader
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel

@Composable
fun EditLesson(
    viewModel: TimetableViewModel,
    onOpenTeachersScreen: (Boolean) -> Unit,
    popNavStackBack: () -> Unit,
    lessonId: Long,
) {
    LaunchedEffect(lessonId) {
        if (viewModel.lesson?.id != lessonId) viewModel.loadLesson(lessonId)
    }

    val lesson = viewModel.lesson
    val selectedTeacher = viewModel.teacher

    val state = rememberTransformingLazyColumnState()
    val spec = rememberTransformationSpec()

    val shortLessonNameState = rememberTextFieldState()
    val longLessonNameState = rememberTextFieldState()

    var elementsEnabled by remember { mutableStateOf(true) }

    var selectedLessonType by rememberSaveable { mutableStateOf<LessonType?>(null) }

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showLessonTypeDialog by remember { mutableStateOf(false) }

    var initialized by rememberSaveable { mutableStateOf(false) }
    var navigating by remember { mutableStateOf(false) }

    LaunchedEffect(lesson?.id) {
        lesson?.let {
            if (!initialized) {
                shortLessonNameState.setTextAndPlaceCursorAtEnd(it.shortSubjectName)
                longLessonNameState.setTextAndPlaceCursorAtEnd(it.longSubjectName)
                selectedLessonType = it.lessonType
                initialized = true
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (!navigating) {
                viewModel.clearTeacher()
                viewModel.clearLesson()
            }
        }
    }

    ScreenScaffold(
        scrollState = state
    ) { contentPadding ->
        TransformingLazyColumn(
            state = state,
            contentPadding = contentPadding.let {
                PaddingValues(
                    start = it.calculateStartPadding(LayoutDirection.Ltr),
                    top = it.calculateTopPadding(),
                    end = it.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = it.calculateBottomPadding() + 26.dp
                )
            },
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (lesson != null) {
                sectionHeader(R.string.short_name)
                item {
                    TextBox(
                        state = shortLessonNameState,
                        spec = spec,
                        itemScope = this,
                        modifier = Modifier.transformedHeight(this, spec),
                        decorator = { innerTextField ->
                            Box {
                                if (shortLessonNameState.text.isEmpty()) {
                                    Text(stringResource(R.string.short_name))
                                }
                                innerTextField()
                            }
                        },
                        enabled = elementsEnabled
                    )
                }

                sectionHeader(R.string.long_name)
                item {
                    TextBox(
                        state = longLessonNameState,
                        spec = spec,
                        itemScope = this,
                        modifier = Modifier.transformedHeight(this, spec),
                        decorator = { innerTextField ->
                            Box {
                                if (longLessonNameState.text.isEmpty()) {
                                    Text(stringResource(R.string.long_name))
                                }
                                innerTextField()
                            }
                        },
                        enabled = elementsEnabled
                    )
                }

                sectionHeader(R.string.icon)
                item {
                    ClickRowElement(
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec),
                        icon = selectedLessonType?.icon,
                        title = stringResource(
                            selectedLessonType?.localized
                                ?: R.string.select_icon
                        ),
                        enabled = elementsEnabled,
                        onClick = { showLessonTypeDialog = true }
                    )
                }

                sectionHeader(R.string.teacher)
                item {
                    ClickRowElement(
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec),
                        icon = Icons.Default.Person,
                        title = selectedTeacher?.name ?: stringResource(R.string.select_teacher),
                        enabled = elementsEnabled,
                        onClick = {
                            navigating = true
                            onOpenTeachersScreen(false)
                        }
                    )
                }

                item {
                    ClickRowElement(
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec),
                        icon = Icons.Default.Save,
                        title = stringResource(R.string.save),
                        enabled = elementsEnabled,
                        onClick = {
                            val shortName = shortLessonNameState.text.toString().trim()
                            val longName = longLessonNameState.text.toString().trim()
                            val lessonType = selectedLessonType ?: lesson.lessonType

                            val updatedLesson = lesson.copy(
                                shortSubjectName = shortName,
                                longSubjectName = longName,
                                lessonType = lessonType,
                                teacherId = selectedTeacher?.id
                            )
                            viewModel.updateLesson(updatedLesson)
                            popNavStackBack()
                        }
                    )
                }


                item {
                    ClickRowElement(
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec),
                        icon = Icons.Default.Delete,
                        title = stringResource(R.string.delete_lesson),
                        enabled = elementsEnabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        onClick = {
                            showDeleteConfirmDialog = true
                        }
                    )
                }
            } else {
                item {
                    CircularProgressIndicator()
                }
            }
        }

        // delete room confirm dialog
        if (showDeleteConfirmDialog) {
            DeleteConfirmationDialog(
                onConfirm = { viewModel.deleteLesson(lesson!!); popNavStackBack() },
                onDismiss = { showDeleteConfirmDialog = false },
                visible = true
            )
        }

        // lesson type (icon) picker dialog
        if (showLessonTypeDialog) {
            LessonTypeAlertDialog(
                onDismiss = { showLessonTypeDialog = false },
                visible = true,
                selectedLessonType = selectedLessonType,
                onSelectSpecifiedLessonType = { lessonType ->
                    selectedLessonType = lessonType
                }
            )
        }
    }
}