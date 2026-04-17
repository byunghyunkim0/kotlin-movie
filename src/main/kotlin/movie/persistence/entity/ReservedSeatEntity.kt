package movie.persistence.entity

data class ReservedSeatEntity(
    val id: Long? = null,
    val reservationId: Long,
    val seatNumber: String,
)
