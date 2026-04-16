package domain.timetable.items

import domain.dto.SeatStatusDto
import domain.money.Money
import domain.seat.Seat
import domain.seat.items.SeatPosition

class Seats(
    private val seats: List<Seat>,
) {
    fun toSeatPositions(): List<SeatPosition> = seats.map { it.toSeatPosition() }

    fun findSeat(seatNumber: SeatPosition): Seat =
        seats.find { it.isExistSeatPosition(seatNumber) } ?: throw IllegalArgumentException("해당 좌석을 찾을 수 없습니다.")

    fun sumPrice(): Money {
        val initMoney = Money(0)
        return seats.fold(initMoney) { total, seat ->
            seat.addSeatPrice(total)
        }
    }

    fun getSeats() = seats

    fun getLayout(reservedSeats: ReservedSeats): List<List<SeatStatusDto>> =
        seats
            .groupBy { it.getRow() }
            .toSortedMap()
            .values
            .map { row ->
                row
                    .sortedBy { it.getColumn() }
                    .map { seat ->
                        val isReserved = reservedSeats.isReservedSeatPosition(seat.toSeatPosition())
                        SeatStatusDto.of(
                            seat = seat,
                            isReserved = isReserved,
                        )
                    }
            }
}
