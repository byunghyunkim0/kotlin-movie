package movie.domain.paycalculator.items

import movie.domain.discountpolicy.EarlyAndLateDiscountPolicy
import movie.domain.discountpolicy.MovieDayDiscountPolicy
import movie.domain.money.Money
import movie.domain.timetable.items.ScreenTime

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
