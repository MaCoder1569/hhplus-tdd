package io.hhplus.tdd.point.infrastructure;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.domain.UserPointRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("사용자 포인트 이력 메모리 리포지토리 테스트")
class MemoryUserPointRepositoryTest {
    UserPointRepository userPointRepository = new MemoryUserPointRepository(new UserPointTable());

    @Test
    void 미가입자_사용자_조회시_기본값_생성(){
        //given & when
        UserPoint userPoint = userPointRepository.findById(1L);

        //then
        assertThat(userPoint).isNotNull();
        assertThat((userPoint.id())).isEqualTo(1L);
        assertThat(userPoint.point()).isEqualTo(0);
    }

    @Test
    void 사용자_가입() {
        //given & when
        UserPoint userPoint = userPointRepository.insertOrUpdate(1L, 100);

        //then
        assertThat(userPoint.id()).isEqualTo(1L);
        assertThat(userPoint.point()).isEqualTo(100);
    }

    @Test
    void 가입자_사용자_조회() {
        //given
        UserPoint newUserPoint = userPointRepository.insertOrUpdate(2L, 100);

        //when
        UserPoint userPoint = userPointRepository.findById(2L);

        //then
        assertThat(userPoint.id()).isEqualTo(newUserPoint.id());
        assertThat(userPoint.point()).isEqualTo(newUserPoint.point());
    }
}
