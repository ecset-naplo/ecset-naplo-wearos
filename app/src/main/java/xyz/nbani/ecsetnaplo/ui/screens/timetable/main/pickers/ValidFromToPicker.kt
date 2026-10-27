package xyz.nbani.ecsetnaplo.ui.screens.timetable.main.pickers

import androidx.compose.runtime.Composable
import androidx.wear.compose.material3.DatePicker
import androidx.wear.compose.material3.Dialog
import java.time.LocalDate

@Composable
fun ValidFromToPicker(
    initialDate: LocalDate,
    onDatePicked: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        visible = true,
        onDismissRequest = onDismissRequest
    ) {
        DatePicker(
            initialDate = initialDate,
            onDatePicked = onDatePicked
        )
    }
}