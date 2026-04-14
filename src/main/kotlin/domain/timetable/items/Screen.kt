package domain.timetable.items

import domain.dto.SeatStatusDto
import domain.seat.Seat
import domain.seat.items.SeatPosition

class Screen(
    private val name: ScreenName,
    private val seats: Seats,
) {
    fun isExistSeat(number: SeatPosition): Boolean = seats.isExistSeatNumber(number)

    fun findSeat(position: SeatPosition): Seat = seats.findSeat(position)

    fun getLayout(reservedSeats: ReservedSeats): List<List<SeatStatusDto>> = seats.getLayout(reservedSeats)
}

@JvmInline
value class ScreenName(
    private val name: String,
)
