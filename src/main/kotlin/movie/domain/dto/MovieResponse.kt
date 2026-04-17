package movie.domain.dto

import movie.persistence.entity.ScreeningScheduleEntity
import java.time.LocalDateTime

data class MoviesResponse(
    val movies: List<MovieResponse>,
)

data class MovieResponse(
    val id: Long?,
    val title: String,
    val runningTimeMinutes: Int,
    val screenings: List<ScreeningResponse>,
)

data class ScreeningResponse(
    val id: Long?,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val reservedSeats: List<String>,
) {
    companion object {
        fun from(
            entity: ScreeningScheduleEntity,
            reservedSeats: List<String>,
        ) = ScreeningResponse(
            id = entity.id,
            startAt = entity.startAt,
            endAt = entity.endAt,
            reservedSeats = reservedSeats,
        )
    }
}
