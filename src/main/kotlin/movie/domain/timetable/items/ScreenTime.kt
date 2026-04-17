package movie.domain.timetable.items

import java.time.LocalDate
import java.time.LocalTime

class ScreenTime(
    private val startTime: LocalTime,
    private val endTime: LocalTime,
    private val screeningDate: LocalDate,
) {
    fun isStartTimeBetween(
        start: LocalTime,
        end: LocalTime,
    ): Boolean = startTime in start..end

    fun isDayOfMonth(days: List<Int>): Boolean = screeningDate.dayOfMonth in days

    fun getStartTime(): LocalTime = startTime

    fun getDate(): LocalDate = screeningDate

    fun isSame(otherTime: ScreenTime): Boolean =
        startTime == otherTime.startTime && endTime == otherTime.endTime && screeningDate == otherTime.screeningDate

    fun isScreeningAt(date: LocalDate): Boolean = screeningDate == date

    fun isDuplicatedScreenTime(otherTime: ScreenTime): Boolean {
        if (this.screeningDate != otherTime.screeningDate) return false

        return this.startTime < otherTime.endTime && otherTime.startTime < this.endTime
    }
}
