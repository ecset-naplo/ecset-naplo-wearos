package xyz.nbani.ecsetnaplo.ui.screens.timetable.main

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
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    visible: Boolean,
    deletingRegularPeriod: Boolean,
) {
    val transformationSpec = rememberTransformationSpec()

    val defaultButtonColors = ButtonDefaults.buttonColors()

    AlertDialog(
        icon = { Icon(imageVector = Icons.Default.Warning, contentDescription = null) },
        visible = visible,
        onDismissRequest = onDismiss,
        title = {
            if (deletingRegularPeriod) Text(stringResource(R.string.delete_regular_period))
            else Text(stringResource(R.string.delete_irregular_period))
        },
        text = {
            if (deletingRegularPeriod) Text(stringResource(R.string.regular_period_delete_confirmation))
            else Text(stringResource(R.string.irregular_period_delete_confirmation))
        },
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