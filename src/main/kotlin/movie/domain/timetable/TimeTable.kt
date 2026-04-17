package movie.domain.timetable

import movie.domain.dto.ScreeningScheduleDto
import movie.domain.movie.itmes.Title
import movie.domain.reservations.items.Reservation
import movie.domain.timetable.items.ScreeningSchedule
import java.time.LocalDate

class TimeTable(
    private val schedules: List<ScreeningSchedule>,
) {
    fun reserve(reservation: Reservation): TimeTable {
        val newSchedule =
            schedules.mapIndexed { i, schedule ->
                reservation.toUpdatedSchedule(schedule)
            }
        return TimeTable(newSchedule)
    }

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

    fun toScreeningScheduleDto(): List<ScreeningScheduleDto> = schedules.map { it.toDto() }
}
