package view

import domain.discountpolicy.PayMethod

object InputView {
    fun readReservation(): Boolean {
        println("영화 예매를 시작합니다. 새 예매를 생성하시겠습니까? (Y/N)")
        return readYesOrNo()
    }

    fun readMovieTitle(): String {
        println("\n예매할 영화 제목을 입력하세요:")
        return readln().trim()
    }

    fun readDate(): String {
        println("\n날짜를 입력하세요 (YYYY-MM-DD):")
        return readln().trim()
    }

    fun readScheduleNumber(): Int {
        println("상영 번호를 선택하세요:")
        return readln().toIntOrNull() ?: throw IllegalArgumentException("숫자만 입력해 주세요.")
    }

    fun readSeats(): List<String> {
        println("\n예약할 좌석을 입력하세요 (예: A1, B2):")
        val input = readln()
        return input.split(",").map { it.trim() }
    }

    fun readAddMovie(): Boolean {
        println("\n다른 영화를 추가하시겠습니까? (Y/N)")
        return readYesOrNo()
    }

    fun readUsePoint(): Int {
        println("\n사용할 포인트를 입력하세요 (없으면 0):")
        return readln().toIntOrNull() ?: throw IllegalArgumentException("숫자만 입력해 주세요.")
    }

    fun readPayMethod(): PayMethod {
        println("\n결제 수단을 선택하세요:\n1) 신용카드(5% 할인)\n2) 현금(2% 할인)")
        val input = readln().trim()
        return when (input) {
            "1" -> PayMethod.CARD
            "2" -> PayMethod.CASH
            else -> throw IllegalArgumentException("1 또는 2를 선택해 주세요.")
        }
    }

    fun readConfirmPayment(): Boolean {
        println("\n위 금액으로 결제하시겠습니까? (Y/N)")
        return readYesOrNo()
    }

    private fun readYesOrNo(): Boolean {
        val input = readln().trim().uppercase()
        return when (input) {
            "Y" -> true
            "N" -> false
            else -> throw IllegalArgumentException("Y 또는 N만 입력해 주세요.")
        }
    }
}
