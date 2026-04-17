package movie.domain.reservations

import movie.domain.dto.ReservationDto
import movie.domain.money.Money
import movie.domain.paycalculator.items.PriceDiscountCalculator
import movie.domain.reservations.items.Reservation
import movie.domain.timetable.TimeTable
import movie.domain.timetable.items.ScreeningSchedule
import kotlin.collections.fold

class Reservations(
    private val reservations: List<Reservation> = emptyList(),
) {
    fun toUpdatedTimeTable(timeTable: TimeTable): TimeTable =
        reservations.fold(timeTable) { table, reservation ->
            table.reserve(reservation)
        }

    fun addReservation(reservation: Reservation): Reservations {
        val duplicated = reservations.any { it.isDuplicatedReservation(reservation) }

        require(!duplicated) { "선택하신 상영 시간이 겹칩니다." }

        return Reservations(reservations + reservation)
    }

    fun validateScreenTime(screeningSchedule: ScreeningSchedule) {
        val duplicated = reservations.any { it.isDuplicatedScreenTime(screeningSchedule) }
        require(!duplicated) { "선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요." }
    }

    fun calculateTotalDiscountPrice(priceDiscountCalculator: PriceDiscountCalculator): Money {
        val price = Money(0)
        return reservations.fold(price) { total, reservation ->
            total + reservation.calculateDiscountPrice(priceDiscountCalculator)
        }
    }

    fun toReservationDtoList(): List<ReservationDto> = reservations.map { it.toDto() }

    fun getReservations() = reservations
}
