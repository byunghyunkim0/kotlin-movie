package movie.domain.dto

import movie.persistence.entity.MovieEntity
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
) {
    companion object {
        fun of(
            movie: MovieEntity,
            screenings: List<ScreeningScheduleEntity>,
        ) = MovieResponse(
            id = movie.id,
            title = movie.title,
            runningTimeMinutes = movie.runningTimeMinutes,
            screenings = screenings.map { ScreeningResponse.from(it) },
        )
    }
}

data class ScreeningResponse(
    val id: Long?,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
) {
    companion object {
        fun from(entity: ScreeningScheduleEntity) =
            ScreeningResponse(
                id = entity.id,
                startAt = entity.startAt,
                endAt = entity.endAt,
            )
    }
}
