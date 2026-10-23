package xyz.nbani.ecsetnaplo.data.local.entity

import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "teachers",
    indices = [Index(value = ["name"], unique = true)]
)
data class Teacher(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)
