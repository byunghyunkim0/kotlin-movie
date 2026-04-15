package domain.seat.items

@JvmInline
value class RowNumber(
    private val rowNumber: String,
) {
    fun isSame(number: RowNumber): Boolean = rowNumber == number.rowNumber

    fun getRowNumberName() = rowNumber
}
