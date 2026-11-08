package xyz.nbani.ecsetnaplo.data.model

import java.time.LocalTime

sealed interface TimetableEntry {
    val isCurrent: Boolean
    val startTime: LocalTime
    val endTime: LocalTime

    data class PeriodItem(
        val period: Period,
        override val isCurrent: Boolean
    ) : TimetableEntry {
        override val startTime get() = period.startTime
        override val endTime get() = period.endTime
    }

    data class Break(
        val previousPeriod: Period,
        val nextPeriod: Period,
        override val isCurrent: Boolean
    ) : TimetableEntry {
        override val startTime get() = previousPeriod.endTime
        override val endTime get() = nextPeriod.startTime
    }
}