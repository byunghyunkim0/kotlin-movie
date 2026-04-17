package movie.config

import movie.persistence.jdbcrepository.JdbcMovieRepository
import movie.persistence.jdbcrepository.JdbcScreeningScheduleRepository
import movie.persistence.jdbcrepository.MovieRepository
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
}
