package movie.domain.timetable.items

import movie.domain.dto.SeatStatusDto
import movie.domain.seat.Seat
import movie.domain.seat.items.SeatPosition

class Screen(
    private val name: ScreenName,
    private val seats: Seats,
) {
    fun findSeat(position: SeatPosition): Seat = seats.findSeat(position)

    fun getLayout(reservedSeats: ReservedSeats): List<List<SeatStatusDto>> = seats.getLayout(reservedSeats)
}

@JvmInline
value class ScreenName(
    private val name: String,
)
