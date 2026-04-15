package domain.discountpolicy

import domain.money.Money
import domain.timetable.items.ScreenTime

interface TimeDiscountPolicy {
    fun applyDiscount(
        price: Money,
        screenTime: ScreenTime,
    ): Money
}

class EarlyAndLateDiscountPolicy(
    private val timeDiscountCondition: TimeDiscountCondition,
) : TimeDiscountPolicy {
    override fun applyDiscount(
        price: Money,
        screenTime: ScreenTime,
    ): Money {
        if (timeDiscountCondition.isSatisfiedBy(screenTime)) return price - TIME_DISCOUNT
        return price
    }

    companion object {
        private val TIME_DISCOUNT = Money(2000)
    }
}

class MovieDayDiscountPolicy(
    private val timeDiscountCondition: TimeDiscountCondition,
) : TimeDiscountPolicy {
    override fun applyDiscount(
        price: Money,
        screenTime: ScreenTime,
    ): Money {
        if (timeDiscountCondition.isSatisfiedBy(screenTime)) return price * MOVIE_DAY_DISCOUNT
        return price
    }

    companion object {
        private const val MOVIE_DAY_DISCOUNT = 0.9
    }
}
