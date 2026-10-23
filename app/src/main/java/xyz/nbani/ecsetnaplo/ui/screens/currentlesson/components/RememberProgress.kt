package xyz.nbani.ecsetnaplo.ui.screens.currentlesson.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import xyz.nbani.ecsetnaplo.data.model.Period
import java.time.LocalTime
import java.time.Duration as JavaDuration
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun rememberProgress(period: Period): Float {
    var now by remember { mutableStateOf(LocalTime.now()) }

    LaunchedEffect(period) {
        while (true) {
            now = LocalTime.now()
            delay(1.milliseconds)
        }
    }

    val total = JavaDuration.between(period.startTime, period.endTime).toMillis()
    val elapsed = JavaDuration.between(period.startTime, now).toMillis()
    return (elapsed.toFloat() / total).coerceIn(0f, 1f)
}