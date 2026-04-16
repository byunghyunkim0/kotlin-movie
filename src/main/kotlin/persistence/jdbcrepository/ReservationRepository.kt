package persistence.jdbcrepository

import persistence.entity.ReservationEntity

interface ReservationRepository {
    fun save(reservation: ReservationEntity): ReservationEntity

    fun findById(id: Long): ReservationEntity?
}
