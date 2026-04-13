import controller.MovieReservationController
import domain.discountpolicy.CardDiscountPolicy
import domain.discountpolicy.CashDiscountPolicy
import domain.discountpolicy.DateCondition
import domain.discountpolicy.EarlyAndLateDiscountPolicy
import domain.discountpolicy.MovieDayDiscountPolicy
import domain.discountpolicy.PayMethod
import domain.discountpolicy.TimeCondition
import domain.movie.Movie
import domain.movie.itmes.RunningTime
import domain.movie.itmes.ScreeningPeriod
import domain.movie.itmes.Title
import domain.paycalculator.PayCalculator
import domain.paycalculator.items.PayMethodDiscountCalculator
import domain.paycalculator.items.PriceDiscountCalculator
import domain.point.Point
import domain.seat.Seat
import domain.seat.items.ColumnNumber
import domain.seat.items.GradeA
import domain.seat.items.GradeB
import domain.seat.items.GradeS
import domain.seat.items.RowNumber
import domain.seat.items.SeatPosition
import domain.timetable.TimeTable
import domain.timetable.items.Screen
import domain.timetable.items.ScreenName
import domain.timetable.items.ScreenTime
import domain.timetable.items.ScreeningSchedule
import domain.timetable.items.Seats
import java.time.LocalDate
import java.time.LocalTime

fun main() {
    val userPoint = Point(10000)
    val priceDiscountCalculator =
        PriceDiscountCalculator(
            policies =
                listOf(
                    EarlyAndLateDiscountPolicy(
                        timeDiscountCondition = TimeCondition(),
                    ),
                    MovieDayDiscountPolicy(
                        timeDiscountCondition = DateCondition(),
                    ),
                ),
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
                                    "A", "B" -> GradeB()
                                    "C", "D" -> GradeS()
                                    else -> GradeA()
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
