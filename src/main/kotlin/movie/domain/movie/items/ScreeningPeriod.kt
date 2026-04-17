package movie.domain.movie.items

import java.time.LocalDate

class ScreeningPeriod(
    private val startDate: LocalDate,
    private val endDate: LocalDate,
) {
    init {
        require(startDate.isBefore(endDate)) { "사영 시작일은 종료일보다 빨라야 합니다. (startDate: $startDate / endDate: $endDate)" }
    }

    fun contains(inputDate: LocalDate): Boolean = inputDate in startDate..endDate
}
