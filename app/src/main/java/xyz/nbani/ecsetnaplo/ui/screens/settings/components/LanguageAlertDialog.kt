package xyz.nbani.ecsetnaplo.ui.screens.settings.components

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.material3.AlertDialog
import androidx.wear.compose.material3.RadioButton
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import xyz.nbani.ecsetnaplo.R
import xyz.nbani.ecsetnaplo.data.model.supportedLanguages

@Composable
fun LanguageAlertDialog(
    onDismiss: () -> Unit,
    visible: Boolean,
) {
    val currentTag = AppCompatDelegate.getApplicationLocales()[0]?.language
    val transformationSpec = rememberTransformationSpec()

    AlertDialog(
        visible = visible,
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.language)) },
        transformationSpec = transformationSpec
    ) {
        item {
            RadioButton(
                label = { Text(stringResource(R.string.system_language)) },
                selected = currentTag == null,
                modifier = Modifier.transformedHeight(this, transformationSpec),
                transformation = SurfaceTransformation(transformationSpec),
                onSelect = {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
                    onDismiss()
                }
            )
        }
        itemsIndexed(supportedLanguages) { _, lang ->
            RadioButton(
                label = { Text(lang.nativeName) },
                selected = currentTag == lang.tag,
                modifier = Modifier.transformedHeight(this, transformationSpec),
                transformation = SurfaceTransformation(transformationSpec),
                onSelect = {
                    AppCompatDelegate.setApplicationLocales(
                        LocaleListCompat.forLanguageTags(lang.tag)
                    )
                    onDismiss()
                }
            )
        }
    }
}