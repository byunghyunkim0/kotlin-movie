package movie.domain.movie.items

import movie.domain.movie.itmes.ScreeningPeriod
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

class ScreeningPeriodTest {
    @Test
    fun `영화의 상영 종료일이 상영 시작일보다 빠르면 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> {
            ScreeningPeriod(
                startDate = LocalDate.of(2026, 4, 1),
                endDate = LocalDate.of(2026, 3, 30),
            )
        }
    }

    @Test
    fun `입력 받은 날자가 영화의 상영 기간에 포함된다면 true가 반환된다`() {
        val screeningPeriod =
            ScreeningPeriod(
                startDate = LocalDate.of(2026, 4, 1),
                endDate = LocalDate.of(2026, 4, 30),
            )

        val result =
            screeningPeriod.contains(
                LocalDate.of(
                    2026,
                    4,
                    8,
                ),
            )

        assertThat(result).isTrue()
    }

    @Test
    fun `입력 받은 날자가 영화의 상영 기간에 포함되지 않는다면 false가 반환된다`() {
        val screeningPeriod =
            ScreeningPeriod(
                startDate = LocalDate.of(2026, 4, 1),
                endDate = LocalDate.of(2026, 4, 30),
            )

        val result =
            screeningPeriod.contains(
                LocalDate.of(
                    2025,
                    4,
                    8,
                ),
            )

        assertThat(result).isFalse()
    }
}
