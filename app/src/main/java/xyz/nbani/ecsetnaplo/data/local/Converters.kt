package xyz.nbani.ecsetnaplo.data.local

import androidx.room3.ColumnTypeConverter
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class Converters {
    @ColumnTypeConverter fun fromLocalDate(date: LocalDate?): String? = date?.toString()
    @ColumnTypeConverter fun toLocalDate(s: String?): LocalDate? = s?.let(LocalDate::parse)

    @ColumnTypeConverter fun fromDayOfWeek(day: DayOfWeek): Int = day.value
    @ColumnTypeConverter fun toDayOfWeek(int: Int): DayOfWeek = DayOfWeek.of(int)

    @ColumnTypeConverter fun fromLocalTime(time: LocalTime?): Int? = time?.toSecondOfDay()
    @ColumnTypeConverter fun toLocalTime(time: Int?): LocalTime? = time?.let {
        LocalTime.ofSecondOfDay(it.toLong())
    }
}