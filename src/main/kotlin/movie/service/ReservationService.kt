package movie.service

import movie.domain.discountpolicy.PayMethod
import movie.domain.dto.ReservationRequest
import movie.domain.point.Point
import movie.domain.reservations.Reservations

interface ReservationService {
    fun reserve(request: ReservationRequest)

    fun reserve(
        reservations: Reservations,
        usedPoint: Point,
        payMethod: PayMethod,
    )
}
