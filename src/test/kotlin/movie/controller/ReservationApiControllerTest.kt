package movie.controller

import movie.service.ReservationService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class ReservationApiControllerTest {
    private lateinit var mockMvc: MockMvc
    private val reservationService: ReservationService = mock(ReservationService::class.java)

    @BeforeEach
    fun setUp() {
        val controller = ReservationApiController(reservationService)
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    }

    @Test
    fun `예매를 진행한다`() {
        val requestBody =
            """
            {
              "reservations": [
                {
                  "screeningId": 101,
                  "seats": ["C2", "C3"]
                },
                {
                  "screeningId": 202,
                  "seats": ["E2"]
                },
                {
                  "screeningId": 301,
                  "seats": ["A1"]
                }
              ],
              "usedPoints": 2000,
              "paymentMethod": "CREDIT_CARD"
            }
            """.trimIndent()

        mockMvc
            .perform(
                post("/api/reservations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody),
            ).andExpect(status().isCreated)
    }
}
