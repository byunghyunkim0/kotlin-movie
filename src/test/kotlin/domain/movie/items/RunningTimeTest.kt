package domain.movie.items

import domain.movie.itmes.RunningTime
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RunningTimeTest {
    @Test
    fun `영화의 러닝 타임이 0이면 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> { RunningTime(0) }
    }

    @Test
    fun `영화의 러닝 타임이 0보다 작으면 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> { RunningTime(-1) }
    }
}
