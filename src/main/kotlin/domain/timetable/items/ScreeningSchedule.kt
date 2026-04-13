package domain.timetable.items

import domain.dto.ScreeningScheduleDto
import domain.movie.Movie
import domain.movie.itmes.Title
import domain.reservations.items.Reservation
import domain.seat.items.SeatPosition
import java.time.LocalDate

class ScreeningSchedule(
    private val movie: Movie,
    private val screen: Screen,
    private val screenTime: ScreenTime,
) {
    private val reservedSeat: ReservedSeats = ReservedSeats()

    fun isScreeningMovieTitle(title: Title) = movie.isValidTitle(title)

    fun isScreeningDate(date: LocalDate) = screenTime.isScreeningAt(date)

    fun addReserveSeat(positions: List<SeatPosition>) {
        positions.forEach { reservedSeat.addSeat(it) }
    }

    fun makeReservation(positions: List<SeatPosition>): Reservation {
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

    fun getSeatLayout(): List<List<String>> {
        val seats = screen.getSeats()
        return seats
            .groupBy { it.getRow() }
            .toSortedMap()
            .values
            .map { rowSeats ->
                rowSeats
                    .sortedBy { it.getColumn() }
                    .map { seat ->
                        if (reservedSeat.isReservedSeatPosition(SeatPosition.of(seat.getName()))) {
                            "X"
                        } else {
                            seat.getSeatGradeName()
                        }
                    }
            }
    }
}
