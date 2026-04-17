package movie.controller

import movie.persistence.entity.MovieEntity
import movie.persistence.entity.ScreeningScheduleEntity
import movie.persistence.jdbcrepository.MovieRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.LocalDateTime

class MovieControllerTest {
    private lateinit var mockMvc: MockMvc
    private val movieRepository: MovieRepository = mock(MovieRepository::class.java)

    @BeforeEach
    fun setUp() {
        val controller = MovieController(movieRepository)
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    }

    @Test
    fun `전체 영화 목록을 조회한다`() {
        val movie1 = MovieEntity(id = 1, title = "인터스텔라", runningTimeMinutes = 169)
        val movie2 = MovieEntity(id = 2, title = "오펜하이머", runningTimeMinutes = 180)

        val screenings1 =
            listOf(
                ScreeningScheduleEntity(
                    id = 101,
                    movieId = 1,
                    startAt = LocalDateTime.parse("2025-09-20T13:30:00"),
                    endAt = LocalDateTime.parse("2025-09-20T16:19:00"),
                ),
                ScreeningScheduleEntity(
                    id = 102,
                    movieId = 1,
                    startAt = LocalDateTime.parse("2025-09-20T18:00:00"),
                    endAt = LocalDateTime.parse("2025-09-20T20:49:00"),
                ),
            )

        `when`(movieRepository.findAllWithScreenings()).thenReturn(
            listOf(
                movie1 to screenings1,
                movie2 to emptyList(),
            ),
        )

        mockMvc
            .perform(get("/api/movies"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.movies.length()").value(2))
            .andExpect(jsonPath("$.movies[0].title").value("인터스텔라"))
            .andExpect(jsonPath("$.movies[0].screenings.length()").value(2))
            .andExpect(jsonPath("$.movies[0].screenings[0].id").value(101))
            .andExpect(jsonPath("$.movies[1].title").value("오펜하이머"))
    }
}
