package xyz.nbani.ecsetnaplo.ui.screens.timetable.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import androidx.wear.compose.material3.rememberPickerState
import xyz.nbani.ecsetnaplo.R
import xyz.nbani.ecsetnaplo.data.local.entity.IrregularPeriod
import xyz.nbani.ecsetnaplo.ui.components.ClickRowElement
import xyz.nbani.ecsetnaplo.ui.components.ToggleRowElement
import xyz.nbani.ecsetnaplo.ui.components.sectionHeader
import xyz.nbani.ecsetnaplo.ui.screens.timetable.main.pickers.DayPicker
import xyz.nbani.ecsetnaplo.ui.screens.timetable.main.pickers.PeriodIndexPicker
import xyz.nbani.ecsetnaplo.ui.screens.timetable.main.pickers.StartEndTimePicker
import xyz.nbani.ecsetnaplo.ui.screens.timetable.main.pickers.ValidFromToPicker
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle

@Composable
fun EditPeriod(
    viewModel: TimetableViewModel,
    popNavStackBack: () -> Unit,
    onOpenRoomsScreen: (Boolean) -> Unit,
    onOpenLessonsScreen: (Boolean) -> Unit,
    dateDefault: LocalDate,
) {

    val dayDefault = dateDefault.dayOfWeek

    val state = rememberTransformingLazyColumnState()
    val spec = rememberTransformationSpec()
    val pickerState = rememberPickerState(
        initialNumberOfOptions = 7,
        initiallySelectedIndex = dayDefault.ordinal,
        shouldRepeatOptions = false,
    )

    val pickerDescription = stringResource(R.string.day)

    val locale = LocalConfiguration.current.locales[0]
    val days = remember { DayOfWeek.entries }
    val daysName = remember(locale) {
        DayOfWeek.entries.map { it.getDisplayName(TextStyle.FULL, locale) }
    }

    var elementsEnabled by remember { mutableStateOf(true) }

    val period = viewModel.period

    val selectedLesson = viewModel.lesson ?: period?.lesson
    val selectedRoom = viewModel.room ?: period?.roomEntity
    var day by rememberSaveable { mutableStateOf(dayDefault) }
    var periodIndex by rememberSaveable { mutableStateOf(period?.period) }
    var startTime by rememberSaveable { mutableStateOf(period?.startTime) }
    var endTime by rememberSaveable { mutableStateOf(period?.endTime)}
    var validFrom by rememberSaveable { mutableStateOf(period?.regularPeriod?.validFrom) }
    var validTo by rememberSaveable { mutableStateOf(period?.regularPeriod?.validTo) }

    var showDayPicker by remember { mutableStateOf(false) }
    var showPeriodPicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var showValidFromPicker by remember { mutableStateOf(false) }
    var showValidToPicker by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    var initialized by rememberSaveable { mutableStateOf(false) }
    var navigating by remember { mutableStateOf(false) }

    /* if false, editing a regular period (on every week)
    if true. editing irregular (just this once/only on this day) */
    var editingIrregular by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(period) {
        if (!initialized) {
            initialized = true
            period?.let {
                periodIndex = it.period
                startTime = it.startTime
                endTime = it.endTime
                validFrom = it.regularPeriod?.validFrom
                validTo = it.regularPeriod?.validTo
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (!navigating) {
                viewModel.clearLesson()
                viewModel.clearRoom()
            }
        }
    }

    ScreenScaffold(
        scrollState = state
    ) { contentPadding ->
        if (period != null) {
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (period.regularPeriod != null) {
                    sectionHeader(R.string.edit_period)
                    item {
                        ToggleRowElement(
                            modifier = Modifier.transformedHeight(this, spec),
                            transformation = SurfaceTransformation(spec),
                            title = if (!editingIrregular) stringResource(R.string.every_week)
                            else stringResource(R.string.only_this_day),
                            checked = editingIrregular,
                            onCheckedChange = { editingIrregular = !editingIrregular }
                        )
                    }
                }

                sectionHeader(R.string.lesson)
                item {
                    ClickRowElement(
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec),
                        icon = selectedLesson?.lessonType?.icon ?: period.lesson.lessonType.icon,
                        title = selectedLesson?.longSubjectName ?: period.lesson.longSubjectName,
                        enabled = elementsEnabled,
                        onClick = {
                            navigating = true
                            onOpenLessonsScreen(false)
                        }
                    )
                }

                sectionHeader(R.string.room_optional)
                item {
                    ClickRowElement(
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec),
                        icon = Icons.Default.Groups,
                        title = selectedRoom?.name
                            ?: period.roomEntity?.name
                            ?: stringResource(R.string.select_room),
                        enabled = elementsEnabled,
                        onClick = {
                            navigating = true
                            onOpenRoomsScreen(false)
                        }
                    )
                }

                if (period.regularPeriod != null) {
                    sectionHeader(R.string.day_and_period)
                    item {
                        ClickRowElement(
                            modifier = Modifier.transformedHeight(this, spec),
                            transformation = SurfaceTransformation(spec),
                            icon = Icons.Default.WbSunny,
                            title = day.getDisplayName(TextStyle.FULL, locale),
                            enabled = elementsEnabled,
                            onClick = { showDayPicker = true }
                        )
                    }
                } else {
                    sectionHeader(R.string.period)
                }
                item {
                    ClickRowElement(
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec),
                        icon = Icons.Default.FormatListNumbered,
                        title = periodIndex.toString(),
                        enabled = elementsEnabled,
                        onClick = { showPeriodPicker = true }
                    )
                }

                sectionHeader(R.string.start_time)
                item {
                    ClickRowElement(
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec),
                        icon = Icons.Default.AccessTime,
                        title = startTime?.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                            ?: period.startTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                        enabled = elementsEnabled,
                        onClick = { showStartTimePicker = true }
                    )
                }

                sectionHeader(R.string.end_time_optional)
                item {
                    ClickRowElement(
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec),
                        icon = Icons.Default.AccessTimeFilled,
                        title = endTime?.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                            ?: period.endTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                        enabled = elementsEnabled,
                        onClick = { showEndTimePicker = true }
                    )
                }

                if (period.regularPeriod != null) {
                    sectionHeader(R.string.valid_from)
                    item {
                        ClickRowElement(
                            modifier = Modifier.transformedHeight(this, spec),
                            transformation = SurfaceTransformation(spec),
                            icon = Icons.Outlined.CalendarMonth,
                            title = validFrom?.format(
                                DateTimeFormatter
                                    .ofLocalizedDate(FormatStyle.MEDIUM)
                                    .withLocale(locale)
                            )
                                ?: period.regularPeriod.validFrom.format(
                                    DateTimeFormatter
                                        .ofLocalizedDate(FormatStyle.MEDIUM)
                                        .withLocale(locale)
                                ),
                            enabled = elementsEnabled,
                            onClick = { showValidFromPicker = true }
                        )
                    }

                    sectionHeader(R.string.valid_to_optional)
                    item {
                        ClickRowElement(
                            modifier = Modifier.transformedHeight(this, spec),
                            transformation = SurfaceTransformation(spec),
                            icon = Icons.Outlined.CalendarMonth,
                            title = validTo?.format(
                                DateTimeFormatter
                                    .ofLocalizedDate(FormatStyle.MEDIUM)
                                    .withLocale(locale)
                            )
                                ?: period.regularPeriod.validTo?.format(
                                    DateTimeFormatter
                                        .ofLocalizedDate(FormatStyle.MEDIUM)
                                        .withLocale(locale)
                                ) ?: stringResource(R.string.select_valid_to),
                            enabled = elementsEnabled,
                            onClick = { showValidToPicker = true }
                        )
                    }
                }

                item {
                    ClickRowElement(
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec),
                        icon = Icons.Default.Save,
                        title = stringResource(R.string.save),
                        enabled = elementsEnabled,
                        onClick = {
                            if (editingIrregular) {
                                val isStandalone = period.regularPeriod == null

                                var regularPeriodId: Long? = null
                                var roomId: Long?
                                var lessonId: Long?
                                var selectedPeriodIndex: Int?
                                var selectedStartTime: LocalTime?
                                var selectedEndTime: LocalTime?

                                if (isStandalone) {
                                    roomId = selectedRoom?.id ?: period.roomEntity?.id
                                    lessonId = selectedLesson?.id ?: period.lesson.id
                                    selectedPeriodIndex = periodIndex ?: period.period
                                    selectedStartTime = startTime ?: period.startTime
                                    selectedEndTime = endTime ?: period.endTime
                                } else {
                                    regularPeriodId = period.regularPeriod.id

                                    lessonId = if (
                                        period.irregularPeriod?.lessonId
                                        == (selectedLesson?.id ?: period.lesson.id)
                                    ) { null } else {
                                        selectedLesson?.id ?: period.lesson.id
                                    }

                                    roomId = if (
                                        period.irregularPeriod?.roomId
                                        == (selectedRoom?.id ?: period.roomEntity?.id)
                                    ) { null } else {
                                        selectedRoom?.id ?: period.roomEntity?.id
                                    }

                                    selectedPeriodIndex = if (
                                        period.irregularPeriod?.period
                                        == (periodIndex ?: period.period)
                                    ) { null } else {
                                        periodIndex ?: period.period
                                    }

                                    selectedStartTime = if (
                                        period.irregularPeriod?.startTime
                                        == (startTime ?: period.startTime)
                                    ) { null } else {
                                        startTime ?: period.startTime
                                    }

                                    selectedEndTime = if (
                                        period.irregularPeriod?.endTime
                                        == (endTime ?: period.endTime)
                                    ) { null } else {
                                        endTime ?: period.endTime
                                    }
                                }

                                val updatedIrregularPeriod = period.irregularPeriod?.copy(
                                    regularPeriodId = regularPeriodId,
                                    roomId = roomId,
                                    lessonId = lessonId,
                                    period = selectedPeriodIndex,
                                    startTime = selectedStartTime,
                                    endTime = selectedEndTime,
                                ) ?: IrregularPeriod(
                                    date = dateDefault,
                                    regularPeriodId = regularPeriodId,
                                    roomId = roomId,
                                    lessonId = lessonId,
                                    period = selectedPeriodIndex,
                                    startTime = selectedStartTime,
                                    endTime = selectedEndTime,
                                )
                                viewModel.updateIrregularPeriod(updatedIrregularPeriod)
                                popNavStackBack()
                            } else {
                                val updatedRegularPeriod = checkNotNull(period.regularPeriod) {
                                    "Regular period is null when editing regular period!"
                                }.copy(
                                    lessonId = selectedLesson?.id ?: period.lesson.id,
                                    roomId = selectedRoom?.id ?: period.roomEntity?.id,
                                    weekday = day,
                                    period = periodIndex ?: period.period,
                                    startTime = startTime ?: period.startTime,
                                    endTime = endTime ?: period.endTime,
                                    validFrom = validFrom ?: period.regularPeriod.validFrom,
                                    validTo = validTo ?: period.regularPeriod.validTo
                                )
                                viewModel.updateRegularPeriod(updatedRegularPeriod)
                                popNavStackBack()
                            }
                        }
                    )
                }
                item {
                    ClickRowElement(
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec),
                        icon = Icons.Default.Delete,
                        title = stringResource(R.string.delete_lesson),
                        enabled = elementsEnabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        onClick = {
                            showDeleteConfirmDialog = true
                        }
                    )
                }
            }
        }

        // show period index picker
        if (showPeriodPicker) {
            PeriodIndexPicker(
                periodIndex = periodIndex ?: checkNotNull(period) {
                    "Period is null while showing period picker!" +
                            "Did you check if period was null before enabling it?"
                }.period,
                onDismissRequest = { showPeriodPicker = false },
                onValueChange = { periodIndex = it }
            )
        }

        // show day picker
        if (showDayPicker) {
            DayPicker(
                pickerState = pickerState,
                pickerDescription = pickerDescription,
                daysName = daysName,
                onClick = { day = days[pickerState.selectedOptionIndex]; showDayPicker = false },
                onDismissRequest = { day = days[pickerState.selectedOptionIndex]; showDayPicker = false }
            )
        }

        // show start time picker
        if (showStartTimePicker) {
            StartEndTimePicker(
                initialTime = startTime ?: checkNotNull(period) {
                    "Period is null while showing start time picker!" +
                            "Did you check if period was null before enabling it?"
                }.startTime,
                onTimePicked = { startTime = it; showStartTimePicker = false},
                onDismissRequest = { showStartTimePicker }
            )
        }

        // show end time picker
        if (showEndTimePicker) {
            StartEndTimePicker(
                initialTime = endTime ?: checkNotNull(period) {
                    "Period is null while showing end time picker!" +
                            "Did you check if period was null before enabling it?"
                }.startTime,
                onTimePicked = { endTime = it; showEndTimePicker = false },
                onDismissRequest = { showEndTimePicker = false }
            )
        }

        // show valid from date picker
        if (showValidFromPicker) {
            ValidFromToPicker(
                initialDate = validFrom ?: checkNotNull(period?.regularPeriod) {
                    "Regular period is null while showing valid from picker!" +
                            "Did you check if regular period was null before enabling it?"
                }.validFrom,
                onDatePicked = { validFrom = it; showValidFromPicker = false },
                onDismissRequest = { showValidFromPicker = false }
            )
        }

        // show valid to date picker
        if (showValidToPicker) {
            ValidFromToPicker(
                initialDate = validTo ?: checkNotNull(period?.regularPeriod) {
                    "Regular period is null while showing valid to picker!" +
                            "Did you check if regular period was null before enabling it?"
                }.validTo ?: validFrom ?: checkNotNull(period?.regularPeriod) {
                    "Regular period is null while showing valid to picker!" +
                            "Did you check if regular period was null before enabling it?"
                }.validFrom,
                onDatePicked = { validTo = it; showValidToPicker = false },
                onDismissRequest = { showValidToPicker = false }
            )
        }

        // delete regular period confirm dialog
        if (showDeleteConfirmDialog) {
            DeleteConfirmationDialog(
                onConfirm = {
                    if (period?.regularPeriod != null) {
                        viewModel.deleteRegularPeriod(period.regularPeriod); popNavStackBack()
                    } else if (period?.irregularPeriod != null) {
                        viewModel.deleteIrregularPeriod(period.irregularPeriod); popNavStackBack()
                    }
                },
                onDismiss = { showDeleteConfirmDialog = false },
                visible = true,
                deletingRegularPeriod = period?.regularPeriod != null
            )
        }
    }
}