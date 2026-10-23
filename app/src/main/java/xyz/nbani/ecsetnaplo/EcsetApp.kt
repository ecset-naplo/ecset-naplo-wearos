package xyz.nbani.ecsetnaplo

import android.app.Application
import xyz.nbani.ecsetnaplo.data.local.AppDatabase
import xyz.nbani.ecsetnaplo.data.local.DatabaseHolder
import xyz.nbani.ecsetnaplo.data.local.buildDatabase
import xyz.nbani.ecsetnaplo.data.repository.TimetableRepository

class EcsetApp : Application(), DatabaseHolder {
    override val db: AppDatabase by lazy { buildDatabase(this) }

    val timetableRepository by lazy {
        TimetableRepository(
            db.timetableDao(),
            db.lessonDao(),
            db.roomDao(),
            db.teacherDao(),
        )
    }
}