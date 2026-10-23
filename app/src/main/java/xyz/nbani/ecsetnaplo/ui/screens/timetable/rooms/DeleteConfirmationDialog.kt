package xyz.nbani.ecsetnaplo.ui.screens.timetable.rooms

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.material3.AlertDialog
import androidx.wear.compose.material3.AlertDialogDefaults
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButtonColors
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.RadioButton
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import xyz.nbani.ecsetnaplo.R
import xyz.nbani.ecsetnaplo.data.model.supportedLanguages
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    visible: Boolean,
) {
    val currentTag = AppCompatDelegate.getApplicationLocales()[0]?.language
    val transformationSpec = rememberTransformationSpec()

    val defaultButtonColors = ButtonDefaults.buttonColors()

    var elementsEnabled by remember { mutableStateOf(true) }

    AlertDialog(
        icon = { Icon(imageVector = Icons.Default.Warning, contentDescription = null) },
        visible = visible,
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.delete_room)) },
        text = { Text(stringResource(R.string.room_delete_confirmation)) },
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