package xyz.nbani.ecsetnaplo.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity("rooms")
data class RoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)
