package movie.controller

import movie.domain.dto.MovieResponse
import movie.domain.dto.MoviesResponse
import movie.domain.dto.ScreeningResponse
import movie.persistence.jdbcrepository.MovieRepository
import movie.persistence.jdbcrepository.ReservedSeatRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/movies")
class MovieController(
    private val movieRepository: MovieRepository,
    private val reservedSeatRepository: ReservedSeatRepository,
) {
    @GetMapping
    fun getAllMovies(): MoviesResponse {
        val moviesWithScreenings = movieRepository.findAllWithScreenings()
        val movieResponses =
            moviesWithScreenings.map { (movie, screenings) ->
                val screeningResponses =
                    screenings.map { screening ->
                        val reservedSeats =
                            reservedSeatRepository
                                .findByScreeningId(screening.id!!)
                                .map { it.seatNumber }
                        ScreeningResponse.from(screening, reservedSeats)
                    }
                MovieResponse(
                    id = movie.id,
                    title = movie.title,
                    runningTimeMinutes = movie.runningTimeMinutes,
                    screenings = screeningResponses,
                )
            }
        return MoviesResponse(movieResponses)
    }
}
