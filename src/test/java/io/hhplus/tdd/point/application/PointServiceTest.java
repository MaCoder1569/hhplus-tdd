package io.hhplus.tdd.point.application;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.infrastructure.MemoryPointHistoryRepository;
import io.hhplus.tdd.point.infrastructure.MemoryUserPointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PointServiceTest {

    PointService pointService;

    @BeforeEach
    void setUp() {
        PointHistoryTable historyTable = new PointHistoryTable();
        UserPointTable userPointTable = new UserPointTable();

        var userPointRepository = new MemoryUserPointRepository(userPointTable);
        var pointHistoryRepository = new MemoryPointHistoryRepository(historyTable);

        pointService = new PointService(userPointRepository, pointHistoryRepository);
    }

    @Test
    void 미등록_유저_포인트_추가() throws Exception {
        //given
        long userId = 1L;
        long amount = 100;

        //when
        UserPoint userPoint = pointService.charge(userId, amount);

        //then
        assertThat(userPoint.id()).isEqualTo(userId);
        assertThat(userPoint.point()).isEqualTo(amount);
    }

    @Test
    void 등록_유저_포인트_추가() throws Exception {
        //given
        long userId = 1L;
        long amount1 = 100;
        long amount2 = 100;
        pointService.charge(userId, amount1);

        //when
        UserPoint userPoint = pointService.charge(userId, amount2);

        //then
        assertThat(userPoint.id()).isEqualTo(userId);
        assertThat(userPoint.point()).isEqualTo(amount1+amount2);
    }

    @Test
    void 오류_ID_유저_포인트_추가() throws Exception {
        //given
        long userId = -1L;
        long amount = 100;

        //when & then
        assertThatThrownBy(() -> pointService.charge(userId, amount))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("id 값은 0보다 작을 수 없습니다.");
    }

    @Test
    void 오류_포인트_유저_추가() throws Exception {
        //given
        long userId = 1L;
        long amount = -100;

        //when & then
        assertThatThrownBy(() -> pointService.charge(userId, amount))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("amount 값은 0보다 작을 수 없습니다.");
    }

    @Test
    void 미등록_유저_포인트_사용() throws Exception {
        //given
        long userId = 1L;
        long amount = 100;

        //when & then
        assertThatThrownBy(() -> pointService.use(userId, amount))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("사용 포인트 보다 남은 포인트가 적습니다.");
    }

    @Test
    void 등록_유저_포인트_사용() throws Exception {
        //given
        long userId = 1L;
        long amount = 500;
        long useAmount = 200;
        pointService.charge(userId, amount);

        //when
        UserPoint userPoint = pointService.use(userId, useAmount);

        //then
        assertThat(userPoint.id()).isEqualTo(userId);
        assertThat(userPoint.point()).isEqualTo(amount-useAmount);
    }

    @Test
    void 등록_유저_포인트_초과_사용() throws Exception {
        //given
        long userId = 1L;
        long amount = 200;
        long useAmount = 500;
        pointService.charge(userId, amount);

        //when & then
        assertThatThrownBy(() -> pointService.use(userId, useAmount))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("사용 포인트 보다 남은 포인트가 적습니다.");
    }

    @Test
    void 오류_ID_유저_포인트_사용() throws Exception {
        //given
        long userId = -1L;
        long amount = 100;

        //when & then
        assertThatThrownBy(() -> pointService.use(userId, amount))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("id 값은 0보다 작을 수 없습니다.");
    }

    @Test
    void 오류_포인트_유저_사용() throws Exception {
        //given
        long userId = 1L;
        long amount = -100;

        //when & then
        assertThatThrownBy(() -> pointService.use(userId, amount))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("amount 값은 0보다 작을 수 없습니다.");
    }

    @Test
    void 등록_유저_포인트_조회() throws Exception {
        //given
        long userId = 10L;
        long amount = 1000;
        pointService.charge(userId, amount);

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

    @Test
    void 미등록_유저_포인트_내역_조회() throws Exception {
        //given & when
        long userId = 1L;
        List<PointHistory> pointHistories = pointService.getPointHistories(userId);

        //then
        assertThat(pointHistories.size()).isEqualTo(0);
    }

    @Test
    void 등록_유저_포인트_내역_조회() throws Exception {
        //given
        long userId = 1L;
        long aoumt = 100;
        pointService.charge(userId, aoumt);

        //when
        List<PointHistory> pointHistories = pointService.getPointHistories(userId);

        //then
        assertThat(pointHistories.size()).isEqualTo(1);
        assertThat(pointHistories.get(0).userId()).isEqualTo(userId);
        assertThat(pointHistories.get(0).amount()).isEqualTo(aoumt);
        assertThat(pointHistories.get(0).type()).isEqualTo(TransactionType.CHARGE);
    }

    @Test
    void 등록_유저_포인트_내역_조회2() throws Exception {
        //given
        long userId = 1L;
        long amount = 1000;
        long useAmount = 200;
        pointService.charge(userId, amount);
        pointService.use(userId, useAmount);

        //when
        List<PointHistory> pointHistories = pointService.getPointHistories(userId);

        //then
        assertThat(pointHistories.size()).isEqualTo(2);

        assertThat(pointHistories.get(0).userId()).isEqualTo(userId);
        assertThat(pointHistories.get(0).amount()).isEqualTo(amount);
        assertThat(pointHistories.get(0).type()).isEqualTo(TransactionType.CHARGE);

        assertThat(pointHistories.get(1).userId()).isEqualTo(userId);
        assertThat(pointHistories.get(1).amount()).isEqualTo(useAmount);
        assertThat(pointHistories.get(1).type()).isEqualTo(TransactionType.USE);
    }

    @Test
    void 오류_유저_포인트_내역_조회(){
        //given
        long userId = -1L;

        //when & then
        assertThatThrownBy(() -> pointService.getPoint(userId))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("id 값은 0보다 작을 수 없습니다.");

    }
}
