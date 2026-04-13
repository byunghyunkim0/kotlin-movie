package domain.paycalculator.items

import domain.discountpolicy.CardDiscountPolicy
import domain.discountpolicy.CashDiscountPolicy
import domain.discountpolicy.PayMethod
import domain.money.Money
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PayMethodDiscountCalculatorTest {
    val cardDiscountPolicy =
        CardDiscountPolicy()
    val cashDiscountPolicy =
        CashDiscountPolicy()

    @Test
    fun `입력받은 금액을 입력받은 결제 방식을 적용한 결제 금액을 반환한다`() {
        val payMethodCard = PayMethod.CARD
        val payMethodCash = PayMethod.CASH
        val policies =
            mapOf(
                PayMethod.CARD to cardDiscountPolicy,
                PayMethod.CASH to cashDiscountPolicy,
            )
        val payMethodDiscountCalculator =
            PayMethodDiscountCalculator(
                policies = policies,
            )

        val moneyCard = Money(10000)
        val moneyCash = Money(10000)

        val cardResult = payMethodDiscountCalculator.calculate(moneyCard, payMethodCard)
        val cashResult = payMethodDiscountCalculator.calculate(moneyCash, payMethodCash)

        assertThat(cardResult).isEqualTo(Money(9500))
        assertThat(cashResult).isEqualTo(Money(9800))
    }
}
