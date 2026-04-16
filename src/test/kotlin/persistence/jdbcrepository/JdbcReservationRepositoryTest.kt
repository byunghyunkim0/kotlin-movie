package persistence.jdbcrepository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import persistence.entity.ReservationEntity
import java.sql.Connection
import java.sql.DriverManager

class JdbcReservationRepositoryTest {
    private lateinit var connection: Connection
    private lateinit var reservationRepository: ReservationRepository

    @BeforeEach
    fun setUp() {
        connection = DriverManager.getConnection("jdbc:h2:mem:test_reservation;DB_CLOSE_DELAY=-1", "sa", "")
        connection.createStatement().use { stmt ->
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
        }
        reservationRepository = JdbcReservationRepository(connection)
    }

    @AfterEach
    fun tearDown() {
        connection.createStatement().use { stmt ->
            stmt.execute("DROP TABLE reservations")
        }
        connection.close()
    }

    @Test
    fun `전체 예매 정보를 저장할 수 있다`() {
        val reservation =
            ReservationEntity(
                usedPoints = 1000,
                paymentMethod = "CARD",
                totalPrice = 12000,
            )

        val savedReservation = reservationRepository.save(reservation)

        assertThat(savedReservation.id).isNotNull()
        assertThat(savedReservation.usedPoints).isEqualTo(1000)
        assertThat(savedReservation.paymentMethod).isEqualTo("CARD")
        assertThat(savedReservation.totalPrice).isEqualTo(12000)
    }

    @Test
    fun `reservations_id를 통해 전체 예매 정보를 조회할 수 있다`() {
        val reservation =
            ReservationEntity(
                usedPoints = 500,
                paymentMethod = "CASH",
                totalPrice = 15000,
            )
        val savedReservation = reservationRepository.save(reservation)

        val foundReservation = reservationRepository.findById(savedReservation.id!!)

        assertThat(foundReservation).isNotNull()
        assertThat(foundReservation?.id).isEqualTo(savedReservation.id)
        assertThat(foundReservation?.usedPoints).isEqualTo(500)
        assertThat(foundReservation?.paymentMethod).isEqualTo("CASH")
        assertThat(foundReservation?.totalPrice).isEqualTo(15000)
    }

    @Test
    fun `존재하지 않는 reservations_id로 조회하면 null을 반환한다`() {
        val foundReservation = reservationRepository.findById(999L)
        assertThat(foundReservation).isNull()
    }
}
