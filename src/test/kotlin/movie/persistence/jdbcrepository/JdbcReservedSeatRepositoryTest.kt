package movie.persistence.jdbcrepository

import movie.persistence.entity.MovieEntity
import movie.persistence.entity.ReservationEntity
import movie.persistence.entity.ReservationItemEntity
import movie.persistence.entity.ReservedSeatEntity
import movie.persistence.entity.ScreeningScheduleEntity
import movie.persistence.jdbcrepository.JdbcMovieRepository
import movie.persistence.jdbcrepository.JdbcReservationItemRepository
import movie.persistence.jdbcrepository.JdbcReservationRepository
import movie.persistence.jdbcrepository.JdbcReservedSeatRepository
import movie.persistence.jdbcrepository.JdbcScreeningScheduleRepository
import movie.persistence.jdbcrepository.ReservedSeatRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDateTime

class JdbcReservedSeatRepositoryTest {
    private lateinit var connection: Connection
    private lateinit var reservedSeatRepository: ReservedSeatRepository

    @BeforeEach
    fun setUp() {
        connection = DriverManager.getConnection("jdbc:h2:mem:test_reserved_seat;DB_CLOSE_DELAY=-1", "sa", "")
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
            stmt.execute(
                """
                CREATE TABLE reserved_seat (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    reservation_id BIGINT,
                    seat_number VARCHAR(10),
                    FOREIGN KEY (reservation_id) REFERENCES reservation_item(id)
                )
                """.trimIndent(),
            )
        }
        reservedSeatRepository = JdbcReservedSeatRepository(connection)
    }

    @AfterEach
    fun tearDown() {
        connection.createStatement().use { stmt ->
            stmt.execute("DROP TABLE reserved_seat")
            stmt.execute("DROP TABLE reservation_item")
            stmt.execute("DROP TABLE reservations")
            stmt.execute("DROP TABLE screening_schedule")
            stmt.execute("DROP TABLE movie")
        }
        connection.close()
    }

    @Test
    fun `예약 좌석 정보를 저장할 수 있다`() {
        // Given
        val reservationId = createReservationItem()
        val reservedSeat = ReservedSeatEntity(reservationId = reservationId, seatNumber = "A1")

        // When
        val savedSeat = reservedSeatRepository.save(reservedSeat)

        // Then
        assertThat(savedSeat.id).isNotNull()
        assertThat(savedSeat.reservationId).isEqualTo(reservationId)
        assertThat(savedSeat.seatNumber).isEqualTo("A1")
    }

    @Test
    fun `id를 통해 예매 좌석을 조회할 수 있다`() {
        // Given
        val reservationId = createReservationItem()
        val savedSeat = reservedSeatRepository.save(ReservedSeatEntity(reservationId = reservationId, seatNumber = "B2"))

        // When
        val foundSeat = reservedSeatRepository.findById(savedSeat.id!!)

        // Then
        assertThat(foundSeat).isNotNull()
        assertThat(foundSeat?.id).isEqualTo(savedSeat.id)
        assertThat(foundSeat?.seatNumber).isEqualTo("B2")
    }

    @Test
    fun `reservation_id를 통해 예약 좌석들을 조회할 수 있다`() {
        // Given
        val reservationId = createReservationItem()
        reservedSeatRepository.save(ReservedSeatEntity(reservationId = reservationId, seatNumber = "C3"))
        reservedSeatRepository.save(ReservedSeatEntity(reservationId = reservationId, seatNumber = "C4"))

        // When
        val seats = reservedSeatRepository.findByReservationId(reservationId)

        // Then
        assertThat(seats).hasSize(2)
        assertThat(seats.map { it.seatNumber }).containsExactlyInAnyOrder("C3", "C4")
    }

    private fun createReservationItem(): Long {
        val movieRepo = JdbcMovieRepository(connection)
        val movie = movieRepo.save(MovieEntity(title = "Test Movie", runningTimeMinutes = 100))

        val scheduleRepo = JdbcScreeningScheduleRepository(connection)
        val screeningId =
            scheduleRepo
                .save(
                    ScreeningScheduleEntity(
                        movieId = movie.id!!,
                        startAt = LocalDateTime.now(),
                        endAt = LocalDateTime.now().plusHours(2),
                    ),
                ).id!!

        val reservationsRepo = JdbcReservationRepository(connection)
        val reservationsId = reservationsRepo.save(ReservationEntity(usedPoints = 0, paymentMethod = "CARD", totalPrice = 10000)).id!!

        val itemRepo = JdbcReservationItemRepository(connection)
        return itemRepo.save(ReservationItemEntity(reservationsId = reservationsId, screeningId = screeningId)).id!!
    }
}
