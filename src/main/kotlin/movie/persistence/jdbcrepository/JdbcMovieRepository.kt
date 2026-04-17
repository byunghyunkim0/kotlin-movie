package movie.persistence.jdbcrepository

import movie.persistence.entity.MovieEntity
import movie.persistence.entity.ScreeningScheduleEntity
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

    override fun findAllWithScreenings(): List<Pair<MovieEntity, List<ScreeningScheduleEntity>>> {
        val sql =
            """
            SELECT 
                m.id as m_id, m.title, m.runningTimeMinutes,
                s.id as s_id, s.movie_id as s_movie_id, s.start_at, s.end_at
            FROM movie m
            LEFT JOIN screening_schedule s ON m.id = s.movie_id
            """.trimIndent()

        return connection.prepareStatement(sql).use { pstmt ->
            val rs = pstmt.executeQuery()
            val movieMap = mutableMapOf<Long, MovieEntity>()
            val screeningsMap = mutableMapOf<Long, MutableList<ScreeningScheduleEntity>>()
            val movieOrder = mutableListOf<Long>()

            while (rs.next()) {
                val movieId = rs.getLong("m_id")
                if (!movieMap.containsKey(movieId)) {
                    movieOrder.add(movieId)
                    movieMap[movieId] =
                        MovieEntity(
                            id = movieId,
                            title = rs.getString("title"),
                            runningTimeMinutes = rs.getInt("runningTimeMinutes"),
                        )
                }

                val screeningId = rs.getLong("s_id")
                if (!rs.wasNull()) {
                    val screenings = screeningsMap.getOrPut(movieId) { mutableListOf() }
                    screenings.add(
                        ScreeningScheduleEntity(
                            id = screeningId,
                            movieId = movieId,
                            startAt = rs.getTimestamp("start_at").toLocalDateTime(),
                            endAt = rs.getTimestamp("end_at").toLocalDateTime(),
                        ),
                    )
                }
            }

            movieOrder.map { id ->
                movieMap[id]!! to (screeningsMap[id] ?: emptyList())
            }
        }
    }
}
