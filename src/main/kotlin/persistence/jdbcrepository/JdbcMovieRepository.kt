package persistence.jdbcrepository

import persistence.entity.MovieEntity
import java.sql.Connection
import java.sql.Statement

class JdbcMovieRepository(
    private val connection: Connection,
) : MovieRepository {
    override fun save(movie: MovieEntity): MovieEntity {
        val sql = "INSERT INTO movie (title, runningTimeMinutes) VALUES (?, ?)"
        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { pstmt ->
            pstmt.setString(1, movie.title)
            pstmt.setInt(2, movie.runningTimeMinutes)
            pstmt.executeUpdate()

            val generatedKeys = pstmt.generatedKeys
            if (generatedKeys.next()) {
                movie.copy(id = generatedKeys.getLong(1))
            } else {
                movie
            }
        }
    }

    override fun findByTitle(title: String): MovieEntity? {
        val sql = "SELECT id, title, runningTimeMinutes FROM movie WHERE title = ?"
        return connection.prepareStatement(sql).use { pstmt ->
            pstmt.setString(1, title)
            val rs = pstmt.executeQuery()
            if (rs.next()) {
                MovieEntity(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    runningTimeMinutes = rs.getInt("runningTimeMinutes"),
                )
            } else {
                null
            }
        }
    }

    override fun findById(id: Long): MovieEntity? {
        val sql = "SELECT id, title, runningTimeMinutes FROM movie WHERE id = ?"
        return connection.prepareStatement(sql).use { pstmt ->
            pstmt.setLong(1, id)
            val rs = pstmt.executeQuery()
            if (rs.next()) {
                MovieEntity(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    runningTimeMinutes = rs.getInt("runningTimeMinutes"),
                )
            } else {
                null
            }
        }
    }
}
