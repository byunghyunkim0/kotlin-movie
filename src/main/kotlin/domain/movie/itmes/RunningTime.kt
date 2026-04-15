package domain.movie.itmes

@JvmInline
value class RunningTime(
    private val runningTime: Int,
) {
    init {
        require(runningTime > 0) { "영화의 러닝 타임은 0보다 커야합니다. (입력값: $runningTime)" }
    }
}
