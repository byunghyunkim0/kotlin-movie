package movie.service

import movie.domain.discountpolicy.PayMethod
import movie.domain.dto.ReservationRequest
import movie.domain.movie.Movie
import movie.domain.movie.items.RunningTime
import movie.domain.movie.items.ScreeningPeriod
import movie.domain.movie.items.Title
import movie.domain.paycalculator.PayCalculator
import movie.domain.point.Point
import movie.domain.reservations.Reservations
import movie.domain.reservations.items.Reservation
import movie.domain.seat.Seat
import movie.domain.seat.items.ColumnNumber
import movie.domain.seat.items.RowNumber
import movie.domain.seat.items.SeatGrade
import movie.domain.seat.items.SeatPosition
import movie.domain.timetable.items.ScreenTime
import movie.domain.timetable.items.Seats
import movie.persistence.entity.ReservationEntity
import movie.persistence.entity.ReservationItemEntity
import movie.persistence.entity.ReservedSeatEntity
import movie.persistence.jdbcrepository.MovieRepository
import movie.persistence.jdbcrepository.ReservationItemRepository
import movie.persistence.jdbcrepository.ReservationRepository
import movie.persistence.jdbcrepository.ReservedSeatRepository
import movie.persistence.jdbcrepository.ScreeningScheduleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class DbReservationService(
    private val movieRepository: MovieRepository,
    private val screeningScheduleRepository: ScreeningScheduleRepository,
    private val reservationRepository: ReservationRepository,
    private val reservationItemRepository: ReservationItemRepository,
    private val reservedSeatRepository: ReservedSeatRepository,
    private val payCalculator: PayCalculator,
) : ReservationService {
    @Transactional
    override fun reserve(request: ReservationRequest) {
        val reservationsDomain = convertToDomain(request)
        val payMethod =
            when (request.paymentMethod) {
                "CREDIT_CARD", "CARD" -> PayMethod.CARD
                "CASH" -> PayMethod.CASH
                else -> PayMethod.CARD
            }
        reserveWithPaymentMethod(reservationsDomain, Point(request.usedPoints), payMethod, request.paymentMethod)
    }

    private fun convertToDomain(request: ReservationRequest): Reservations {
        val reservationItems =
            request.reservations.map { itemRequest ->
                val screeningEntity =
                    screeningScheduleRepository.findById(itemRequest.screeningId)
                        ?: throw IllegalArgumentException("Screening not found: ${itemRequest.screeningId}")
                val movieEntity =
                    movieRepository.findById(screeningEntity.movieId)
                        ?: throw IllegalArgumentException("Movie not found: ${screeningEntity.movieId}")

                val movie =
                    Movie(
                        title = Title(movieEntity.title),
                        runningTime = RunningTime(movieEntity.runningTimeMinutes),
                        screeningPeriod =
                            ScreeningPeriod(
                                startDate = LocalDate.of(2020, 1, 1),
                                endDate = LocalDate.of(2030, 12, 31),
                            ),
                    )

                val screenTime =
                    ScreenTime(
                        startTime = screeningEntity.startAt.toLocalTime(),
                        endTime = screeningEntity.endAt.toLocalTime(),
                        screeningDate = screeningEntity.startAt.toLocalDate(),
                    )

                val seats =
                    Seats(
                        itemRequest.seats.map { seatStr ->
                            val row = seatStr.substring(0, 1)
                            val col = seatStr.substring(1).toInt()
                            val grade =
                                when (row) {
                                    "A", "B" -> SeatGrade.B
                                    "C", "D" -> SeatGrade.S
                                    else -> SeatGrade.A
                                }
                            Seat(SeatPosition(RowNumber(row), ColumnNumber(col)), grade)
                        },
                    )

                Reservation(movie, screenTime, seats, itemRequest.screeningId)
            }

        var reservations = Reservations()
        reservationItems.forEach {
            reservations = reservations.addReservation(it)
        }
        return reservations
    }

    @Transactional
    override fun reserve(
        reservations: Reservations,
        usedPoint: Point,
        payMethod: PayMethod,
    ) {
        reserveWithPaymentMethod(reservations, usedPoint, payMethod, payMethod.name)
    }

    @Transactional
    fun reserveWithPaymentMethod(
        reservations: Reservations,
        usedPoint: Point,
        payMethod: PayMethod,
        paymentMethodName: String,
    ) {
        val finalPrice = payCalculator.calculate(reservations, usedPoint, payMethod)

        val reservationEntity =
            reservationRepository.save(
                ReservationEntity(
                    usedPoints = usedPoint.toMoney(usedPoint).getAmount(),
                    paymentMethod = paymentMethodName,
                    totalPrice = finalPrice.getAmount(),
                ),
            )

        reservations.getReservations().forEach { reservation ->
            val screeningId =
                reservation.getScreeningId()
                    ?: throw IllegalStateException("Screening ID is missing in reservation")

            val itemEntity =
                reservationItemRepository.save(
                    ReservationItemEntity(
                        reservationsId = reservationEntity.id!!,
                        screeningId = screeningId,
                    ),
                )

            reservation.getSeats().getSeats().forEach { seat ->
                reservedSeatRepository.save(
                    ReservedSeatEntity(
                        reservationId = itemEntity.id!!,
                        seatNumber = seat.getName(),
                    ),
                )
            }
        }
    }
}
