package domain.money

@JvmInline
value class Money(
    private val amount: Int,
) {
    init {
        require(amount >= 0) { "가격은 0보다 작을 수 없습니다. (입력값: $amount)" }
    }

    fun getAmount() = amount

    operator fun plus(other: Money): Money = Money(amount + other.amount)

    operator fun minus(other: Money): Money = Money(amount - other.amount)

    operator fun times(scale: Double): Money = Money((amount * scale).toInt())
}
