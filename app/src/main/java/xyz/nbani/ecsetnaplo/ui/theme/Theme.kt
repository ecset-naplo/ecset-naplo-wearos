package xyz.nbani.ecsetnaplo.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.dynamicColorScheme

@Composable
fun EcsetTheme(
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = if (dynamicColor) {
        dynamicColorScheme(context)
    } else {
        null
    }

    MaterialTheme(
        colorScheme = colorScheme ?: MaterialTheme.colorScheme,
        content = content
    )
}