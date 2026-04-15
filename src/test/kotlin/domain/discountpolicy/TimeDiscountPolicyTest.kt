package domain.discountpolicy

import domain.money.Money
import domain.timetable.items.ScreenTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class TimeDiscountPolicyTest {
    @Test
    fun `예약 시간이 오전 11이전 또는 오후 8시 이후라면 2,000 할인한 금액을 반환한다`() {
        val morningTime =
            ScreenTime(
                startTime = LocalTime.of(10, 0),
                endTime = LocalTime.of(12, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )

        val nightTime =
            ScreenTime(
                startTime = LocalTime.of(21, 0),
                endTime = LocalTime.of(23, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )

        val morningPrice = Money(10000)
        val nightPrice = Money(10000)

        val timeDiscountPolicy =
            EarlyAndLateDiscountPolicy(
                timeDiscountCondition = TimeCondition(),
            )

        val morningResult = timeDiscountPolicy.applyDiscount(morningPrice, morningTime)
        val nightResult = timeDiscountPolicy.applyDiscount(nightPrice, nightTime)

        assertThat(morningResult).isEqualTo(Money(8000))
        assertThat(nightResult).isEqualTo(Money(8000))
    }

    @Test
    fun `시간에 해당하지 않는 다면 받은 금액을 반환한다`() {
        val screenTime =
            ScreenTime(
                startTime = LocalTime.of(12, 0),
                endTime = LocalTime.of(14, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )

        val price = Money(10000)

        val timeDiscountPolicy =
            EarlyAndLateDiscountPolicy(
                timeDiscountCondition = TimeCondition(),
            )

        val result = timeDiscountPolicy.applyDiscount(price, screenTime)

        assertThat(result).isEqualTo(Money(10000))
    }

    @Test
    fun `예약 날짜가 10, 20, 30일 중 하나라면 10% 할인한 금액을 반환한다`() {
        val screenTime10 =
            ScreenTime(
                startTime = LocalTime.of(10, 0),
                endTime = LocalTime.of(12, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )

        val screenTime20 =
            ScreenTime(
                startTime = LocalTime.of(10, 0),
                endTime = LocalTime.of(12, 0),
                screeningDate = LocalDate.of(2026, 4, 20),
            )

        val screenTime30 =
            ScreenTime(
                startTime = LocalTime.of(10, 0),
                endTime = LocalTime.of(12, 0),
                screeningDate = LocalDate.of(2026, 4, 30),
            )

        val priceDate10 = Money(10000)
        val priceDate20 = Money(10000)
        val priceDate30 = Money(10000)

        val timeDiscountPolicy =
            MovieDayDiscountPolicy(
                timeDiscountCondition = DateCondition(),
            )

        val dateResult10 = timeDiscountPolicy.applyDiscount(priceDate10, screenTime10)
        val dateResult20 = timeDiscountPolicy.applyDiscount(priceDate20, screenTime20)
        val dateResult30 = timeDiscountPolicy.applyDiscount(priceDate30, screenTime30)

        assertThat(dateResult10).isEqualTo(Money(9000))
        assertThat(dateResult20).isEqualTo(Money(9000))
        assertThat(dateResult30).isEqualTo(Money(9000))
    }

    @Test
    fun `예약 날짜에 해당하지 않는 다면 받은 금액을 반환한다`() {
        val screenTime =
            ScreenTime(
                startTime = LocalTime.of(10, 0),
                endTime = LocalTime.of(12, 0),
                screeningDate = LocalDate.of(2026, 4, 1),
            )

        val priceDate = Money(10000)

        val timeDiscountPolicy =
            MovieDayDiscountPolicy(
                timeDiscountCondition = DateCondition(),
            )

        val dateResult = timeDiscountPolicy.applyDiscount(priceDate, screenTime)

        assertThat(dateResult).isEqualTo(Money(10000))
    }
}
