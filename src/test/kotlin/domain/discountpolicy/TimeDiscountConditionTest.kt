package domain.discountpolicy

import domain.timetable.items.ScreenTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class TimeDiscountConditionTest {
    @Test
    fun `예약 시간이 오전 11이전 또는 오후 8시 이후라면 true를 반환한다`() {
        val morningScreenTime =
            ScreenTime(
                startTime = LocalTime.of(8, 0),
                endTime = LocalTime.of(11, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )

        val nightScreenTime =
            ScreenTime(
                startTime = LocalTime.of(21, 0),
                endTime = LocalTime.of(23, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )

        val morningResult = TimeCondition().isSatisfiedBy(morningScreenTime)
        val nightResult = TimeCondition().isSatisfiedBy(nightScreenTime)

        assertThat(morningResult).isTrue()
        assertThat(nightResult).isTrue()
    }

    @Test
    fun `예약 시간이 오전 11이전 또는 오후 8시 이후가 아니라면 false를 반환한다`() {
        val screenTime =
            ScreenTime(
                startTime = LocalTime.of(12, 0),
                endTime = LocalTime.of(14, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )

        val morningResult = TimeCondition().isSatisfiedBy(screenTime)
        val nightResult = TimeCondition().isSatisfiedBy(screenTime)

        assertThat(morningResult).isFalse()
        assertThat(nightResult).isFalse()
    }

    @Test
    fun `예약 날자가 10, 20, 30일 중 하나라면 true를 반환한다`() {
        val screenTime10 =
            ScreenTime(
                startTime = LocalTime.of(12, 0),
                endTime = LocalTime.of(14, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )

        val screenTime20 =
            ScreenTime(
                startTime = LocalTime.of(12, 0),
                endTime = LocalTime.of(14, 0),
                screeningDate = LocalDate.of(2026, 4, 20),
            )

        val screenTime30 =
            ScreenTime(
                startTime = LocalTime.of(12, 0),
                endTime = LocalTime.of(14, 0),
                screeningDate = LocalDate.of(2026, 4, 30),
            )

        val dateResult10 = DateCondition().isSatisfiedBy(screenTime10)
        val dateResult20 = DateCondition().isSatisfiedBy(screenTime20)
        val dateResult30 = DateCondition().isSatisfiedBy(screenTime30)

        assertThat(dateResult10).isTrue()
        assertThat(dateResult20).isTrue()
        assertThat(dateResult30).isTrue()
    }

    @Test
    fun `예약 날자가 10, 20, 30일이 아니라면 false를 반환한다`() {
        val screenTime =
            ScreenTime(
                startTime = LocalTime.of(12, 0),
                endTime = LocalTime.of(14, 0),
                screeningDate = LocalDate.of(2026, 4, 1),
            )

        val result = DateCondition().isSatisfiedBy(screenTime)

        assertThat(result).isFalse()
    }
}
