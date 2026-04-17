package movie.domain.dto

data class ReservationDto(
    val title: String,
    val dateTime: String,
    val seats: List<String>,
)
