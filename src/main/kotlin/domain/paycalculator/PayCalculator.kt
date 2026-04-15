package domain.paycalculator

import domain.discountpolicy.PayMethod
import domain.money.Money
import domain.paycalculator.items.PayMethodDiscountCalculator
import domain.paycalculator.items.PriceDiscountCalculator
import domain.point.Point
import domain.reservations.Reservations

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
