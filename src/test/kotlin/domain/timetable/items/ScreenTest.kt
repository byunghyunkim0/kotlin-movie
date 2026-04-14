package domain.timetable.items

import domain.seat.Seat
import domain.seat.items.ColumnNumber
import domain.seat.items.RowNumber
import domain.seat.items.SeatGrade
import domain.seat.items.SeatPosition
import domain.timetable.items.ScreenSeatMock.seats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ScreenTest {
    @Test
    fun `입력된 좌석 번호가 상영관에 존재하는 좌석이면 true가 반환된다`() {
        val screen =
            Screen(
                seats = Seats(seats),
                name = ScreenName("1관"),
            )

        val result = screen.isExistSeat(SeatPosition.of("A1"))
        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 좌석 번호가 상영관에 존재하지 않는 좌석이면 false가 반환된다`() {
        val screen =
            Screen(
                seats = Seats(seats),
                name = ScreenName("1관"),
            )

        val result = screen.isExistSeat(SeatPosition.of("F1"))
        assertThat(result).isFalse()
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
