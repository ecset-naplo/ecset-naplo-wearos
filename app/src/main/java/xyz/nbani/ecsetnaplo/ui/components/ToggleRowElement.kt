package xyz.nbani.ecsetnaplo.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.SwitchButton
import androidx.wear.compose.material3.SwitchButtonColors
import androidx.wear.compose.material3.SwitchButtonDefaults
import androidx.wear.compose.material3.Text

@Composable
fun ToggleRowElement(
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation? = null,
    icon: ImageVector? = null,
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    SwitchButton(
        icon = icon?.let { { Icon(it, contentDescription = null) } },
        label = { Text(title) },
        secondaryLabel = subtitle?.let { { Text(it) } },
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.fillMaxWidth(),
        transformation = transformation,
        enabled = enabled
    )
}