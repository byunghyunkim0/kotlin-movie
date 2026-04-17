package movie

import movie.controller.MovieReservationController
import movie.domain.discountpolicy.CardDiscountPolicy
import movie.domain.discountpolicy.CashDiscountPolicy
import movie.domain.discountpolicy.DateCondition
import movie.domain.discountpolicy.EarlyAndLateDiscountPolicy
import movie.domain.discountpolicy.MovieDayDiscountPolicy
import movie.domain.discountpolicy.PayMethod
import movie.domain.discountpolicy.TimeCondition
import movie.domain.movie.Movie
import movie.domain.movie.items.RunningTime
import movie.domain.movie.items.ScreeningPeriod
import movie.domain.movie.items.Title
import movie.domain.paycalculator.PayCalculator
import movie.domain.paycalculator.items.PayMethodDiscountCalculator
import movie.domain.paycalculator.items.PriceDiscountCalculator
import movie.domain.point.Point
import movie.domain.seat.Seat
import movie.domain.seat.items.ColumnNumber
import movie.domain.seat.items.RowNumber
import movie.domain.seat.items.SeatGrade
import movie.domain.seat.items.SeatPosition
import movie.domain.timetable.TimeTable
import movie.domain.timetable.items.Screen
import movie.domain.timetable.items.ScreenName
import movie.domain.timetable.items.ScreenTime
import movie.domain.timetable.items.ScreeningSchedule
import movie.domain.timetable.items.Seats
import java.time.LocalDate
import java.time.LocalTime

fun main() {
    val userPoint = Point(10000)
    val priceDiscountCalculator =
        PriceDiscountCalculator(
            movieDayDiscountPolicy = MovieDayDiscountPolicy(timeDiscountCondition = DateCondition()),
            timeDiscountPolicy = EarlyAndLateDiscountPolicy(timeDiscountCondition = TimeCondition()),
        )
    val payMethodDiscountCalculator =
        PayMethodDiscountCalculator(
            policies =
                mapOf(
                    PayMethod.CARD to
                        CardDiscountPolicy(),
                    PayMethod.CASH to
                        CashDiscountPolicy(),
                ),
        )

    val calculator =
        PayCalculator(
            payMethodDiscountCalculator = payMethodDiscountCalculator,
            priceDiscountCalculator = priceDiscountCalculator,
        )

    val controller =
        MovieReservationController(
            payCalculator = calculator,
            timeTable = MockTimeTable.timeTable,
            point = userPoint,
        )

    controller.run()
}

object MockTimeTable {
    private val row = listOf("A", "B", "C", "D", "E")
    private val col = listOf(1, 2, 3, 4)

    private val screen =
        Screen(
            name = ScreenName("1관"),
            seats =
                Seats(
                    row.flatMap { row ->
                        col.map { col ->
                            val grade =
                                when (row) {
                                    "A", "B" -> SeatGrade.B
                                    "C", "D" -> SeatGrade.S
                                    else -> SeatGrade.A
                                }
                            Seat(
                                seatPosition =
                                    SeatPosition(
                                        RowNumber(row),
                                        ColumnNumber(col),
                                    ),
                                seatGrade = grade,
                            )
                        }
                    },
                ),
        )

    val timeTable =
        TimeTable(
            schedules =
                listOf(
                    createSchedule(
                        "F1 더 무비",
                        screenTime =
                            ScreenTime(
                                startTime = LocalTime.of(10, 20),
                                endTime = LocalTime.of(12, 20),
                                screeningDate = LocalDate.of(2026, 4, 10),
                            ),
                    ),
                    createSchedule(
                        "F1 더 무비",
                        screenTime =
                            ScreenTime(
                                startTime = LocalTime.of(13, 0),
                                endTime = LocalTime.of(15, 0),
                                screeningDate = LocalDate.of(2026, 4, 10),
                            ),
                    ),
                    createSchedule(
                        "F1 더 무비",
                        screenTime =
                            ScreenTime(
                                startTime = LocalTime.of(15, 40),
                                endTime = LocalTime.of(17, 40),
                                screeningDate = LocalDate.of(2026, 4, 10),
                            ),
                    ),
                    createSchedule(
                        "F1 더 무비",
                        screenTime =
                            ScreenTime(
                                startTime = LocalTime.of(20, 10),
                                endTime = LocalTime.of(22, 10),
                                screeningDate = LocalDate.of(2026, 4, 10),
                            ),
                    ),
                    createSchedule(
                        "토이 스토리",
                        screenTime =
                            ScreenTime(
                                startTime = LocalTime.of(13, 30),
                                endTime = LocalTime.of(15, 30),
                                screeningDate = LocalDate.of(2026, 4, 10),
                            ),
                    ),
                    createSchedule(
                        "토이 스토리",
                        screenTime =
                            ScreenTime(
                                startTime = LocalTime.of(16, 0),
                                endTime = LocalTime.of(18, 0),
                                screeningDate = LocalDate.of(2026, 4, 10),
                            ),
                    ),
                    createSchedule(
                        "아이언맨",
                        screenTime =
                            ScreenTime(
                                startTime = LocalTime.of(9, 50),
                                endTime = LocalTime.of(11, 50),
                                screeningDate = LocalDate.of(2026, 4, 10),
                            ),
                    ),
                ),
        )

    private fun createMovie(title: String) =
        Movie(
            title = Title(title),
            runningTime = RunningTime(120),
            screeningPeriod =
                ScreeningPeriod(
                    startDate = LocalDate.of(2026, 4, 1),
                    endDate = LocalDate.of(2026, 4, 30),
                ),
        )

    private fun createSchedule(
        title: String,
        screenTime: ScreenTime,
    ) = ScreeningSchedule(
        movie = createMovie(title),
        screen = screen,
        screenTime = screenTime,
    )
}
