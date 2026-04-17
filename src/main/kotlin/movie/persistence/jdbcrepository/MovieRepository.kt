package movie.persistence.jdbcrepository

import movie.persistence.entity.MovieEntity

interface MovieRepository {
    fun save(movie: MovieEntity): MovieEntity

    fun findByTitle(title: String): MovieEntity?

    fun findById(id: Long): MovieEntity?
}
