package domain.movie

import domain.movie.itmes.RunningTime
import domain.movie.itmes.ScreeningPeriod
import domain.movie.itmes.Title
import java.time.LocalDate

class Movie(
    private val title: Title,
    private val runningTime: RunningTime,
    private val screeningPeriod: ScreeningPeriod,
) {
    fun isSame(movie: Movie): Boolean = title.isSame(movie.title)

    fun isValidTitle(other: Title): Boolean = title.isSame(other)

    fun isScreening(date: LocalDate): Boolean = screeningPeriod.contains(date)

    fun getMovieTitle() = title.getTitle()
}
