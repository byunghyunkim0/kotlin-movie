package domain.timetable.items

import domain.dto.ScreeningScheduleDto
import domain.dto.SeatStatusDto
import domain.movie.Movie
import domain.movie.itmes.Title
import domain.reservations.items.Reservation
import domain.seat.items.SeatPosition
import java.time.LocalDate

class ScreeningSchedule(
    private val movie: Movie,
    private val screen: Screen,
    private val screenTime: ScreenTime,
    private val reservedSeat: ReservedSeats = ReservedSeats(),
) {
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
        )
    }

    fun toDto(): ScreeningScheduleDto =
        ScreeningScheduleDto(
            time = screenTime.getStartTime().toString(),
        )

    fun getSeatLayout(): List<List<SeatStatusDto>> = screen.getLayout(reservedSeat)
}
