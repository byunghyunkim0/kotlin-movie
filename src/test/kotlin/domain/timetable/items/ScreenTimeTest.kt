package domain.timetable.items

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class ScreenTimeTest {
    @Test
    fun `ScreeningTime 시간이 startTime과 endTime 사이에 있다면 true를 반환한다`() {
        val screenTime = createScreenTime()
        val newTime =
            createScreenTime(
                startTime = LocalTime.of(11, 0),
                endTime = LocalTime.of(13, 0),
            )

        val result = screenTime.isDuplicatedScreenTime(newTime)

        assertThat(result).isTrue()
    }

    @Test
    fun `ScreeningTime 시간이 startTime과 endTime 사이에 없다면 false를 반환한다`() {
        val screenTime = createScreenTime()
        val newTime =
            createScreenTime(
                startTime = LocalTime.of(9, 0),
                endTime = LocalTime.of(11, 0),
            )

        val result = screenTime.isDuplicatedScreenTime(newTime)

        assertThat(result).isFalse()
    }

    @Test
    fun `입력받은 날자가 소유하고 있는 날자와 같다면 true를 반환한다`() {
        val screenTime = createScreenTime(date = LocalDate.of(2026, 4, 1))
        val newDate = createScreenTime(date = LocalDate.of(2026, 4, 1))

        val result = screenTime.isDuplicatedScreenTime(newDate)

        assertThat(result).isTrue()
    }

    @Test
    fun `입력받은 날자가 소유하고 있는 날자와 다르다면 false를 반환하다`() {
        val screenTime = createScreenTime(date = LocalDate.of(2026, 4, 1))
        val newDate = createScreenTime(date = LocalDate.of(2026, 4, 10))

        val result = screenTime.isDuplicatedScreenTime(newDate)

        assertThat(result).isFalse()
    }

    private fun createScreenTime(
        startTime: LocalTime = LocalTime.of(12, 0),
        endTime: LocalTime = LocalTime.of(14, 0),
        date: LocalDate = LocalDate.of(2026, 4, 10),
    ) = ScreenTime(
        startTime = startTime,
        endTime = endTime,
        screeningDate = date,
    )
}
