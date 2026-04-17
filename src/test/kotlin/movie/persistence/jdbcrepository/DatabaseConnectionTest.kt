package movie.persistence.jdbcrepository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.sql.DriverManager

class DatabaseConnectionTest {
    @Test
    fun `H2 데이터베이스에 연결할 수 있다`() {
        val url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
        val user = "sa"
        val password = ""

        val connection = DriverManager.getConnection(url, user, password)

        assertThat(connection.isClosed).isFalse()
        assertThat(connection.isValid(1)).isTrue()

        connection.close()
        assertThat(connection.isClosed).isTrue()
    }
}
