package domain.reservations.items

import domain.dto.ReservationDto
import domain.money.Money
import domain.movie.Movie
import domain.paycalculator.items.PriceDiscountCalculator
import domain.timetable.items.ScreenTime
import domain.timetable.items.ScreeningSchedule
import domain.timetable.items.Seats
import java.time.LocalDate
import java.time.LocalTime

class Reservation(
    private val movie: Movie,
    private val screenTime: ScreenTime,
    private val seats: Seats,
) {
    fun toUpdatedSchedule(screeningSchedule: ScreeningSchedule): ScreeningSchedule {
        if (!screeningSchedule.isSame(screenTime) || !screeningSchedule.isSameMovie(movie)) {
            return screeningSchedule
        }
        return screeningSchedule.addReserveSeat(seats.toSeatPositions())
    }

    fun isDuplicatedScreenTime(otherSchedule: ScreeningSchedule): Boolean = otherSchedule.isDuplicatedScreenTime(screenTime)

    fun isDuplicatedDate(date: LocalDate): Boolean = screenTime.isScreeningAt(date)

    fun isDuplicatedTime(time: LocalTime): Boolean = screenTime.isContainsTime(time)

    fun isDuplicatedReservation(otherReservation: Reservation): Boolean =
        this.screenTime.isDuplicatedScreenTime(otherReservation.screenTime)

    fun sumSeatPrice(): Money = seats.sumPrice()

    fun calculateDiscountPrice(calculator: PriceDiscountCalculator): Money {
        val price = sumSeatPrice()
        return calculator.calculate(price, screenTime)
    }

    fun toDto(): ReservationDto {
        val date = screenTime.getDate()
        val time = screenTime.getStartTime()
        val seatNames = seats.getSeats().map { it.getName() }
        return ReservationDto(
            title = movie.getMovieTitle(),
            dateTime = "$date $time",
            seats = seatNames,
        )
    }
}
