package movie.domain.paycalculator

import movie.domain.discountpolicy.PayMethod
import movie.domain.money.Money
import movie.domain.paycalculator.items.PayMethodDiscountCalculator
import movie.domain.paycalculator.items.PriceDiscountCalculator
import movie.domain.point.Point
import movie.domain.reservations.Reservations

class PayCalculator(
    private val payMethodDiscountCalculator: PayMethodDiscountCalculator,
    private val priceDiscountCalculator: PriceDiscountCalculator,
) {
    fun calculate(
        reservations: Reservations,
        inputPoint: Point,
        payMethod: PayMethod,
    ): Money {
        val price = reservations.calculateTotalDiscountPrice(priceDiscountCalculator)
        val pointMoney = inputPoint.toMoney(inputPoint)
        val applyPoint = price - pointMoney
        val finalPrice = payMethodDiscountCalculator.calculate(applyPoint, payMethod)
        return finalPrice
    }
}
