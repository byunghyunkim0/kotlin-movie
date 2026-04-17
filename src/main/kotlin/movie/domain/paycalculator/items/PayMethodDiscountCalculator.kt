package movie.domain.paycalculator.items

import movie.domain.discountpolicy.PayMethod
import movie.domain.discountpolicy.PayMethodDiscountPolicy
import movie.domain.money.Money

class PayMethodDiscountCalculator(
    private val policies: Map<PayMethod, PayMethodDiscountPolicy>,
) {
    fun calculate(
        price: Money,
        payMethod: PayMethod,
    ): Money = policies[payMethod]?.applyDiscount(price) ?: price
}
