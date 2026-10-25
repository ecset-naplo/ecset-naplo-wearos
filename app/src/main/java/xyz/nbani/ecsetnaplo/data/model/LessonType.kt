package xyz.nbani.ecsetnaplo.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.ui.graphics.vector.ImageVector
import xyz.nbani.ecsetnaplo.R

enum class LessonType(val icon: ImageVector, val localized: Int) {
    MATH(Icons.Default.Calculate, R.string.math),
    NATIVE_LANGUAGE(Icons.Default.Book, R.string.native_language),
    HOMEROOM(Icons.Default.Group, R.string.homeroom),
    LANGUAGE(Icons.Default.Language, R.string.language),
    MUSIC(Icons.Default.MusicNote, R.string.music),
    EXERCISE(Icons.Default.SportsSoccer, R.string.excercise),
    HISTORY(Icons.Default.HistoryEdu, R.string.history),
    LITERATURE(Icons.AutoMirrored.Filled.MenuBook, R.string.literature),
    RELIGION(Icons.Default.Church, R.string.religion),
    BIOLOGY(Icons.Default.Biotech, R.string.biology),
    IT(Icons.Default.Computer, R.string.informatics),
    PHYSICS(Icons.Default.Speed, R.string.physics),
    CHEMISTRY(Icons.Default.Science, R.string.chemistry),
    ARTS(Icons.Default.Draw, R.string.arts),
    GEOGRAPHY(Icons.Default.Public, R.string.geography),
    OTHER(Icons.Default.School, R.string.other)
}