package xyz.nbani.ecsetnaplo.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow
import xyz.nbani.ecsetnaplo.data.local.entity.IrregularPeriod
import xyz.nbani.ecsetnaplo.data.local.entity.RegularPeriod
import java.time.DayOfWeek
import java.time.LocalDate

@Dao
interface TimetableDao {
    // Regular period
    @Query("""
        SELECT * FROM regular_periods
        WHERE weekday = :day AND validFrom <= :date AND (validTo IS NULL OR validTo >= :date)     
    """)
    fun observeRegularPeriodsOfDay(day: DayOfWeek, date: LocalDate): Flow<List<RegularPeriod>>

    @Query("SELECT * FROM regular_periods WHERE id = :id")
    suspend fun getRegularPeriod(id: Long): RegularPeriod?

    @Insert
    suspend fun insertRegularPeriod(regularPeriod: RegularPeriod)

    @Update
    suspend fun updateRegularPeriod(regularPeriod: RegularPeriod)

    @Delete
    suspend fun deleteRegularPeriod(regularPeriod: RegularPeriod)

    // Irregular period
    @Query("SELECT * FROM irregular_periods WHERE date = :date")
    fun observeIrregularPeriodsOfDate(date: LocalDate): Flow<List<IrregularPeriod>>

    @Insert
    suspend fun insertIrregularPeriod(irregularPeriod: IrregularPeriod)

    @Update
    suspend fun updateIrregularPeriod(irregularPeriod: IrregularPeriod)

    @Delete
    suspend fun deleteIrregularPeriod(irregularPeriod: IrregularPeriod)
}