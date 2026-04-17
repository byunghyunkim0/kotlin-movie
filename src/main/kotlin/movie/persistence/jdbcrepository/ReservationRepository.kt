package movie.persistence.jdbcrepository

import movie.persistence.entity.ReservationEntity

interface ReservationRepository {
    fun save(reservation: ReservationEntity): ReservationEntity

    fun findById(id: Long): ReservationEntity?
}
