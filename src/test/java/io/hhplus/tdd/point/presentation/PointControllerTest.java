package io.hhplus.tdd.point.presentation;

import io.hhplus.tdd.TddApplication;
import io.hhplus.tdd.point.application.PointService;
import io.hhplus.tdd.point.domain.UserPoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {TddApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class PointControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PointService pointService;

    @Test
    void 미등록_유저_포인트_조회() throws Exception {
        // given
        long userId = 1L;

        // when & then
        mockMvc.perform(get("/point/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.point").value(0));
    }

    @Test
    void 등록_유저_포인트_조회() throws Exception {
        // given
        long userId = 2L;
        long amount = 100;
        UserPoint userPoint = pointService.saveUserPoint(userId, amount);

        // when & then
        mockMvc.perform(get("/point/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.point").value(amount));
    }

    @Test
    void 오류_유저_포인트_조회() throws Exception {
        // given
        long userId = -1L;

        // when & then
        mockMvc.perform(get("/point/{id}", userId))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void 유저_포인트_내역_조회() throws Exception {
        // given
        long userId = 0L;

        // when & then
        mockMvc.perform(get("/point/{id}/histories", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void 유저_포인트_충전() throws Exception {
        // given
        long userId = 0L;
        long amount = 0;

        // when & then
        mockMvc.perform(patch("/point/{id}/charge", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(amount)))  // body에 숫자 그대로 넣기
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.point").value(amount))
                .andExpect(jsonPath("$.updateMillis").isNumber());
    }

    @Test
    void 유저_포인트_사용() throws Exception {
        // given
        long userId = 0L;
        long amount = 0;

        // when & then
        mockMvc.perform(patch("/point/{id}/use", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(amount)))  // body에 숫자 그대로 넣기
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.point").value(amount))
                .andExpect(jsonPath("$.updateMillis").isNumber());
    }
}