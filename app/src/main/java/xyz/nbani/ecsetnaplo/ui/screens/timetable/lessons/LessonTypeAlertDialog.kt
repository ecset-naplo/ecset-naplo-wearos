package xyz.nbani.ecsetnaplo.ui.screens.timetable.lessons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.material3.AlertDialog
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.RadioButton
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import xyz.nbani.ecsetnaplo.R
import xyz.nbani.ecsetnaplo.data.model.LessonType

@Composable
fun LessonTypeAlertDialog(
    onDismiss: () -> Unit,
    visible: Boolean,
    selectedLessonType: LessonType?,
    onSelectSpecifiedLessonType: (LessonType) -> Unit
) {
    val lessonTypes = LessonType.entries
    val transformationSpec = rememberTransformationSpec()

    AlertDialog(
        visible = visible,
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.select_icon)) },
        transformationSpec = transformationSpec
    ) {
        itemsIndexed(lessonTypes) { _, lessonType ->
            RadioButton(
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(lessonType.icon, contentDescription = null)
                        Text(stringResource(lessonType.localized))
                    }
                },
                selected = selectedLessonType == lessonType,
                modifier = Modifier.transformedHeight(this, transformationSpec),
                transformation = SurfaceTransformation(transformationSpec),
                onSelect = { onSelectSpecifiedLessonType(lessonType); onDismiss() }
            )
        }
    }
}