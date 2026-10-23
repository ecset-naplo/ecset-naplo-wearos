package xyz.nbani.ecsetnaplo.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Room
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow
import xyz.nbani.ecsetnaplo.data.local.entity.RoomEntity

@Dao
interface RoomDao {
    @Query("SELECT * FROM rooms")
    fun observeRooms(): Flow<List<RoomEntity>>

    @Query("SELECT * FROM rooms WHERE id = :id")
    suspend fun getRoom(id: Long): RoomEntity?

    @Insert
    suspend fun insert(room: RoomEntity)

    @Update
    suspend fun update(room: RoomEntity)

    @Delete
    suspend fun delete(roomEntity: RoomEntity)
}