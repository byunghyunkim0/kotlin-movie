package movie.domain.timetable.items

import movie.domain.seat.items.SeatPosition
import movie.domain.timetable.items.ReservedSeats
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class ReservedSeatsTest {
    @Test
    fun `입력된 좌석 번호를 reserved seats에 추가한다`() {
        val reservedSeats = ReservedSeats()
        val seatA1 = SeatPosition.of("A1")

        assertDoesNotThrow { reservedSeats.addSeat(listOf(seatA1)) }
    }

    @Test
    fun `입력된 좌석이 reserved seats에 이미 있다면 예외를 발생한다`() {
        val reservedSeats = ReservedSeats()
        val seatA1 = SeatPosition.of("A1")
        val newReservedSeats = reservedSeats.addSeat(listOf(seatA1))

        assertThrows<IllegalArgumentException> { newReservedSeats.addSeat(listOf(seatA1)) }
    }
}
