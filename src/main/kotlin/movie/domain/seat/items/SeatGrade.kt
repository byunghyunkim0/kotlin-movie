package movie.domain.seat.items

import movie.domain.money.Money

enum class SeatGrade(
    private val price: Money,
) {
    S(Money(18_000)),
    A(Money(15_000)),
    B(Money(12_000)),
    ;

    fun addPrice(money: Money): Money = price + money
}

fun SeatGrade.toDisplaySeatGrade(): String =
    when (this) {
        SeatGrade.S -> "S"
        SeatGrade.A -> "A"
        SeatGrade.B -> "B"
    }
