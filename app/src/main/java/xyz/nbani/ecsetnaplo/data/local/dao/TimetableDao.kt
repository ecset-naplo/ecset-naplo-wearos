package xyz.nbani.ecsetnaplo.data.local.dao

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Transaction
import kotlinx.coroutines.flow.Flow
import xyz.nbani.ecsetnaplo.data.local.entity.IrregularPeriod
import xyz.nbani.ecsetnaplo.data.local.entity.RegularPeriod
import java.time.DayOfWeek
import java.time.LocalDate

@Dao
interface TimetableDao {
    @Query("""
        SELECT * FROM regular_periods
        WHERE weekday = :day AND validFrom <= :date AND (validTo IS NULL OR validTo >= :date)     
    """)
    fun observeRegularPeriodsOfDay(day: DayOfWeek, date: LocalDate): Flow<List<RegularPeriod>>

    @Query("SELECT * FROM irregular_periods WHERE date = :date")
    fun observeIrregularPeriodsOfDate(date: LocalDate): Flow<List<IrregularPeriod>>
}