package xyz.nbani.ecsetnaplo.ui.screens.timetable.teachers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material3.AlertDialog
import androidx.wear.compose.material3.AlertDialogDefaults
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButtonColors
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import xyz.nbani.ecsetnaplo.R

@Composable
fun DeleteWithKeepLessonsConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    visible: Boolean,
) {
    val transformationSpec = rememberTransformationSpec()

    val defaultButtonColors = ButtonDefaults.buttonColors()

    AlertDialog(
        icon = { Icon(imageVector = Icons.Default.Warning, contentDescription = null) },
        visible = visible,
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.delete_teacher)) },
        text = { Text(stringResource(R.string.teacher_delete_confirmation_keep_lessons)) },
        transformationSpec = transformationSpec,
        confirmButton = {
            AlertDialogDefaults.ConfirmButton(
                colors = IconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onError,
                    containerColor = MaterialTheme.colorScheme.error,
                    disabledContentColor = defaultButtonColors.disabledContentColor,
                    disabledContainerColor = defaultButtonColors.disabledContainerColor
                ),
                onClick = onConfirm,
                content = { Icon(Icons.Default.Delete, contentDescription = null) }
            )
        },
        dismissButton = {
            AlertDialogDefaults.DismissButton(
                onClick = onDismiss
            )
        }
    )
}