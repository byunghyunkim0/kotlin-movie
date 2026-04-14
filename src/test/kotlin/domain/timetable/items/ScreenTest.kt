package domain.timetable.items

import domain.seat.Seat
import domain.seat.items.ColumnNumber
import domain.seat.items.RowNumber
import domain.seat.items.SeatGrade
import domain.seat.items.SeatPosition
import domain.timetable.items.ScreenSeatMock.seats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ScreenTest {
    @Test
    fun `입력된 좌석 번호가 상영관에 존재하는 좌석이면 해당 좌석을 반환한다`() {
        val screen =
            Screen(
                seats = Seats(seats),
                name = ScreenName("1관"),
            )

        val result = screen.findSeat(SeatPosition.of("A1"))
        assertThat(result).isInstanceOf(Seat::class.java)
    }

    @Test
    fun `입력된 좌석 번호가 상영관에 존재하지 않는 좌석이면 예외를 발생시킨다`() {
        val screen =
            Screen(
                seats = Seats(seats),
                name = ScreenName("1관"),
            )

        assertThrows<IllegalArgumentException> { screen.findSeat(SeatPosition.of("F1")) }
    }
}

object ScreenSeatMock {
    val row = listOf("A", "B", "C", "D", "E")
    val col = listOf(1, 2, 3, 4, 5, 6)
    val seats =
        row.flatMap { row ->
            col.map { col ->
                val grade =
                    when (row) {
                        "A", "B" -> SeatGrade.B
                        "C", "D" -> SeatGrade.S
                        else -> SeatGrade.A
                    }
                Seat(
                    seatPosition =
                        SeatPosition(
                            RowNumber(row),
                            ColumnNumber(col),
                        ),
                    seatGrade = grade,
                )
            }
        }
}
