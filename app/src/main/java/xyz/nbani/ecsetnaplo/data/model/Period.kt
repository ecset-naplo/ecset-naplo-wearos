package xyz.nbani.ecsetnaplo.data.model

import xyz.nbani.ecsetnaplo.data.local.entity.IrregularPeriod
import xyz.nbani.ecsetnaplo.data.local.entity.Lesson
import xyz.nbani.ecsetnaplo.data.local.entity.RegularPeriod
import xyz.nbani.ecsetnaplo.data.local.entity.RoomEntity

data class Period (
    val regularPeriod: RegularPeriod? = null,
    val irregularPeriod: IrregularPeriod? = null,
    val lesson: Lesson,
    val roomEntity: RoomEntity?
) {
    init {
        require(regularPeriod != null || irregularPeriod != null) {
            "Period requires a regularPeriod or an irregularPeriod"
        }
    }

    val isStandaloneIrregular get() = regularPeriod == null
    val isChangedRegularPeriod get() = regularPeriod != null && irregularPeriod != null

    val period get() = irregularPeriod?.period
        ?: regularPeriod?.period
        ?: error("Period has no period int set")
    val startTime get() = irregularPeriod?.startTime
        ?: regularPeriod?.startTime
        ?: error("Period has no start time")
    val endTime get() = irregularPeriod?.endTime
        ?: regularPeriod?.endTime
        ?: error("Period has no end time")
}
