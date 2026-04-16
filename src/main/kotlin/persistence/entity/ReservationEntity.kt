package persistence.entity

data class ReservationEntity(
    val id: Long? = null,
    val usedPoints: Int,
    val paymentMethod: String,
    val totalPrice: Int,
)
