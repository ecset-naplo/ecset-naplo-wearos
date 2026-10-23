package xyz.nbani.ecsetnaplo.ui.screens.timetable.rooms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.rememberTextFieldState
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
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import xyz.nbani.ecsetnaplo.R
import xyz.nbani.ecsetnaplo.data.local.entity.RoomEntity
import xyz.nbani.ecsetnaplo.ui.components.ClickRowElement
import xyz.nbani.ecsetnaplo.ui.components.TextBox
import xyz.nbani.ecsetnaplo.ui.components.sectionHeader
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel

@Composable
fun AddRoom(
    viewModel: TimetableViewModel,
    popNavStackBack: () -> Unit
) {
    val state = rememberTransformingLazyColumnState()
    val spec = rememberTransformationSpec()

    val roomNameState = rememberTextFieldState()

    var elementsEnabled by remember { mutableStateOf(true) }

    ScreenScaffold(
        scrollState = state
    ) { contentPadding ->
        TransformingLazyColumn(
            state = state,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            sectionHeader(R.string.add_room)

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
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                ClickRowElement(
                    modifier = Modifier.transformedHeight(this, spec),
                    transformation = SurfaceTransformation(spec),
                    title = stringResource(R.string.add_room),
                    enabled = elementsEnabled,
                    onClick = {
                        val text = roomNameState.text.toString().trim()
                        if (text.isNotEmpty()) {
                            elementsEnabled = false
                            viewModel.insertRoom(RoomEntity(name = text))
                            popNavStackBack()
                        }
                    }
                )
            }
        }
    }
}