package persistence.jdbcrepository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import persistence.entity.MovieEntity
import persistence.entity.ScreeningScheduleEntity
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDateTime

class JdbcScreeningScheduleRepositoryTest {
    private lateinit var connection: Connection
    private lateinit var movieRepository: MovieRepository
    private lateinit var screeningScheduleRepository: ScreeningScheduleRepository

    @BeforeEach
    fun setUp() {
        connection = DriverManager.getConnection("jdbc:h2:mem:test_schedule;DB_CLOSE_DELAY=-1", "sa", "")
        connection.createStatement().use { stmt ->
            stmt.execute("CREATE TABLE movie (id BIGINT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(255), runningTimeMinutes INT)")
            stmt.execute(
                """
                CREATE TABLE screening_schedule (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    movie_id BIGINT,
                    start_at TIMESTAMP,
                    end_at TIMESTAMP,
                    FOREIGN KEY (movie_id) REFERENCES movie(id)
                )
                """.trimIndent(),
            )
        }
        movieRepository = JdbcMovieRepository(connection)
        screeningScheduleRepository = JdbcScreeningScheduleRepository(connection)
    }

    @AfterEach
    fun tearDown() {
        connection.createStatement().use { stmt ->
            stmt.execute("DROP TABLE screening_schedule")
            stmt.execute("DROP TABLE movie")
        }
        connection.close()
    }

    @Test
    fun `상영 일정 정보를 저장할 수 있다`() {
        val movie = movieRepository.save(MovieEntity(title = "토이 스토리", runningTimeMinutes = 120))
        val startAt = LocalDateTime.of(2026, 4, 10, 10, 0)
        val endAt = LocalDateTime.of(2026, 4, 10, 12, 0)
        val schedule = ScreeningScheduleEntity(movieId = movie.id!!, startAt = startAt, endAt = endAt)

        val savedSchedule = screeningScheduleRepository.save(schedule)

        assertThat(savedSchedule.id).isNotNull()
        assertThat(savedSchedule.movieId).isEqualTo(movie.id)
        assertThat(savedSchedule.startAt).isEqualTo(startAt)
        assertThat(savedSchedule.endAt).isEqualTo(endAt)
    }

    @Test
    fun `screening_id를 통해 상영 일정을 조회할 수 있다`() {
        val movie = movieRepository.save(MovieEntity(title = "아이언맨", runningTimeMinutes = 120))
        val schedule =
            ScreeningScheduleEntity(
                movieId = movie.id!!,
                startAt = LocalDateTime.of(2026, 4, 10, 13, 0),
                endAt = LocalDateTime.of(2026, 4, 10, 15, 0),
            )
        val savedSchedule = screeningScheduleRepository.save(schedule)

        val foundSchedule = screeningScheduleRepository.findById(savedSchedule.id!!)

        assertThat(foundSchedule).isNotNull()
        assertThat(foundSchedule?.id).isEqualTo(savedSchedule.id)
    }

    @Test
    fun `movie_id에 해당하는 상영 일정을 조회할 수 있다`() {
        val movie = movieRepository.save(MovieEntity(title = "아이언맨", runningTimeMinutes = 120))
        screeningScheduleRepository.save(
            ScreeningScheduleEntity(
                movieId = movie.id!!,
                startAt = LocalDateTime.of(2026, 4, 10, 10, 0),
                endAt = LocalDateTime.of(2026, 4, 10, 12, 0),
            ),
        )
        screeningScheduleRepository.save(
            ScreeningScheduleEntity(
                movieId = movie.id,
                startAt = LocalDateTime.of(2026, 4, 10, 13, 0),
                endAt = LocalDateTime.of(2026, 4, 10, 15, 0),
            ),
        )

        val schedules = screeningScheduleRepository.findByMovieId(movie.id)

        assertThat(schedules).hasSize(2)
        assertThat(schedules).allMatch { it.movieId == movie.id }
    }

    @Test
    fun `존재하지 않는 screening_id로 조회하면 null을 반환한다`() {
        val foundSchedule = screeningScheduleRepository.findById(999L)
        assertThat(foundSchedule).isNull()
    }
}
