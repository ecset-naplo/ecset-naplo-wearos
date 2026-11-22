package xyz.nbani.ecsetnaplo.ui.screens.currentlesson.components

import androidx.compose.runtime.Composable
import java.time.Duration
import java.time.LocalTime

@Composable
fun calculateProgress(start: LocalTime, end: LocalTime, now: LocalTime): Float {
    val total = Duration.between(start, end).toMillis()
    if (total <= 0) return 0f
    val elapsed = Duration.between(start, now).toMillis()
    return (elapsed.toFloat() / total).coerceIn(0f, 1f)
}