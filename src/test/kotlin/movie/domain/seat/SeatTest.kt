package movie.domain.seat

import movie.domain.money.Money
import movie.domain.seat.Seat
import movie.domain.seat.items.ColumnNumber
import movie.domain.seat.items.RowNumber
import movie.domain.seat.items.SeatGrade
import movie.domain.seat.items.SeatPosition
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SeatTest {
    @Test
    fun `입력된 좌석 번호가 저장된 좌석 번호와 일치하면 true를 반환한다`() {
        val seat =
            Seat(
                seatPosition =
                    SeatPosition(
                        RowNumber("A"),
                        ColumnNumber(1),
                    ),
                seatGrade = SeatGrade.S,
            )

        val result = seat.isExistSeatPosition(SeatPosition.of("A1"))

        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 좌석 번호가 저장된 좌석 번호와 일치하지 않으면 false를 반환한다`() {
        val seat =
            Seat(
                seatPosition =
                    SeatPosition(
                        RowNumber("A"),
                        ColumnNumber(1),
                    ),
                seatGrade = SeatGrade.S,
            )

        val result = seat.isExistSeatPosition(SeatPosition.of("B2"))

        assertThat(result).isFalse()
    }

    @Test
    fun `좌석의 등급이 S이면 가격은 18000원이다`() {
        val seat =
            Seat(
                seatPosition =
                    SeatPosition(
                        RowNumber("A"),
                        ColumnNumber(1),
                    ),
                seatGrade = SeatGrade.S,
            )
        val initMoney = Money(0)
        val result = seat.addSeatPrice(initMoney)
        assertThat(result).isEqualTo(Money(18_000))
    }

    @Test
    fun `좌석의 등급이 A이면 가격은 15000원이다`() {
        val seat =
            Seat(
                seatPosition =
                    SeatPosition(
                        RowNumber("A"),
                        ColumnNumber(1),
                    ),
                seatGrade = SeatGrade.A,
            )
        val initMoney = Money(0)
        val result = seat.addSeatPrice(initMoney)
        assertThat(result).isEqualTo(Money(15_000))
    }

    @Test
    fun `좌석의 등급이 B이면 가격은 12000원이다`() {
        val seat =
            Seat(
                seatPosition =
                    SeatPosition(
                        RowNumber("A"),
                        ColumnNumber(1),
                    ),
                seatGrade = SeatGrade.B,
            )
        val initMoney = Money(0)
        val result = seat.addSeatPrice(initMoney)
        assertThat(result).isEqualTo(Money(12_000))
    }
}
