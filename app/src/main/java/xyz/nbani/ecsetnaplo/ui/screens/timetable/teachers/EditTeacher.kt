package xyz.nbani.ecsetnaplo.ui.screens.timetable.teachers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import xyz.nbani.ecsetnaplo.ui.components.ClickRowElement
import xyz.nbani.ecsetnaplo.ui.components.TextBox
import xyz.nbani.ecsetnaplo.ui.components.sectionHeader
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel

@Composable
fun EditTeacher(
    viewModel: TimetableViewModel,
    popNavStackBack: () -> Unit,
    onOpenDeleteTeacherScreen: () -> Unit,
    teacherId: Long
) {
    LaunchedEffect(teacherId) {
        viewModel.loadTeacher(teacherId)
    }

    val state = rememberTransformingLazyColumnState()
    val spec = rememberTransformationSpec()

    val teacherNameState = rememberTextFieldState()

    var elementsEnabled by remember { mutableStateOf(true) }

    val teacher = viewModel.teacher

    LaunchedEffect(teacher?.id) {
        teacher?.let { teacherNameState.setTextAndPlaceCursorAtEnd(it.name) }
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
            sectionHeader(R.string.edit_teacher)

            if (teacher != null) {
                item {
                    TextBox(
                        state = teacherNameState,
                        spec = spec,
                        itemScope = this,
                        modifier = Modifier.transformedHeight(this, spec),
                        decorator = { innerTextField ->
                            Box {
                                if (teacherNameState.text.isEmpty()) {
                                    Text(stringResource(R.string.teacher_name))
                                }
                                innerTextField()
                            }
                        },
                        enabled = elementsEnabled
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
                            elementsEnabled = false
                            val updatedTeacher = teacher.copy(name = teacherNameState.text.toString())
                            viewModel.updateTeacher(updatedTeacher)
                            popNavStackBack()
                        }
                    )
                }

                item {
                    ClickRowElement(
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec),
                        icon = Icons.Default.Delete,
                        title = stringResource(R.string.delete_teacher),
                        enabled = elementsEnabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        onClick = {
                            onOpenDeleteTeacherScreen()
                        }
                    )
                }
            } else {
                item {
                    CircularProgressIndicator()
                }
            }
        }
    }
}