package xyz.nbani.ecsetnaplo.ui.screens.timetable.rooms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import xyz.nbani.ecsetnaplo.R
import xyz.nbani.ecsetnaplo.data.local.entity.RoomEntity
import xyz.nbani.ecsetnaplo.ui.components.ClickRowElement
import xyz.nbani.ecsetnaplo.ui.components.TextBox
import xyz.nbani.ecsetnaplo.ui.components.sectionHeader
import xyz.nbani.ecsetnaplo.ui.screens.settings.components.LanguageAlertDialog
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel

@Composable
fun EditRoom(
    viewModel: TimetableViewModel,
    popNavStackBack: () -> Unit,
    roomId: Long
) {
    LaunchedEffect(roomId) {
        viewModel.loadRoom(roomId)
    }

    val state = rememberTransformingLazyColumnState()
    val spec = rememberTransformationSpec()

    val roomNameState = rememberTextFieldState()

    var elementsEnabled by remember { mutableStateOf(true) }

    val room = viewModel.room

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(room?.id) {
        room?.let { roomNameState.setTextAndPlaceCursorAtEnd(it.name) }
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
            sectionHeader(R.string.edit_room)

            if (room != null) {
                item {
                    TextBox(
                        state = roomNameState,
                        spec = spec,
                        itemScope = this,
                        modifier = Modifier.transformedHeight(this, spec),
                        decorator = { innerTextField ->
                            Box {
                                if (roomNameState.text.isEmpty()) {
                                    Text(stringResource(R.string.room_name))
                                }
                                innerTextField()
                            }
                        },
                        enabled = elementsEnabled
                    )
                }

                item {
                    ClickRowElement(
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec),
                        icon = Icons.Default.Save,
                        title = stringResource(R.string.save),
                        enabled = elementsEnabled,
                        onClick = {
                            elementsEnabled = false
                            val updatedRoom = room.copy(name = roomNameState.text.toString())
                            viewModel.updateRoom(updatedRoom)
                            popNavStackBack()
                        }
                    )
                }

                item {
                    ClickRowElement(
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec),
                        icon = Icons.Default.Delete,
                        title = stringResource(R.string.delete_room),
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
            } else {
                item {
                    CircularProgressIndicator()
                }
            }
        }

        // delete room confirm dialog
        if (showDeleteConfirmDialog) {
            DeleteConfirmationDialog(
                onConfirm = { viewModel.deleteRoom(room!!); popNavStackBack() },
                onDismiss = { showDeleteConfirmDialog = false },
                visible = true
            )
        }
    }
}