package xyz.nbani.ecsetnaplo.ui.screens.timetable.main.pickers

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material3.Dialog
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.Stepper
import androidx.wear.compose.material3.Text
import xyz.nbani.ecsetnaplo.R

@Composable
fun PeriodIndexPicker(
    periodIndex: Int,
    onValueChange: (Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        visible = true,
        onDismissRequest = onDismissRequest,
        modifier = Modifier.fillMaxSize()
    ) {
        Stepper(
            value = periodIndex,
            onValueChange = onValueChange,
            valueProgression = 0..100,
            decreaseIcon = {
                Icon(
                    Icons.Default.Remove,
                    contentDescription = stringResource(R.string.decrease)
                )
            },
            increaseIcon = {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.increase)
                )
            }
        ) { Text("$periodIndex") }
    }
}