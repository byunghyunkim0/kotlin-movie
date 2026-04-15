package domain.movie.items

import domain.movie.itmes.Title
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TitleTest {
    @Test
    fun `영화의 이름은 빈 문자열이 예외를 던진다`() {
        assertThrows<IllegalArgumentException> { Title("") }
    }

    @Test
    fun `입력받은 영화 제목이 Title과 같으면 True를 반환한다`() {
        val title = Title("신바드의 모험")

        val result = title.isSame(Title("신바드의 모험"))

        assertThat(result).isTrue()
    }

    @Test
    fun `입력받은 영화 제목이 Title과 같지 않으면 false를 반환한다`() {
        val title = Title("신바드의 모험")

        val result = title.isSame(Title("신밧드의 모험"))

        assertThat(result).isFalse()
    }
}
