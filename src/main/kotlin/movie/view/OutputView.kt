package movie.view

import movie.domain.dto.ReservationDto
import movie.domain.dto.ScreeningScheduleDto
import movie.domain.dto.SeatStatusDto

object OutputView {
    fun printSchedules(schedules: List<ScreeningScheduleDto>) {
        println("\n해당 날짜의 상영 목록")
        schedules.forEachIndexed { index, schedule ->
            println("[${index + 1}] ${schedule.time}")
        }
    }

    fun printSeatMap(seats: List<List<SeatStatusDto>>) {
        println("\n좌석 배치도")
        println("    1    2    3    4")

        val rows = listOf('A', 'B', 'C', 'D', 'E')
        for (i in seats.indices) {
            val rowString =
                seats[i].joinToString(" ") { seatStatus ->
                    val seat = if (seatStatus.isReserved) "X" else seatStatus.gradeName
                    "[ $seat]"
                }
            println("${rows[i]} $rowString")
        }
    }

    fun printReservationsAdded(item: ReservationDto) {
        println("\n장바구니에 추가됨")
        println("- [${item.title}] ${item.dateTime}  좌석: ${item.seats.joinToString(", ")}")
    }

    fun printReservationsList(items: List<ReservationDto>) {
        println("\n장바구니")
        items.forEach { item ->
            println("- [${item.title}] ${item.dateTime}  좌석: ${item.seats.joinToString(", ")}")
        }
    }

    fun printPaymentAmount(price: Int) {
        println("\n가격 계산")
        val formattedPrice = "%,d".format(price)
        println("최종 결제 금액: ${formattedPrice}원")
    }

    fun printFinalReceipt(
        items: List<ReservationDto>,
        price: Int,
        point: Int,
    ) {
        println("\n예매 완료\n내역:")
        items.forEach { item ->
            println("- [${item.title}] ${item.dateTime}  좌석: ${item.seats}")
        }

        val formattedPrice = "%,d".format(price)
        val formattedPoint = "%,d".format(point)
        println("결제 금액: ${formattedPrice}원  (포인트 ${formattedPoint}원 사용)")
        println("\n감사합니다.")
    }
}
