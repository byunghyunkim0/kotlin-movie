package domain.paycalculator.items

import domain.discountpolicy.DateCondition
import domain.discountpolicy.EarlyAndLateDiscountPolicy
import domain.discountpolicy.MovieDayDiscountPolicy
import domain.discountpolicy.TimeCondition
import domain.money.Money
import domain.timetable.items.ScreenTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class PriceDiscountCalculatorTest {
    val timeDiscountPolicy =
        EarlyAndLateDiscountPolicy(
            timeDiscountCondition = TimeCondition(),
        )

    val dateDiscountPolicy =
        MovieDayDiscountPolicy(
            timeDiscountCondition = DateCondition(),
        )

    @Test
    fun `입력받은 금액을 시간 관련 할인 정책을 적용한 결제 금액을 반환한다`() {
        val screenTime =
            ScreenTime(
                startTime = LocalTime.of(8, 0),
                endTime = LocalTime.of(11, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )
        val screenTime1 =
            ScreenTime(
                startTime = LocalTime.of(8, 0),
                endTime = LocalTime.of(11, 0),
                screeningDate = LocalDate.of(2026, 4, 1),
            )
        val screenTime2 =
            ScreenTime(
                startTime = LocalTime.of(13, 0),
                endTime = LocalTime.of(15, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )
        val priceDiscountCalculator =
            PriceDiscountCalculator(
                movieDayDiscountPolicy = dateDiscountPolicy,
                timeDiscountPolicy = timeDiscountPolicy,
            )

        val money = Money(10_000)
        val money1 = Money(10_000)
        val money2 = Money(10_000)

        val result = priceDiscountCalculator.calculate(money, screenTime)
        val result1 = priceDiscountCalculator.calculate(money1, screenTime1)
        val result2 = priceDiscountCalculator.calculate(money2, screenTime2)

        assertThat(result).isEqualTo(Money(7_000))
        assertThat(result1).isEqualTo(Money(8_000))
        assertThat(result2).isEqualTo(Money(9_000))
    }

    @Test
    fun `입력받은 금액을 영화 시작 시간이 11시 이전이면 2000원 할인된 금액을 반환한다`() {
        val screenTime =
            ScreenTime(
                startTime = LocalTime.of(8, 0),
                endTime = LocalTime.of(11, 0),
                screeningDate = LocalDate.of(2026, 4, 1),
            )

        val priceDiscountCalculator =
            PriceDiscountCalculator(
                movieDayDiscountPolicy = dateDiscountPolicy,
                timeDiscountPolicy = timeDiscountPolicy,
            )

        val money = Money(10_000)

        val result = priceDiscountCalculator.calculate(money, screenTime)

        assertThat(result).isEqualTo(Money(8_000))
    }

    @Test
    fun `입력받은 금액을 영화 시작 시간이 20시 이후이면 2000원 할인된 금액을 반환한다`() {
        val screenTime =
            ScreenTime(
                startTime = LocalTime.of(21, 0),
                endTime = LocalTime.of(23, 0),
                screeningDate = LocalDate.of(2026, 4, 1),
            )

        val priceDiscountCalculator =
            PriceDiscountCalculator(
                movieDayDiscountPolicy = dateDiscountPolicy,
                timeDiscountPolicy = timeDiscountPolicy,
            )

        val money = Money(10_000)

        val result = priceDiscountCalculator.calculate(money, screenTime)

        assertThat(result).isEqualTo(Money(8_000))
    }

    @Test
    fun `입력받은 금액을 영화 시작 날자가 10일이면 10% 할인된 금액을 반환한다`() {
        val screenTime =
            ScreenTime(
                startTime = LocalTime.of(13, 0),
                endTime = LocalTime.of(15, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )

        val priceDiscountCalculator =
            PriceDiscountCalculator(
                movieDayDiscountPolicy = dateDiscountPolicy,
                timeDiscountPolicy = timeDiscountPolicy,
            )

        val money = Money(10_000)

        val result = priceDiscountCalculator.calculate(money, screenTime)

        assertThat(result).isEqualTo(Money(9_000))
    }

    @Test
    fun `입력받은 금액을 영화 시작 날자가 20일이면 10% 할인된 금액을 반환한다`() {
        val screenTime =
            ScreenTime(
                startTime = LocalTime.of(13, 0),
                endTime = LocalTime.of(15, 0),
                screeningDate = LocalDate.of(2026, 4, 20),
            )

        val priceDiscountCalculator =
            PriceDiscountCalculator(
                movieDayDiscountPolicy = dateDiscountPolicy,
                timeDiscountPolicy = timeDiscountPolicy,
            )

        val money = Money(10_000)

        val result = priceDiscountCalculator.calculate(money, screenTime)

        assertThat(result).isEqualTo(Money(9_000))
    }

    @Test
    fun `입력받은 금액을 영화 시작 날자가 30일이면 10% 할인된 금액을 반환한다`() {
        val screenTime =
            ScreenTime(
                startTime = LocalTime.of(13, 0),
                endTime = LocalTime.of(15, 0),
                screeningDate = LocalDate.of(2026, 4, 30),
            )

        val priceDiscountCalculator =
            PriceDiscountCalculator(
                movieDayDiscountPolicy = dateDiscountPolicy,
                timeDiscountPolicy = timeDiscountPolicy,
            )

        val money = Money(10_000)

        val result = priceDiscountCalculator.calculate(money, screenTime)

        assertThat(result).isEqualTo(Money(9_000))
    }

    @Test
    fun `입력받은 금액을 할인 정책 순서에 따라 결제 금액을 반환한다`() {
        val screenTime =
            ScreenTime(
                startTime = LocalTime.of(8, 0),
                endTime = LocalTime.of(11, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )

        val priceDiscountCalculator =
            PriceDiscountCalculator(
                movieDayDiscountPolicy = dateDiscountPolicy,
                timeDiscountPolicy = timeDiscountPolicy,
            )

        val money = Money(10_000)

        val result = priceDiscountCalculator.calculate(money, screenTime)

        assertThat(result).isEqualTo(Money(7_000))
    }
}
