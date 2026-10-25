package xyz.nbani.ecsetnaplo.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow
import xyz.nbani.ecsetnaplo.data.local.entity.Lesson

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons")
    fun observeLessons(): Flow<List<Lesson>>

    @Query("SELECT * FROM lessons WHERE id = :id")
    suspend fun getLesson(id: Long): Lesson?

    @Insert
    suspend fun insert(lesson: Lesson)

    @Update
    suspend fun update(lesson: Lesson)

    @Delete
    suspend fun delete(lesson: Lesson)
}