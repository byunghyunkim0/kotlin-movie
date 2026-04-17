package movie.domain.timetable.items

import movie.domain.seat.items.SeatPosition

class ReservedSeats(
    private val reservedSeats: List<SeatPosition> = emptyList(),
) {
    fun addSeat(seatPosition: List<SeatPosition>): ReservedSeats {
        seatPosition.forEach { require(!isReservedSeatPosition(it)) { "이미 예약된 좌석 입니다." } }
        return ReservedSeats(reservedSeats + seatPosition)
    }

    fun isReservedSeatPosition(seatPosition: SeatPosition): Boolean = reservedSeats.any { it.isExistSeatPosition(seatPosition) }
}
