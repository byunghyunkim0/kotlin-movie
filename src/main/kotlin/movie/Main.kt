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
import movie.service.ApiReservationService
import movie.service.ApiTimeTableService
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

    // Application API 서버를 호출하는 서비스 주입 (서버가 8080 포트에서 실행 중이어야 함)
    val baseUrl = "http://localhost:8080"
    val apiReservationService = ApiReservationService(baseUrl)
    val apiTimeTableService = ApiTimeTableService(baseUrl)

    val controller =
        MovieReservationController(
            payCalculator = calculator,
            timeTableService = apiTimeTableService,
            point = userPoint,
            reservationService = apiReservationService,
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
                        1L,
                        "인터스텔라",
                        screenTime =
                            ScreenTime(
                                startTime = LocalTime.of(13, 30),
                                endTime = LocalTime.of(16, 19),
                                screeningDate = LocalDate.of(2025, 9, 20),
                            ),
                    ),
                    createSchedule(
                        2L,
                        "인터스텔라",
                        screenTime =
                            ScreenTime(
                                startTime = LocalTime.of(18, 0),
                                endTime = LocalTime.of(20, 49),
                                screeningDate = LocalDate.of(2025, 9, 20),
                            ),
                    ),
                    createSchedule(
                        3L,
                        "오펜하이머",
                        screenTime =
                            ScreenTime(
                                startTime = LocalTime.of(10, 0),
                                endTime = LocalTime.of(13, 0),
                                screeningDate = LocalDate.of(2025, 9, 20),
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
                    startDate = LocalDate.of(2025, 1, 1),
                    endDate = LocalDate.of(2025, 12, 31),
                ),
        )

    private fun createSchedule(
        id: Long,
        title: String,
        screenTime: ScreenTime,
    ) = ScreeningSchedule(
        movie = createMovie(title),
        screen = screen,
        screenTime = screenTime,
        id = id,
    )
}
