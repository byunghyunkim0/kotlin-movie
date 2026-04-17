package movie.persistence.jdbcrepository

import movie.persistence.entity.ScreeningScheduleEntity

interface ScreeningScheduleRepository {
    fun save(schedule: ScreeningScheduleEntity): ScreeningScheduleEntity

    fun findById(id: Long): ScreeningScheduleEntity?

    fun findByMovieId(movieId: Long): List<ScreeningScheduleEntity>
}
