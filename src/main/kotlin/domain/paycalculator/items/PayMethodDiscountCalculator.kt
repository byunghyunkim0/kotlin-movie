package domain.paycalculator.items

import domain.discountpolicy.PayMethod
import domain.discountpolicy.PayMethodDiscountPolicy
import domain.money.Money

class PayMethodDiscountCalculator(
    private val policies: Map<PayMethod, PayMethodDiscountPolicy>,
) {
    fun calculate(
        price: Money,
        payMethod: PayMethod,
    ): Money = policies[payMethod]?.applyDiscount(price) ?: price
}
