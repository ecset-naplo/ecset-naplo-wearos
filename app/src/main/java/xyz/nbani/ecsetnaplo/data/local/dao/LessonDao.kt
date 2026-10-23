package xyz.nbani.ecsetnaplo.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow
import xyz.nbani.ecsetnaplo.data.local.entity.Lesson

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons")
    fun observeLessons(): Flow<List<Lesson>>

    @Insert
    suspend fun insert(lesson: Lesson)

    @Delete
    suspend fun delete(lesson: Lesson)
}