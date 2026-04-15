package domain.movie

import domain.movie.itmes.RunningTime
import domain.movie.itmes.ScreeningPeriod
import domain.movie.itmes.Title
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class MovieTest {
    @Test
    fun `입력받은 영화 제목이 Title과 같으면 True를 반환한다`() {
        val movie =
            Movie(
                title = Title("신바드의 모험"),
                runningTime = RunningTime(120),
                screeningPeriod =
                    ScreeningPeriod(
                        startDate = LocalDate.of(2026, 4, 1),
                        endDate = LocalDate.of(2026, 4, 30),
                    ),
            )

        val result = movie.isValidTitle(Title("신바드의 모험"))

        assertThat(result).isTrue()
    }

    @Test
    fun `입력받은 영화 제목이 Title과 다르면 False를 반환한다`() {
        val movie =
            Movie(
                title = Title("신바드의 모험"),
                runningTime = RunningTime(120),
                screeningPeriod =
                    ScreeningPeriod(
                        startDate = LocalDate.of(2026, 4, 1),
                        endDate = LocalDate.of(2026, 4, 30),
                    ),
            )

        val result = movie.isValidTitle(Title("신밧드의 모험"))

        assertThat(result).isFalse()
    }

    @Test
    fun `입력 받은 날자가 영화의 상영 기간에 포함된다면 true가 반환된다`() {
        val movie =
            Movie(
                title = Title("신바드의 모험"),
                runningTime = RunningTime(120),
                screeningPeriod =
                    ScreeningPeriod(
                        startDate = LocalDate.of(2026, 4, 1),
                        endDate = LocalDate.of(2026, 4, 30),
                    ),
            )

        val result = movie.isScreening(LocalDate.of(2026, 4, 8))

        assertThat(result).isTrue()
    }

    @Test
    fun `입력 받은 날자가 영화의 상영 기간에 포함되지 않는다면 false가 반환된다`() {
        val movie =
            Movie(
                title = Title("신바드의 모험"),
                runningTime = RunningTime(120),
                screeningPeriod =
                    ScreeningPeriod(
                        startDate = LocalDate.of(2026, 4, 1),
                        endDate = LocalDate.of(2026, 4, 30),
                    ),
            )

        val result = movie.isScreening(LocalDate.of(2025, 4, 8))

        assertThat(result).isFalse()
    }
}
