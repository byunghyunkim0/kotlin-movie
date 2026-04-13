package domain.discountpolicy

import domain.money.Money

interface PayMethodDiscountPolicy {
    fun applyDiscount(price: Money): Money
}

class CardDiscountPolicy : PayMethodDiscountPolicy {
    override fun applyDiscount(price: Money): Money = price * CARD_DISCOUNT

    companion object {
        private const val CARD_DISCOUNT = 0.95
    }
}

class CashDiscountPolicy : PayMethodDiscountPolicy {
    override fun applyDiscount(price: Money): Money = price * CASH_DISCOUNT

    companion object {
        private const val CASH_DISCOUNT = 0.98
    }
}

enum class PayMethod {
    CARD,
    CASH,
}
