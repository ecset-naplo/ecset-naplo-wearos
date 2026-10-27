package xyz.nbani.ecsetnaplo.ui.screens.timetable.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import xyz.nbani.ecsetnaplo.R
import xyz.nbani.ecsetnaplo.ui.components.ClickRowElement
import xyz.nbani.ecsetnaplo.ui.components.sectionHeader

@Composable
fun AddPeriodPicker(
    onOpenAddRegularPeriodScreen: () -> Unit,
    onOpenAddIrregularPeriodScreen: () -> Unit
) {
    val state = rememberTransformingLazyColumnState()
    val spec = rememberTransformationSpec()

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
            sectionHeader(R.string.add_period)
            item {
                ClickRowElement(
                    modifier = Modifier.transformedHeight(this, spec),
                    transformation = SurfaceTransformation(spec),
                    icon = Icons.Default.Add,
                    title = stringResource(R.string.add_regular_period),
                    onClick = { onOpenAddRegularPeriodScreen() }
                )
            }
            item {
                ClickRowElement(
                    modifier = Modifier.transformedHeight(this, spec),
                    transformation = SurfaceTransformation(spec),
                    icon = Icons.Default.Add,
                    title = stringResource(R.string.add_irregular_period),
                    onClick = { onOpenAddIrregularPeriodScreen() }
                )
            }
        }
    }
}