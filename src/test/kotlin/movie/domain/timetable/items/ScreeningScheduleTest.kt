package movie.domain.timetable.items

import movie.domain.movie.Movie
import movie.domain.movie.itmes.RunningTime
import movie.domain.movie.itmes.ScreeningPeriod
import movie.domain.movie.itmes.Title
import movie.domain.reservations.items.Reservation
import movie.domain.seat.items.SeatPosition
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.LocalDate
import java.time.LocalTime

class ScreeningScheduleTest {
    @Test
    fun `입력된 영화 제목이 소유하고 있는 영화 제목과 같다면 true를 반환받는다`() {
        val schedule = createScreeningSchedule()

        val result = schedule.isScreeningMovieTitle(Title("신바드의 모험"))

        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 영화 제목이 소유하고 있는 영화 제목과 다르다면 false를 반환받는다`() {
        val schedule = createScreeningSchedule()

        val result = schedule.isScreeningMovieTitle(Title("신밧드의 모험"))

        assertThat(result).isFalse()
    }

    @Test
    fun `입력된 상영 일자가 소유하고 있는 영화 상영 일자와 같다면 true를 반환받는다`() {
        val schedule = createScreeningSchedule()

        val result = schedule.isScreeningDate(LocalDate.of(2026, 4, 10))
        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 상영 일자가 소유하고 있는 영화 상영 일자와 다르다면 false를 반환받는다`() {
        val schedule = createScreeningSchedule()

        val result = schedule.isScreeningDate(LocalDate.of(2025, 4, 10))

        assertThat(result).isFalse()
    }

    @Test
    fun `입력된 좌석을 예약 목록에 추가한다`() {
        val schedule = createScreeningSchedule()

        val seat = listOf(SeatPosition.of("A1"))

        assertDoesNotThrow { schedule.addReservedSeat(seat) }
    }

    @Test
    fun `영화, 시간, 예약 좌석을 바탕으로 예매 정보를 제공한다`() {
        val schedule = createScreeningSchedule()

        val positions =
            listOf(
                SeatPosition.of("A1"),
                SeatPosition.of("A2"),
            )

        val result = schedule.makeReservation(positions)

        assertThat(result).isInstanceOf(Reservation::class.java)
    }

    private fun createScreeningSchedule(
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
