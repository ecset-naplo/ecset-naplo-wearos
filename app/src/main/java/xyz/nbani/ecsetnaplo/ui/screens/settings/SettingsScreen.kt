package xyz.nbani.ecsetnaplo.ui.screens.settings

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DeveloperMode
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import xyz.nbani.ecsetnaplo.BuildConfig
import xyz.nbani.ecsetnaplo.R
import xyz.nbani.ecsetnaplo.ui.components.ClickRowElement
import xyz.nbani.ecsetnaplo.ui.components.PageTimeText
import xyz.nbani.ecsetnaplo.ui.components.ToggleRowElement
import xyz.nbani.ecsetnaplo.ui.components.sectionHeader
import xyz.nbani.ecsetnaplo.ui.screens.settings.components.LanguageAlertDialog
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel

@Composable
fun SettingsScreen(
    viewModel: TimetableViewModel,
    onOpenTimetable: (Boolean) -> Unit,
    onOpenRoomsScreen: (Boolean) -> Unit,
    onOpenTeachersScreen: (Boolean) -> Unit,
    onOpenLessonsScreen: (Boolean) -> Unit,
    onOpenDeveloperOptionsScreen: () -> Unit
) {
    val state = rememberTransformingLazyColumnState()
    val spec = rememberTransformationSpec()

    var versionTapped by remember { mutableIntStateOf(0) }

    val context = LocalContext.current
    val toastMessage = stringResource(R.string.developer_options_countdown_toast, 7 - (versionTapped + 1))
    var toast by remember { mutableStateOf<Toast?>(null) }

    var showLanguagePickerDialog by remember { mutableStateOf(false) }

    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val currentSettings = settings
    
    ScreenScaffold(
        scrollState = state, timeText = { PageTimeText(R.string.settings) }
    ) { contentPadding ->
        if (currentSettings != null) {
            TransformingLazyColumn(
                state = state,
                contentPadding = contentPadding.let {
                    PaddingValues(
                        start = it.calculateStartPadding(LayoutDirection.Ltr),
                        top = it.calculateTopPadding(),
                        end = it.calculateEndPadding(LayoutDirection.Ltr),
                        bottom = it.calculateBottomPadding() + 26.dp
                    )
                },
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Appearance settings
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
                item {
                    ToggleRowElement(
                        icon = Icons.Default.Timer,
                        title = stringResource(R.string.breaks_in_timetable),
                        subtitle = stringResource(R.string.breaks_in_timetable_description),
                        checked = currentSettings.showBreaksInTimetable,
                        onCheckedChange = { viewModel.toggleShowBreaksInTimetable(currentSettings) },
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
                            onOpenTimetable(true)
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
                item {
                    ClickRowElement(
                        icon = Icons.Default.Person,
                        title = stringResource(R.string.manage_teachers),
                        subtitle = stringResource(R.string.manage_teachers_button_subtitle),
                        onClick = {
                            onOpenTeachersScreen(true)
                        },
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec)
                    )
                }
                item {
                    ClickRowElement(
                        icon = Icons.Default.AccessTime,
                        title = stringResource(R.string.manage_lessons),
                        subtitle = stringResource(R.string.manage_lessons_button_subtitle),
                        onClick = {
                            onOpenLessonsScreen(true)
                        },
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec)
                    )
                }

                // About
                sectionHeader(R.string.about)
                item {
                    ClickRowElement(
                        icon = Icons.Default.Smartphone,
                        title = stringResource(R.string.version),
                        subtitle = BuildConfig.VERSION_NAME,
                        onClick = {
                            toast?.cancel()

                            if (!currentSettings.developerOptions) {
                                versionTapped += 1
                                Log.d("SettingsScreen", "tapped version: $versionTapped")
                                if (versionTapped == 7) {
                                    Log.d("SettingsScreen", "dev options on")
                                    viewModel.toggleDeveloperOptions(currentSettings)

                                    toast = Toast.makeText(
                                        context,
                                        R.string.you_are_a_developer,
                                        Toast.LENGTH_SHORT
                                    )
                                } else {
                                    toast = Toast.makeText(
                                        context,
                                        toastMessage,
                                        Toast.LENGTH_SHORT
                                    )
                                }
                            } else {
                                Log.d("SettingsScreen", "already a dev")
                                toast = Toast.makeText(
                                    context,
                                    R.string.already_a_developer,
                                    Toast.LENGTH_SHORT
                                )
                            }
                            toast?.show()
                        },
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec)
                    )
                }
                if (currentSettings.developerOptions) {
                    item {
                        ClickRowElement(
                            icon = Icons.Default.DeveloperMode,
                            title = stringResource(R.string.developer_options),
                            onClick = { onOpenDeveloperOptionsScreen() },
                            modifier = Modifier.transformedHeight(this, spec),
                            transformation = SurfaceTransformation(spec)
                        )
                    }
                }
            }
        } else {
            CircularProgressIndicator()
        }

        // language picker dialog
        if (showLanguagePickerDialog) {
            LanguageAlertDialog(onDismiss = { showLanguagePickerDialog = false }, visible = true)
        }
    }
}