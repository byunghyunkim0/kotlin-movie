package movie.service

import movie.domain.discountpolicy.CardDiscountPolicy
import movie.domain.discountpolicy.CashDiscountPolicy
import movie.domain.discountpolicy.DateCondition
import movie.domain.discountpolicy.EarlyAndLateDiscountPolicy
import movie.domain.discountpolicy.MovieDayDiscountPolicy
import movie.domain.discountpolicy.PayMethod
import movie.domain.discountpolicy.TimeCondition
import movie.domain.dto.ReservationItemRequest
import movie.domain.dto.ReservationRequest
import movie.domain.paycalculator.PayCalculator
import movie.domain.paycalculator.items.PayMethodDiscountCalculator
import movie.domain.paycalculator.items.PriceDiscountCalculator
import movie.persistence.entity.MovieEntity
import movie.persistence.entity.ScreeningScheduleEntity
import movie.persistence.jdbcrepository.JdbcMovieRepository
import movie.persistence.jdbcrepository.JdbcReservationItemRepository
import movie.persistence.jdbcrepository.JdbcReservationRepository
import movie.persistence.jdbcrepository.JdbcReservedSeatRepository
import movie.persistence.jdbcrepository.JdbcScreeningScheduleRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDateTime

class ReservationServiceIntegrationTest {
    private lateinit var connection: Connection
    private lateinit var reservationService: ReservationService
    private lateinit var movieRepo: JdbcMovieRepository
    private lateinit var scheduleRepo: JdbcScreeningScheduleRepository
    private lateinit var reservationRepo: JdbcReservationRepository
    private lateinit var itemRepo: JdbcReservationItemRepository
    private lateinit var seatRepo: JdbcReservedSeatRepository

    @BeforeEach
    fun setUp() {
        connection = DriverManager.getConnection("jdbc:h2:mem:test_service;DB_CLOSE_DELAY=-1", "sa", "")
        createTables()

        movieRepo = JdbcMovieRepository(connection)
        scheduleRepo = JdbcScreeningScheduleRepository(connection)
        reservationRepo = JdbcReservationRepository(connection)
        itemRepo = JdbcReservationItemRepository(connection)
        seatRepo = JdbcReservedSeatRepository(connection)

        val priceCalculator =
            PriceDiscountCalculator(
                movieDayDiscountPolicy = MovieDayDiscountPolicy(timeDiscountCondition = DateCondition()),
                timeDiscountPolicy = EarlyAndLateDiscountPolicy(timeDiscountCondition = TimeCondition()),
            )
        val payMethodCalculator =
            PayMethodDiscountCalculator(
                policies =
                    mapOf(
                        PayMethod.CARD to CardDiscountPolicy(),
                        PayMethod.CASH to CashDiscountPolicy(),
                    ),
            )
        val payCalculator = PayCalculator(payMethodCalculator, priceCalculator)

        reservationService =
            ReservationService(
                movieRepo,
                scheduleRepo,
                reservationRepo,
                itemRepo,
                seatRepo,
                payCalculator,
            )
    }

    private fun createTables() {
        connection.createStatement().use { stmt ->
            stmt.execute("CREATE TABLE movie (id BIGINT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(255), runningTimeMinutes INT)")
            stmt.execute(
                "CREATE TABLE screening_schedule (id BIGINT AUTO_INCREMENT PRIMARY KEY, movie_id BIGINT, start_at TIMESTAMP, end_at TIMESTAMP, FOREIGN KEY (movie_id) REFERENCES movie(id))",
            )
            stmt.execute(
                "CREATE TABLE reservations (id BIGINT AUTO_INCREMENT PRIMARY KEY, used_points INT, payment_method VARCHAR(50), total_price INT)",
            )
            stmt.execute(
                "CREATE TABLE reservation_item (id BIGINT AUTO_INCREMENT PRIMARY KEY, reservations_id BIGINT, screening_id BIGINT, FOREIGN KEY (reservations_id) REFERENCES reservations(id), FOREIGN KEY (screening_id) REFERENCES screening_schedule(id))",
            )
            stmt.execute(
                "CREATE TABLE reserved_seat (id BIGINT AUTO_INCREMENT PRIMARY KEY, reservation_id BIGINT, seat_number VARCHAR(10), FOREIGN KEY (reservation_id) REFERENCES reservation_item(id))",
            )
        }
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
    fun `예매 요청이 오면 정상적으로 데이터가 저장된다`() {
        // Given
        val movie = movieRepo.save(MovieEntity(title = "인터스텔라", runningTimeMinutes = 169))
        val schedule =
            scheduleRepo.save(
                ScreeningScheduleEntity(
                    movieId = movie.id!!,
                    startAt = LocalDateTime.of(2026, 4, 10, 13, 0),
                    endAt = LocalDateTime.of(2026, 4, 10, 15, 49),
                ),
            )

        val request =
            ReservationRequest(
                reservations =
                    listOf(
                        ReservationItemRequest(screeningId = schedule.id!!, seats = listOf("C2", "C3")),
                    ),
                usedPoints = 2000,
                paymentMethod = "CREDIT_CARD",
            )

        // When
        reservationService.reserve(request)

        // Then
        // 1. Reservation 저장 확인
        val reservations = findAllReservations()
        assertThat(reservations).hasSize(1)
        val reservation = reservations[0]
        assertThat(reservation.usedPoints).isEqualTo(2000)
        assertThat(reservation.paymentMethod).isEqualTo("CREDIT_CARD")
        // 가격 계산 확인:
        // C2, C3 (S등급 18000원 * 2 = 36000원)
        // 4월 10일은 Movie Day이므로 10% 할인: 36000 * 0.9 = 32400
        // 포인트 사용: 32400 - 2000 = 30400
        // CREDIT_CARD 할인 5% 적용: 30400 * 0.95 = 28880
        assertThat(reservation.totalPrice).isEqualTo(28880)

        // 2. ReservationItem 저장 확인
        val items = itemRepo.findByReservationsId(reservation.id!!)
        assertThat(items).hasSize(1)
        assertThat(items[0].screeningId).isEqualTo(schedule.id)

        // 3. ReservedSeat 저장 확인
        val seats = seatRepo.findByReservationId(items[0].id!!)
        assertThat(seats).hasSize(2)
        assertThat(seats.map { it.seatNumber }).containsExactlyInAnyOrder("C2", "C3")
    }

    private fun findAllReservations(): List<movie.persistence.entity.ReservationEntity> {
        val result = mutableListOf<movie.persistence.entity.ReservationEntity>()
        connection.prepareStatement("SELECT id, used_points, payment_method, total_price FROM reservations").use { pstmt ->
            val rs = pstmt.executeQuery()
            while (rs.next()) {
                result.add(
                    movie.persistence.entity.ReservationEntity(
                        id = rs.getLong("id"),
                        usedPoints = rs.getInt("used_points"),
                        paymentMethod = rs.getString("payment_method"),
                        totalPrice = rs.getInt("total_price"),
                    ),
                )
            }
        }
        return result
    }
}
