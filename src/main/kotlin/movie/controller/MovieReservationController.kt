package movie.controller

import movie.domain.movie.items.Title
import movie.domain.paycalculator.PayCalculator
import movie.domain.point.Point
import movie.domain.reservations.Reservations
import movie.domain.seat.items.SeatPosition
import movie.domain.timetable.TimeTable
import movie.service.ReservationService
import movie.service.TimeTableService
import movie.view.InputView
import movie.view.OutputView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class MovieReservationController
    @Autowired
    constructor(
        private val payCalculator: PayCalculator,
        @Autowired(required = false) private val timeTableService: TimeTableService? = null,
        @Autowired(required = false) private val reservationService: ReservationService? = null,
    ) {
        // 상태 값들은 생성자 주입에서 제외하여 스프링 에러 방지
        private var timeTable: TimeTable = TimeTable(emptyList())
        private var point: Point = Point(10000)
        private var reservations: Reservations = Reservations()

        // Main.kt 등에서 수동으로 초기값을 설정하고 싶을 때 사용하는 보조 생성자
        constructor(
            payCalculator: PayCalculator,
            timeTableService: TimeTableService? = null,
            reservationService: ReservationService? = null,
            timeTable: TimeTable = TimeTable(emptyList()),
            point: Point = Point(10000),
        ) : this(payCalculator, timeTableService, reservationService) {
            this.timeTable = timeTable
            this.point = point
        }

        fun run() {
            // timeTableService가 있으면 DB(또는 API)에서 로드, 없으면 현재 필드의 timeTable 사용
            timeTableService?.let {
                timeTable = it.getTimeTable()
            }

            try {
                val isStart = InputView.readReservation()
                if (!isStart) return
            } catch (e: Exception) {
                println("[ERROR] ${e.message}")
                return run()
            }

            reserveMovie()

            processPayment()
        }

        private fun reserveMovie() {
            try {
                val title = Title(InputView.readMovieTitle())
                val date = readDateWithLocalDate()

                val schedule = timeTable.filterByTitle(title).filterByDate(date)
                OutputView.printSchedules(schedule.toScreeningScheduleDto())

                val scheduleNumber = InputView.readScheduleNumber()
                val selectedSchedule = schedule.selectSchedule(scheduleNumber)

                reservations.validateScreenTime(selectedSchedule)

                OutputView.printSeatMap(selectedSchedule.getSeatLayout())
                val seatInput =
                    InputView.readSeats().map {
                        SeatPosition.of(it)
                    }
                val reservation = selectedSchedule.makeReservation(seatInput)

                reservations = reservations.addReservation(reservation)

                OutputView.printReservationsAdded(reservation.toDto())
            } catch (e: Exception) {
                println("[ERROR] ${e.message}")
                return reserveMovie()
            }

            val isContinue = InputView.readAddMovie()
            if (isContinue) return reserveMovie()

            OutputView.printReservationsList(reservations.toReservationDtoList())
        }

        private fun processPayment() {
            try {
                val usePoint = InputView.readUsePoint()
                val usePayMethod = InputView.readPayMethod()

                val finalMoney =
                    payCalculator
                        .calculate(
                            reservations = reservations,
                            inputPoint = Point(usePoint),
                            payMethod = usePayMethod,
                        ).getAmount()

                OutputView.printPaymentAmount(finalMoney)

                val isPay = InputView.readConfirmPayment()
                if (!isPay) return

                // reservationService가 있으면 서버/DB에 저장 요청
                reservationService?.reserve(reservations, Point(usePoint), usePayMethod)

                point = point.subtractPoint(Point(usePoint))
                timeTable = reservations.toUpdatedTimeTable(timeTable)

                OutputView.printFinalReceipt(
                    items = reservations.toReservationDtoList(),
                    price = finalMoney,
                    point = usePoint,
                )
            } catch (e: Exception) {
                println(e.message)
                return processPayment()
            }
        }

        private fun readDateWithLocalDate(): LocalDate {
            try {
                val input = InputView.readDate()
                return LocalDate.parse(input)
            } catch (e: Exception) {
                println("[ERROR] 날짜 형식이 올바르지 않습니다.")
                return readDateWithLocalDate()
            }
        }
    }
