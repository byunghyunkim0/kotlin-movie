package domain.timetable

import domain.movie.Movie
import domain.movie.itmes.RunningTime
import domain.movie.itmes.ScreeningPeriod
import domain.movie.itmes.Title
import domain.timetable.items.Screen
import domain.timetable.items.ScreenName
import domain.timetable.items.ScreenSeatMock
import domain.timetable.items.ScreenTime
import domain.timetable.items.ScreeningSchedule
import domain.timetable.items.Seats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalTime

class TimeTableTest {
    val timeTable =
        TimeTable(
            listOf(
                createSchedule(
                    movieTitle = Title("신바드의 모험"),
                    screenTime =
                        ScreenTime(
                            startTime = LocalTime.of(11, 0),
                            endTime = LocalTime.of(12, 0),
                            screeningDate = LocalDate.of(2026, 4, 8),
                        ),
                ),
                createSchedule(
                    movieTitle = Title("신밧드의 모험"),
                    screenTime =
                        ScreenTime(
                            startTime = LocalTime.of(12, 0),
                            endTime = LocalTime.of(13, 0),
                            screeningDate = LocalDate.of(2026, 4, 9),
                        ),
                ),
                createSchedule(
                    movieTitle = Title("신바드의 모험"),
                    screenTime =
                        ScreenTime(
                            startTime = LocalTime.of(14, 0),
                            endTime = LocalTime.of(15, 0),
                            screeningDate = LocalDate.of(2026, 4, 9),
                        ),
                ),
            ),
        )

    @Test
    fun `영화 제목을 입력받아 입력값과 같은 영화 제목을 갖는 screening schedule의 목록을 TimeTable 형태로 반환한다`() {
        val filterTable = timeTable.filterByTitle(Title("신바드의 모험"))

        val selectSchedule = filterTable.selectSchedule(1)

        val result = selectSchedule.isScreeningMovieTitle(Title("신바드의 모험"))

        assertThat(result).isTrue()
    }

    @Test
    fun `상영 일자를 입력받아 입력값과 같은 상영 일자를 갖는 screening schedule의 목록을 TimeTable 형태로 반환한다`() {
        val filterTable = timeTable.filterByDate(LocalDate.of(2026, 4, 9))

        val selectSchedule = filterTable.selectSchedule(1)

        val result = selectSchedule.isScreeningDate(LocalDate.of(2026, 4, 9))

        assertThat(result).isTrue()
    }

    @Test
    fun `schedule 리스트의 번호를 입력받아 해당 Schedule를 반환한다`() {
        val filterTable = timeTable.filterByTitle(Title("신바드의 모험"))

        val selectSchedule = filterTable.selectSchedule(1)

        val result = selectSchedule.isScreeningMovieTitle(Title("신바드의 모험"))

        assertThat(result).isTrue()
    }

    @Test
    fun `schedule 리스트의 올바르지 않은 번호를 입력받으면 예외를 발생한다`() {
        val filterTable = timeTable.filterByTitle(Title("신바드의 모험"))

        assertThrows<IllegalArgumentException> { filterTable.selectSchedule(1000000) }
    }

    private fun createSchedule(
        movieTitle: Title = Title("신바드의 모험"),
        screenTime: ScreenTime =
            ScreenTime(
                startTime = LocalTime.of(11, 0),
                endTime = LocalTime.of(13, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            ),
    ) = ScreeningSchedule(
        movie =
            Movie(
                title = movieTitle,
                runningTime = RunningTime(120),
                screeningPeriod =
                    ScreeningPeriod(
                        startDate = LocalDate.of(2026, 1, 1),
                        endDate = LocalDate.of(2026, 6, 1),
                    ),
            ),
        screen =
            Screen(
                name = ScreenName("1관"),
                seats = Seats(ScreenSeatMock.seats),
            ),
        screenTime = screenTime,
    )
}
