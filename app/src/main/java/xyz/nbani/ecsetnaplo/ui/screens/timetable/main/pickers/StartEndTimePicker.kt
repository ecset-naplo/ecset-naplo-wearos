package xyz.nbani.ecsetnaplo.ui.screens.timetable.main.pickers

import androidx.compose.runtime.Composable
import androidx.wear.compose.material3.Dialog
import androidx.wear.compose.material3.TimePicker
import java.time.LocalTime

@Composable
fun StartEndTimePicker(
    initialTime: LocalTime,
    onTimePicked: (LocalTime) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        visible = true,
        onDismissRequest = onDismissRequest
    ) {
        TimePicker(
            initialTime = initialTime, onTimePicked = onTimePicked
        )
    }
}