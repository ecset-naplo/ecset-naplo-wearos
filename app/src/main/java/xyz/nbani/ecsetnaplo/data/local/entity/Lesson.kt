package xyz.nbani.ecsetnaplo.data.local.entity

import android.os.Parcelable
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey
import kotlinx.parcelize.Parcelize
import xyz.nbani.ecsetnaplo.data.model.LessonType

@Entity(
    tableName = "lessons",
    foreignKeys = [
        ForeignKey(
            entity = Teacher::class,
            parentColumns = ["id"],
            childColumns = ["teacherId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("teacherId")]
)
@Parcelize
data class Lesson(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val shortSubjectName: String,
    val longSubjectName: String,
    val lessonType: LessonType,
    val teacherId: Long? = null
) : Parcelable
