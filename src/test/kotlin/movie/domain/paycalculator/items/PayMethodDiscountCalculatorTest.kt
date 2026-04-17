package movie.domain.paycalculator.items

import movie.domain.discountpolicy.CardDiscountPolicy
import movie.domain.discountpolicy.CashDiscountPolicy
import movie.domain.discountpolicy.PayMethod
import movie.domain.money.Money
import movie.domain.paycalculator.items.PayMethodDiscountCalculator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PayMethodDiscountCalculatorTest {
    val cardDiscountPolicy =
        CardDiscountPolicy()
    val cashDiscountPolicy =
        CashDiscountPolicy()

    val policies =
        mapOf(
            PayMethod.CARD to cardDiscountPolicy,
            PayMethod.CASH to cashDiscountPolicy,
        )
    val payMethodDiscountCalculator =
        PayMethodDiscountCalculator(
            policies = policies,
        )

    @Test
    fun `입력받은 금액의 결제 방식이 CARD이면 5%할인된 가격을 반환한다`() {
        val payMethodCard = PayMethod.CARD
        val moneyCard = Money(10000)

        val cardResult = payMethodDiscountCalculator.calculate(moneyCard, payMethodCard)

        assertThat(cardResult).isEqualTo(Money(9500))
    }

    @Test
    fun `입력받은 금액의 결제 방식이 CASH이면 5%할인된 가격을 반환한다`() {
        val payMethodCash = PayMethod.CASH
        val moneyCash = Money(10000)

        val cashResult = payMethodDiscountCalculator.calculate(moneyCash, payMethodCash)

        assertThat(cashResult).isEqualTo(Money(9800))
    }
}
