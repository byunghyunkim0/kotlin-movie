package controller

import domain.movie.itmes.Title
import domain.paycalculator.PayCalculator
import domain.point.Point
import domain.reservations.Reservations
import domain.seat.items.SeatPosition
import domain.timetable.TimeTable
import view.InputView
import view.OutputView
import java.time.LocalDate

class MovieReservationController(
    private val payCalculator: PayCalculator,
    private var timeTable: TimeTable,
    private var point: Point,
    private var reservations: Reservations = Reservations(),
) {
    fun run() {
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
