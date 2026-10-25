package xyz.nbani.ecsetnaplo.ui.screens.timetable.rooms

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.ButtonColors
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.ColorScheme
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import xyz.nbani.ecsetnaplo.R
import xyz.nbani.ecsetnaplo.data.local.entity.RoomEntity
import xyz.nbani.ecsetnaplo.ui.components.ClickRowElement
import xyz.nbani.ecsetnaplo.ui.components.sectionHeader
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel

@Composable
fun RoomsScreen(
    viewModel: TimetableViewModel,
    onOpenAddRoomScreen: () -> Unit,
    onOpenEditRoomScreen: (Long) -> Unit,
    isEditor: Boolean,
    onSelectRoom: ((RoomEntity) -> Unit)? = null
) {
    LaunchedEffect(isEditor, onSelectRoom == null) {
        if (isEditor && onSelectRoom == null) {
            Log.w("RoomsScreen", "onSelectRoom was not passed even though isEditor is true")
        }
    }

    val state = rememberTransformingLazyColumnState()
    val spec = rememberTransformationSpec()

    val rooms by viewModel.rooms.collectAsStateWithLifecycle()

    ScreenScaffold(
        scrollState = state
    ) { contentPadding ->
        TransformingLazyColumn(
            state = state,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            sectionHeader(R.string.rooms)

            rooms?.let {
                items(it.size) { index ->
                    val room = it[index]
                    ClickRowElement(
                        title = room.name,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = {
                            if (isEditor) {
                                onOpenEditRoomScreen(room.id)
                            } else {
                                onSelectRoom?.invoke(room)
                            }
                        },
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec),
                        onLongClick = {
                            onOpenEditRoomScreen(room.id)
                        }
                    )
                }
            }
            item {
                ClickRowElement(
                    icon = Icons.Default.Add,
                    title = stringResource(R.string.add_room),
                    border = BorderStroke(5.dp, Color.White),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    onClick = { onOpenAddRoomScreen() },
                    modifier = Modifier.transformedHeight(this, spec),
                    transformation = SurfaceTransformation(spec),
                )
            }
        }
    }
}