package domain.seat.items

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ColumnNumberTest {
    @Test
    fun `입력된 열 번호가 자신의 번호와 같다면 true를 반환한다`() {
        val colNum = ColumnNumber(1)

        val result = colNum.isSame(ColumnNumber(1))

        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 열 번호가 자신의 번호와 같지 않다면 false를 반환한다`() {
        val colNum = ColumnNumber(1)

        val result = colNum.isSame(ColumnNumber(2))

        assertThat(result).isFalse()
    }
}
