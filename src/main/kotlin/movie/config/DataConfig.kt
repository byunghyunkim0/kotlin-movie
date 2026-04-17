package movie.config

import movie.domain.discountpolicy.CardDiscountPolicy
import movie.domain.discountpolicy.CashDiscountPolicy
import movie.domain.discountpolicy.DateCondition
import movie.domain.discountpolicy.EarlyAndLateDiscountPolicy
import movie.domain.discountpolicy.MovieDayDiscountPolicy
import movie.domain.discountpolicy.PayMethod
import movie.domain.discountpolicy.TimeCondition
import movie.domain.paycalculator.PayCalculator
import movie.domain.paycalculator.items.PayMethodDiscountCalculator
import movie.domain.paycalculator.items.PriceDiscountCalculator
import movie.persistence.jdbcrepository.JdbcMovieRepository
import movie.persistence.jdbcrepository.JdbcReservationItemRepository
import movie.persistence.jdbcrepository.JdbcReservationRepository
import movie.persistence.jdbcrepository.JdbcReservedSeatRepository
import movie.persistence.jdbcrepository.JdbcScreeningScheduleRepository
import movie.persistence.jdbcrepository.MovieRepository
import movie.persistence.jdbcrepository.ReservationItemRepository
import movie.persistence.jdbcrepository.ReservationRepository
import movie.persistence.jdbcrepository.ReservedSeatRepository
import movie.persistence.jdbcrepository.ScreeningScheduleRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceUtils
import javax.sql.DataSource

@Configuration
class DataConfig {
    @Bean
    fun movieRepository(dataSource: DataSource): MovieRepository = JdbcMovieRepository(DataSourceUtils.getConnection(dataSource))

    @Bean
    fun screeningScheduleRepository(dataSource: DataSource): ScreeningScheduleRepository =
        JdbcScreeningScheduleRepository(DataSourceUtils.getConnection(dataSource))

    @Bean
    fun reservationRepository(dataSource: DataSource): ReservationRepository =
        JdbcReservationRepository(DataSourceUtils.getConnection(dataSource))

    @Bean
    fun reservationItemRepository(dataSource: DataSource): ReservationItemRepository =
        JdbcReservationItemRepository(DataSourceUtils.getConnection(dataSource))

    @Bean
    fun reservedSeatRepository(dataSource: DataSource): ReservedSeatRepository =
        JdbcReservedSeatRepository(DataSourceUtils.getConnection(dataSource))

    @Bean
    fun priceDiscountCalculator(): PriceDiscountCalculator =
        PriceDiscountCalculator(
            movieDayDiscountPolicy = MovieDayDiscountPolicy(timeDiscountCondition = DateCondition()),
            timeDiscountPolicy = EarlyAndLateDiscountPolicy(timeDiscountCondition = TimeCondition()),
        )

    @Bean
    fun payMethodDiscountCalculator(): PayMethodDiscountCalculator =
        PayMethodDiscountCalculator(
            policies =
                mapOf(
                    PayMethod.CARD to CardDiscountPolicy(),
                    PayMethod.CASH to CashDiscountPolicy(),
                ),
        )

    @Bean
    fun payCalculator(
        priceDiscountCalculator: PriceDiscountCalculator,
        payMethodDiscountCalculator: PayMethodDiscountCalculator,
    ): PayCalculator =
        PayCalculator(
            priceDiscountCalculator = priceDiscountCalculator,
            payMethodDiscountCalculator = payMethodDiscountCalculator,
        )
}
