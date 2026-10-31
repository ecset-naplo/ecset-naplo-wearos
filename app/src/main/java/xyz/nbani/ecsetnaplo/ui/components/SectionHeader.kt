package xyz.nbani.ecsetnaplo.ui.components

import androidx.annotation.StringRes
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnScope
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.Text

fun TransformingLazyColumnScope.sectionHeader(title: String, modifier: Modifier = Modifier) {
    item {
        ListHeader { Text(title, modifier)}
    }
}

fun TransformingLazyColumnScope.sectionHeader(@StringRes titleRes: Int, modifier: Modifier = Modifier) {
    item { ListHeader { Text(stringResource(titleRes), modifier) } }
}