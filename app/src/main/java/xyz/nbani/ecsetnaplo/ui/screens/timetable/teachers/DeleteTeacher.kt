package xyz.nbani.ecsetnaplo.ui.screens.timetable.teachers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
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
fun DeleteTeacher(
    viewModel: TimetableViewModel,
    popNavStackBack: () -> Unit,
) {
    val state = rememberTransformingLazyColumnState()
    val spec = rememberTransformationSpec()

    val teacher = viewModel.teacher

    var showDeleteWithDeleteLessonsConfirmDialog by remember { mutableStateOf(false) }
    var showDeleteWithKeepLessonsConfirmDialog by remember { mutableStateOf(false) }

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
            sectionHeader(R.string.delete_teacher)

            if (teacher != null) {
                item {
                    ClickRowElement(
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec),
                        icon = Icons.Default.Delete,
                        title = stringResource(R.string.delete_teacher_keep_lessons),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        onClick = {
                            showDeleteWithKeepLessonsConfirmDialog = true
                        }
                    )
                }
                item {
                    ClickRowElement(
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec),
                        icon = Icons.Default.Delete,
                        title = stringResource(R.string.delete_teacher_delete_associated_lessons),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        onClick = {
                            showDeleteWithDeleteLessonsConfirmDialog = true
                        }
                    )
                }
            }
        }

        // delete teacher with keep lessons confirm dialog
        if (showDeleteWithKeepLessonsConfirmDialog) {
            DeleteWithKeepLessonsConfirmationDialog(
                onConfirm = {
                    viewModel.deleteTeacherWithKeepLessons(teacher!!)
                    popNavStackBack()
                    popNavStackBack()
                },
                onDismiss = { showDeleteWithKeepLessonsConfirmDialog = false },
                visible = true
            )
        }

        // delete teacher with delete lessons confirm dialog
        if (showDeleteWithDeleteLessonsConfirmDialog) {
            DeleteWithDeleteLessonsConfirmationDialog(
                onConfirm = {
                    viewModel.deleteTeacherWithDeleteLessons(teacher!!)
                    popNavStackBack()
                    popNavStackBack()
                },
                onDismiss = { showDeleteWithKeepLessonsConfirmDialog = false },
                visible = true
            )
        }
    }
}