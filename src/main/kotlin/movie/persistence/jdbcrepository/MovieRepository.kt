package movie.persistence.jdbcrepository

import movie.persistence.entity.MovieEntity
import movie.persistence.entity.ScreeningScheduleEntity

interface MovieRepository {
    fun save(movie: MovieEntity): MovieEntity

    fun findByTitle(title: String): MovieEntity?

    fun findById(id: Long): MovieEntity?

    fun findAllWithScreenings(): List<Pair<MovieEntity, List<ScreeningScheduleEntity>>>
}
