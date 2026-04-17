package movie.persistence.jdbcrepository

import movie.persistence.entity.ReservationEntity
import java.sql.Connection
import java.sql.Statement

class JdbcReservationRepository(
    private val connection: Connection,
) : ReservationRepository {
    override fun save(reservation: ReservationEntity): ReservationEntity {
        val sql = "INSERT INTO reservations (used_points, payment_method, total_price) VALUES (?, ?, ?)"
        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { pstmt ->
            pstmt.setInt(1, reservation.usedPoints)
            pstmt.setString(2, reservation.paymentMethod)
            pstmt.setInt(3, reservation.totalPrice)
            pstmt.executeUpdate()

            val generatedKeys = pstmt.generatedKeys
            if (generatedKeys.next()) {
                reservation.copy(id = generatedKeys.getLong(1))
            } else {
                reservation
            }
        }
    }

    override fun findById(id: Long): ReservationEntity? {
        val sql = "SELECT id, used_points, payment_method, total_price FROM reservations WHERE id = ?"
        return connection.prepareStatement(sql).use { pstmt ->
            pstmt.setLong(1, id)
            val rs = pstmt.executeQuery()
            if (rs.next()) {
                ReservationEntity(
                    id = rs.getLong("id"),
                    usedPoints = rs.getInt("used_points"),
                    paymentMethod = rs.getString("payment_method"),
                    totalPrice = rs.getInt("total_price"),
                )
            } else {
                null
            }
        }
    }
}
