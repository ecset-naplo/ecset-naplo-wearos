package xyz.nbani.ecsetnaplo.ui.screens.timetable.lessons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import xyz.nbani.ecsetnaplo.R
import xyz.nbani.ecsetnaplo.data.local.entity.Lesson
import xyz.nbani.ecsetnaplo.data.local.entity.Teacher
import xyz.nbani.ecsetnaplo.data.model.LessonType
import xyz.nbani.ecsetnaplo.ui.components.ClickRowElement
import xyz.nbani.ecsetnaplo.ui.components.TextBox
import xyz.nbani.ecsetnaplo.ui.components.sectionHeader
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel

@Composable
fun AddLesson(
    viewModel: TimetableViewModel,
    onOpenTeachersScreen: (Boolean) -> Unit,
    popNavStackBack: () -> Unit
) {
    val state = rememberTransformingLazyColumnState()
    val spec = rememberTransformationSpec()

    val shortLessonNameState = rememberTextFieldState()
    val longLessonNameState = rememberTextFieldState()

    var elementsEnabled by remember { mutableStateOf(true) }

    var selectedLessonType by rememberSaveable { mutableStateOf<LessonType?>(null) }
    var selectedTeacher by remember { mutableStateOf<Teacher?>(null) }

    var showLessonTypeDialog by remember { mutableStateOf(false) }
    var showLessonTypeMissingDialog by remember { mutableStateOf(false) }
    var showTeacherMissingWarningDialog by remember { mutableStateOf(false) }

    var navigating by remember { mutableStateOf(false) }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        selectedTeacher = viewModel.teacher
    }

    DisposableEffect(Unit) {
        onDispose {
            if (!navigating) viewModel.clearTeacher()
        }
    }

    fun addLesson(
        shortName: String,
        longName: String,
        lessonType: LessonType,
        teacher: Teacher?
    ) {
        elementsEnabled = false
        viewModel.insertLesson(
            Lesson(
                shortSubjectName = shortName,
                longSubjectName = longName,
                lessonType = lessonType,
                teacherId = teacher?.id
            )
        )
        popNavStackBack()
    }

    ScreenScaffold(
        scrollState = state
    ) { contentPadding ->
        TransformingLazyColumn(
            state = state,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
                    title = stringResource(R.string.add_lesson),
                    enabled = elementsEnabled,
                    onClick = {
                        val shortName = shortLessonNameState.text.toString().trim()
                        val longName = longLessonNameState.text.toString().trim()
                        val lessonType = selectedLessonType
                        val teacher = selectedTeacher

                        if (lessonType == null) {
                            showLessonTypeMissingDialog = true
                            return@ClickRowElement
                        }
                        if (teacher == null) {
                            showTeacherMissingWarningDialog = true
                            return@ClickRowElement
                        }

                        addLesson(shortName, longName, lessonType, teacher)
                    }
                )
            }
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
        // no lesson type (icon) specified error dialog
        if (showLessonTypeMissingDialog) {
            LessonTypeMissingDialog(
                onDismiss = { showLessonTypeMissingDialog = false },
                visible = true
            )
        }

        // no teacher specified warning dialog
        if (showTeacherMissingWarningDialog) {
            TeacherMissingWarningDialog(
                onConfirm = {
                    showTeacherMissingWarningDialog = false
                    val lessonType = selectedLessonType ?: return@TeacherMissingWarningDialog
                    addLesson(
                        shortName = shortLessonNameState.text.toString().trim(),
                        longName = longLessonNameState.text.toString().trim(),
                        lessonType = lessonType,
                        teacher = selectedTeacher
                    )
                },
                onDismiss = { showTeacherMissingWarningDialog = false },
                visible = true
            )
        }
    }
}