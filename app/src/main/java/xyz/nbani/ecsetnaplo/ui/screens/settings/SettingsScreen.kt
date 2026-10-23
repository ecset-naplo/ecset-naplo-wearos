package xyz.nbani.ecsetnaplo.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import xyz.nbani.ecsetnaplo.R
import xyz.nbani.ecsetnaplo.ui.components.ClickRowElement
import xyz.nbani.ecsetnaplo.ui.components.PageTimeText
import xyz.nbani.ecsetnaplo.ui.components.sectionHeader
import xyz.nbani.ecsetnaplo.ui.screens.settings.components.LanguageAlertDialog

@Composable
fun SettingsScreen(
    onOpenTimetable: (Boolean) -> Unit,
    onOpenRoomsScreen: (Boolean) -> Unit,
) {
    val state = rememberTransformingLazyColumnState()
    val spec = rememberTransformationSpec()

    var showLanguagePickerDialog by remember { mutableStateOf(false) }
    
    ScreenScaffold(
        scrollState = state, timeText = { PageTimeText(R.string.settings) }
    ) { contentPadding ->
        TransformingLazyColumn(
            state = state,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Appearance settings (only language but whatever)
            sectionHeader(R.string.appearance)
            item {
                ClickRowElement(
                    icon = Icons.Default.Language,
                    title = stringResource(R.string.language),
                    subtitle = stringResource(R.string.language_button_subtitle),
                    onClick = { showLanguagePickerDialog = true },
                    modifier = Modifier.transformedHeight(this, spec),
                    transformation = SurfaceTransformation(spec)
                )
            }

            // Timetable settings
            sectionHeader(R.string.timetable)
            item {
                ClickRowElement(
                    icon = Icons.Default.TableChart,
                    title = stringResource(R.string.manage_timetable),
                    subtitle = stringResource(R.string.manage_timetable_button_subtitle),
                    onClick = {

                    },
                    modifier = Modifier.transformedHeight(this, spec),
                    transformation = SurfaceTransformation(spec)
                )
            }
            item {
                ClickRowElement(
                    icon = Icons.Default.Groups,
                    title = stringResource(R.string.manage_rooms),
                    subtitle = stringResource(R.string.manage_rooms_button_subtitle),
                    onClick = {
                        onOpenRoomsScreen(true)
                    },
                    modifier = Modifier.transformedHeight(this, spec),
                    transformation = SurfaceTransformation(spec)
                )
            }
        }

        // language picker dialog
        if (showLanguagePickerDialog) {
            LanguageAlertDialog(onDismiss = { showLanguagePickerDialog = false }, visible = true)
        }
    }
}