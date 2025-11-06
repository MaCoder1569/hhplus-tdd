package io.hhplus.tdd.point.application;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.infrastructure.MemoryPointHistoryRepository;
import io.hhplus.tdd.point.infrastructure.MemoryUserPointRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PointServiceTest {

    static PointService pointService;

    @BeforeAll
    static void setUp() {
        PointHistoryTable historyTable = new PointHistoryTable();
        UserPointTable userPointTable = new UserPointTable();

        var userPointRepository = new MemoryUserPointRepository(userPointTable);
        var pointHistoryRepository = new MemoryPointHistoryRepository(historyTable);

        pointService = new PointService(userPointRepository, pointHistoryRepository);
    }

    @Test
    void 유저_추가() throws Exception {
        //given
        long userId = 1L;
        long amount = 100;

        //when
        UserPoint userPoint = pointService.saveUserPoint(userId, amount);

        //then
        assertThat(userPoint.id()).isEqualTo(userId);
        assertThat(userPoint.point()).isEqualTo(amount);
    }

    @Test
    void 오류_ID_유저_추가() throws Exception {
        //given
        long userId = -1L;
        long amount = 100;

        //when & then
        assertThatThrownBy(() -> pointService.saveUserPoint(userId, amount))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("id 값은 0보다 작을 수 없습니다.");
    }

    @Test
    void 오류_포인트_유저_추가() throws Exception {
        //given
        long userId = 1L;
        long amount = -100;

        //when & then
        assertThatThrownBy(() -> pointService.saveUserPoint(userId, amount))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("amount 값은 0보다 작을 수 없습니다.");
    }

    @Test
    void 등록_유저_포인트_조회() throws Exception {
        //given
        long userId = 10L;
        long amount = 1000;
        pointService.saveUserPoint(userId, amount);

        //when
        UserPoint userPoint = pointService.getPoint(userId);

        //then
        assertThat(userPoint.id()).isEqualTo(userId);
        assertThat(userPoint.point()).isEqualTo(amount);
    }

    @Test
    void 미등록_유저_포인트_조회() throws Exception {
        //given & when
        long userId = 2L;
        UserPoint userPoint = pointService.getPoint(userId);

        //then
        assertThat(userPoint.id()).isEqualTo(userId);
        assertThat(userPoint.point()).isEqualTo(0);
    }

    @Test
    void 오류_유저_포인트_조회(){
        //given
        long userId = -1L;

        //when & then
        assertThatThrownBy(() -> pointService.getPoint(userId))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("id 값은 0보다 작을 수 없습니다.");

    }
}
