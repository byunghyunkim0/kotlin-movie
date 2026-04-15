package domain.reservations.items

import domain.money.Money
import domain.movie.Movie
import domain.movie.itmes.RunningTime
import domain.movie.itmes.ScreeningPeriod
import domain.movie.itmes.Title
import domain.seat.Seat
import domain.seat.items.SeatGrade
import domain.seat.items.SeatPosition
import domain.timetable.items.ScreenTime
import domain.timetable.items.Seats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class ReservationTest {
    @Test
    fun `예약된 좌석 가격의 합을 Money로 반환한다`() {
        val reservation = createReservation()

        val result = reservation.sumSeatPrice()

        assertThat(result).isEqualTo(Money(45_000))
    }

    @Test
    fun `입력된 예약의 screenTime이 겹친다면 true를 반환한다`() {
        val reservation = createReservation()

        val newReservation =
            createReservation(
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(11, 0),
                        endTime = LocalTime.of(13, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
            )

        val result = reservation.isDuplicatedReservation(newReservation)

        assertThat(result).isTrue()
    }

    @Test
    fun `입력된 예약의 screenTime이 겹치지 않는다면 false를 반환한다`() {
        val reservation = createReservation()

        val newReservation =
            createReservation(
                screenTime =
                    ScreenTime(
                        startTime = LocalTime.of(1, 0),
                        endTime = LocalTime.of(5, 0),
                        screeningDate = LocalDate.of(2026, 4, 10),
                    ),
            )

        val result = reservation.isDuplicatedReservation(newReservation)

        assertThat(result).isFalse()
    }

    private fun createReservation(
        screenTime: ScreenTime =
            ScreenTime(
                startTime = LocalTime.of(12, 0),
                endTime = LocalTime.of(14, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            ),
    ) = Reservation(
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
