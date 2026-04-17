package movie.service

import movie.domain.movie.Movie
import movie.domain.movie.items.RunningTime
import movie.domain.movie.items.ScreeningPeriod
import movie.domain.movie.items.Title
import movie.domain.seat.Seat
import movie.domain.seat.items.ColumnNumber
import movie.domain.seat.items.RowNumber
import movie.domain.seat.items.SeatGrade
import movie.domain.seat.items.SeatPosition
import movie.domain.timetable.TimeTable
import movie.domain.timetable.items.ReservedSeats
import movie.domain.timetable.items.Screen
import movie.domain.timetable.items.ScreenName
import movie.domain.timetable.items.ScreenTime
import movie.domain.timetable.items.ScreeningSchedule
import movie.domain.timetable.items.Seats
import movie.persistence.jdbcrepository.MovieRepository
import movie.persistence.jdbcrepository.ReservedSeatRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DbTimeTableService(
    private val movieRepository: MovieRepository,
    private val reservedSeatRepository: ReservedSeatRepository,
) : TimeTableService {
    override fun getTimeTable(): TimeTable {
        val moviesWithScreenings = movieRepository.findAllWithScreenings()

        val schedules =
            moviesWithScreenings.flatMap { (movieEntity, screeningEntities) ->
                val movie =
                    Movie(
                        title = Title(movieEntity.title),
                        runningTime = RunningTime(movieEntity.runningTimeMinutes),
                        screeningPeriod =
                            ScreeningPeriod(
                                startDate = LocalDate.of(2020, 1, 1),
                                endDate = LocalDate.of(2030, 12, 31),
                            ),
                    )

                screeningEntities.map { screeningEntity ->
                    val reservedSeatsInDb = reservedSeatRepository.findByScreeningId(screeningEntity.id!!)
                    val positions = reservedSeatsInDb.map { SeatPosition.of(it.seatNumber) }

                    ScreeningSchedule(
                        movie = movie,
                        screen = defaultScreen(),
                        screenTime =
                            ScreenTime(
                                startTime = screeningEntity.startAt.toLocalTime(),
                                endTime = screeningEntity.endAt.toLocalTime(),
                                screeningDate = screeningEntity.startAt.toLocalDate(),
                            ),
                        reservedSeat = ReservedSeats(positions),
                        id = screeningEntity.id,
                    )
                }
            }

        return TimeTable(schedules)
    }

    private fun defaultScreen(): Screen {
        val row = listOf("A", "B", "C", "D", "E")
        val col = listOf(1, 2, 3, 4)
        return Screen(
            name = ScreenName("1관"),
            seats =
                Seats(
                    row.flatMap { r ->
                        col.map { c ->
                            val grade =
                                when (r) {
                                    "A", "B" -> SeatGrade.B
                                    "C", "D" -> SeatGrade.S
                                    else -> SeatGrade.A
                                }
                            Seat(SeatPosition(RowNumber(r), ColumnNumber(c)), grade)
                        }
                    },
                ),
        )
    }
}
