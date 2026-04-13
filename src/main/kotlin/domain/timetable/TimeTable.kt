package domain.timetable

import domain.dto.ScreeningScheduleDto
import domain.movie.itmes.Title
import domain.seat.items.SeatPosition
import domain.timetable.items.ScreeningSchedule
import java.time.LocalDate

class TimeTable(
    private val schedules: List<ScreeningSchedule>,
) {
    fun filterByTitle(title: Title): TimeTable {
        val filterSchedules = schedules.filter { it.isScreeningMovieTitle(title) }
        return TimeTable(filterSchedules)
    }

    fun filterByDate(date: LocalDate): TimeTable {
        val filterSchedules = schedules.filter { it.isScreeningDate(date) }
        return TimeTable(filterSchedules)
    }

    fun selectSchedule(number: Int): ScreeningSchedule {
        require(number in 1..schedules.size) { "잘못된 상영 번호입니다." }

        return schedules[number - 1]
    }

    fun addSchedule(
        screeningSchedule: ScreeningSchedule,
        seatPosition: List<SeatPosition>,
    ): TimeTable {
        val newTable =
            schedules.map {
                if (it.isSame(screeningSchedule)) {
                    screeningSchedule.addReserveSeat(seatPosition)
                } else {
                    it
                }
            }
        return TimeTable(
            schedules = newTable,
        )
    }

    fun toScreeningScheduleDto(): List<ScreeningScheduleDto> = schedules.map { it.toDto() }
}
