package xyz.nbani.ecsetnaplo.data.local.dao

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import xyz.nbani.ecsetnaplo.data.local.entity.Settings

@Dao
interface SettingsDao {
    /**
     * Saves settings by upserting (updates the settings table, or if not set, inserts values).
     * > **IMPORTANT:** Do not update the id field, as that won't update settings,
     * since the app **always** looks for ID 1 in the table.
     */
    @Upsert
    fun saveSettings(settings: Settings)

    /** Observes all settings with a flow, unless they haven't been created/initialized yet. */
    @Query("SELECT * FROM settings WHERE id = 1")
    fun observeNullableSettings(): Flow<Settings?>

    /** Initializes all settings. */
    @Transaction
    suspend fun initializeSettings() {
        if (observeNullableSettings().first() == null) {
            saveSettings(Settings())
        }
    }
}