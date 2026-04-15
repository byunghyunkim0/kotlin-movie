package domain.point

import domain.money.Money
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PointTest {
    @Test
    fun `포인트 초기값이 0보다 작으면 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> { Point(-1) }
    }

    @Test
    fun `사용할 포인트를 입력 받을 때 입력받은 값이 0보다 작으면 예외가 발생한다`() {
        val point = Point(0)
        assertThrows<IllegalArgumentException> { point.subtractPoint(Point(-1)) }
    }

    @Test
    fun `사용할 포인트를 입력 받을 때 입력받은 값이 잔여 포인트보다 많으면 예외가 발생한다`() {
        val point = Point(0)
        assertThrows<IllegalArgumentException> { point.subtractPoint(Point(1)) }
    }

    @Test
    fun `입력받은 값이 0보다 크고 잔여 포인트보다 작으면 잔여 포인트에서 입력값 만큼 차감한다`() {
        val point = Point(100)

        val newPoint = point.subtractPoint(Point(10))

        assertThat(newPoint).isEqualTo(Point(90))
    }

    @Test
    fun `입력받은 값이 0보다 크고 잔여 포인트보다 작으면 입력받은 값만큼 Money로 환전한다`() {
        val point = Point(100)

        val money = point.toMoney(Point(10))

        assertThat(money).isEqualTo(Money(10))
    }
}
