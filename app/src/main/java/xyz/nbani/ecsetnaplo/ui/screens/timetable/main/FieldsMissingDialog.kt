package xyz.nbani.ecsetnaplo.ui.screens.timetable.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material3.AlertDialog
import androidx.wear.compose.material3.AlertDialogDefaults
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import xyz.nbani.ecsetnaplo.R

@Composable
fun FieldsMissingDialog(
    onDismiss: () -> Unit,
    visible: Boolean,
) {
    val transformationSpec = rememberTransformationSpec()

    AlertDialog(
        icon = { Icon(imageVector = Icons.Default.Error, contentDescription = null) },
        visible = visible,
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.missing_fields)) },
        text = { Text(stringResource(R.string.missing_fields_dialog_body)) },
        transformationSpec = transformationSpec,
        confirmButton = {
            AlertDialogDefaults.ConfirmButton(
                onClick = onDismiss
            )
        },
    )
}