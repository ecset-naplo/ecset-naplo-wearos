package xyz.nbani.ecsetnaplo.data.local.entity

import android.os.Parcelable
import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "teachers",
    indices = [Index(value = ["name"], unique = true)]
)
@Parcelize
data class Teacher(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
) : Parcelable
