package persistence.jdbcrepository

import persistence.entity.ReservationItemEntity
import java.sql.Connection
import java.sql.Statement

class JdbcReservationItemRepository(
    private val connection: Connection,
) : ReservationItemRepository {
    override fun save(reservationItem: ReservationItemEntity): ReservationItemEntity {
        val sql = "INSERT INTO reservation_item (reservations_id, screening_id) VALUES (?, ?)"
        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { pstmt ->
            pstmt.setLong(1, reservationItem.reservationsId)
            pstmt.setLong(2, reservationItem.screeningId)
            pstmt.executeUpdate()

            val generatedKeys = pstmt.generatedKeys
            if (generatedKeys.next()) {
                reservationItem.copy(id = generatedKeys.getLong(1))
            } else {
                reservationItem
            }
        }
    }

    override fun findById(id: Long): ReservationItemEntity? {
        val sql = "SELECT id, reservations_id, screening_id FROM reservation_item WHERE id = ?"
        return connection.prepareStatement(sql).use { pstmt ->
            pstmt.setLong(1, id)
            val rs = pstmt.executeQuery()
            if (rs.next()) {
                ReservationItemEntity(
                    id = rs.getLong("id"),
                    reservationsId = rs.getLong("reservations_id"),
                    screeningId = rs.getLong("screening_id"),
                )
            } else {
                null
            }
        }
    }

    override fun findByReservationsId(reservationsId: Long): List<ReservationItemEntity> {
        val sql = "SELECT id, reservations_id, screening_id FROM reservation_item WHERE reservations_id = ?"
        val items = mutableListOf<ReservationItemEntity>()
        connection.prepareStatement(sql).use { pstmt ->
            pstmt.setLong(1, reservationsId)
            val rs = pstmt.executeQuery()
            while (rs.next()) {
                items.add(
                    ReservationItemEntity(
                        id = rs.getLong("id"),
                        reservationsId = rs.getLong("reservations_id"),
                        screeningId = rs.getLong("screening_id"),
                    ),
                )
            }
        }
        return items
    }
}
