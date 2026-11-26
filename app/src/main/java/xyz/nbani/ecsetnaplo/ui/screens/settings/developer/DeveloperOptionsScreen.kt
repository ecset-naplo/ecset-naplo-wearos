package xyz.nbani.ecsetnaplo.ui.screens.settings.developer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import xyz.nbani.ecsetnaplo.R
import xyz.nbani.ecsetnaplo.ui.components.ToggleRowElement
import xyz.nbani.ecsetnaplo.ui.components.sectionHeader
import xyz.nbani.ecsetnaplo.ui.viewmodels.TimetableViewModel

@Composable
fun DeveloperOptionsScreen(
    viewModel: TimetableViewModel,
    popNavStackBack: () -> Unit
) {
    val state = rememberTransformingLazyColumnState()
    val spec = rememberTransformationSpec()

    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val currentSettings = settings

    ScreenScaffold(
        scrollState = state
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
                sectionHeader(R.string.developer_options)
                item {
                    ToggleRowElement(
                        title = stringResource(R.string.developer_options),
                        checked = currentSettings.developerOptions,
                        onCheckedChange = {
                            viewModel.toggleDeveloperOptions(currentSettings)
                            if (currentSettings.setDeveloperOptionsToDefaultOnDisable) {
                                viewModel.setDefaultDeveloperOptions(currentSettings)
                            }
                            popNavStackBack()
                        },
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec)
                    )
                }
                item {
                    ToggleRowElement(
                        title = stringResource(R.string.default_dev_options_on_disable),
                        checked = currentSettings.setDeveloperOptionsToDefaultOnDisable,
                        onCheckedChange = {
                            viewModel.toggleSetDeveloperOptionsToDefaultOnDisable(currentSettings)
                        },
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec)
                    )
                }
                sectionHeader(R.string.experimental_features)
                item {
                    ToggleRowElement(
                        title = stringResource(R.string.ongoing_activity),
                        checked = currentSettings.devOptionsExpFeatOngoingActivity,
                        onCheckedChange = {
                            viewModel.toggleExpFeatOngoingActivity(currentSettings)
                        },
                        modifier = Modifier.transformedHeight(this, spec),
                        transformation = SurfaceTransformation(spec)
                    )
                }
            }
        }
    }
}