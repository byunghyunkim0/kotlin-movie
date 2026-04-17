package movie.controller

import movie.domain.dto.MovieResponse
import movie.domain.dto.MoviesResponse
import movie.persistence.jdbcrepository.MovieRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/movies")
class MovieController(
    private val movieRepository: MovieRepository,
) {
    @GetMapping
    fun getAllMovies(): MoviesResponse {
        val moviesWithScreenings = movieRepository.findAllWithScreenings()
        val movieResponses =
            moviesWithScreenings.map { (movie, screenings) ->
                MovieResponse.of(movie, screenings)
            }
        return MoviesResponse(movieResponses)
    }
}
