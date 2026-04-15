package domain.seat.items

class SeatPosition(
    private val rowNumber: RowNumber,
    private val columnNumber: ColumnNumber,
) {
    fun isExistSeatPosition(seatNumber: SeatPosition): Boolean =
        rowNumber.isSame(seatNumber.rowNumber) && columnNumber.isSame(seatNumber.columnNumber)

    fun getRow(): String = rowNumber.getRowNumberName()

    fun getColumn(): Int = columnNumber.getColumnNumberName()

    fun getName(): String = "${getRow()}${getColumn()}"

    companion object {
        private const val SEAT_POSITION_REGEX = "^([A-Z]+)(\\d+)$"
        private val REGEX = Regex(SEAT_POSITION_REGEX)

        fun of(seatNumber: String): SeatPosition {
            val matchResult =
                REGEX.matchEntire(seatNumber.trim())
                    ?: throw IllegalArgumentException("잘못된 좌석 번호 형식입니다. (입력값: $seatNumber)")

            val (row, col) = matchResult.destructured
            return SeatPosition(RowNumber(row), ColumnNumber(col.toInt()))
        }
    }
}
