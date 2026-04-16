package persistence.entity

data class MovieEntity(
    val id: Long? = null,
    val title: String,
    val runningTimeMinutes: Int,
)
