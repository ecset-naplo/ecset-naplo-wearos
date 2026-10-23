package xyz.nbani.ecsetnaplo.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import xyz.nbani.ecsetnaplo.data.local.dao.LessonDao
import xyz.nbani.ecsetnaplo.data.local.dao.RoomDao
import xyz.nbani.ecsetnaplo.data.local.dao.TeacherDao
import xyz.nbani.ecsetnaplo.data.local.dao.TimetableDao
import xyz.nbani.ecsetnaplo.data.local.entity.IrregularPeriod
import xyz.nbani.ecsetnaplo.data.local.entity.Lesson
import xyz.nbani.ecsetnaplo.data.local.entity.RegularPeriod
import xyz.nbani.ecsetnaplo.data.local.entity.RoomEntity
import xyz.nbani.ecsetnaplo.data.local.entity.Teacher
import xyz.nbani.ecsetnaplo.data.model.Period
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class TimetableRepository(
    private val timetableDao: TimetableDao,
    private val lessonDao: LessonDao,
    private val roomDao: RoomDao,
    private val teacherDao: TeacherDao
) {
    private fun createPeriod(
        regularPeriod: RegularPeriod?,
        irregularPeriod: IrregularPeriod?,
        lessonById: Map<Long, Lesson>,
        roomEntityById: Map<Long, RoomEntity>
    ): Period? {
        val lessonId = irregularPeriod?.lessonId ?: regularPeriod?.lessonId ?: return null
        val lesson = lessonById[lessonId] ?: return null

        if (irregularPeriod != null &&
            (irregularPeriod.startTime == null || irregularPeriod.endTime == null)
        ) return null

        val room = (irregularPeriod?.roomId ?: regularPeriod?.roomId)
            ?.let { roomEntityById[it] }

        return Period(regularPeriod, irregularPeriod, lesson, room)
    }

    private fun buildPeriods(
        regularPeriods: List<RegularPeriod>,
        irregularPeriods: List<IrregularPeriod>,
        lessonById: Map<Long, Lesson>,
        roomEntityById: Map<Long, RoomEntity>
    ): List<Period> {
        val replacedRegularPeriodIds = irregularPeriods
            .mapNotNull { it.regularPeriodId }
            .toSet()

        val regularPeriods = regularPeriods
            .filter { it.id !in replacedRegularPeriodIds }
            .mapNotNull { createPeriod(it, null, lessonById, roomEntityById) }

        val irregularPeriods = irregularPeriods
            .mapNotNull { createPeriod(null, it, lessonById, roomEntityById) }

        return (regularPeriods + irregularPeriods).sortedBy { it.startTime }
    }

    fun observeAllPeriodsOfDate(date: LocalDate): Flow<List<Period>> =
        combine(
            timetableDao.observeRegularPeriodsOfDay(date.dayOfWeek, date),
            timetableDao.observeIrregularPeriodsOfDate(date),
            lessonDao.observeLessons(),
            roomDao.observeRooms(),
        ) { regularPeriods, irregularPeriods, lessons, rooms ->
            buildPeriods(
                regularPeriods = regularPeriods,
                irregularPeriods = irregularPeriods,
                lessonById = lessons.associateBy { it.id },
                roomEntityById = rooms.associateBy { it.id },
            )
        }
            .flowOn(Dispatchers.Default)
            .distinctUntilChanged()

    private fun ticker(interval: Duration): Flow<LocalDateTime> = flow {
        while (true) {
            emit(LocalDateTime.now())
            delay(interval)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeCurrentPeriod(): Flow<Period?> {
        val ticker = ticker(1.milliseconds)

        return ticker
            .map { it.toLocalDate() }
            .distinctUntilChanged()
            .flatMapLatest { date -> observeAllPeriodsOfDate(date) }
            .combine(ticker) { periods, now ->
                val time = now.toLocalTime()
                periods.firstOrNull { time >= it.startTime && time < it.endTime }
            }
            .distinctUntilChanged()
    }

    // Room related functions
    fun observeAllRooms(): Flow<List<RoomEntity>> = roomDao.observeRooms()

    suspend fun getRoom(id: Long): RoomEntity? = roomDao.getRoom(id)

    suspend fun insertRoom(room: RoomEntity) = roomDao.insert(room)

    suspend fun updateRoom(room: RoomEntity) = roomDao.update(room)

    suspend fun deleteRoom(room: RoomEntity) = roomDao.delete(room)


    // Teacher related functions
    fun observeAllTeachers(): Flow<List<Teacher>> = teacherDao.observeTeachers()

    suspend fun getTeacher(id: Long): Teacher? = teacherDao.getTeacher(id)

    suspend fun insertTeacher(teacher: Teacher) = teacherDao.insert(teacher)

    suspend fun updateTeacher(teacher: Teacher) = teacherDao.update(teacher)

    suspend fun deleteTeacher(teacher: Teacher) = teacherDao.delete(teacher)
}