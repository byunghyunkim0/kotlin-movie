package movie.persistence.entity

import java.time.LocalDateTime

data class ScreeningScheduleEntity(
    val id: Long? = null,
    val movieId: Long,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
)
