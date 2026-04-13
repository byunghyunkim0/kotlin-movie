package domain.discountpolicy

import domain.money.Money
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PayMethodDiscountPolicyTest {
    @Test
    fun `카드 결제시 5% 할인한 금액을 반환한다`() {
        val payMethod = PayMethod.CARD

        val discountPolicy = CardDiscountPolicy()

        val money = Money(10000)

        val result = discountPolicy.applyDiscount(money)

        assertThat(result).isEqualTo(Money(9500))
    }

    @Test
    fun `현금 결제시 2% 할인한 금액을 반환한다`() {
        val payMethod = PayMethod.CASH

        val discountPolicy =
            CashDiscountPolicy()

        val money = Money(10000)

        val result = discountPolicy.applyDiscount(money)

        assertThat(result).isEqualTo(Money(9800))
    }
}
