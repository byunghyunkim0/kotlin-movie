package domain.money

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MoneyTest {
    @Test
    fun `입력된 값이 0보다 작으면 예외를 던진다`() {
        assertThrows<IllegalArgumentException> { Money(-1) }
    }
}
