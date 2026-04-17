package movie.persistence.jdbcrepository

import movie.persistence.entity.MovieEntity
import movie.persistence.entity.ReservationEntity
import movie.persistence.entity.ReservationItemEntity
import movie.persistence.entity.ScreeningScheduleEntity
import movie.persistence.jdbcrepository.JdbcMovieRepository
import movie.persistence.jdbcrepository.JdbcReservationItemRepository
import movie.persistence.jdbcrepository.JdbcReservationRepository
import movie.persistence.jdbcrepository.JdbcScreeningScheduleRepository
import movie.persistence.jdbcrepository.ReservationItemRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDateTime

class JdbcReservationItemRepositoryTest {
    private lateinit var connection: Connection
    private lateinit var reservationItemRepository: ReservationItemRepository

    @BeforeEach
    fun setUp() {
        connection = DriverManager.getConnection("jdbc:h2:mem:test_reservation_item;DB_CLOSE_DELAY=-1", "sa", "")
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
            stmt.execute(
                """
                CREATE TABLE reservations (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    used_points INT,
                    payment_method VARCHAR(50),
                    total_price INT
                )
                """.trimIndent(),
            )
            stmt.execute(
                """
                CREATE TABLE reservation_item (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    reservations_id BIGINT,
                    screening_id BIGINT,
                    FOREIGN KEY (reservations_id) REFERENCES reservations(id),
                    FOREIGN KEY (screening_id) REFERENCES screening_schedule(id)
                )
                """.trimIndent(),
            )
        }
        reservationItemRepository = JdbcReservationItemRepository(connection)
    }

    @AfterEach
    fun tearDown() {
        connection.createStatement().use { stmt ->
            stmt.execute("DROP TABLE reservation_item")
            stmt.execute("DROP TABLE reservations")
            stmt.execute("DROP TABLE screening_schedule")
            stmt.execute("DROP TABLE movie")
        }
        connection.close()
    }

    @Test
    fun `개별 예매 정보를 저장할 수 있다`() {
        // Given
        val reservationsId = createReservations()
        val screeningId = createScreeningSchedule()
        val item = ReservationItemEntity(reservationsId = reservationsId, screeningId = screeningId)

        // When
        val savedItem = reservationItemRepository.save(item)

        // Then
        assertThat(savedItem.id).isNotNull()
        assertThat(savedItem.reservationsId).isEqualTo(reservationsId)
        assertThat(savedItem.screeningId).isEqualTo(screeningId)
    }

    @Test
    fun `reservation_id를 통해 예매 정보를 조회할 수 있다`() {
        // Given
        val reservationsId = createReservations()
        val screeningId = createScreeningSchedule()
        val savedItem = reservationItemRepository.save(ReservationItemEntity(reservationsId = reservationsId, screeningId = screeningId))

        // When
        val foundItem = reservationItemRepository.findById(savedItem.id!!)

        // Then
        assertThat(foundItem).isNotNull()
        assertThat(foundItem?.id).isEqualTo(savedItem.id)
        assertThat(foundItem?.reservationsId).isEqualTo(reservationsId)
    }

    @Test
    fun `reservations_id를 통해 예매 목록을 알 수 있다`() {
        // Given
        val reservationsId = createReservations()
        val screeningId1 = createScreeningSchedule()
        val screeningId2 = createScreeningSchedule()
        reservationItemRepository.save(ReservationItemEntity(reservationsId = reservationsId, screeningId = screeningId1))
        reservationItemRepository.save(ReservationItemEntity(reservationsId = reservationsId, screeningId = screeningId2))

        // When
        val items = reservationItemRepository.findByReservationsId(reservationsId)

        // Then
        assertThat(items).hasSize(2)
        assertThat(items).allMatch { it.reservationsId == reservationsId }
    }

    private fun createReservations(): Long {
        val repo = JdbcReservationRepository(connection)
        return repo.save(ReservationEntity(usedPoints = 0, paymentMethod = "CARD", totalPrice = 10000)).id!!
    }

    private fun createScreeningSchedule(): Long {
        val movieRepo = JdbcMovieRepository(connection)
        val movie = movieRepo.save(MovieEntity(title = "Test Movie", runningTimeMinutes = 100))
        val scheduleRepo = JdbcScreeningScheduleRepository(connection)
        return scheduleRepo
            .save(
                ScreeningScheduleEntity(
                    movieId = movie.id!!,
                    startAt = LocalDateTime.now(),
                    endAt = LocalDateTime.now().plusHours(2),
                ),
            ).id!!
    }
}
