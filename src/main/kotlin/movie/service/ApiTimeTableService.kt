package movie.service

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import movie.domain.dto.MoviesResponse
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
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDate

class ApiTimeTableService(
    private val baseUrl: String,
) : TimeTableService {
    private val client = HttpClient.newBuilder().build()
    private val mapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    override fun getTimeTable(): TimeTable {
        val httpRequest =
            HttpRequest
                .newBuilder()
                .uri(URI.create("$baseUrl/api/movies"))
                .GET()
                .build()

        val response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() != 200) {
            throw RuntimeException("시간표 로드 실패: ${response.statusCode()}")
        }

        val moviesResponse: MoviesResponse = mapper.readValue(response.body())

        val schedules =
            moviesResponse.movies.flatMap { movieDto ->
                val movie =
                    Movie(
                        title = Title(movieDto.title),
                        runningTime = RunningTime(movieDto.runningTimeMinutes),
                        screeningPeriod =
                            ScreeningPeriod(
                                startDate = LocalDate.of(2020, 1, 1),
                                endDate = LocalDate.of(2030, 12, 31),
                            ),
                    )

                movieDto.screenings.map { screeningDto ->
                    val reservedPositions = screeningDto.reservedSeats.map { SeatPosition.of(it) }

                    ScreeningSchedule(
                        movie = movie,
                        screen = defaultScreen(),
                        screenTime =
                            ScreenTime(
                                startTime = screeningDto.startAt.toLocalTime(),
                                endTime = screeningDto.endAt.toLocalTime(),
                                screeningDate = screeningDto.startAt.toLocalDate(),
                            ),
                        reservedSeat = ReservedSeats(reservedPositions),
                        id = screeningDto.id,
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
