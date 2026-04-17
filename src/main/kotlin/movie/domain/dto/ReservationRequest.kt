package movie.domain.dto

data class ReservationRequest(
    val reservations: List<ReservationItemRequest>,
    val usedPoints: Int,
    val paymentMethod: String,
)

data class ReservationItemRequest(
    val screeningId: Long,
    val seats: List<String>,
)
