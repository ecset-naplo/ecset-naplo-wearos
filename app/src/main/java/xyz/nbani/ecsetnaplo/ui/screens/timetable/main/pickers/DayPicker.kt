package xyz.nbani.ecsetnaplo.ui.screens.timetable.main.pickers

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.AlertDialogDefaults.EdgeButton
import androidx.wear.compose.material3.Dialog
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Picker
import androidx.wear.compose.material3.PickerState
import androidx.wear.compose.material3.Text
import xyz.nbani.ecsetnaplo.R

@Composable
fun DayPicker(
    pickerState: PickerState,
    pickerDescription: String,
    daysName: List<String>,
    onClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        visible = true,
        onDismissRequest = onDismissRequest
    ) {
        Box {
            Picker(
                verticalSpacing = 16.dp,
                state = pickerState,
                contentDescription = { pickerDescription },
                modifier = Modifier.align(Alignment.Center)
            ) { index ->
                val selected = index == pickerState.selectedOptionIndex
                Text(
                    daysName[index],
                    style = if (selected) MaterialTheme.typography.titleLarge
                    else MaterialTheme.typography.bodyMedium
                )
            }
            EdgeButton(
                modifier = Modifier.align(Alignment.BottomCenter),
                onClick = onClick
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = stringResource(R.string.confirm)
                )
            }
        }
    }
}