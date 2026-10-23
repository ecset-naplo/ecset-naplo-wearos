package xyz.nbani.ecsetnaplo.ui.components

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material3.TimeText
import androidx.wear.compose.material3.timeTextCurvedText
import androidx.wear.compose.material3.timeTextSeparator

@Composable
fun PageTimeText(@StringRes titleRes: Int) {
    val title = stringResource(titleRes)

    TimeText { time ->
        timeTextCurvedText(title)
        timeTextSeparator()
        timeTextCurvedText(time)
    }
}

@Composable
fun PageTimeText(title: String) {
    TimeText { time ->
        timeTextCurvedText(title)
        timeTextSeparator()
        timeTextCurvedText(time)
    }
}