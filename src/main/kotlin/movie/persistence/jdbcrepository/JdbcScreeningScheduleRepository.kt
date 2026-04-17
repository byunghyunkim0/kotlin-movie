package movie.persistence.jdbcrepository

import movie.persistence.entity.ScreeningScheduleEntity
import java.sql.Connection
import java.sql.Statement
import java.sql.Timestamp

class JdbcScreeningScheduleRepository(
    private val connection: Connection,
) : ScreeningScheduleRepository {
    override fun save(schedule: ScreeningScheduleEntity): ScreeningScheduleEntity {
        val sql = "INSERT INTO screening_schedule (movie_id, start_at, end_at) VALUES (?, ?, ?)"
        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { pstmt ->
            pstmt.setLong(1, schedule.movieId)
            pstmt.setTimestamp(2, Timestamp.valueOf(schedule.startAt))
            pstmt.setTimestamp(3, Timestamp.valueOf(schedule.endAt))
            pstmt.executeUpdate()

            val generatedKeys = pstmt.generatedKeys
            if (generatedKeys.next()) {
                schedule.copy(id = generatedKeys.getLong(1))
            } else {
                schedule
            }
        }
    }

    override fun findById(id: Long): ScreeningScheduleEntity? {
        val sql = "SELECT id, movie_id, start_at, end_at FROM screening_schedule WHERE id = ?"
        return connection.prepareStatement(sql).use { pstmt ->
            pstmt.setLong(1, id)
            val rs = pstmt.executeQuery()
            if (rs.next()) {
                ScreeningScheduleEntity(
                    id = rs.getLong("id"),
                    movieId = rs.getLong("movie_id"),
                    startAt = rs.getTimestamp("start_at").toLocalDateTime(),
                    endAt = rs.getTimestamp("end_at").toLocalDateTime(),
                )
            } else {
                null
            }
        }
    }

    override fun findByMovieId(movieId: Long): List<ScreeningScheduleEntity> {
        val sql = "SELECT id, movie_id, start_at, end_at FROM screening_schedule WHERE movie_id = ?"
        val schedules = mutableListOf<ScreeningScheduleEntity>()
        connection.prepareStatement(sql).use { pstmt ->
            pstmt.setLong(1, movieId)
            val rs = pstmt.executeQuery()
            while (rs.next()) {
                schedules.add(
                    ScreeningScheduleEntity(
                        id = rs.getLong("id"),
                        movieId = rs.getLong("movie_id"),
                        startAt = rs.getTimestamp("start_at").toLocalDateTime(),
                        endAt = rs.getTimestamp("end_at").toLocalDateTime(),
                    ),
                )
            }
        }
        return schedules
    }
}
