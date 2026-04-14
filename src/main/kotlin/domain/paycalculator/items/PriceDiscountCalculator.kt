package domain.paycalculator.items

import domain.discountpolicy.EarlyAndLateDiscountPolicy
import domain.discountpolicy.MovieDayDiscountPolicy
import domain.money.Money
import domain.timetable.items.ScreenTime

class PriceDiscountCalculator(
    private val movieDayDiscountPolicy: MovieDayDiscountPolicy,
    private val timeDiscountPolicy: EarlyAndLateDiscountPolicy,
) {
    fun calculate(
        price: Money,
        screenTime: ScreenTime,
    ): Money {
        val movieDayPrice = movieDayDiscountPolicy.applyDiscount(price, screenTime)
        return timeDiscountPolicy.applyDiscount(movieDayPrice, screenTime)
    }
}
