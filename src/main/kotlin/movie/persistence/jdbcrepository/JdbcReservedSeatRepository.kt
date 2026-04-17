package movie.persistence.jdbcrepository

import movie.persistence.entity.ReservedSeatEntity
import java.sql.Connection
import java.sql.Statement

class JdbcReservedSeatRepository(
    private val connection: Connection,
) : ReservedSeatRepository {
    override fun save(reservedSeat: ReservedSeatEntity): ReservedSeatEntity {
        val sql = "INSERT INTO reserved_seat (reservation_id, seat_number) VALUES (?, ?)"
        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { pstmt ->
            pstmt.setLong(1, reservedSeat.reservationId)
            pstmt.setString(2, reservedSeat.seatNumber)
            pstmt.executeUpdate()

            val generatedKeys = pstmt.generatedKeys
            if (generatedKeys.next()) {
                reservedSeat.copy(id = generatedKeys.getLong(1))
            } else {
                reservedSeat
            }
        }
    }

    override fun findById(id: Long): ReservedSeatEntity? {
        val sql = "SELECT id, reservation_id, seat_number FROM reserved_seat WHERE id = ?"
        return connection.prepareStatement(sql).use { pstmt ->
            pstmt.setLong(1, id)
            val rs = pstmt.executeQuery()
            if (rs.next()) {
                ReservedSeatEntity(
                    id = rs.getLong("id"),
                    reservationId = rs.getLong("reservation_id"),
                    seatNumber = rs.getString("seat_number"),
                )
            } else {
                null
            }
        }
    }

    override fun findByReservationId(reservationId: Long): List<ReservedSeatEntity> {
        val sql = "SELECT id, reservation_id, seat_number FROM reserved_seat WHERE reservation_id = ?"
        val seats = mutableListOf<ReservedSeatEntity>()
        connection.prepareStatement(sql).use { pstmt ->
            pstmt.setLong(1, reservationId)
            val rs = pstmt.executeQuery()
            while (rs.next()) {
                seats.add(
                    ReservedSeatEntity(
                        id = rs.getLong("id"),
                        reservationId = rs.getLong("reservation_id"),
                        seatNumber = rs.getString("seat_number"),
                    ),
                )
            }
        }
        return seats
    }

    override fun findByScreeningId(screeningId: Long): List<ReservedSeatEntity> {
        val sql =
            """
            SELECT rs.id, rs.reservation_id, rs.seat_number 
            FROM reserved_seat rs
            JOIN reservation_item ri ON rs.reservation_id = ri.id
            WHERE ri.screening_id = ?
            """.trimIndent()
        val seats = mutableListOf<ReservedSeatEntity>()
        connection.prepareStatement(sql).use { pstmt ->
            pstmt.setLong(1, screeningId)
            val rs = pstmt.executeQuery()
            while (rs.next()) {
                seats.add(
                    ReservedSeatEntity(
                        id = rs.getLong("id"),
                        reservationId = rs.getLong("reservation_id"),
                        seatNumber = rs.getString("seat_number"),
                    ),
                )
            }
        }
        return seats
    }
}
