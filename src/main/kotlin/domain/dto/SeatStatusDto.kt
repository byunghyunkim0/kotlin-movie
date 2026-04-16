package domain.dto

import domain.seat.Seat
import domain.seat.items.SeatGrade

data class SeatStatusDto(
    val gradeName: String,
    val isReserved: Boolean,
) {
    companion object {
        fun of(
            seat: Seat,
            isReserved: Boolean,
        ): SeatStatusDto =
            SeatStatusDto(
                gradeName = toDisplayGradeName(seat.getSeatGrade()),
                isReserved = isReserved,
            )

        private fun toDisplayGradeName(grade: SeatGrade): String =
            when (grade) {
                SeatGrade.S -> "S"
                SeatGrade.A -> "A"
                SeatGrade.B -> "B"
            }
    }
}
