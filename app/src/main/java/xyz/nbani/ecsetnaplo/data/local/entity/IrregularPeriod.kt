package xyz.nbani.ecsetnaplo.data.local.entity

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    tableName = "irregular_periods",
    foreignKeys = [
        ForeignKey(
            entity = RegularPeriod::class,
            parentColumns = ["id"],
            childColumns = ["regularPeriodId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = ["id"],
            childColumns = ["roomId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Lesson::class,
            parentColumns = ["id"],
            childColumns = ["lessonId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("regularPeriodId"),
        Index("roomId"),
        Index("lessonId")
    ]
)
data class IrregularPeriod(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val regularPeriodId: Long? = null,
    val date: LocalDate,
    val roomId: Long? = null,
    val lessonId: Long? = null,
    val period: Int? = null,
    val startTime: LocalTime? = null,
    val endTime: LocalTime ? = null
)
