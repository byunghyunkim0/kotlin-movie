package domain.movie.itmes

@JvmInline
value class Title(
    private val title: String,
) {
    init {
        require(title.isNotBlank()) { "타이틀은 비어있을 수 없습니다. (입력값: $title)" }
    }

    fun isSame(other: Title): Boolean = title == other.title

    fun getTitle() = title
}
