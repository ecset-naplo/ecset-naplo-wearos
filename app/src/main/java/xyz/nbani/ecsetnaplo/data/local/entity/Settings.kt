package xyz.nbani.ecsetnaplo.data.local.entity

import android.os.Parcelable
import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * This Settings data class ("settings" table) keeps (almost) all the settings set by a user
 * that aren't period, lesson, teacher, or room entries.
 *
 * @property id The ID of the settings entry.
 * Should always be 1, as since there aren't multiple entries, unlike a lesson, period, room,
 * or teacher, which can have multiple rows.
 * @property showBreaksInTimetable The setting to show breaks in the timetable.
 */
@Entity("settings")
@Parcelize
data class Settings(
    @PrimaryKey val id: Int = 1,
    val showBreaksInTimetable: Boolean = true,

    // Developer options
    @ColumnInfo(defaultValue = "0")
    val developerOptions: Boolean = false,

    @ColumnInfo(defaultValue = "1")
    val setDeveloperOptionsToDefaultOnDisable: Boolean = true,
) : Parcelable
