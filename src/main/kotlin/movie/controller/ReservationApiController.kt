package movie.controller

import movie.domain.dto.ReservationRequest
import movie.service.ReservationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reservations")
class ReservationApiController(
    private val reservationService: ReservationService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun reserve(
        @RequestBody request: ReservationRequest,
    ) {
        reservationService.reserve(request)
    }
}
