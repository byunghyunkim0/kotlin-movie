package movie.service

import movie.domain.timetable.TimeTable

interface TimeTableService {
    fun getTimeTable(): TimeTable
}
