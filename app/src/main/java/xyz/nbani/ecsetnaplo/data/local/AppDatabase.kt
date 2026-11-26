package xyz.nbani.ecsetnaplo.data.local

import android.content.Context
import androidx.room3.AutoMigration
import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.AndroidSQLiteDriver
import xyz.nbani.ecsetnaplo.data.local.dao.LessonDao
import xyz.nbani.ecsetnaplo.data.local.dao.RoomDao
import xyz.nbani.ecsetnaplo.data.local.dao.SettingsDao
import xyz.nbani.ecsetnaplo.data.local.dao.TeacherDao
import xyz.nbani.ecsetnaplo.data.local.dao.TimetableDao
import xyz.nbani.ecsetnaplo.data.local.entity.IrregularPeriod
import xyz.nbani.ecsetnaplo.data.local.entity.Lesson
import xyz.nbani.ecsetnaplo.data.local.entity.RegularPeriod
import xyz.nbani.ecsetnaplo.data.local.entity.RoomEntity
import xyz.nbani.ecsetnaplo.data.local.entity.Settings
import xyz.nbani.ecsetnaplo.data.local.entity.Teacher

@Database(
    entities = [
        RoomEntity::class,
        Lesson::class,
        Teacher::class,
        IrregularPeriod::class,
        RegularPeriod::class,
        Settings::class
    ],
    version = 4,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4)
    ]
)
@ColumnTypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDao
    abstract fun lessonDao(): LessonDao
    abstract fun teacherDao(): TeacherDao
    abstract fun timetableDao(): TimetableDao
    abstract fun settingsDao(): SettingsDao
}


fun buildDatabase(context: Context): AppDatabase =
    Room.databaseBuilder<AppDatabase>(context.applicationContext, "app.db")
        .setDriver(AndroidSQLiteDriver())
        .build()

interface DatabaseHolder {
    val db: AppDatabase
}