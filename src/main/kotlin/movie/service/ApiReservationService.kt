package movie.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import movie.domain.discountpolicy.PayMethod
import movie.domain.dto.ReservationItemRequest
import movie.domain.dto.ReservationRequest
import movie.domain.point.Point
import movie.domain.reservations.Reservations
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class ApiReservationService(
    private val baseUrl: String,
) : ReservationService {
    private val client = HttpClient.newBuilder().build()
    private val mapper = jacksonObjectMapper()

    override fun reserve(request: ReservationRequest) {
        val json = mapper.writeValueAsString(request)
        val httpRequest =
            HttpRequest
                .newBuilder()
                .uri(URI.create("$baseUrl/api/reservations"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build()

        val response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() != 201) {
            throw RuntimeException("API 요청 실패: ${response.statusCode()} - ${response.body()}")
        }
    }

    override fun reserve(
        reservations: Reservations,
        usedPoint: Point,
        payMethod: PayMethod,
    ) {
        // 도메인 모델을 API Request DTO로 변환
        val items =
            reservations.getReservations().map { reservation ->
                ReservationItemRequest(
                    screeningId = reservation.getScreeningId() ?: 0L,
                    seats = reservation.getSeats().getSeats().map { it.getName() },
                )
            }

        val request =
            ReservationRequest(
                reservations = items,
                usedPoints = usedPoint.toMoney(usedPoint).getAmount(),
                paymentMethod = payMethod.name,
            )

        reserve(request)
    }
}
