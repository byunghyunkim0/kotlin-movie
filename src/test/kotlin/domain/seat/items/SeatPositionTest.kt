package domain.seat.items

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SeatPositionTest {
    @Test
    fun `입력된 좌석 번호가 저장된 좌석 번호와 일치하면 true를 반환한다`() {
        val seatPosition =
            SeatPosition(
                rowNumber = RowNumber("A"),
                columnNumber = ColumnNumber(1),
            )

        val result = seatPosition.isExistSeatPosition(SeatPosition.of("A1"))

        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 좌석 번호가 저장된 좌석 번호와 일치하지 않으면 false를 반환한다`() {
        val seatPosition =
            SeatPosition(
                rowNumber = RowNumber("A"),
                columnNumber = ColumnNumber(1),
            )

        val result = seatPosition.isExistSeatPosition(SeatPosition.of("A2"))

        assertThat(result).isFalse()
    }
}
