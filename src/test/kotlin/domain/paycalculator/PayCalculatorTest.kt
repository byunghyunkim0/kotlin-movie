package domain.paycalculator

import domain.discountpolicy.CardDiscountPolicy
import domain.discountpolicy.CashDiscountPolicy
import domain.discountpolicy.DateCondition
import domain.discountpolicy.EarlyAndLateDiscountPolicy
import domain.discountpolicy.MovieDayDiscountPolicy
import domain.discountpolicy.PayMethod
import domain.discountpolicy.TimeCondition
import domain.money.Money
import domain.movie.Movie
import domain.movie.itmes.RunningTime
import domain.movie.itmes.ScreeningPeriod
import domain.movie.itmes.Title
import domain.paycalculator.items.PayMethodDiscountCalculator
import domain.paycalculator.items.PriceDiscountCalculator
import domain.point.Point
import domain.reservations.Reservations
import domain.reservations.items.Reservation
import domain.seat.Seat
import domain.seat.items.SeatGrade
import domain.seat.items.SeatPosition
import domain.timetable.items.ScreenTime
import domain.timetable.items.Seats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class PayCalculatorTest {
    val cardDiscountPolicy =
        CardDiscountPolicy()
    val cashDiscountPolicy =
        CashDiscountPolicy()
    val payMethodPolicies =
        mapOf(
            PayMethod.CARD to cardDiscountPolicy,
            PayMethod.CASH to cashDiscountPolicy,
        )
    val timeDiscountPolicy =
        EarlyAndLateDiscountPolicy(
            timeDiscountCondition = TimeCondition(),
        )

    val dateDiscountPolicy =
        MovieDayDiscountPolicy(
            timeDiscountCondition = DateCondition(),
        )
    val timePolicies = listOf(dateDiscountPolicy, timeDiscountPolicy)

    @Test
    fun `입력받은 reservation 목록에 할인, 포인트, 결제 수단이 순서로 최종 결제 금액을 반환한다`() {
        val calculator =
            PayCalculator(
                payMethodDiscountCalculator =
                    PayMethodDiscountCalculator(
                        policies = payMethodPolicies,
                    ),
                priceDiscountCalculator =
                    PriceDiscountCalculator(
                        policies = timePolicies,
                    ),
            )
        val inputPoint = Point(2000)
        val payMethod = PayMethod.CARD

        val reservations =
            Reservations(
                reservations =
                    mutableListOf(
                        createReservation(), // 38500
                        createReservation(
                            screenTime =
                                ScreenTime(
                                    startTime = LocalTime.of(12, 0),
                                    endTime = LocalTime.of(14, 0),
                                    screeningDate = LocalDate.of(2026, 4, 10),
                                ),
                        ), // 40500
                        createReservation(
                            screenTime =
                                ScreenTime(
                                    startTime = LocalTime.of(12, 0),
                                    endTime = LocalTime.of(14, 0),
                                    screeningDate = LocalDate.of(2026, 4, 1),
                                ),
                        ), // 45000
                    ),
            )
        val result =
            calculator.calculate(
                reservations = reservations,
                inputPoint = inputPoint,
                payMethod = payMethod,
            )
        // 기본 합 124_000 포인트 122_000 카드결제 115_900
        assertThat(result).isEqualTo(Money(115_900))
    }

    // 한 예매당 가격은 45_000, 조조할인o, movieDay 할인o
    private fun createReservation(
        screenTime: ScreenTime =
            ScreenTime(
                startTime = LocalTime.of(9, 0),
                endTime = LocalTime.of(14, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            ),
    ) = Reservation(
        movie =
            Movie(
                title = Title("신바드의 모험"),
                runningTime = RunningTime(120),
                screeningPeriod =
                    ScreeningPeriod(
                        startDate = LocalDate.of(2026, 4, 1),
                        endDate = LocalDate.of(2026, 4, 30),
                    ),
            ),
        screenTime = screenTime,
        seats =
            Seats(
                seats =
                    listOf(
                        createSeat(SeatGrade.B),
                        createSeat(SeatGrade.A),
                        createSeat(SeatGrade.S),
                    ),
            ),
    )

    private fun createSeat(grade: SeatGrade) =
        Seat(
            seatPosition = SeatPosition.of("A1"),
            seatGrade = grade,
        )
}
