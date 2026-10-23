package xyz.nbani.ecsetnaplo.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow
import xyz.nbani.ecsetnaplo.data.local.entity.Teacher

@Dao
interface TeacherDao {
    @Delete
    suspend fun delete(teacher: Teacher)

    @Query("DELETE FROM lessons where id = :id")
    suspend fun deleteLessonsOf(id: Long)

    @Transaction
    suspend fun deleteWithLessons(teacher: Teacher) {
        deleteLessonsOf(teacher.id)
        delete(teacher)
    }

    @Insert
    suspend fun insert(teacher: Teacher)

    @Update
    suspend fun update(teacher: Teacher)

    @Query("SELECT * FROM teachers")
    fun observeTeachers(): Flow<List<Teacher>>

    @Query("SELECT * FROM teachers WHERE id = :id")
    suspend fun getTeacher(id: Long): Teacher?
}