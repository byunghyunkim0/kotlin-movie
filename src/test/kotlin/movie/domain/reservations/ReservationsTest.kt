package movie.domain.reservations

import movie.domain.movie.Movie
import movie.domain.movie.items.RunningTime
import movie.domain.movie.items.ScreeningPeriod
import movie.domain.movie.items.Title
import movie.domain.reservations.items.Reservation
import movie.domain.seat.Seat
import movie.domain.seat.items.SeatGrade
import movie.domain.seat.items.SeatPosition
import movie.domain.timetable.items.ScreenTime
import movie.domain.timetable.items.Seats
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalTime

class ReservationsTest {
    @Test
    fun `reservations 목록 중 입력받은 reservation의 시간과 겹치는 reservation이 없으면 추가한다`() {
        val reservations = Reservations()

        val reservation =
            createReservation(
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(11, 0),
                        endTime = LocalTime.of(13, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
            )
        val newReservation =
            createReservation(
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(7, 0),
                        endTime = LocalTime.of(9, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
            )
        reservations.addReservation(reservation)
        assertDoesNotThrow { reservations.addReservation(newReservation) }
    }

    @Test
    fun `reservations 목록 중 입력받은 reservation의 시간과 겹치는 reservation이 없으면 추가한다2`() {
        val reservations = Reservations()

        val reservation =
            createReservation(
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(11, 0),
                        endTime = LocalTime.of(13, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
            )
        val newReservation =
            createReservation(
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(11, 0),
                        endTime = LocalTime.of(13, 0),
                        screeningDate = LocalDate.of(2026, 4, 9),
                    ),
            )
        reservations.addReservation(reservation)
        assertDoesNotThrow { reservations.addReservation(newReservation) }
    }

    @Test
    fun `reservations 목록 중 입력받은 reservation과 겹치는 reservation이 있으면 에러를 발생한다`() {
        val reservations = Reservations()

        val reservation =
            createReservation(
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(11, 0),
                        endTime = LocalTime.of(13, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
            )
        val newReservation =
            createReservation(
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(12, 0),
                        endTime = LocalTime.of(15, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
            )
        val newReservations = reservations.addReservation(reservation)
        assertThrows<IllegalArgumentException> { newReservations.addReservation(newReservation) }
    }

    private fun createReservation(screenTime: ScreenTime) =
        Reservation(
            movie =
                Movie(
                    title = Title("신바드의 모험"),
                    runningTime = RunningTime(120),
                    screeningPeriod =
                        ScreeningPeriod(
                            startDate = LocalDate.of(2026, 4, 1),
                            endDate = LocalDate.of(2026, 4, 30),
                        ),
                ),
            screenTime = screenTime,
            seats =
                Seats(
                    seats =
                        listOf(
                            createSeat(SeatGrade.B),
                            createSeat(SeatGrade.A),
                            createSeat(SeatGrade.S),
                        ),
                ),
        )

    private fun createSeat(grade: SeatGrade) =
        Seat(
            seatPosition = SeatPosition.of("A1"),
            seatGrade = grade,
        )
}
