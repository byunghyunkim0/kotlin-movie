package domain.timetable.items

import domain.money.Money
import domain.seat.Seat
import domain.seat.items.SeatPosition

class Seats(
    private val seats: List<Seat>,
) {
    fun toSeatPositions(): List<SeatPosition> = seats.map { it.toSeatPosition() }

    fun isExistSeatNumber(seatNumber: SeatPosition): Boolean = seats.any { it.isExistSeatPosition(seatNumber) }

    fun findSeat(seatNumber: SeatPosition): Seat =
        seats.find { it.isExistSeatPosition(seatNumber) } ?: throw IllegalArgumentException("해당 좌석을 찾을 수 없습니다.")

    fun sumPrice(): Money {
        val initMoney = Money(0)
        return seats.fold(initMoney) { total, seat ->
            seat.addSeatPrice(total)
        }
    }

    fun getSeats() = seats
}
