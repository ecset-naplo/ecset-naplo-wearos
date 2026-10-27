package xyz.nbani.ecsetnaplo.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonColors
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text

@Composable
fun ClickRowElement(
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation? = null,
    icon: ImageVector? = null,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
    colors: ButtonColors? = null,
    border: BorderStroke? = null,
    enabled: Boolean = true,
    onLongClick: (() -> Unit)? = null,
) {
    Button(
        icon = icon?.let { { Icon(it, contentDescription = null) } },
        label = { Text(title) },
        secondaryLabel = subtitle?.let { { Text(it) } },
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        transformation = transformation,
        colors = colors ?: ButtonDefaults.buttonColors(),
        border = border,
        enabled = enabled,
        onLongClick = onLongClick
    )
}