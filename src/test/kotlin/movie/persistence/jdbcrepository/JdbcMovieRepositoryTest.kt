package movie.persistence.jdbcrepository

import movie.persistence.entity.MovieEntity
import movie.persistence.jdbcrepository.JdbcMovieRepository
import movie.persistence.jdbcrepository.MovieRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.DriverManager

class JdbcMovieRepositoryTest {
    private lateinit var connection: Connection
    private lateinit var movieRepository: MovieRepository

    @BeforeEach
    fun setUp() {
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "")
        connection.createStatement().use { stmt ->
            stmt.execute("CREATE TABLE movie (id BIGINT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(255), runningTimeMinutes INT)")
        }
        movieRepository = JdbcMovieRepository(connection)
    }

    @AfterEach
    fun tearDown() {
        connection.createStatement().use { stmt ->
            stmt.execute("DROP TABLE movie")
        }
        connection.close()
    }

    @Test
    fun `Movie 정보를 저장할 수 있다`() {
        val movie = MovieEntity(title = "토이 스토리", runningTimeMinutes = 120)
        val savedMovie = movieRepository.save(movie)

        assertThat(savedMovie.id).isNotNull()
        assertThat(savedMovie.title).isEqualTo("토이 스토리")
        assertThat(savedMovie.runningTimeMinutes).isEqualTo(120)
    }

    @Test
    fun `title을 통해 Movie 정보를 조회할 수 있다`() {
        val movie = MovieEntity(title = "아이언맨", runningTimeMinutes = 120)
        movieRepository.save(movie)

        val foundMovie = movieRepository.findByTitle("아이언맨")

        assertThat(foundMovie).isNotNull()
        assertThat(foundMovie?.title).isEqualTo("아이언맨")
        assertThat(foundMovie?.runningTimeMinutes).isEqualTo(120)
    }

    @Test
    fun `id를 통해 Movie 정보를 조회할 수 있다`() {
        val movie = MovieEntity(title = "토이 스토리", runningTimeMinutes = 120)
        val savedMovie = movieRepository.save(movie)

        val foundMovie = savedMovie.id?.let { movieRepository.findById(it) }

        assertThat(foundMovie).isNotNull()
        assertThat(foundMovie?.id).isEqualTo(savedMovie.id)
        assertThat(foundMovie?.title).isEqualTo("토이 스토리")
        assertThat(foundMovie?.runningTimeMinutes).isEqualTo(120)
    }

    @Test
    fun `존재하지 않는 title로 조회하면 null을 반환한다`() {
        val foundMovie = movieRepository.findByTitle("없는 영화")

        assertThat(foundMovie).isNull()
    }

    @Test
    fun `존재하지 않는 id로 조회하면 null을 반환한다`() {
        val foundMovie = movieRepository.findById(999L)

        assertThat(foundMovie).isNull()
    }
}
