package xyz.nbani.ecsetnaplo.ui.screens.timetable.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import androidx.wear.compose.material3.rememberPickerState
import xyz.nbani.ecsetnaplo.R
import xyz.nbani.ecsetnaplo.data.local.entity.Lesson
import xyz.nbani.ecsetnaplo.data.local.entity.RegularPeriod
import xyz.nbani.ecsetnaplo.data.local.entity.RoomEntity
import xyz.nbani.ecsetnaplo.ui.components.ClickRowElement
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
fun AddRegularPeriod(
    viewModel: TimetableViewModel,
    popNavStackBack: () -> Unit,
    onOpenRoomsScreen: (Boolean) -> Unit,
    onOpenLessonsScreen: (Boolean) -> Unit,
    dayDefault: DayOfWeek,
    periodDefault: Int,
    startTimeDefault: LocalTime,
    validFromDefault: LocalDate
) {
    val state = rememberTransformingLazyColumnState()
    val spec = rememberTransformationSpec()
    val pickerState = rememberPickerState(
        initialNumberOfOptions = 7,
        initiallySelectedIndex = LocalDate.now().dayOfWeek.ordinal,
        shouldRepeatOptions = false,
    )

    val pickerDescription = stringResource(R.string.day)

    val locale = LocalConfiguration.current.locales[0]
    val days = remember { DayOfWeek.entries }
    val daysName = remember(locale) {
        DayOfWeek.entries.map { it.getDisplayName(TextStyle.FULL, locale) }
    }

    var elementsEnabled by remember { mutableStateOf(true) }

    var selectedLesson by rememberSaveable { mutableStateOf<Lesson?>(null) }
    var selectedRoom by rememberSaveable { mutableStateOf<RoomEntity?>(null) }
    var day by rememberSaveable { mutableStateOf(dayDefault) }
    var period by rememberSaveable { mutableIntStateOf(periodDefault) }
    var startTime by rememberSaveable { mutableStateOf(startTimeDefault) }
    var endTime by rememberSaveable { mutableStateOf<LocalTime?>(null) }
    var validFrom by rememberSaveable { mutableStateOf(validFromDefault) }
    var validTo by rememberSaveable { mutableStateOf<LocalDate?>(null) }

    var showDayPicker by remember { mutableStateOf(false) }
    var showPeriodPicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var showValidFromPicker by remember { mutableStateOf(false) }
    var showValidToPicker by remember { mutableStateOf(false) }
    var showRoomMissingWarningDialog by remember { mutableStateOf(false) }
    var showfieldsMissingDialog by remember { mutableStateOf(false) }

    var navigating by remember { mutableStateOf(false) }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        selectedLesson = viewModel.lesson
        selectedRoom = viewModel.room
    }

    DisposableEffect(Unit) {
        onDispose {
            if (!navigating) {
                viewModel.clearLesson()
                viewModel.clearRoom()
            }
        }
    }

    fun addRegularPeriod(
        lessonId: Long,
        roomId: Long? = null,
        weekday: DayOfWeek,
        period: Int,
        startTime: LocalTime,
        endTime: LocalTime,
        validFrom: LocalDate,
        validTo: LocalDate? = null
    ) {
        elementsEnabled = false
        viewModel.insertRegularPeriod(
            RegularPeriod(
                lessonId = lessonId,
                roomId = roomId,
                weekday = weekday,
                period = period,
                startTime = startTime,
                endTime = endTime,
                validFrom = validFrom,
                validTo = validTo
            )
        )
        popNavStackBack()
    }

    ScreenScaffold(
        scrollState = state
    ) { contentPadding ->
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
            sectionHeader(R.string.lesson)
            item {
                ClickRowElement(
                    modifier = Modifier.transformedHeight(this, spec),
                    transformation = SurfaceTransformation(spec),
                    icon = Icons.Default.AccessTime,
                    title = selectedLesson?.longSubjectName
                        ?: stringResource(R.string.select_lesson),
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
                    title = selectedRoom?.name ?: stringResource(R.string.select_room),
                    enabled = elementsEnabled,
                    onClick = {
                        navigating = true
                        onOpenRoomsScreen(false)
                    }
                )
            }

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
            item {
                ClickRowElement(
                    modifier = Modifier.transformedHeight(this, spec),
                    transformation = SurfaceTransformation(spec),
                    icon = Icons.Default.FormatListNumbered,
                    title = period.toString(),
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
                    title = startTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
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
                        ?: stringResource(R.string.select_end_time),
                    enabled = elementsEnabled,
                    onClick = { showEndTimePicker = true }
                )
            }

            sectionHeader(R.string.valid_from)
            item {
                ClickRowElement(
                    modifier = Modifier.transformedHeight(this, spec),
                    transformation = SurfaceTransformation(spec),
                    icon = Icons.Outlined.CalendarMonth,
                    title = validFrom.format(
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
                    ) ?: stringResource(R.string.select_valid_to),
                    enabled = elementsEnabled,
                    onClick = { showValidToPicker = true }
                )
            }

            item {
                ClickRowElement(
                    modifier = Modifier.transformedHeight(this, spec),
                    transformation = SurfaceTransformation(spec),
                    icon = Icons.Default.Add,
                    title = stringResource(R.string.add_period),
                    enabled = elementsEnabled,
                    onClick = {
                        val lessonId = selectedLesson?.id
                        val lessonEndTime = endTime

                        if (lessonId == null || lessonEndTime == null) {
                            showfieldsMissingDialog = true
                            return@ClickRowElement
                        }

                        if (selectedRoom == null) {
                            showRoomMissingWarningDialog = true
                            return@ClickRowElement
                        }

                        addRegularPeriod(
                            lessonId,
                            selectedRoom?.id,
                            days[pickerState.selectedOptionIndex],
                            period = period,
                            startTime = startTime,
                            endTime = lessonEndTime,
                            validFrom = validFrom,
                            validTo = validTo
                        )
                    }
                )
            }
        }

        // show period index picker
        if (showPeriodPicker) {
            PeriodIndexPicker(
                periodIndex = period,
                onDismissRequest = { showPeriodPicker = false },
                onValueChange = { period = it }
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
                initialTime = startTime,
                onTimePicked = { startTime = it; showStartTimePicker = false},
                onDismissRequest = { showStartTimePicker }
            )
        }

        // show end time picker
        if (showEndTimePicker) {
            StartEndTimePicker(
                initialTime = endTime ?: LocalTime.now(),
                onTimePicked = { endTime = it; showEndTimePicker = false },
                onDismissRequest = { showEndTimePicker = false }
            )
        }

        // show valid from date picker
        if (showValidFromPicker) {
            ValidFromToPicker(
                initialDate = validFrom,
                onDatePicked = { validFrom = it; showValidFromPicker = false },
                onDismissRequest = { showValidFromPicker = false }
            )
        }

        // show valid to date picker
        if (showValidToPicker) {
            ValidFromToPicker(
                initialDate = validTo ?: validFrom,
                onDatePicked = { validTo = it; showValidToPicker = false },
                onDismissRequest = { showValidToPicker = false }
            )
        }

        // no room specified warning dialog
        if (showRoomMissingWarningDialog) {
            RoomMissingWarningDialog(
                onConfirm = {
                    showRoomMissingWarningDialog = false
                    val lessonId = selectedLesson?.id ?: return@RoomMissingWarningDialog
                    val lessonEndTime = endTime ?: return@RoomMissingWarningDialog

                    addRegularPeriod(
                        lessonId,
                        selectedRoom?.id,
                        days[pickerState.selectedOptionIndex],
                        period = period,
                        startTime = startTime,
                        endTime = lessonEndTime,
                        validFrom = validFrom,
                        validTo = validTo
                    )
                },
                onDismiss = { showRoomMissingWarningDialog = false },
                visible = true
            )
        }

        // missing fields error dialog
        if (showfieldsMissingDialog) {
            FieldsMissingDialog(
                onDismiss = { showfieldsMissingDialog = false },
                visible = true
            )
        }
    }
}