package movie.persistence.jdbcrepository

import movie.persistence.entity.ReservationItemEntity

interface ReservationItemRepository {
    fun save(reservationItem: ReservationItemEntity): ReservationItemEntity

    fun findById(id: Long): ReservationItemEntity?

    fun findByReservationsId(reservationsId: Long): List<ReservationItemEntity>
}
