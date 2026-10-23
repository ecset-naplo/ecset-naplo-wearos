package xyz.nbani.ecsetnaplo.ui.components

import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnScope
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.Text

fun TransformingLazyColumnScope.sectionHeader(title: String) {
    item { ListHeader { Text(title) } }
}

fun TransformingLazyColumnScope.sectionHeader(@StringRes titleRes: Int) {
    item { ListHeader { Text(stringResource(titleRes)) } }
}