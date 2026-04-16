package persistence.entity

data class ReservationItemEntity(
    val id: Long? = null,
    val reservationsId: Long,
    val screeningId: Long,
)
