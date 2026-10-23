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

enum class LessonType(val icon: ImageVector) {
    MATH(Icons.Default.Calculate),
    NATIVE_LANGUAGE(Icons.Default.Book),
    HOMEROOM(Icons.Default.Group),
    LANGUAGE(Icons.Default.Language),
    MUSIC(Icons.Default.MusicNote),
    EXERCISE(Icons.Default.SportsSoccer),
    HISTORY(Icons.Default.HistoryEdu),
    LITERATURE(Icons.AutoMirrored.Filled.MenuBook),
    RELIGION(Icons.Default.Church),
    BIOLOGY(Icons.Default.Biotech),
    IT(Icons.Default.Computer),
    PHYSICS(Icons.Default.Speed),
    CHEMISTRY(Icons.Default.Science),
    ARTS(Icons.Default.Draw),
    GEOGRAPHY(Icons.Default.Public),
    OTHER(Icons.Default.School)
}