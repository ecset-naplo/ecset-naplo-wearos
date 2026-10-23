package xyz.nbani.ecsetnaplo.data.local.entity

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    tableName = "regular_periods",
    foreignKeys = [
        ForeignKey(
            entity = Lesson::class,
            parentColumns = ["id"],
            childColumns = ["lessonId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = ["id"],
            childColumns = ["roomId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("lessonId"),
        Index("roomId"),
        Index(value = ["weekday", "period",])
    ]
)
data class RegularPeriod(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val lessonId: Long,
    val roomId: Long? = null,
    val weekday: DayOfWeek,
    val period: Int,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val validFrom: LocalDate,
    val validTo: LocalDate? = null
)