package domain.seat.items

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RowNumberTest {
    @Test
    fun `입력된 행 번호가 자신의 번호와 같다면 true를 반환한다`() {
        val rowNumber = RowNumber("A")

        val result = rowNumber.isSame(RowNumber("A"))

        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 행 번호가 자신의 번호와 같지 않다면 false를 반환한다`() {
        val rowNumber = RowNumber("A")

        val result = rowNumber.isSame(RowNumber("B"))

        assertThat(result).isFalse()
    }
}
