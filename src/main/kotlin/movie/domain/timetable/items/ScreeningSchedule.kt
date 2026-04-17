package movie.domain.timetable.items

import movie.domain.dto.ScreeningScheduleDto
import movie.domain.dto.SeatStatusDto
import movie.domain.movie.Movie
import movie.domain.movie.items.Title
import movie.domain.reservations.items.Reservation
import movie.domain.seat.items.SeatPosition
import java.time.LocalDate

class ScreeningSchedule(
    private val movie: Movie,
    private val screen: Screen,
    private val screenTime: ScreenTime,
    private val reservedSeat: ReservedSeats = ReservedSeats(),
    private val id: Long? = null,
) {
    fun getId() = id

    fun isSameMovie(otherMovie: Movie): Boolean = movie.isSame(otherMovie)

    fun isSameTime(otherTime: ScreenTime): Boolean = screenTime.isSame(otherTime)

    fun isDuplicatedScreenTime(otherTime: ScreenTime): Boolean = screenTime.isDuplicatedScreenTime(otherTime)

    fun isScreeningMovieTitle(title: Title) = movie.isValidTitle(title)

    fun isScreeningDate(date: LocalDate) = screenTime.isScreeningAt(date)

    fun addReservedSeat(positions: List<SeatPosition>): ScreeningSchedule =
        ScreeningSchedule(
            movie = movie,
            screen = screen,
            screenTime = screenTime,
            reservedSeat = reservedSeat.addSeat(positions),
        )

    fun makeReservation(positions: List<SeatPosition>): Reservation {
        positions.forEach { require(!reservedSeat.isReservedSeatPosition(it)) { "이미 예약된 좌석 입니다." } }
        val seats = positions.map { screen.findSeat(it) }
        return Reservation(
            movie = movie,
            screenTime = screenTime,
            seats = Seats(seats),
            screeningId = id,
        )
    }

    fun toDto(): ScreeningScheduleDto =
        ScreeningScheduleDto(
            time = screenTime.getStartTime().toString(),
        )

    fun getSeatLayout(): List<List<SeatStatusDto>> = screen.getLayout(reservedSeat)
}
