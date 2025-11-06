package io.hhplus.tdd.point.infrastructure;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.PointHistoryRepository;
import io.hhplus.tdd.point.domain.TransactionType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemoryPointHistoryRepositoryTest {
    PointHistoryRepository pointHistoryRepository = new MemoryPointHistoryRepository(new PointHistoryTable());

    @Test
    void 히스토리_없는_사용자_조회시_빈값전송(){
        //given & when
        List<PointHistory> histories = pointHistoryRepository.findAllByUserId(1L);

        //then
        assertThat(histories).isNotNull();
        assertThat(histories.size()).isEqualTo(0);
    }

    @Test
    void 히스토리_추가(){
        //given & when
        PointHistory history = pointHistoryRepository.insert(1L, 100, TransactionType.CHARGE, 10);

        //then
        assertThat(history.userId()).isEqualTo(1L);
        assertThat(history.amount()).isEqualTo(100);
        assertThat(history.type()).isEqualTo(TransactionType.CHARGE);
        assertThat(history.updateMillis()).isEqualTo(10);
    }
}
