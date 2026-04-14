package domain.seat

import domain.money.Money
import domain.seat.items.SeatGrade
import domain.seat.items.SeatPosition
import domain.seat.items.toDisplaySeatGrade

class Seat(
    private val seatPosition: SeatPosition,
    private val seatGrade: SeatGrade,
) {
    fun isExistSeatPosition(otherSeatPosition: SeatPosition): Boolean = seatPosition.isExistSeatPosition(otherSeatPosition)

    fun addSeatPrice(money: Money): Money = seatGrade.addPrice(money)

    fun getRow(): String = seatPosition.getRow()

    fun getColumn(): Int = seatPosition.getColumn()

    fun getName(): String = seatPosition.getName()

    fun getSeatGradeName(): String = seatGrade.toDisplaySeatGrade()
}
