package movie.persistence.jdbcrepository

import movie.persistence.entity.ReservedSeatEntity

interface ReservedSeatRepository {
    fun save(reservedSeat: ReservedSeatEntity): ReservedSeatEntity

    fun findById(id: Long): ReservedSeatEntity?

    fun findByReservationId(reservationId: Long): List<ReservedSeatEntity>
}
